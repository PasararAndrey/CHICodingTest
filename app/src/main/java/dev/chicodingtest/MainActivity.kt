package dev.chicodingtest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.chicodingtest.adapter.UserAdapter
import dev.chicodingtest.adapter.UsersListItemDecorator
import dev.chicodingtest.databinding.ActivityMainBinding

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
        adapter = UserAdapter(onCheckboxListener = { chosenUser ->
            Log.d(TAG, "Clicked on checkbox with user:${chosenUser} ")
            viewModel.toggleUserStudentStatus(chosenUser)
        }, onItemListener = { user ->
            Log.d(TAG, "Clicked on item with user: $user")
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf("user_id" to user.id)
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.root_container, fragment).addToBackStack(null).commit()

        })
        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            addItemDecoration(UsersListItemDecorator(16, 64, 16))
        }
        viewModel.users.observe(this@MainActivity) { users ->
            adapter.submitList(users)
        }
    }

    companion object {
        private const val TAG = "MainActivityTag"
    }
}