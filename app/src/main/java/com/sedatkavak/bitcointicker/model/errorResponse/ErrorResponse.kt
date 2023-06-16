package com.sedatkavak.bitcointicker.model.errorResponse

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    val status: Status?
)