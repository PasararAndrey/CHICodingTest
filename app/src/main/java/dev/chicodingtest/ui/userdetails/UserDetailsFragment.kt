package dev.chicodingtest.ui.userdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.chicodingtest.ChiApplication
import dev.chicodingtest.R
import dev.chicodingtest.databinding.FragmentUserDetailsBinding
import dev.chicodingtest.util.formatToStandardString
import java.util.*

class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel by viewModels<UserDetailsViewModel> {
        UserDetailsViewModel.Factory(
            (requireActivity().application as ChiApplication).repository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserDetailsBinding.bind(view)
        getUser()
        observeUserDetails()
    }

    private fun observeUserDetails() {
        detailsViewModel.user.observe(viewLifecycleOwner) { newUser ->
            Log.d(TAG, "User updated:${newUser} ")
            binding.tvName.text = getString(R.string.user_details_default_name_tv)
            binding.tvAge.text = getString(R.string.user_details_default_age_tv)
            binding.userName.text = getString(R.string.user_details_name, newUser.name)
            binding.userAge.text = getString(R.string.user_details_age, newUser.age)
            binding.userIsStudent.text = getString(
                if (newUser.isStudent) R.string.user_details_is_student
                else R.string.user_details_is_not_student
            )
            binding.userBirthday.text = getString(R.string.user_details_birthday, Date(newUser.birthday).formatToStandardString())
        }
    }

    private fun getUser() {
        Log.d(TAG, "User value in viewmodel on view creation: ${detailsViewModel.user.value}")
        val userId = arguments?.getInt("user_id")
        if (userId != null) {
            detailsViewModel.getUserById(userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = ""
    }
}