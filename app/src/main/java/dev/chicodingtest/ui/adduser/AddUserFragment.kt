package dev.chicodingtest.ui.adduser

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dev.chicodingtest.ChiApplication
import dev.chicodingtest.R
import dev.chicodingtest.databinding.FragmentAddUserBinding
import dev.chicodingtest.util.Constants
import dev.chicodingtest.util.formatToStandardString
import kotlinx.coroutines.launch
import java.util.*

class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddUserViewModel> {
        AddUserViewModel.Factory(
            (requireActivity().application as ChiApplication).repository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddUserBinding.bind(view)
        setListeners()
        onAddUserEvents()
        observeChosenBirthday()
    }

    private fun setListeners() {
        setNameTextListener()
        setAgeTextListener()
        setChooseBirthdayListener()
        addUserListener()
    }

    private fun addUserListener() {
        binding.addUser.setOnClickListener {
            viewModel.onAddUser()
        }
    }

    private fun setAgeTextListener() {
        binding.inputAge.editText?.addTextChangedListener {
            viewModel.userAge = it.toString()
        }
    }

    private fun setNameTextListener() {
        binding.inputName.editText?.addTextChangedListener {
            viewModel.userName = it.toString()
        }
    }

    private fun setChooseBirthdayListener() {
        binding.chooseBirthday.setOnClickListener {
            chooseBirthdayWithDatePicker()
        }
    }

    private fun observeChosenBirthday() {
        viewModel.birthday.observe(viewLifecycleOwner) { time ->
            binding.chosenBirthday.text = Date(time).formatToStandardString()
        }
    }

    private fun onAddUserEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addUserEvents.collect { event ->
                    when (event) {
                        is AddUserViewModel.AddUserEvent.NavigateBackWithMessage -> {
                            setFragmentResult(Constants.ADD_USER_REQUEST, bundleOf(Constants.ADD_USER_RESULT to event.result))
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                        is AddUserViewModel.AddUserEvent.ShowInvalidInput -> Snackbar.make(
                            requireActivity().findViewById(android.R.id.content), event.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun chooseBirthdayWithDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select your birthday").build()
        datePicker.show(requireActivity().supportFragmentManager, "Birthday")
        datePicker.addOnPositiveButtonClickListener { time ->
            viewModel.updateBirthday(time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
