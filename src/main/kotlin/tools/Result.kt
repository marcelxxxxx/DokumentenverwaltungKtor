package online.marcel.tools

import kotlinx.serialization.Serializable

@Serializable
class Result<T> {

    private val errorlist: MutableList<String> = mutableListOf()
    private var result: T? = null

    fun setResult(result: T?) {
        this.result = result
    }

    fun unwrapOrNull(): T? {
        return this.result
    }

    fun unwrapOrError(): T {
        return this.result!!
    }

    fun isResultNull(): Boolean {
        return (this.result == null)
    }

    fun isSuccess(): Boolean {
        return this.errorlist.isEmpty()
    }

    fun addError(errormessage: String) {
        this.errorlist.add(errormessage)
    }

    fun getErrorList(): String {
        return this.errorlist.joinToString("\n")
    }

}