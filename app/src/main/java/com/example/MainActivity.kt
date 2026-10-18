package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.RestaurantViewModel
import com.example.ui.Screen
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminIncomingOrdersScreen
import com.example.ui.admin.AdminLoginScreen
import com.example.ui.admin.AdminMenuManagementScreen
import com.example.ui.admin.AdminOrderDetailScreen
import com.example.ui.components.AdminBottomNav
import com.example.ui.components.CustomerBottomNav
import com.example.ui.components.TastyGoTopBar
import com.example.ui.customer.CartScreen
import com.example.ui.customer.CategoryMenuScreen
import com.example.ui.customer.CheckoutScreen
import com.example.ui.customer.HomeScreen
import com.example.ui.customer.ItemDetailScreen
import com.example.ui.customer.OrderHistoryScreen
import com.example.ui.customer.OrderTrackingScreen
import com.example.ui.theme.TastyGoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TastyGoTheme {
                TastyGoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TastyGoApp(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItemCount by viewModel.cartItemCount.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val pendingOrdersCount by viewModel.pendingOrdersCount.collectAsState()

    // Handle Android system back gesture
    val canNavigateBack = currentScreen != Screen.HOME
    BackHandler(enabled = canNavigateBack) {
        viewModel.navigateBack()
    }

    val topBarTitle = when (currentScreen) {
        Screen.HOME -> "TastyGo"
        Screen.CATEGORY_MENU -> "Menu"
        Screen.ITEM_DETAIL -> "Customization"
        Screen.CART -> "Your Cart"
        Screen.CHECKOUT -> "Checkout"
        Screen.ORDER_TRACKING -> "Track Order"
        Screen.ORDER_HISTORY -> "Order History"
        Screen.ADMIN_LOGIN -> "Staff Login"
        Screen.ADMIN_DASHBOARD -> "Dashboard"
        Screen.ADMIN_MENU_MANAGEMENT -> "Menu Catalog"
        Screen.ADMIN_INCOMING_ORDERS -> "Kitchen Feed"
        Screen.ADMIN_ORDER_DETAIL -> "Order Detail"
    }

    val isAdminScreen = currentScreen in listOf(
        Screen.ADMIN_DASHBOARD,
        Screen.ADMIN_MENU_MANAGEMENT,
        Screen.ADMIN_INCOMING_ORDERS,
        Screen.ADMIN_ORDER_DETAIL
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TastyGoTopBar(
                title = topBarTitle,
                canNavigateBack = canNavigateBack,
                onNavigateBack = { viewModel.navigateBack() },
                cartItemCount = cartItemCount,
                onCartClick = { viewModel.navigateTo(Screen.CART) },
                isAdminLoggedIn = isAdminLoggedIn,
                onAdminClick = {
                    if (isAdminLoggedIn) {
                        viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                    } else {
                        viewModel.navigateTo(Screen.ADMIN_LOGIN)
                    }
                }
            )
        },
        bottomBar = {
            if (isAdminScreen) {
                AdminBottomNav(
                    currentScreen = currentScreen,
                    pendingOrdersCount = pendingOrdersCount,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            } else if (currentScreen != Screen.ADMIN_LOGIN && currentScreen != Screen.ITEM_DETAIL && currentScreen != Screen.CHECKOUT) {
                CustomerBottomNav(
                    currentScreen = currentScreen,
                    cartItemCount = cartItemCount,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                Screen.HOME -> HomeScreen(viewModel = viewModel)
                Screen.CATEGORY_MENU -> CategoryMenuScreen(viewModel = viewModel)
                Screen.ITEM_DETAIL -> ItemDetailScreen(viewModel = viewModel)
                Screen.CART -> CartScreen(viewModel = viewModel)
                Screen.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                Screen.ORDER_TRACKING -> OrderTrackingScreen(viewModel = viewModel)
                Screen.ORDER_HISTORY -> OrderHistoryScreen(viewModel = viewModel)
                Screen.ADMIN_LOGIN -> AdminLoginScreen(viewModel = viewModel)
                Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(viewModel = viewModel)
                Screen.ADMIN_MENU_MANAGEMENT -> AdminMenuManagementScreen(viewModel = viewModel)
                Screen.ADMIN_INCOMING_ORDERS -> AdminIncomingOrdersScreen(viewModel = viewModel)
                Screen.ADMIN_ORDER_DETAIL -> AdminOrderDetailScreen(viewModel = viewModel)
            }
        }
    }
}
