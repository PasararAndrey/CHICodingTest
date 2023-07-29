package dev.chicodingtest.useradapter

import dev.chicodingtest.model.User

interface UserAdapterListeners {
    fun onLongClickListener(userId: Int)
    fun onClickListener(userId: Int)
    fun onCheckboxClickedListener(user: User)
}