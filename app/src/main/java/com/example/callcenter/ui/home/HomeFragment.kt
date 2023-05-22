package com.example.callcenter.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.callcenter.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tm: TelecomManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.keypad.target.text.clear()
        tm = activity?.getSystemService(Activity.TELECOM_SERVICE) as TelecomManager

        binding.keypad.target.showSoftInputOnFocus = false
        binding.keypad.clear.setOnClickListener {
            val cursorPosition: Int = binding.keypad.target.selectionStart
            if (cursorPosition > 0) {
                binding.keypad.target.text =
                    binding.keypad.target.text.delete(cursorPosition - 1, cursorPosition)
                binding.keypad.target.setSelection(cursorPosition - 1)
            }
        }
        binding.keypad.clear.setOnLongClickListener {
            binding.keypad.target.text.clear()
            return@setOnLongClickListener true
        }
        binding.call.setOnClickListener {
            val uri = Uri.fromParts("tel", binding.keypad.target.text.toString(), null)
            val extras = Bundle()
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
            tm?.placeCall(uri, extras)
        }
        binding.root.allViews.forEach {
            if (it is Button && it.text.length == 1) it.setOnClickListener(
                this
            )
        }
        return root
    }

    override fun onClick(p0: View?) {
        if (p0 is Button) {
            val cursorPosition: Int = binding.keypad.target.selectionStart
            binding.keypad.target.text.insert(cursorPosition, p0.text)
            binding.keypad.target.setSelection(cursorPosition + 1)
        }
//        binding.keypad.target.text = "${binding.keypad.target.text}${(p0 as? Button)?.text}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}