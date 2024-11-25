package id.co.mondo.storyapp.helper

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

class MyEmail: TextInputEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!isValidEmail(s.toString())) {
            setError("Format email tidak valid", null)
        } else {
            error = null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}