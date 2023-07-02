package com.example.entregafundamentosandroid.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.entregafundamentosandroid.databinding.ActivityHomeBinding
import com.example.entregafundamentosandroid.ui.home.herosFight.CharacterDetailFragment
import com.example.entregafundamentosandroid.ui.home.herosList.CharacterListFragment

class HomeActivity : AppCompatActivity(){

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            characterListFragment()
        }
    }

    private fun characterListFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, CharacterListFragment())
            .commit()
    }
    fun characterDetailFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, CharacterDetailFragment())
            .addToBackStack(null)
            .commit()
    }
}
