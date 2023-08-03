package com.example.projectmanag.actvities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectmanag.R
import com.example.projectmanag.databinding.ActivitySignInBinding
import com.example.projectmanag.firebase.FirestoreClass

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.projemanag.model.User

class SignInActivity : BaseActivity() {
    private var binding: ActivitySignInBinding? = null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()

        binding?.btnSignIn?.setOnClickListener {
            signInRegisteredUser()
        }
        binding?.forgotText?.setOnClickListener {
            val intent = Intent(this,ResetPasswordActivitity::class.java)
            startActivity(intent)
            finish()
        }
        setupActionBar()
    }
     fun signInSuccess( user : User){
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignInActivity)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding?.toolbarSignInActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun signInRegisteredUser(){
        val email: String  = binding?.etEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.etPassword?.text.toString().trim { it <= ' ' }

        if(validateForm(email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = auth.currentUser
//                        startActivity(Intent(this,MainActivity::class.java))
                        FirestoreClass().loadUserData(this)

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }
}