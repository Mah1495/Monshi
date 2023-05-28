package com.example.callcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.callcenter.databinding.AddContactBinding
import com.example.callcenter.entities.Contact
import com.example.callcenter.ui.notifications.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactActivity : AppCompatActivity() {
    lateinit var binding: AddContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm by viewModels<ContactsViewModel>()
        binding = AddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitV.setOnClickListener {
            lifecycleScope.launch {
                vm.add(Contact(0, "test", listOf("123345"), emptyList()))
                finish()
            }
        }
    }
}