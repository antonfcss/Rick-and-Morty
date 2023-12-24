package com.example.rickmorty.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel<State> : ViewModel() {

    private val state = MutableLiveData<ViewState<State>>()

    private val emptyDataLiveData = MutableLiveData<String?>()

    fun getEmptyDataLiveData(): LiveData<String?> = emptyDataLiveData

    val viewModelState: LiveData<ViewState<State>>
        get() = state

    protected fun showEmptyDataDialog(emptyDataError: String) {
        emptyDataLiveData.postValue(emptyDataError)
    }

    fun onPostEmptyDataDialogShown() {
        emptyDataLiveData.postValue(null)
    }


    protected fun updateState(newState: ViewState<State>) {
        state.postValue(newState)
    }

    fun ViewModel.launchIO(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }
}