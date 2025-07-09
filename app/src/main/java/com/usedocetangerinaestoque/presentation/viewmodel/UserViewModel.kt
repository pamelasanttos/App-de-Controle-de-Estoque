package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.services.SessionManager
import com.usedocetangerinaestoque.usecases.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun loadUser() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == -1L) {
                handleInvalidSession()
                return@launch
            }

            val user = userDao.getById(userId)
            if (user == null) {
                handleUserNotFound()
            } else {
                _user.postValue(user)
            }
        }
    }

    fun updateUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updated = updateUserUseCase(user)
                _user.postValue(updated)
                onSuccess(updated)
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao atualizar usuário")
            }
        }
    }

    private fun handleInvalidSession() {
        _user.postValue(null)
    }

    private fun handleUserNotFound() {
        sessionManager.clearSession()
        _user.postValue(null)
    }
}