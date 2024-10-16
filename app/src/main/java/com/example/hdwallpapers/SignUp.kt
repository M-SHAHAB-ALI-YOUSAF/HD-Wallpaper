package com.example.hdwallpapers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.example.hdwallpapers.databinding.ActivityLoginBinding
import com.example.hdwallpapers.databinding.ActivitySignUpBinding
import com.example.hdwallpapers.models.SignUpViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tashila.pleasewait.PleaseWaitDialog


class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressDialog = PleaseWaitDialog(context = this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading...")

        binding.toLoginPage.setOnClickListener {
            finish()
        }


        binding.ShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.UserPasswordSignup.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.confirmpassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()

            } else {
                binding.UserPasswordSignup.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.confirmpassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()

            }
            binding.UserPasswordSignup.setSelection(binding.UserPasswordSignup.text.length)
            binding.confirmpassword.setSelection(binding.confirmpassword.text.length)
        }

        binding.SignUpButton.setOnClickListener {
            val email = binding.UserEmailSignup.text.toString()
            val password = binding.UserPasswordSignup.text.toString()
            val confirmPassword = binding.confirmpassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                signUpViewModel.signUpUser(email, password)
            }
        }

        signUpViewModel.signupStatus.observe(this, Observer { success ->
            progressDialog.dismiss()
            if (success) {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
            }
        })


        signUpViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}