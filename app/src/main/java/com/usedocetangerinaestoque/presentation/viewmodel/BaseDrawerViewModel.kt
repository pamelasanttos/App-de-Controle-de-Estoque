package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.services.SessionManager
import com.usedocetangerinaestoque.usecases.user.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseDrawerViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun loadUser() {
        viewModelScope.launch {
            val userId = getLoggedUserId()
            if (userId == null) {
                _user.postValue(null)
                return@launch
            }

            fetchUserById(userId)
        }
    }

    private fun getLoggedUserId(): Long? {
        val id = sessionManager.getUserId()
        return if (id == -1L) null else id
    }

    private suspend fun fetchUserById(userId: Long) {
        try {
            val user = getUserByIdUseCase(userId)
            _user.postValue(user)
        } catch (e: Exception) {
            _user.postValue(null)
        }
    }
}