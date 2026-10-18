package com.example.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MenuCategory
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.SpiceLevel
import com.example.ui.RestaurantViewModel
import com.example.ui.Screen
import com.example.ui.components.SpiceIndicator
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.GoldenYellowContainer
import com.example.ui.theme.OnGoldenYellowContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -------------------------------------------------------------
// 8. Staff / Admin Login Screen
// -------------------------------------------------------------
@Composable
fun AdminLoginScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    var pin by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_login"),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = WarmRedContainer,
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = WarmRedPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Staff & Kitchen Portal",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "Enter staff PIN or manager password",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it
                        loginError = false
                    },
                    label = { Text("Staff PIN / Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = WarmRedPrimary)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WarmRedPrimary,
                        unfocusedBorderColor = WarmBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_admin_pin")
                )

                if (loginError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Invalid PIN. Try '1234' for Manager or '0000' for Kitchen",
                        color = WarmRedPrimary,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Demo Login Pills
                Text(
                    text = "Quick Demo Access:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        onClick = { pin = "1234" },
                        shape = RoundedCornerShape(10.dp),
                        color = GoldenYellowContainer,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_quick_pin_1234")
                    ) {
                        Text(
                            text = "Manager (1234)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnGoldenYellowContainer,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Surface(
                        onClick = { pin = "0000" },
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE0F2FE),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_quick_pin_0000")
                    ) {
                        Text(
                            text = "Kitchen (0000)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0369A1),
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val ok = viewModel.loginAdmin(pin)
                        if (!ok) loginError = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_admin_login")
                ) {
                    Text("Sign In to Portal", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    modifier = Modifier.testTag("btn_cancel_admin_login")
                ) {
                    Text("Return to Customer App", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 9. Admin Dashboard Screen
// -------------------------------------------------------------
@Composable
fun AdminDashboardScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val ordersToday by viewModel.ordersTodayCount.collectAsState()
    val revenueToday by viewModel.revenueToday.collectAsState()
    val pendingCount by viewModel.pendingOrdersCount.collectAsState()
    val orders by viewModel.orders.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_dashboard"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Logout Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TastyGo Restaurant Hub",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "Live kitchen & order management",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.logoutAdmin() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_admin_logout")
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Exit", fontSize = 12.sp)
                }
            }
        }

        // Stat Cards 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        title = "Orders Today",
                        value = "$ordersToday",
                        subtitle = "Active shifts",
                        icon = Icons.Default.ShoppingBag,
                        accentColor = WarmRedPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    DashboardStatCard(
                        title = "Revenue Today",
                        value = "$${String.format("%.2f", revenueToday)}",
                        subtitle = "+18% vs yesterday",
                        icon = Icons.Default.AttachMoney,
                        accentColor = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        title = "Pending Orders",
                        value = "$pendingCount",
                        subtitle = "Needs prep",
                        icon = Icons.Default.PendingActions,
                        accentColor = GoldenYellowAccent,
                        modifier = Modifier.weight(1f)
                    )

                    DashboardStatCard(
                        title = "Avg Prep Time",
                        value = "14 min",
                        subtitle = "Kitchen velocity",
                        icon = Icons.Default.Timer,
                        accentColor = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Navigation Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.ADMIN_INCOMING_ORDERS) },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_shortcut_orders")
                ) {
                    Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Live Orders (${pendingCount})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.ADMIN_MENU_MANAGEMENT) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_shortcut_menu")
                ) {
                    Icon(imageVector = Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Menu Manager", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Live Order Feed Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Order Feed",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = WarmRedPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { viewModel.navigateTo(Screen.ADMIN_INCOMING_ORDERS) }
                        .padding(4.dp)
                )
            }
        }

        // Recent Orders list
        items(orders.take(5)) { order ->
            AdminOrderFeedCard(
                order = order,
                onClick = { viewModel.openAdminOrderDetail(order) },
                onQuickStatusChange = { newStatus ->
                    viewModel.updateOrderStatus(order.id, newStatus)
                }
            )
        }
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AdminOrderFeedCard(
    order: Order,
    onClick: () -> Unit,
    onQuickStatusChange: (OrderStatus) -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_feed_${order.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.id,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (order.orderType == OrderType.DINE_IN) "🍽️ ${order.tableNumber}" else "🛵 Delivery",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.items.joinToString(", ") { "${it.quantity}x ${it.menuItem.name}" },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", order.total)} • ${order.customerName}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (order.status == OrderStatus.RECEIVED) {
                    Button(
                        onClick = { onQuickStatusChange(OrderStatus.PREPARING) },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Accept & Prep", fontSize = 12.sp)
                    }
                } else if (order.status == OrderStatus.PREPARING) {
                    Button(
                        onClick = { onQuickStatusChange(OrderStatus.READY) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenYellowAccent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Mark Ready", fontSize = 12.sp, color = Color(0xFF451A03), fontWeight = FontWeight.Bold)
                    }
                } else if (order.status == OrderStatus.READY) {
                    Button(
                        onClick = { onQuickStatusChange(OrderStatus.SERVED_DELIVERED) },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Mark Served", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 10. Menu Management Screen (Admin)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMenuManagementScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val menuItems by viewModel.menuItems.collectAsState()
    var selectedCat by remember { mutableStateOf<MenuCategory?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<MenuItem?>(null) }

    val filtered = if (selectedCat == null) menuItems else menuItems.filter { it.category == selectedCat }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_menu")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Menu Catalog (${menuItems.size})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_add_dish")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Dish", fontSize = 13.sp)
                }
            }

            // Category Filter Row
            ScrollableTabRow(
                selectedTabIndex = if (selectedCat == null) 0 else selectedCat!!.ordinal + 1,
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 16.dp,
                divider = {}
            ) {
                Tab(
                    selected = selectedCat == null,
                    onClick = { selectedCat = null },
                    text = { Text("All (${menuItems.size})") }
                )
                MenuCategory.values().forEach { cat ->
                    val count = menuItems.count { it.category == cat }
                    Tab(
                        selected = selectedCat == cat,
                        onClick = { selectedCat = cat },
                        text = { Text("${cat.displayName} ($count)") }
                    )
                }
            }

            // Menu Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { item ->
                    AdminMenuItemRow(
                        item = item,
                        onToggleAvailability = { viewModel.toggleAvailability(item.id) },
                        onEdit = { itemToEdit = item }
                    )
                }
            }
        }

        // Add Item Dialog
        if (showAddDialog) {
            AddMenuItemDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, cat, desc, price, spice ->
                    viewModel.addMenuItem(
                        name = name,
                        category = cat,
                        description = desc,
                        price = price,
                        spiceLevel = spice,
                        calories = 450,
                        prepTimeMin = 15,
                        isChefSpecial = false
                    )
                    showAddDialog = false
                }
            )
        }

        // Edit Item Dialog
        if (itemToEdit != null) {
            EditMenuItemDialog(
                item = itemToEdit!!,
                onDismiss = { itemToEdit = null },
                onSave = { updated ->
                    viewModel.updateMenuItem(updated)
                    itemToEdit = null
                }
            )
        }
    }
}

