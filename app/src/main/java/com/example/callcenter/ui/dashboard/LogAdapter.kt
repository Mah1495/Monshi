package com.example.callcenter.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.callcenter.R
import com.example.callcenter.databinding.LogItemBinding
import com.example.callcenter.entities.CallLog

class LogAdapter(val items: List<CallLog>) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    lateinit var binding: LogItemBinding

    inner class LogViewHolder(binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(log: CallLog) {
            binding.numberV.text = "${log.name} -> ${log.number} ${log.date.format()}"
            binding.date.text = log.date.format()
            binding.typeIcon.setImageResource(R.drawable.incoming_call)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        binding = LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.setItem(items[position])
    }
}