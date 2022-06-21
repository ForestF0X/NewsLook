package com.example.newslook.core.ui

sealed class ViewState<ResultType> {

    data class Success<ResultType>(
            val data: ResultType
    ) : ViewState<ResultType>()

    class Loading<ResultType> : ViewState<ResultType>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int = javaClass.hashCode()
    }

    companion object {
        fun <ResultType> success(data: ResultType): ViewState<ResultType> = Success(data)

        fun <ResultType> loading(): ViewState<ResultType> = Loading()
    }
}