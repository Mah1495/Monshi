package com.example.callcenter.utils

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDateTime


interface IGroupable {
    val name: String
    val number: String
    fun compare(item: IGroupable): Boolean
    fun groupName(): String
}

interface IIcon {
    val icon: ImageVector
}

interface IDate {
    val date: LocalDateTime
}

interface IImage {
    val imageUri: String?
}