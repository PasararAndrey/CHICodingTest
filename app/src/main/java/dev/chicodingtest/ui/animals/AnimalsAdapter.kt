package dev.chicodingtest.ui.animals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.chicodingtest.databinding.AnimalListItemBinding
import dev.chicodingtest.domain.model.Animal

class AnimalsAdapter : RecyclerView.Adapter<AnimalsAdapter.ViewHolder>() {

    var animals: List<Animal> = listOf<Animal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val binding: AnimalListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            Glide.with(binding.root.context)
                .load(animal.url)
                .into(binding.animalImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AnimalListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(animals[position])
    }
}