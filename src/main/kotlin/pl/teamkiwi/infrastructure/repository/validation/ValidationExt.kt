package pl.teamkiwi.infrastructure.repository.validation

import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
import org.valiktor.validate
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun <E: Exception, T> validateOrThrow(exception: KClass<E>, obj: T, block: Validator<T>.(T) -> Unit): T {
    try {
        validate(obj, block)
    } catch (e: ConstraintViolationException) {
        throw exception.createInstance().initCause(e)
    }

    return obj
}