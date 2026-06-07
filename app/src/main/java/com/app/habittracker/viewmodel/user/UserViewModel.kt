package com.app.habittracker.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.habittracker.data.User
import com.app.habittracker.repository.UserRepository
import com.app.habittracker.utils.calculateLevel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _levelUpEvent = Channel<Int>(Channel.Factory.CONFLATED)
    val levelUpEvent = _levelUpEvent.receiveAsFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val user: StateFlow<User?> = repository.getUserFlow()
        .onEach { _isLoading.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )

    fun loadUser() {
        // isLoading is handled by the Flow collector above
    }

    fun addXp(amount: Int) {
        viewModelScope.launch {
            repository.getUser()?.let { currentUser ->
                val newXp = maxOf(0, currentUser.xp + amount)
                val newLevel = calculateLevel(newXp)

                if (newLevel > currentUser.level) {
                    _levelUpEvent.trySend(newLevel)
                }

                repository.saveUser(
                    currentUser.copy(
                        xp = newXp,
                        level = newLevel
                    )
                )
            }
        }
    }

    fun saveUser(
        name: String,
        theme: String = "Amber",
        onSaved: () -> Unit
    ) {

        viewModelScope.launch {

            repository.saveUser(
                User(
                    name = name,
                    appTheme = theme
                )
            )
            onSaved()
        }
    }
}