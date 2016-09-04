package src.com.djulia.transactions

import org.mockito.Mockito

fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

fun <T> any(): T {
    return Mockito.anyObject<T>()
}

fun <T> whenever(mock : T) = org.mockito.Mockito.`when`(mock)

fun <T> upon(): T {
    return Mockito.anyObject<T>()
}