@Composable
fun AdminMenuItemRow(
    item: MenuItem,
    onToggleAvailability: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_item_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.category.displayName} • $${String.format("%.2f", item.price)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = WarmRedPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = if (item.isAvailable) "● In Stock" else "○ Sold Out",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (item.isAvailable) SuccessGreen else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            IconButton(
                onClick = onEdit,
                modifier = Modifier.testTag("btn_edit_${item.id}")
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }

            Switch(
                checked = item.isAvailable,
                onCheckedChange = { onToggleAvailability() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WarmRedPrimary),
                modifier = Modifier.testTag("switch_available_${item.id}")
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuItemDialog(
    onDismiss: () -> Unit,
    onAdd: (String, MenuCategory, String, Double, SpiceLevel) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(MenuCategory.MAINS) }
    var desc by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var spice by remember { mutableStateOf(SpiceLevel.MILD) }
    var expandedCat by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Menu Item", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_dish_name")
                )

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = expandedCat,
                    onExpandedChange = { expandedCat = !expandedCat }
                ) {
                    OutlinedTextField(
                        value = selectedCat.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCat,
                        onDismissRequest = { expandedCat = false }
                    ) {
                        MenuCategory.values().forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    selectedCat = cat
                                    expandedCat = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_dish_price")
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toDoubleOrNull() ?: 12.99
                    if (name.isNotBlank()) {
                        onAdd(name, selectedCat, desc, p, spice)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                modifier = Modifier.testTag("btn_confirm_add_dish")
            ) {
                Text("Add to Menu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EditMenuItemDialog(
    item: MenuItem,
    onDismiss: () -> Unit,
    onSave: (MenuItem) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var priceText by remember { mutableStateOf(item.price.toString()) }
    var desc by remember { mutableStateOf(item.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit ${item.name}", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toDoubleOrNull() ?: item.price
                    onSave(item.copy(name = name, price = p, description = desc))
                },
                colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// -------------------------------------------------------------
// 11. Incoming Orders Screen (Admin)
// -------------------------------------------------------------
@Composable
fun AdminIncomingOrdersScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    var selectedStatusFilter by remember { mutableStateOf<OrderStatus?>(null) }

    val filtered = if (selectedStatusFilter == null) orders else orders.filter { it.status == selectedStatusFilter }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_incoming")
    ) {
        // Status Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedStatusFilter) {
                null -> 0
                OrderStatus.RECEIVED -> 1
                OrderStatus.PREPARING -> 2
                OrderStatus.READY -> 3
                OrderStatus.SERVED_DELIVERED -> 4
                else -> 0
            },
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 16.dp,
            divider = {}
        ) {
            Tab(
                selected = selectedStatusFilter == null,
                onClick = { selectedStatusFilter = null },
                text = { Text("All (${orders.size})") }
            )
            Tab(
                selected = selectedStatusFilter == OrderStatus.RECEIVED,
                onClick = { selectedStatusFilter = OrderStatus.RECEIVED },
                text = { Text("New (${orders.count { it.status == OrderStatus.RECEIVED }})") }
            )
            Tab(
                selected = selectedStatusFilter == OrderStatus.PREPARING,
                onClick = { selectedStatusFilter = OrderStatus.PREPARING },
                text = { Text("Kitchen (${orders.count { it.status == OrderStatus.PREPARING }})") }
            )
            Tab(
                selected = selectedStatusFilter == OrderStatus.READY,
                onClick = { selectedStatusFilter = OrderStatus.READY },
                text = { Text("Ready (${orders.count { it.status == OrderStatus.READY }})") }
            )
            Tab(
                selected = selectedStatusFilter == OrderStatus.SERVED_DELIVERED,
                onClick = { selectedStatusFilter = OrderStatus.SERVED_DELIVERED },
                text = { Text("Completed") }
            )
        }

        // List of Orders
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filtered, key = { it.id }) { order ->
                AdminIncomingOrderCard(
                    order = order,
                    onViewDetail = { viewModel.openAdminOrderDetail(order) },
                    onAccept = { viewModel.acceptOrder(order.id) },
                    onReject = { viewModel.rejectOrder(order.id) },
                    onUpdateStatus = { nextStatus -> viewModel.updateOrderStatus(order.id, nextStatus) }
                )
            }
        }
    }
}

