package kz.adamant.tictactoe.fragments

import android.os.Bundle
import android.view.View
import kz.adamant.tictactoe.*
import kz.adamant.tictactoe.databinding.FragmentUserRecordsBinding
import kz.adamant.tictactoe.util.BindingFragment

class UserRecordsFragment :
    BindingFragment<FragmentUserRecordsBinding>(FragmentUserRecordsBinding::inflate) {
    private lateinit var adapter: UserRecordsAdapter
    private val userRepository = UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val user1Name: String? = it.getString("user1Name")
            val user2Name: String? = it.getString("user2Name")
            val gameState: Int = it.getInt("gameState", Game.GAME_STATE.TIE)
            if (user1Name != null && user2Name != null) {
                initializeUserRecords(user1Name, user2Name, gameState)
            }
        }
    }

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

    private fun initializeUserRecords(user1Name: String, user2Name: String, gameState: Int) {
        val searchSuccess =
            userRepository.searchUsers(user1Name, user2Name, gameState)

        when (searchSuccess) {
            UserRepository.SearchSuccess.FOUND_ALL -> return
            UserRepository.SearchSuccess.FOUND_FIRST ->
                userRepository.addUser(user2Name, gameState, 2)
            UserRepository.SearchSuccess.FOUND_SECOND ->
                userRepository.addUser(user1Name, gameState, 1)
            UserRepository.SearchSuccess.FOUND_NONE -> {
                userRepository.addUser(user1Name, gameState, 1)
                userRepository.addUser(user2Name, gameState, 2)
            }
        }
    }
}