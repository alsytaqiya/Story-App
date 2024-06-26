package com.dicoding.picodiploma.storyappsubmission.data.modeldata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginDataClass(
    val name: String,
    val email: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean
) : Parcelable
