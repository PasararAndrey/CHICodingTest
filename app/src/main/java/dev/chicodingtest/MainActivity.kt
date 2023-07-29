package dev.chicodingtest

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dev.chicodingtest.data.PreferenceManager
import dev.chicodingtest.databinding.ActivityMainBinding
import dev.chicodingtest.model.User
import dev.chicodingtest.ui.adduser.AddUserFragment
import dev.chicodingtest.ui.userdetails.UserDetailsFragment
import dev.chicodingtest.useradapter.UserAdapter
import dev.chicodingtest.useradapter.UserAdapterListeners
import dev.chicodingtest.useradapter.UsersListItemDecorator
import dev.chicodingtest.util.Constants
import dev.chicodingtest.util.SortOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel: UserViewModel by viewModels {
        UserViewModel.Factory((application as ChiApplication).repository, PreferenceManager(datastore = dataStore))
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

    private fun setMainAppBar() {
        binding.mainAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_user -> {
                    navigateWithReplace<AddUserFragment>("AddUserFragment")
                    true
                }
                R.id.sort_user -> {
                    showSortConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun setUserList() {
        setUserAdapter()
        setUserRecyclerView()
    }

    private fun setUserAdapter() {
        adapter = UserAdapter(object : UserAdapterListeners {

            override fun onClickListener(userId: Int) {
                Log.d(TAG, "Clicked on item with userId: $userId")
                navigateWithReplace<UserDetailsFragment>("UserDetailsFragment", args = bundleOf("user_id" to userId))
            }

            override fun onCheckboxClickedListener(user: User) {
                Log.d(TAG, "Clicked on checkbox with user:${user} ")
                viewModel.toggleUserStudentStatus(user)
            }

            override fun onLongClickListener(userId: Int) {
                showDeleteDialog(userId)
            }
        })
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


    private fun showDeleteDialog(userId: Int) {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.dialog_delete_title))
            .setPositiveButton(getString(R.string.dialog_delete_yes)) { _, _ ->
                Log.d(TAG, "Chosen positive")
                viewModel.deleteUser(userId)
            }.setNegativeButton(getString(R.string.dialog_delete_no)) { _, _ -> }.show()
    }


    private fun showSortConfirmationDialog() {
        var checkedItem = 0
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkedItem = viewModel.sortOrder.first().ordinal
            }
        }
        val sortOrderOptionArray: Array<String> = SortOrder.values().map { sortOrder -> sortOrder.shownSortOption }.toTypedArray()
        MaterialAlertDialogBuilder(this).setTitle(resources.getString(R.string.dialog_sort_title))
            .setNeutralButton(resources.getString(R.string.dialog_sort_cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.dialog_sort_ok)) { _, _ -> }
            .setSingleChoiceItems(sortOrderOptionArray, checkedItem) { _: DialogInterface, which: Int ->
                viewModel.updateSortOrder(SortOrder.values()[which])
            }.show()
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

    /**
     * Commits a transaction to specified fragment
     * @param [T] - fragment class to which to move
     * @param [args] - specifies the argument that stored by [Bundle] to [T] fragment and can be resolve by [Fragment.getArguments]
     * @param [tag] - tag to be applied in backstack
     */
    private inline fun <reified T : Fragment> navigateWithReplace(tag: String? = null, args: Bundle? = null) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            Log.d(TAG, "navigateWithReplace user bundle:${args} ")
            replace<T>(R.id.root_container, args = args)
            addToBackStack(tag)
        }

    }

    companion object {
        private const val TAG = "MainActivityTag"
    }
}