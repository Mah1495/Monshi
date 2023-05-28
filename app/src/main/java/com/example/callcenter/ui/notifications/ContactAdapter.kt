package com.example.callcenter.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.callcenter.R
import com.example.callcenter.databinding.ContactItemBinding
import com.example.callcenter.entities.Contact

class ContactAdapter(private val contacts: List<Contact>) :
    Adapter<ContactAdapter.ContactViewHolder>() {
    lateinit var binding: ContactItemBinding

    inner class ContactViewHolder(binding: ContactItemBinding) : ViewHolder(binding.root) {
        fun setData(contact: Contact) {
            binding.name.text = contact.name
            binding.picV.setImageResource(R.drawable.ic_clear)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.setData(contacts[position])
    }
}