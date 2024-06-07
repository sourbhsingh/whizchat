package com.whizchat.org

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.whizchat.org.data.APIKEY
import com.whizchat.org.data.CHATS
import com.whizchat.org.data.ChatBotData
import com.whizchat.org.data.ChatBotRoleEnum
import com.whizchat.org.data.ChatData
import com.whizchat.org.data.ChatUser
import com.whizchat.org.data.Event
import com.whizchat.org.data.MESSAGE
import com.whizchat.org.data.Message
import com.whizchat.org.data.STATUS
import com.whizchat.org.data.Status
import com.whizchat.org.data.USER_NODE
import com.whizchat.org.data.UserData
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WapViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val db :  FirebaseFirestore,
    private  val storage : FirebaseStorage
) : ViewModel()  {
    var inProgress = mutableStateOf(false)
    var inProgressChats = mutableStateOf(false)
    var evenMutablestate = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    var chats = mutableStateOf<List<ChatData>>(listOf())
    var chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration?= null
    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    init {
         val currentUser = auth.currentUser
         signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }
    val list by lazy {
        mutableStateListOf<ChatBotData>()
    }
    private val genAI by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = APIKEY
        )

    }
    fun sendChatbotMesg(message: String) = viewModelScope.launch {
        var chat = genAI.startChat()
        list.add(ChatBotData(message,ChatBotRoleEnum.USER.role))
     chat.sendMessage(
         content(ChatBotRoleEnum.USER.role) {
             text(message)
         }
     ).text?.let {
         list.add(ChatBotData(it , ChatBotRoleEnum.MODEL.role))
     }

    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(message = "Enter Fill All Fields")
            return
        }
        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener { it ->
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                        Log.e("Login", "signUp: USer logged in")
                    } else {
                        handleException(it.exception, message = "SignUp failed")
                        Log.e("Login", "SignUp failed")
                    }
                }
            } else {
                handleException(message = "Number Already Exists")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(message = "Enter email and password")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        signIn.value = true
                        inProgress.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(exception = it.exception, message = "Login Failed")
                    }
                }
        }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        uid.let {
            inProgress.value = true
            if (uid != null) {
                db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                    if (it.exists()) {
                        db.collection(USER_NODE).document(uid).set(userData, SetOptions.merge())
                            .addOnSuccessListener {
                                inProgress.value = false
                                getUserData(uid)
                            }
                    } else {
                        db.collection(USER_NODE).document(uid).set(userData)
                        inProgress.value = false
                        getUserData(uid)
                    }
                }.addOnFailureListener {
                    handleException(it, message = "Can not retrieve user")
                }
            }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Can not retrieve user")
            }
            if (value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                inProgress.value = false
                populateChats()
                populateStatus()

            }
        }
    }


    fun handleException( exception: Exception?= null ,message : String = ""){
        Log.e("Whizchat", "Whizchat exception : ", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?:""
        val messsage = if(message.isNullOrEmpty()) errorMsg else message
        evenMutablestate.value = Event(messsage)
        inProgress.value = false
    }

    fun uploadProfileImage(uri: Uri) {
         uploadImage(uri){
            createOrUpdateProfile(imageUrl = it.toString())
         }
    }
    fun logout(){
        auth.signOut()
        signIn.value = false
        userData.value =null
        dePopulate()
        currentChatMessageListener = null
        evenMutablestate.value = Event("Logged Out")
    }
    fun uploadImage(uri: Uri,  onSuccess :(Uri)->Unit){
        inProgress.value= true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef= storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProgress.value = false
        }
            .addOnFailureListener{
                handleException(it)
            }
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(message = "Number must be contain digits only")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user2.number", userData.value?.number),
                        Filter.equalTo("user1.number", number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(message = "Number not found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.number,
                                        userData.value?.imageUrl
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.number,
                                        chatPartner.imageUrl,
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            handleException(it)
                        }
                } else {
                    handleException(message = "Chats already exist")
                }
            }
        }
    }

    private fun populateChats() {
        inProgressChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()

                }
                inProgressChats.value = false
            }
        }
    }


    fun onSendReply(chatId: String , message : String){
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId , message ,time)
        db.collection(CHATS).document(chatId).collection(MESSAGE).document().set(msg)
    }


    fun populateMessasges(chatId : String)
    {
        inProgressChatMessage.value =true
        currentChatMessageListener = db.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener { value, error ->
            if(error!= null){
                handleException(error)
            }
            if(value!= null){
                chatMessages.value = value.documents.mapNotNull {
                    it.toObject<Message>()
                }.sortedBy {
                    it.timeStamp
                }
                inProgressChatMessage.value = false
            }
        }
    }
    fun populateStatus()
    {
        val timeDelta = 24L * 60 * 60 *1000
        val cutoff = System.currentTimeMillis()-timeDelta

        inProgressStatus.value =true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId",userData.value?.userId),
                Filter.equalTo("user2.userId",userData.value?.userId)
            )
        ).addSnapshotListener { value,
                                error ->
            if(error!= null){
                handleException(error)
            }
            if(value!= null){
                val currentConnections = arrayListOf(userData.value?.userId)
                val chats = value.toObjects<ChatData>()
                    chats.forEach{
                        chat->
                        if(chat.user1.userId==userData.value?.userId){
                            currentConnections.add(chat.user2.userId)
                        }
                        else currentConnections.add(chat.user1.userId)
                    }
                db.collection(STATUS).whereGreaterThan("timestamp",cutoff).whereIn("user.userId",currentConnections)
                    .addSnapshotListener { value, error ->
                        if(error!= null){
                            handleException(error)
                        }
                        if(value!= null){
                            status.value=  value.toObjects()
                            inProgressStatus.value = false

                        }                        }
                    }
            }



    }

    fun dePopulate(){
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }
        fun createStatus(imageUrl: String){
            val newStatus = Status(
                ChatUser(
                    userData.value?.userId,
                    userData.value?.name,
                    userData.value?.imageUrl,
                    userData.value?.number
                ),
                imageUrl,
                System.currentTimeMillis()
            )

            db.collection(STATUS).document().set(newStatus)
        }

    fun uploadStatus(uri: Uri) {
          uploadImage(uri){
              createStatus(it.toString())
          }
    }


}