@Composable
fun AdminIncomingOrderCard(
    order: Order,
    onViewDetail: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_incoming_${order.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.id,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = if (order.orderType == OrderType.DINE_IN) "Dine-in • ${order.tableNumber}" else "Delivery • ${order.deliveryAddress}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items checklist
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity} × ${item.menuItem.name}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = item.spiceLevel.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmRedPrimary
                        )
                    }
                    if (item.selectedAddOns.isNotEmpty()) {
                        Text(
                            text = "  + ${item.selectedAddOns.joinToString { it.name }}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (order.specialInstructions.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldenYellowContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Note: ${order.specialInstructions}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnGoldenYellowContainer,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WarmBorder)

            // Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewDetail,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Details", fontSize = 12.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (order.status) {
                        OrderStatus.RECEIVED -> {
                            OutlinedButton(
                                onClick = onReject,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Text("Reject", fontSize = 12.sp)
                            }

                            Button(
                                onClick = onAccept,
                                colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_accept_${order.id}")
                            ) {
                                Text("Accept Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        OrderStatus.PREPARING -> {
                            Button(
                                onClick = { onUpdateStatus(OrderStatus.READY) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenYellowAccent),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_ready_${order.id}")
                            ) {
                                Text("Mark Ready", fontSize = 12.sp, color = Color(0xFF451A03), fontWeight = FontWeight.Bold)
                            }
                        }

                        OrderStatus.READY -> {
                            Button(
                                onClick = { onUpdateStatus(OrderStatus.SERVED_DELIVERED) },
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_complete_${order.id}")
                            ) {
                                Text(
                                    text = if (order.orderType == OrderType.DINE_IN) "Mark Served" else "Mark Delivered",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        else -> {
                            Text(
                                text = "Order Closed",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 12. Admin Order Detail Screen
// -------------------------------------------------------------
@Composable
fun AdminOrderDetailScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val order = viewModel.selectedAdminOrder.collectAsState().value

    if (order == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Order details not found")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_order_detail"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Bar
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Order Details: ${order.id}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // Status Controller Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current Status",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        StatusBadge(status = order.status)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Advance or override order state:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            OrderStatus.RECEIVED to "New",
                            OrderStatus.PREPARING to "Kitchen",
                            OrderStatus.READY to "Ready",
                            OrderStatus.SERVED_DELIVERED to "Done"
                        ).forEach { (st, label) ->
                            val isCurrent = order.status == st
                            Surface(
                                onClick = { viewModel.updateOrderStatus(order.id, st) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCurrent) WarmRedPrimary else MaterialTheme.colorScheme.background,
                                border = if (isCurrent) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_status_btn_${st.name.lowercase()}")
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Customer & Destination Information
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Customer & Delivery Info",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = WarmRedPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Customer: ${order.customerName}", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = WarmRedPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Phone: ${order.customerPhone}", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (order.orderType == OrderType.DINE_IN) Icons.Default.Restaurant else Icons.Default.DeliveryDining,
                            contentDescription = null,
                            tint = WarmRedPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (order.orderType == OrderType.DINE_IN) "Table: ${order.tableNumber}" else "Address: ${order.deliveryAddress}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    if (order.specialInstructions.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldenYellowContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Customer Note: ${order.specialInstructions}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = OnGoldenYellowContainer,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Full Items Breakdown
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dishes Ordered (${order.items.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    order.items.forEach { cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${cartItem.quantity} × ${cartItem.menuItem.name}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Spice: ${cartItem.spiceLevel.label}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmRedPrimary
                                )
                                if (cartItem.selectedAddOns.isNotEmpty()) {
                                    Text(
                                        text = "+ " + cartItem.selectedAddOns.joinToString { it.name },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (cartItem.specialNotes.isNotBlank()) {
                                    Text(
                                        text = "Item Note: ${cartItem.specialNotes}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = "$${String.format("%.2f", cartItem.totalPrice)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = WarmBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Subtotal", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$${String.format("%.2f", order.subtotal)}", style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Delivery Fee", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$${String.format("%.2f", order.deliveryFee)}", style = MaterialTheme.typography.bodyMedium)
                    }

                    if (order.discount > 0.0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Discount", style = MaterialTheme.typography.bodyMedium, color = SuccessGreen)
                            Text(text = "-$${String.format("%.2f", order.discount)}", style = MaterialTheme.typography.bodyMedium, color = SuccessGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Paid", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(
                            text = "$${String.format("%.2f", order.total)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = WarmRedPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}
