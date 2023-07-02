package com.example.entregafundamentosandroid.ui.home.herosList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entregafundamentosandroid.data.model.Character
import com.example.entregafundamentosandroid.databinding.FragmentCharacterListBinding
import com.example.entregafundamentosandroid.ui.home.SharedViewModel
import com.example.entregafundamentosandroid.ui.home.herosList.adapter.CharacterAdapter
import com.example.entregafundamentosandroid.ui.home.herosList.adapter.CharacterAdapterCallback
import kotlinx.coroutines.launch


class CharacterListFragment : Fragment(), CharacterAdapterCallback {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentCharacterListBinding
    private val adapter = CharacterAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        viewLifecycleOwner.lifecycleScope.launch {

            getCharacters()

            viewModel.characters.collect{ state ->
                when (state) {
                    is SharedViewModel.StateCharacter.OnCharacterReceived -> {
                        updateList(state.characters)
                    }
                    is SharedViewModel.StateCharacter.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is SharedViewModel.StateCharacter.Loading -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {

        binding.characterList.layoutManager = LinearLayoutManager(requireContext())
        binding.characterList.adapter = adapter
    }

    private fun updateList(character: List<Character>) {
        adapter.updateList(character)
    }

    private  fun getCharacters() {

        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        viewModel.getCharacters(token.toString())
    }

    override fun onCharacterClicked(character: Character) {
        viewModel.selectedCharater(character)
    }
}


