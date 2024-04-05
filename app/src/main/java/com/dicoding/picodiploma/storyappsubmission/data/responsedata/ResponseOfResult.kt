package com.dicoding.picodiploma.storyappsubmission.data.responsedata

sealed class ResponseOfResult<out R> private constructor() {
    data class Success<out T>(val data: T) : ResponseOfResult<T>()
    data class Error(val error: String) : ResponseOfResult<Nothing>()
    object Loading : ResponseOfResult<Nothing>()
}

