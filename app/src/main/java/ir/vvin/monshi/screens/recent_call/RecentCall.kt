package ir.vvin.monshi.screens.recent_call

import androidx.compose.ui.graphics.vector.ImageVector
import ir.vvin.monshi.utils.IDate
import ir.vvin.monshi.utils.IGroupable
import ir.vvin.monshi.utils.IIcon
import ir.vvin.monshi.utils.getDate
import ir.vvin.monshi.utils.isSameAs
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