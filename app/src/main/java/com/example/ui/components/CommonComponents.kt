package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.SpiceLevel
import com.example.ui.Screen
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.GoldenYellowContainer
import com.example.ui.theme.OnGoldenYellowContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary

@Composable
fun TastyGoTopBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    cartItemCount: Int,
    onCartClick: () -> Unit,
    isAdminLoggedIn: Boolean,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (canNavigateBack) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(WarmRedPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fastfood,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (!canNavigateBack) {
                        Text(
                            text = "Artisan Food • Dine-in & Delivery",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Admin Toggle Button
                Surface(
                    onClick = onAdminClick,
                    shape = RoundedCornerShape(20.dp),
                    color = if (isAdminLoggedIn) WarmRedPrimary else GoldenYellowContainer,
                    modifier = Modifier.testTag("btn_admin_portal")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Portal",
                            tint = if (isAdminLoggedIn) Color.White else OnGoldenYellowContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAdminLoggedIn) "Admin" else "Staff",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isAdminLoggedIn) Color.White else OnGoldenYellowContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Cart Icon with Badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_cart_topbar")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = WarmRedPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(text = "$cartItemCount", fontSize = 11.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Cart",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerBottomNav(
    currentScreen: Screen,
    cartItemCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier
            .navigationBarsPadding()
            .testTag("customer_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onNavigate(Screen.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.CATEGORY_MENU,
            onClick = { onNavigate(Screen.CATEGORY_MENU) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.CATEGORY_MENU) Icons.Filled.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                    contentDescription = "Menu"
                )
            },
            label = { Text("Menu") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("nav_menu")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.CART || currentScreen == Screen.CHECKOUT,
            onClick = { onNavigate(Screen.CART) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = WarmRedPrimary,
                                contentColor = Color.White
                            ) {
                                Text("$cartItemCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentScreen == Screen.CART) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                        contentDescription = "Cart"
                    )
                }
            },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("nav_cart")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ORDER_HISTORY || currentScreen == Screen.ORDER_TRACKING,
            onClick = { onNavigate(Screen.ORDER_HISTORY) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.ORDER_HISTORY) Icons.Filled.History else Icons.Outlined.History,
                    contentDescription = "Orders"
                )
            },
            label = { Text("Orders") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("nav_orders")
        )
    }
}

@Composable
fun AdminBottomNav(
    currentScreen: Screen,
    pendingOrdersCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier
            .navigationBarsPadding()
            .testTag("admin_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.ADMIN_DASHBOARD,
            onClick = { onNavigate(Screen.ADMIN_DASHBOARD) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Dashboard") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("admin_nav_dashboard")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ADMIN_INCOMING_ORDERS || currentScreen == Screen.ADMIN_ORDER_DETAIL,
            onClick = { onNavigate(Screen.ADMIN_INCOMING_ORDERS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (pendingOrdersCount > 0) {
                            Badge(
                                containerColor = WarmRedPrimary,
                                contentColor = Color.White
                            ) {
                                Text("$pendingOrdersCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "Orders"
                    )
                }
            },
            label = { Text("Incoming") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("admin_nav_orders")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ADMIN_MENU_MANAGEMENT,
            onClick = { onNavigate(Screen.ADMIN_MENU_MANAGEMENT) },
            icon = {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = "Menu"
                )
            },
            label = { Text("Menu") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("admin_nav_menu")
        )

        NavigationBarItem(
            selected = false,
            onClick = { onNavigate(Screen.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Customer View"
                )
            },
            label = { Text("Customer") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = WarmRedPrimary,
                selectedTextColor = WarmRedPrimary,
                indicatorColor = WarmRedContainer
            ),
            modifier = Modifier.testTag("admin_nav_customer_view")
        )
    }
}

@Composable
fun StatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        OrderStatus.RECEIVED -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
        OrderStatus.PREPARING -> GoldenYellowContainer to OnGoldenYellowContainer
        OrderStatus.READY -> Color(0xFFDCFCE7) to Color(0xFF15803D)
        OrderStatus.SERVED_DELIVERED -> SuccessGreenContainer to Color(0xFF065F46)
        OrderStatus.CANCELLED -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = status.displayName,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SpiceIndicator(
    spiceLevel: SpiceLevel,
    modifier: Modifier = Modifier
) {
    if (spiceLevel == SpiceLevel.NONE) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(spiceLevel.pepperCount) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Spice Level ${spiceLevel.label}",
                tint = WarmRedPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = spiceLevel.label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = WarmRedPrimary,
            fontSize = 11.sp
        )
    }
}
