package kz.adamant.tictactoe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.adamant.tictactoe.databinding.RecordItemBinding

class UserRecordsAdapter(
    private val userRecords: List<User>
) : RecyclerView.Adapter<UserRecordsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecordItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userRecords[position])
    }

    override fun getItemCount(): Int {
        return userRecords.size
    }

    class ViewHolder(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.run {
                tvUserName.text = user.name
                wins.text = user.wins.toString()
                loses.text = user.loses.toString()
                ties.text = user.ties.toString()
            }
        }
    }
}