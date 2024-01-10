package app.wastelesseats.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wastelesseats.data.AuthRepository
import app.wastelesseats.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email, password).collect{result ->
            when(result){
                is Resource.Success -> {
                    _registerState.send(RegisterState(isSuccess = "Register Success"))

                }
                is Resource.Loading -> {
                    _registerState.send(RegisterState(isLoading = true))

                }
                is Resource.Error -> {
                    _registerState.send(RegisterState(isError = result.message))
                }
            }
        }
    }
}