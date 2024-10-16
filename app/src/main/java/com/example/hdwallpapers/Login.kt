package com.example.hdwallpapers


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.hdwallpapers.databinding.ActivityLoginBinding
import com.example.hdwallpapers.models.LoginViewModel
import com.tashila.pleasewait.PleaseWaitDialog

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressDialog = PleaseWaitDialog(context = this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading...")

        binding.toSignupPage.setOnClickListener {
            val intent = Intent(this@Login, SignUp::class.java)
            startActivity(intent)
        }

        loginViewModel.loginResult.observe(this, Observer { isSuccess ->
            progressDialog.dismiss()
            if (isSuccess) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Login, HomePage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email or Password is wrong.", Toast.LENGTH_SHORT).show()
            }
        })


        binding.login.setOnClickListener {
            val email = binding.UserEmail.text.toString().trim()
            val password = binding.UserPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressDialog.show()
                loginViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }


        binding.ShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.UserPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()

            } else {
                binding.UserPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()

            }
            binding.UserPassword.setSelection(binding.UserPassword.text.length)
        }
    }
}