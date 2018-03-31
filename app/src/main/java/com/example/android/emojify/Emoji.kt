package com.example.android.emojify

sealed class Emoji {
    object SMILE: Emoji()
    object FROWN: Emoji()
    object RIGHT_WINK: Emoji()
    object LEFT_WINK: Emoji()
    object RIGHT_WINK_FROWN: Emoji()
    object LEFT_WINK_FROWN: Emoji()
    object CLOSED_EYE_SMILE: Emoji()
    object CLOSED_EYE_FROWN: Emoji()
}