package com.example.callcenter.screens.recent_call

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.callcenter.utils.IDate
import com.example.callcenter.utils.IGroupable
import com.example.callcenter.utils.IIcon
import com.example.callcenter.utils.getDate
import com.example.callcenter.utils.isSameAs
import java.time.LocalDate
import java.time.LocalDateTime

data class RecentCall(
    override val name: String,
    override val date: LocalDateTime,
    override val icon: ImageVector,
    override val number: String
) : IGroupable, IIcon, IDate {
    override fun compare(item: IGroupable): Boolean {
        if (item is IDate) return date.isSameAs(item.date)
        return false
    }

    override fun groupName(): String {
        return date.getDate()
    }
}