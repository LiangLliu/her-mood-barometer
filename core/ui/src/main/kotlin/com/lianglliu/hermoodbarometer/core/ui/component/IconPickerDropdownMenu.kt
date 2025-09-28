package com.lianglliu.hermoodbarometer.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.AccountBalance
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Apparel
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Attractions
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Autorenew
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.BakeryDining
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Bitcoin
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Book
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Category
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Chair
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.CreditCard
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Dentistry
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Diamond
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.DirectionsBus
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Exercise
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Fastfood
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Handyman
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Language
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.LocalBar
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.LocalGasStation
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.LocalTaxi
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Memory
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Monitoring
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Movie
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.MusicNote
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Payments
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Percent
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Pets
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Phishing
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Pill
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.ReceiptLong
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Restaurant
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.School
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Scooter
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.SelfCare
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.ShoppingCart
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.SimCard
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.SmokingRooms
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.SportsEsports
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Travel
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Work

@Composable
fun IconPickerDropdownMenu(
    currentIcon: ImageVector,
    onSelectedIconClick: (Int) -> Unit,
    onClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(
            onClick = {
                onClick()
                expanded = true
            }
        ) {
            Icon(
                imageVector = currentIcon,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            FlowRow(maxItemsInEachRow = 5) {
                StoredIcon.entries.forEach { icon ->
                    IconButton(
                        onClick = {
                            onSelectedIconClick(icon.storedId)
                            expanded = false
                        }
                    ) {
                        Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

enum class StoredIcon(
    val imageVector: ImageVector,
    val storedId: Int,
) {
    CATEGORY(AppIcons.Outlined.Category, 0),
    ACCOUNT_BALANCE(AppIcons.Outlined.AccountBalance, 1),
    APPAREL(AppIcons.Outlined.Apparel, 2),
    CHAIR(AppIcons.Outlined.Chair, 3),
    EXERCISE(AppIcons.Outlined.Exercise, 4),
    FASTFOOD(AppIcons.Outlined.Fastfood, 5),
    RESTAURANT(AppIcons.Outlined.Restaurant, 17),
    BAKERY_DINING(AppIcons.Outlined.BakeryDining, 37),
    DIRECTIONS_BUS(AppIcons.Outlined.DirectionsBus, 6),
    LOCAL_TAXI(AppIcons.Outlined.LocalTaxi, 35),
    SCOOTER(AppIcons.Outlined.Scooter, 36),
    HANDYMAN(AppIcons.Outlined.Handyman, 7),
    LANGUAGE(AppIcons.Outlined.Language, 8),
    LOCAL_BAR(AppIcons.Outlined.LocalBar, 9),
    LOCAL_GAS_STATION(AppIcons.Outlined.LocalGasStation, 10),
    MEMORY(AppIcons.Outlined.Memory, 11),
    PAYMENTS(AppIcons.Outlined.Payments, 12),
    PETS(AppIcons.Outlined.Pets, 13),
    PHISHING(AppIcons.Outlined.Phishing, 14),
    PILL(AppIcons.Outlined.Pill, 15),
    TRANSACTION(AppIcons.Outlined.ReceiptLong, 16),
    SCHOOL(AppIcons.Outlined.School, 18),
    SELF_CARE(AppIcons.Outlined.SelfCare, 19),
    SHOPPING_CART(AppIcons.Outlined.ShoppingCart, 20),
    SIM_CARD(AppIcons.Outlined.SimCard, 21),
    SMOKING_ROOMS(AppIcons.Outlined.SmokingRooms, 22),
    SPORTS_ESPORTS(AppIcons.Outlined.SportsEsports, 23),
    TRAVEL(AppIcons.Outlined.Travel, 24),
    ATTRACTIONS(AppIcons.Outlined.Attractions, 25),
    CREDIT_CARD(AppIcons.Outlined.CreditCard, 26),
    MONITORING(AppIcons.Outlined.Monitoring, 27),
    MUSIC_NOTE(AppIcons.Outlined.MusicNote, 28),
    WORK(AppIcons.Outlined.Work, 29),
    BITCOIN(AppIcons.Outlined.Bitcoin, 30),
    BOOK(AppIcons.Outlined.Book, 31),
    DENTISTRY(AppIcons.Outlined.Dentistry, 32),
    DIAMOND(AppIcons.Outlined.Diamond, 33),
    MOVIE(AppIcons.Outlined.Movie, 34),
    PERCENT(AppIcons.Outlined.Percent, 38),
    AUTORENEW(AppIcons.Outlined.Autorenew, 39);

    companion object {

        fun asImageVector(storedId: Int?): ImageVector =
            entries.find { it.storedId == storedId }?.imageVector ?: CATEGORY.imageVector
    }
}