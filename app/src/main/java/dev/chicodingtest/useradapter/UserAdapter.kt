package dev.chicodingtest.useradapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.chicodingtest.databinding.ListItemUserBinding
import dev.chicodingtest.model.User

class UserAdapter(
    private val userAdapterListeners: UserAdapterListeners
) : ListAdapter<User, UserViewHolder>(UserItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ListItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(
            view,
            object : UserViewHolderListeners {
                override fun onCheckboxClickedListener(position: Int) {
                    userAdapterListeners.onCheckboxClickedListener(getItem(position))
                }

                override fun onClickListener(position: Int) {
                    userAdapterListeners.onClickListener(getItem(position).id)
                }

                override fun onLongClickListener(position: Int) {
                    userAdapterListeners.onLongClickListener((getItem(position).id))
                }
            }
        )
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

    companion object {
        const val IS_STUDENT_KEY = "is_student_key"
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