package kz.adamant.tictactoe.fragments

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kz.adamant.tictactoe.util.BindingFragment
import kz.adamant.tictactoe.databinding.FragmentUserNameBinding

class UserNameFragment :
    BindingFragment<FragmentUserNameBinding>(FragmentUserNameBinding::inflate) {

    private val EMPTY_MESSAGE = "Empty User!"
    private val SAME_NAME_MESSAGE = "Same User Names!"
    private val LONG_NAME_MESSAGE = "Long User Name!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnNext.setOnClickListener {
                val user1Text = binding.etUser1.text.toString().trim()
                val user2Text = binding.etUser2.text.toString().trim()
                when (validateUsers(user1Text, user2Text)) {
                    ValidateState.Empty -> showSnackbarMessage(EMPTY_MESSAGE)
                    ValidateState.SameName -> showSnackbarMessage(SAME_NAME_MESSAGE)
                    ValidateState.LongName -> showSnackbarMessage(LONG_NAME_MESSAGE)
                    ValidateState.Success -> navigateToGame(user1Text, user2Text)
                }
            }
        }
    }

    private fun navigateToGame(user1Text: String, user2Text: String) {
        val action = UserNameFragmentDirections.actionUserNameFragmentToGameFragment(
            user1 = user1Text,
            user2 = user2Text
        )
        navController.navigate(action)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setText(message)
            .show()
    }

    private fun validateUsers(user1Text: String, user2Text: String): ValidateState {
        if (user1Text.isEmpty() || user2Text.isEmpty()) return ValidateState.Empty
        if (user1Text == user2Text) return ValidateState.SameName
        if (user1Text.length >= 14 || user2Text.length >= 14) return ValidateState.LongName
        return ValidateState.Success
    }

    sealed class ValidateState {
        object Empty : ValidateState()
        object SameName : ValidateState()
        object LongName : ValidateState()
        object Success : ValidateState()
    }
}