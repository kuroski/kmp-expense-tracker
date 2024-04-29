package utils

sealed class RemoteData<out E, out A> {
    data object NotAsked : RemoteData<Nothing, Nothing>()

    data object Loading : RemoteData<Nothing, Nothing>()

    data class Success<out E, out A>(val data: A) : RemoteData<E, A>()

    data class Failure<out E, out A>(val error: E) : RemoteData<E, A>()

    companion object {
        fun <A> success(data: A): RemoteData<Nothing, A> = Success(data)

        fun <E> failure(error: E): RemoteData<E, Nothing> = Failure(error)
    }
}

fun <A, E> RemoteData<E, A>.getOrElse(otherData: A): A =
    when (this) {
        is RemoteData.Success -> data
        else -> otherData
    }
