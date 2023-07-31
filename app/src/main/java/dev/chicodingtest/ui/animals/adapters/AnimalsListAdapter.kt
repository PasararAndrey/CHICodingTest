package dev.chicodingtest.ui.animals.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.chicodingtest.databinding.AnimalListItemBinding
import dev.chicodingtest.domain.model.Animal

class AnimalsListAdapter(
    private val onCheckboxListener: (chosenAnimal: Animal) -> Unit,
) : PagingDataAdapter<Animal, AnimalsListAdapter.ViewHolder>(AnimalsDiffCallback) {


    inner class ViewHolder(private val binding: AnimalListItemBinding, private val onCheckboxListener: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            Glide.with(binding.root.context)
                .load(animal.url)
                .centerCrop()
                .into(binding.animalImage)
            binding.animalIsFavorite.isChecked = animal.isFavorite
            binding.animalIsFavorite.setOnClickListener {
                toggleFavorite(!animal.isFavorite, bindingAdapterPosition)
                onCheckboxListener(this.bindingAdapterPosition)
            }
        }

        private fun toggleFavorite(isFavorite: Boolean, position: Int) {
            snapshot()[position]?.isFavorite = isFavorite
            notifyItemChanged(position, bundleOf(IS_FAVORITE_KEY to isFavorite))
        }

        fun bindFavorite(isFavorite: Boolean) {
            binding.animalIsFavorite.isChecked = isFavorite
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AnimalListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding) { position: Int ->
            getItem(position)?.let { animal -> onCheckboxListener(animal) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val payload = payloads[0] as Bundle
            for (key in payload.keySet()) {
                if (key.equals(IS_FAVORITE_KEY)) {
                    holder.bindFavorite(payload.getBoolean(IS_FAVORITE_KEY))
                }
            }
        }
    }

    companion object {
        const val IS_FAVORITE_KEY = "ADAPTER_FAVORITE_KEY"
    }
}

object AnimalsDiffCallback : DiffUtil.ItemCallback<Animal>() {
    override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Animal, newItem: Animal): Any {
        val payload = Bundle()
        if (oldItem.isFavorite != newItem.isFavorite) payload.putBoolean(AnimalsListAdapter.IS_FAVORITE_KEY, newItem.isFavorite)
        return payload
    }
}