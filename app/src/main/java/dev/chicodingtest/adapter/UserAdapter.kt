package dev.chicodingtest.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.chicodingtest.R
import dev.chicodingtest.databinding.ListItemUserBinding
import dev.chicodingtest.model.User

class UserAdapter(
    private val onCheckboxListener: (chosenUser: User) -> Unit, private val onItemListener: (user: User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ListItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(view, onCheckboxListener = { position: Int ->
            onCheckboxListener(getItem(position))
        }, onItemListener = { position ->
            onItemListener(getItem(position))
        })
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val payload = payloads[0] as Bundle
            for (key in payload.keySet()) {
                if (key.equals(IS_STUDENT_KEY)) {
                    holder.bindIsStudent(payload.getBoolean(IS_STUDENT_KEY))
                }
            }
        }
    }

    class UserViewHolder(
        private val binding: ListItemUserBinding, onCheckboxListener: (position: Int) -> Unit, onItemListener: (position: Int) -> Unit
    ) : ViewHolder(binding.root) {
        init {
            binding.checkIsStudent.setOnClickListener {
                onCheckboxListener(this@UserViewHolder.adapterPosition)
            }
            binding.root.setOnClickListener {
                onItemListener(this@UserViewHolder.adapterPosition)
            }
        }

        fun bind(item: User) {
            binding.tvName.text = item.name
            binding.tvAge.text = binding.root.context.getString(R.string.user_age, item.age)
            binding.checkIsStudent.isChecked = item.isStudent
        }

        fun bindIsStudent(isStudent: Boolean) {
            Log.d(TAG, "bindIsStudent: $isStudent ")
            binding.checkIsStudent.isChecked = isStudent
        }
    }

    companion object {
        const val IS_STUDENT_KEY = "is_student_key"
        private const val TAG = "UserAdapter"
    }
}


private object UserItemCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: User, newItem: User): Any {
        val payload = Bundle()
        if (oldItem.isStudent != newItem.isStudent) payload.putBoolean(UserAdapter.IS_STUDENT_KEY, newItem.isStudent)
        return payload
    }
}