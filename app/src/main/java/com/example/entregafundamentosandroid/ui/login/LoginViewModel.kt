package com.example.entregafundamentosandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregafundamentosandroid.utils.BASE_API_DRAGONBALL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception


class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState
    lateinit var email: String
    lateinit var password: String


    fun login(){

        viewModelScope.launch(Dispatchers.IO){
            val client = OkHttpClient()
            val stringAuthorization = Credentials.basic(email,password)
            val url = "${BASE_API_DRAGONBALL}api/auth/login"
            val formBody = FormBody.Builder()
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", stringAuthorization)
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()

            _uiState.value =  if (response.isSuccessful)
                response.body?.let {
                    UiState.OnLogin(it.string())
                } ?: UiState.Error("Login is incorrect")
            else
                UiState.Error(response.message)
        }
    }

    sealed class UiState{
        object Idle: UiState()
        data class Error(val error: String): UiState()
        data class OnLogin(val validToken: String) : UiState()
    }

}