package com.example.endterm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.endterm.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.loginEmail.setOnClickListener {
            signUp()
        }
    }
    fun signUp() {
        auth?.createUserWithEmailAndPassword(
            binding.emailInput.text.toString(), binding.passwordInput.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toMain(task.result?.user)
            } else {
                signInEmail()
            }
        }
    }
    fun signInEmail() {
        auth?.signInWithEmailAndPassword(binding.emailInput.text.toString()
            , binding.passwordInput.text.toString())?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toMain(task.result?.user)
            } else {
                signInEmail()
            }
        }
    }
    fun toMain(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}