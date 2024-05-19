package com.whizchat.org

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WapViewModel @Inject constructor(
    private val auth : FirebaseAuth
) : ViewModel()  {
    init {

    }

    fun signUp ( name : String , email : String,  password : String){
        auth.createUserWithEmailAndPassword(email, password) .addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("Tag", "signUP: UserLoggedIN")
            }
            else{

            }
        }
    }
}


fun handleException( exception: Exception?= null , custumMessage : String = ""){
    Log.e("Whizchat", "Whizchat exception : ", exception)
    exception?.printStackTrace()
    val errorMsg = exception?.localizedMessage?:""
    val messsage = if(custumMessage.isNullOrEmpty()) errorMsg else custumMessage
}