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
        setDescriptionTextListener()
        setChooseBirthdayListener()
        addUserListener()
    }

    private fun addUserListener() {
        binding.addUser.setOnClickListener {
            viewModel.onAddUser()
        }
    }

    //Survives configuration change
    //At the same time doesn't duplicate setting text on the same instance
    private fun setNameTextListener() {
        var text = ""
        binding.inputName.editText?.addTextChangedListener { editable ->
            text = editable.toString()
            viewModel.userName.value = editable.toString()
        }
        viewModel.userName.observe(viewLifecycleOwner) { newText: String ->
            if (text == newText) {
                return@observe
            }
            binding.inputName.editText?.setText(newText)
        }
    }

    //Survives configuration change
    //At the same time doesn't duplicate setting text on the same instance
    private fun setAgeTextListener() {
        var text = ""
        binding.inputAge.editText?.addTextChangedListener { editable ->
            text = editable.toString()
            viewModel.userAge.value = editable.toString()
        }
        viewModel.userAge.observe(viewLifecycleOwner) { newText: String ->
            if (text == newText) {
                return@observe
            }
            binding.inputAge.editText?.setText(newText)
        }
    }

    //Survives configuration change
    //At the same time doesn't duplicate setting text on the same instance
    private fun setDescriptionTextListener() {
        var text = ""
        binding.inputDescription.editText?.addTextChangedListener { editable ->
            text = editable.toString()
            viewModel.userDescription.value = editable.toString()
        }
        viewModel.userDescription.observe(viewLifecycleOwner) { newText: String ->
            if (text == newText) {
                return@observe
            }
            binding.inputDescription.editText?.setText(newText)
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

    private fun chooseBirthdayWithDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select your birthday").build()
        datePicker.show(requireActivity().supportFragmentManager, "Birthday")
        datePicker.addOnPositiveButtonClickListener { time ->
            viewModel.updateBirthday(time)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
