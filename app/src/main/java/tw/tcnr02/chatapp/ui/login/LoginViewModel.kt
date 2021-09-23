package tw.tcnr02.chatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tw.tcnr02.chatapp.util.Constants
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    //   Can make the network calls
    private val client: ChatClient
    ) :ViewModel() {

        private val _loginEvent = MutableSharedFlow<LoginEvent>()
        val loginEvent  = _loginEvent.asSharedFlow()

        private fun isValidUsername(username:String) =
            username.length >= Constants.MIN_USERNAME_LENGTH

    fun connectUser(username:String){
        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if(isValidUsername(trimmedUsername)){
                val result = client.connectGuestUser(
                    userId = trimmedUsername,
                    username = trimmedUsername
                ).await()
                if(result.isError){
                    _loginEvent.emit(LoginEvent.ErrorLogin(result.error().message ?: "Unknown error"))
                    return@launch
                }
                _loginEvent.emit(LoginEvent.Success)
            }else{
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }
    sealed class LoginEvent{
        object ErrorInputTooShort:LoginEvent()
        data class ErrorLogin(val error:String):LoginEvent()
        object Success:LoginEvent()
    }
}