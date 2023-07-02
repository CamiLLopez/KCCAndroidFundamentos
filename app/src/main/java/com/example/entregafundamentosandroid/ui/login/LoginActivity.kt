package com.example.entregafundamentosandroid.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.entregafundamentosandroid.databinding.ActivityLoginBinding
import com.example.entregafundamentosandroid.ui.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.email.doAfterTextChanged {
           binding.login.isEnabled = binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()

       }

        binding.password.doAfterTextChanged {
            binding.login.isEnabled = binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()

        }

        binding.login.setOnClickListener {
            viewModel.email = binding.email.text.toString()
            viewModel.password = binding.password.text.toString()

            lifecycleScope.launch {
                viewModel.login()
                viewModel.uiState.collect{
                    initCollects()

                }
            }
        }

    }
    private fun initCollects(){
        lifecycleScope.launch{
            viewModel.uiState.collect{
                Log.w("Tag", "Login")
                when(it) {
                    is LoginViewModel.UiState.OnLogin -> {

                        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("token", it.validToken).apply()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)

                        finish()
                    }
                    is LoginViewModel.UiState.Error ->  Toast.makeText(this@LoginActivity, "Login failed because of ${it.error} error, try again!", Toast.LENGTH_LONG).show()
                    else -> {}
                }
            }
        }
    }



}