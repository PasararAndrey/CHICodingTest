package dev.chicodingtest.ui.animals.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.chicodingtest.databinding.FavoriteAnimalListItemBinding
import dev.chicodingtest.domain.model.Animal

class FavoriteAnimalsAdapter : ListAdapter<Animal, FavoriteAnimalsAdapter.ViewHolder>(AnimalsDiffCallback) {

    class ViewHolder(private val binding: FavoriteAnimalListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            Glide.with(binding.root.context)
                .load(animal.url)
                .centerCrop()
                .placeholder(ColorDrawable(Color.BLACK))
                .into(binding.animalImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FavoriteAnimalListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}