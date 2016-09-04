package com.djulia.banking.testhelpers

import org.mockito.Mockito

fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

fun <T> any(): T {
    return Mockito.anyObject<T>()
}

fun <T> whenever(mock : T) = Mockito.`when`(mock)

fun <T> upon(): T {
    return Mockito.anyObject<T>()
}