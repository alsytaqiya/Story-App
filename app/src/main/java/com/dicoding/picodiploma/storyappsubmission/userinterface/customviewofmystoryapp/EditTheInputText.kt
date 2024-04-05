package com.dicoding.picodiploma.storyappsubmission.userinterface.customviewofmystoryapp

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.picodiploma.storyappsubmission.R

class EditTheInputText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        edittingUserEmail()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        edittingUserEmail()
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        styleAttributset: Int
    ) : super(context, attributeSet, styleAttributset) {
        edittingUserEmail()
    }

    private fun edittingUserEmail() {

        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                eText: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }

            override fun onTextChanged(eText: CharSequence?, start: Int, before: Int, count: Int) {
                if (eText.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(eText ?: "")
                        .matches()
                ) {
                    error = resources.getString(R.string.invalid_user_email)
                }
            }

            override fun afterTextChanged(eText: Editable?) {
            }

        })
    }
}