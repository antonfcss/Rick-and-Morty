package com.example.rickmorty.base

sealed class ViewState<out ViewStateType> {
    data class Success<ViewStateType>(val data: ViewStateType) : ViewState<ViewStateType>()
    data class Error(val message: String) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
}
