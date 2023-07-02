package com.example.entregafundamentosandroid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregafundamentosandroid.data.dto.CharacterDTO
import com.example.entregafundamentosandroid.data.model.Character
import com.example.entregafundamentosandroid.utils.BASE_API_DRAGONBALL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class SharedViewModel: ViewModel() {

    private val _characters = MutableStateFlow<StateCharacter>(StateCharacter.Loading)
    val characters: StateFlow<StateCharacter> = _characters

    private var characterList = mutableListOf<Character>()

    fun setCharacteres(character: List<Character>) {
         characterList.clear()
         characterList.addAll(character)
        _characters.value = StateCharacter.OnCharacterReceived(character)
    }

    fun getCharacters(token: String) {

        viewModelScope.launch(Dispatchers.IO) {

            if (characterList.isEmpty()) {
                val client = OkHttpClient()
                val url = "${BASE_API_DRAGONBALL}api/heros/all"
                val formBody = FormBody.Builder()
                    .add("name", "")
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                val call = client.newCall(request)

                val response = call.execute()

                if (response.isSuccessful) {
                    try {
                        val characterDTO: Array<CharacterDTO> =
                            Gson().fromJson(
                                response.body?.string(),
                                Array<CharacterDTO>::class.java
                            )
                        val characterList =
                            characterDTO.map { Character(it.id, it.name, it.photo, it.description) }

                        setCharacteres(characterList)

                    } catch (ex: Exception) {
                        _characters.value = StateCharacter.Error(ex.message.toString())
                    }
                } else {
                    _characters.value = StateCharacter.Error(response.message)
                }
            }
                else {
                _characters.value = StateCharacter.OnCharacterReceived(characterList)
                }
            }
        }
    fun selectedCharater(character: Character){
        _characters.value = StateCharacter.OnCharacterSelected(character)
    }
    fun damageCharacter(characterSelected: Character){
        val randomDamange = Random.nextInt(20, 60)
        characterList.map { character ->
            if (character.id == characterSelected.id){
                val dead = (character.actualLife - randomDamange) <= 0
                if (dead){
                    character.actualLife = 0
                    character.isDead = true
                }else{
                    character.actualLife = character.actualLife - randomDamange
                }
                updateCharacter(character)
            }
        }
    }
    fun healCharacter(characterSelected: Character){
        characterList.map { character ->
            if (character.id == characterSelected.id){
                if (character.isDead){
                    character.isDead = false
                }
                character.actualLife = character.actualLife + 20
                updateCharacter(character)
            }
        }
    }
    fun updateCharacter(character: Character){
        _characters.value = StateCharacter.OnCharacterUpdated(character)
    }

    sealed class StateCharacter {
        data class OnCharacterReceived(val characters: List<Character>) : StateCharacter()
        data class Error(val message: String) : StateCharacter()
        data class OnCharacterSelected(val character: Character): StateCharacter()
        data class OnCharacterUpdated(val character: Character): StateCharacter()
        object Loading : StateCharacter()
    }
}

