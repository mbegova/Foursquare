package com.miguel.myapplication.datasource


/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    INIT
}

const val NO_ERROR = 10
const val API_ERROR = 101
const val UNHANDLE_ERROR_CODE = 111

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val errorCode: Int?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(errorCode: Int? = 999, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, errorCode)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> init(): Resource<T> {
            return Resource(Status.INIT, null, null)
        }
    }
}
