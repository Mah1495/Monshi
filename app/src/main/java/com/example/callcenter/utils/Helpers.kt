package com.example.callcenter.utils

fun CharSequence.removeLast(): CharSequence {
    if (!this.isNullOrEmpty()) {
        return this.substring(0, this.length - 1)
    }
    return this
}
