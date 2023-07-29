package dev.chicodingtest.useradapter

import androidx.recyclerview.widget.RecyclerView
import dev.chicodingtest.R
import dev.chicodingtest.databinding.ListItemUserBinding
import dev.chicodingtest.model.User

class UserViewHolder(
    private val binding: ListItemUserBinding,
    private val vhListeners: UserViewHolderListeners
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.checkIsStudent.setOnClickListener {
            vhListeners.onCheckboxClickedListener(this@UserViewHolder.adapterPosition)
        }
        binding.root.setOnClickListener {
            vhListeners.onClickListener(this@UserViewHolder.adapterPosition)
        }
        binding.root.setOnLongClickListener {
            vhListeners.onLongClickListener(this@UserViewHolder.adapterPosition)
            true
        }
    }

    fun bind(item: User) {
        binding.tvName.text = item.name
        binding.tvAge.text = binding.root.context.getString(R.string.user_age, item.age)
        binding.checkIsStudent.isChecked = item.isStudent
    }

    fun bindIsStudent(isStudent: Boolean) {
        binding.checkIsStudent.isChecked = isStudent
    }
}