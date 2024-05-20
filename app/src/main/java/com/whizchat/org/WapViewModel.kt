package com.whizchat.org

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.whizchat.org.data.Event
import com.whizchat.org.data.USER_NODE
import com.whizchat.org.data.UserData
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WapViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val db :  FirebaseFirestore
) : ViewModel()  {
    var inProgress = mutableStateOf(false)
    var evenMutablestate = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)

    init {
         val currentUser = auth.currentUser
         signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
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

            }
        }
    }

//    fun signUp ( name : String ,number: String ,
//                 email : String,  password : String)
//
//    {
//        inProgress.value= true
//        if(name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
//            handleException(custumMessage = "Please fill all the fields")
//            return
//        }
//        inProgress.value = true
//        db.collection(USER_NODE).whereEqualTo("number",number).get().addOnSuccessListener {
//            if(it.isEmpty){
//                auth.createUserWithEmailAndPassword(email, password) .addOnCompleteListener{
//                    if(it.isSuccessful){
//                        signIn.value = true
//                        createOrUpdateProfile( name , number)
//                        Log.d("Tag", "signUP: UserLoggedIN")
//                    }
//                    else{
//                        handleException(it.exception, custumMessage = "SignUp Failed")
//                    }
//                }
//            }
//            else{
//                handleException(custumMessage = "Phone number already registered")
//
//            }
//        }
//        auth.createUserWithEmailAndPassword(email, password) .addOnCompleteListener{
//            if(it.isSuccessful){
//                signIn.value = true
//                createOrUpdateProfile( name , number)
//                Log.d("Tag", "signUP: UserLoggedIN")
//            }
//            else{
//                handleException(it.exception, custumMessage = "SignUp Failed")
//            }
//        }
//    }
//
//    fun login(email: String , password: String){
//        if(email.isEmpty() or password.isEmpty()){
//            handleException(custumMessage = "Please fill all fields")
//            return
//        }
//        else{
//            inProgress.value = true
//            auth.signInWithEmailAndPassword(email,password).
//                    addOnCompleteListener{
//                        if(it.isSuccessful){
//                            signIn.value = true
//                            inProgress.value = false
//                            auth.currentUser?.uid?.let {
//                                getUserData(it)
//                            }
//                        }
//                        else{
//                            handleException(exception = it.exception , custumMessage = "Login failed")
//                        }
//                    }
//        }
//    }
//    private fun createOrUpdateProfile(name: String?= null, number: String?= null , imageUrl : String? = null) {
//            var uid = auth.currentUser?.uid
//            val userData = UserData(
//                userId = uid,
//                name = name?: userData.value?.name,
//                number = number?: userData.value?.number,
//                imageUrl = imageUrl?: userData.value?.imageUrl
//            )
//
//        uid?.let {
//            inProgress.value = true
//            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
//                if(it.exists()){
//                    //todo {update data}
//                }
//                else {
//                    db.collection(USER_NODE).document(uid).set(userData)
//                    inProgress.value = false
//                    getUserData(uid)
//                }
//            }
//                .addOnFailureListener{
//                    handleException(it, "Cannot Retrieve User")
//                }
//
//        }
//    }
//
//    private fun getUserData(uid: String) {
//        inProgress.value = true
//        db.collection(USER_NODE).document(uid).addSnapshotListener{
//            value, error ->
//            if(error!= null){
//                handleException(error, "Cannot Retrieve User")
//            }
//            if(value!= null){
//                var user = value.toObject<UserData>()
//                userData.value = user
//                inProgress.value = false
//            }
//        }
//    }

    fun handleException( exception: Exception?= null ,message : String = ""){
        Log.e("Whizchat", "Whizchat exception : ", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?:""
        val messsage = if(message.isNullOrEmpty()) errorMsg else message
        evenMutablestate.value = Event(messsage)
        inProgress.value = false
    }
}


