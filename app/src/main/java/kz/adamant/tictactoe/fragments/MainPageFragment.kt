package kz.adamant.tictactoe.fragments

import android.os.Bundle
import android.view.View
import kz.adamant.tictactoe.util.BindingFragment
import kz.adamant.tictactoe.R
import kz.adamant.tictactoe.databinding.FragmentMainPageBinding

class MainPageFragment :
    BindingFragment<FragmentMainPageBinding>(FragmentMainPageBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnStartGame.setOnClickListener {
                navController.navigate(R.id.action_mainPageFragment_to_userNameFragment)
            }
            btnUserRecords.setOnClickListener {
                navController.navigate(R.id.action_mainPageFragment_to_userRecordsFragment)
            }
        }
    }
}