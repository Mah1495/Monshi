package com.example.callcenter.screens.incoming_call

import androidx.lifecycle.ViewModel
import com.example.callcenter.CallHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IncomingCallViewModel @Inject constructor(val callHandler: CallHandler) : ViewModel() {

}