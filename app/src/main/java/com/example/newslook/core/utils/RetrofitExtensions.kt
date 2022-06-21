package com.example.newslook.core.utils

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Retrofit

inline fun <reified T> Retrofit.create(): T = create(T::class.java)

fun <T> httpError(code: Int): Response<T> = Response.error<T>(code, "".toResponseBody(null))