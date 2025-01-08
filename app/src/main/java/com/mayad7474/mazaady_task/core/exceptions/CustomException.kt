package com.mayad7474.mazaady_task.core.exceptions

import androidx.annotation.StringRes
import com.mayad7474.mazaady_task.core.constants.Strings
import com.mayad7474.mazaady_task.core.extensions.loge
import retrofit2.HttpException
import java.io.IOException

sealed class CustomException(
    @StringRes open val msg: Int = Strings.unknown_error
) : RuntimeException() {

    data class NoInternet(override val msg: Int = Strings.check_your_network_connection) :
        CustomException(msg)

    data class HttpErrorException(val httpError: HttpError) : CustomException(httpError.msg)
    data class Validation(override val msg: Int = Strings.invalid_input) : CustomException(msg)
    data class Unknown(val exception: Throwable) : CustomException(Strings.unknown_error)
}

enum class HttpError(val code: Int, @StringRes val msg: Int) {
    BadRequest(400, Strings.bad_request),
    Unauthorized(401, Strings.unauthorized),
    Forbidden(403, Strings.forbidden),
    NotFound(404, Strings.not_found),
    InternalServerError(500, Strings.internal_server_error), // Specific internal server error
    GenericServerError(-1, Strings.generic_server_error),   // For other 5xx errors
    Unknown(-1, Strings.unknown_error); // Default for unmapped errors

    companion object {
        fun fromCode(code: Int): HttpError {
            return when (code) {
                400 -> BadRequest
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                500 -> InternalServerError
                in 500..599 -> GenericServerError
                else -> Unknown
            }
        }
    }
}

fun Throwable.toAppError(): CustomException {
    return when (this) {
        is IOException -> {
            logError("Network error: No internet connection", this)
            CustomException.NoInternet()
        }

        is HttpException -> {
            val httpError = HttpError.fromCode(this.code())
            logError(
                "HTTP error: ${httpError.name} (Code: ${this.code()}) - ${this.message()}",
                this
            )
            CustomException.HttpErrorException(httpError)
        }

        is IllegalArgumentException -> {
            logError("Validation error: Invalid input", this)
            CustomException.Validation()
        }

        else -> {
            logError("Unknown error: ${this.localizedMessage}", this)
            CustomException.Unknown(this)
        }
    }
}

private fun logError(message: String, throwable: Throwable? = null) {
    "throwable: $throwable - message: $message".loge()
}

