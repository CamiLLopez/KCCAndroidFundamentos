package com.example.entregafundamentosandroid.ui.home.herosFight

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.entregafundamentosandroid.R
import com.example.entregafundamentosandroid.data.model.Character
import com.example.entregafundamentosandroid.databinding.FragmentCharacterViewBinding
import com.example.entregafundamentosandroid.ui.home.SharedViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CharacterDetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentCharacterViewBinding

    private lateinit var characterSelected: Character
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterViewBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }
    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.characters.collect{ stateHeroDetails ->
                when(stateHeroDetails) {
                    is SharedViewModel.StateCharacter.Error -> {
                        Toast.makeText(requireContext(), stateHeroDetails.message, Toast.LENGTH_LONG).show()
                        parentFragmentManager.popBackStack()
                    }
                    is SharedViewModel.StateCharacter.OnCharacterSelected -> {
                        displayDetail(stateHeroDetails.character)
                        disableButton(stateHeroDetails.character)
                        characterSelected = stateHeroDetails.character
                    }
                    is SharedViewModel.StateCharacter.OnCharacterUpdated -> {
                        disableButton(stateHeroDetails.character)
                        displayDetail(stateHeroDetails.character)
                    }
                    is SharedViewModel.StateCharacter.Loading -> {

                    }
                    is SharedViewModel.StateCharacter.OnCharacterReceived -> {

                    }
                }
            }
        }
    }
    private fun disableButton(characterSelected: Character) {

        if (characterSelected.actualLife == 100){
            binding.buttonDamage.isEnabled = true
            binding.buttonHeal.setBackgroundColor(Color.LTGRAY)
            binding.buttonHeal.isEnabled = false

        }
        else if(characterSelected.isDead){
            binding.buttonDamage.isEnabled = false
            binding.buttonDamage.setBackgroundColor(Color.LTGRAY)
            binding.buttonHeal.isEnabled = true
        }else{
            binding.buttonDamage.isEnabled = true
            binding.buttonHeal.isEnabled = true
            binding.buttonHeal.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.actual_life_color))
            binding.buttonDamage.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.damage_life_color))
        }
    }
    private fun displayDetail(characterSelected: Character) {
        with(binding) {
            characterName.text = characterSelected.name
            characterDescription.text = characterSelected.description
            Picasso
                .get()
                .load(characterSelected.photo)
                .resize(1240, 860)
                .centerInside()
                .into(characterPhoto)
            progressCharacterLife.progress = characterSelected.actualLife
        }
    }
    private fun setListeners() {
        binding.buttonDamage.setOnClickListener {
            viewModel.damageCharacter(characterSelected)
            setObservers()
            disableButton(characterSelected)
        }
        binding.buttonHeal.setOnClickListener {
            viewModel.healCharacter(characterSelected)
            setObservers()
            disableButton(characterSelected)
        }
    }
}
