package com.bumantra.mystoryapp.ui.customeview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.bumantra.mystoryapp.R

class MyTextInput : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(
                text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int
            ) {
                validateTextInput(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    private fun validateTextInput(text: String) {
        if (id == R.id.ed_login_email || id == R.id.ed_register_email) {
            if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                setError("Email tidak valid", null)
            } else {
                error = null
            }
        } else if (id == R.id.ed_login_password || id == R.id.ed_register_password) {
            if (text.length < 8) {
                setError("Password harus lebih dari 8 karakter", null)
            } else {
                error = null
            }
        }
    }
}