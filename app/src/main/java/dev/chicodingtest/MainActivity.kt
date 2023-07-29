package dev.chicodingtest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.chicodingtest.adapter.UserAdapter
import dev.chicodingtest.adapter.UsersListItemDecorator
import dev.chicodingtest.databinding.ActivityMainBinding
import dev.chicodingtest.ui.adduser.AddUserFragment
import dev.chicodingtest.ui.userdetails.UserDetailsFragment
import dev.chicodingtest.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: UserViewModel by viewModels {
        UserViewModel.Factory((application as ChiApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setMainAppBar()
        setUserList()
        onAddUserListener()
        onUserListUpdate()
    }

    private fun setUserList() {
        setUserAdapter()
        setUserRecyclerView()
    }

    private fun setUserRecyclerView() {
        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            addItemDecoration(UsersListItemDecorator(16, 64, 16))
        }
    }

    private fun onUserListUpdate() {
        viewModel.users.observe(this@MainActivity) { users ->
            adapter.submitList(users) {
                binding.rvStudents.apply {
                    post {
                        invalidateItemDecorations()
                    }
                }
            }
        }
    }

    private fun setUserAdapter() {
        adapter = UserAdapter(onCheckboxListener = { chosenUser ->
            Log.d(TAG, "Clicked on checkbox with user:${chosenUser} ")
            viewModel.toggleUserStudentStatus(chosenUser)
        }, onItemListener = { user ->
            Log.d(TAG, "Clicked on item with user: $user")
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf("user_id" to user.id)
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.root_container, fragment)
                .addToBackStack("UserDetailsFragment").commit()
        })
    }

    private fun setMainAppBar() {
        binding.mainAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_user -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<AddUserFragment>(R.id.root_container)
                        addToBackStack("AddUserFragment")
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun onAddUserListener() {
        supportFragmentManager.setFragmentResultListener(Constants.ADD_USER_REQUEST, this) { _, resultBundle: Bundle ->
            val result = resultBundle.getInt(Constants.ADD_USER_RESULT)
            onAddUserResult(result)
        }
    }

    private fun onAddUserResult(result: Int) {
        when (result) {
            Constants.ADD_USER_RESULT_OK -> {
                Snackbar.make(findViewById(android.R.id.content), "User added successfully", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityTag"
    }
}