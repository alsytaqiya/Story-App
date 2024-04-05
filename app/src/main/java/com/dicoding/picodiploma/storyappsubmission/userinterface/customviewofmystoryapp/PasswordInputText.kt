package com.dicoding.picodiploma.storyappsubmission.userinterface.customviewofmystoryapp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.picodiploma.storyappsubmission.R

class PasswordInputText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        passwordText()
    }

    constructor(context: Context, attributset: AttributeSet) : super(context, attributset) {
        passwordText()
    }

    constructor(context: Context, attributset: AttributeSet, styleAttribut: Int) : super(
        context,
        attributset,
        styleAttribut
    ) {
        passwordText()
    }

    private fun passwordText() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                passText: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                passText: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                if (passText.toString().length < 6) {
                    error = resources.getString(R.string.invalid_user_password)
                } else if (passText.toString().length > 6) {
                    error = null
                }
            }

            override fun afterTextChanged(passText: Editable?) {
            }

        })
    }
}