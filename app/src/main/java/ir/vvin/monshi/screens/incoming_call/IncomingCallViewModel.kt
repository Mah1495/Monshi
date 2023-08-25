package ir.vvin.monshi.screens.incoming_call

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.vvin.monshi.CallHandler
import javax.inject.Inject

@HiltViewModel
class IncomingCallViewModel @Inject constructor(val callHandler: CallHandler) : ViewModel() {

}