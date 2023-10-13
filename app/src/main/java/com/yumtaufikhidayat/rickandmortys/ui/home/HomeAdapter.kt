package com.yumtaufikhidayat.rickandmortys.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yumtaufikhidayat.rickandmortys.core.databinding.ItemCharactersBinding
import com.yumtaufikhidayat.rickandmortys.core.domain.model.Character
import com.yumtaufikhidayat.rickandmortys.ui.utils.Common.loadImage
import com.yumtaufikhidayat.rickandmortys.ui.utils.Common.textStatusColor

class HomeAdapter(
    private val onItemClickListener: (Character) -> Unit
) : ListAdapter<Character, HomeAdapter.ViewHolder>(
    homeDiffCallback
), Filterable {

    private var listCharacters = listOf<Character>()
    private val searchFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filteredList = mutableListOf<Character>()
            if (p0.isEmpty()) {
                filteredList.addAll(listCharacters)
            } else {
                val filterPattern = p0.toString().lowercase()
                listCharacters.forEach {
                    if(it.name.lowercase().contains(filterPattern)) filteredList.add(it)
                }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(p1?.values as MutableList<Character>)
        }
    }

    fun setData(list: List<Character>) {
        this.listCharacters = list
        submitList(list)
    }

    override fun getFilter(): Filter = searchFilter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCharactersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCharactersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Character) {
            binding.apply {
                imgCharacter.loadImage(data.image)
                tvCharacterName.text = data.name
                tvSpecies.text = data.species

                tvStatus.apply {
                    text = data.status
                    textStatusColor(data.status)
                }

                itemView.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val homeDiffCallback = object : ItemCallback<Character>() {
            override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean = oldItem == newItem
        }
    }
}