package com.mashup.ipdam.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mashup.base.BaseViewModel
import com.mashup.base.schedulers.SchedulerProvider
import com.mashup.ipdam.SingleLiveEvent
import com.mashup.ipdam.data.datastore.AuthorizationDataStore
import com.mashup.ipdam.data.datastore.UserDataStore
import com.mashup.ipdam.entity.user.User
import com.mashup.ipdam.error.NotFoundUserException
import com.mashup.ipdam.network.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userService: UserService,
    private val userDataStore: UserDataStore
) : BaseViewModel() {
    override var logTag: String = "LoginViewModel"

    private val _showMainViewEvent = SingleLiveEvent<Unit>()
    val showMainViewEvent: SingleLiveEvent<Unit> = _showMainViewEvent
    private val _isUserNotFound = SingleLiveEvent<Unit>()
    val isUserNotFound: SingleLiveEvent<Unit> = _isUserNotFound
    private val _isLogin = SingleLiveEvent<Boolean>()
    val isLogin: SingleLiveEvent<Boolean> = _isLogin
    val inputId = MutableLiveData("")
    val inputPassword = MutableLiveData("")

    fun login() {
        _isLogin.value = true
        val id = inputId.value
        val password = inputPassword.value

        if (id == null || password == null) {
            _isLogin.value = false
        } else {
        userService.login(id, password)
            .subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.ui())
            .subscribe(
                { user ->
                    setIdWhenLoginSuccess(user)
                }, { exception ->
                    if (exception is NotFoundUserException) {
                        _isUserNotFound.call()
                    }
                    _isLogin.value = false
                    Log.e(logTag, exception.stackTraceToString())
                }
            ).addToDisposable()
        }
    }

    private fun setIdWhenLoginSuccess(user: User) {
        userDataStore.saveId(user.id)
            .subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.ui())
            .subscribe(
                {
                    _showMainViewEvent.call()
                    _isLogin.value = false
                }, { exception ->
                    _isLogin.value = false
                    Log.e(logTag, exception.stackTraceToString())
                }
            ).addToDisposable()
    }
}