package kz.adamant.tictactoe.fragments.userRecords

import android.os.Bundle
import android.view.View
import kz.adamant.tictactoe.R
import kz.adamant.tictactoe.UserRepository
import kz.adamant.tictactoe.databinding.FragmentUserRecordsBinding
import kz.adamant.tictactoe.util.BindingFragment

class UserRecordsFragment :
    BindingFragment<FragmentUserRecordsBinding>(FragmentUserRecordsBinding::inflate) {
    private lateinit var adapter: UserRecordsAdapter
    private val userRepository = UserRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserRecordsAdapter(userRepository.userRecords)
        binding.run {
            rvRecords.adapter = adapter

            // The logic of popping up all previous fragments lies in nav_graph.xml file
            btnMain.setOnClickListener {
                navController.navigate(R.id.action_userRecordsFragment_to_mainPageFragment)
            }
        }
    }
}