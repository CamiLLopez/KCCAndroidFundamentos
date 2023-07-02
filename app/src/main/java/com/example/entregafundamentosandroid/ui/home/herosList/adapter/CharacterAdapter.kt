package com.example.entregafundamentosandroid.ui.home.herosList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entregafundamentosandroid.data.model.Character
import com.example.entregafundamentosandroid.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterAdapter(private val callback: CharacterAdapterCallback):
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characterList =  listOf<Character>()

   inner class CharacterViewHolder(private val binding: ItemCharacterBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {

            with(binding){

            characterName.text = item.name
            progressCharacterLife.progress = item.actualLife

            Picasso
                .get()
                .load(item.photo)
                .resize(1240, 860)
                .centerInside()
                .into(characterPhoto)


            root.setOnClickListener {

                callback.onCharacterClicked(item)
            }
            }
        }

       fun clear() {
           Picasso.get().cancelRequest(binding.characterPhoto)
           binding.characterPhoto.setImageDrawable(null)
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun onViewRecycled(holder: CharacterViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    fun updateList(characters: List<Character>) {
        characterList = characters
        notifyDataSetChanged()
    }
}






