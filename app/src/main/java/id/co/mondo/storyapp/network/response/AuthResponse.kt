package id.co.mondo.storyapp.network.response

import com.google.gson.annotations.SerializedName

    data class RegisterResponse(

        @field:SerializedName("error")
        val error: Boolean? = null,

        @field:SerializedName("message")
        val message: String? = null
    )

    data class LoginResponse(

        @field:SerializedName("error")
        val error: Boolean? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("loginResult")
        val loginResult: User
    )


    data class User(

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("userId")
        val userId: String,

        @field:SerializedName("token")
        val token: String
    )