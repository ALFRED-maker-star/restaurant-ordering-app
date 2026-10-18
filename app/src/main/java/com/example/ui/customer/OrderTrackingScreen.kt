package com.example.ui.customer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.ui.RestaurantViewModel
import com.example.ui.Screen
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.GoldenYellowContainer
import com.example.ui.theme.OnGoldenYellowContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary

@Composable
fun OrderTrackingScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val order = viewModel.activeTrackingOrder.collectAsState().value

    if (order == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No active order to track",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary)
                ) {
                    Text("Explore Menu")
                }
            }
        }
        return
    }

    val currentStep = order.status.stepIndex.coerceAtLeast(0)
    val progressFraction by animateFloatAsState(
        targetValue = when (order.status) {
            OrderStatus.RECEIVED -> 0.25f
            OrderStatus.PREPARING -> 0.55f
            OrderStatus.READY -> 0.85f
            OrderStatus.SERVED_DELIVERED -> 1.0f
            OrderStatus.CANCELLED -> 0.0f
        },
        label = "tracking_progress"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_order_tracking"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Order Status Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Order ${order.id}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (order.orderType == OrderType.DINE_IN) "Dine-in • ${order.tableNumber}" else "Delivery • ${order.deliveryAddress}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        StatusBadge(status = order.status)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estimated Time Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (order.status == OrderStatus.SERVED_DELIVERED) Color(0xFFD1FAE5) else WarmRedContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (order.status == OrderStatus.SERVED_DELIVERED) Icons.Default.DoneAll else Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (order.status == OrderStatus.SERVED_DELIVERED) Color(0xFF065F46) else WarmRedPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (order.status == OrderStatus.SERVED_DELIVERED) {
                                        "Order Completed!"
                                    } else {
                                        "Estimated Time: ~${order.estimatedMinutes} mins"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (order.status == OrderStatus.SERVED_DELIVERED) Color(0xFF065F46) else WarmRedPrimary
                                )
                                Text(
                                    text = when (order.status) {
                                        OrderStatus.RECEIVED -> "We received your order and kitchen is accepting it"
                                        OrderStatus.PREPARING -> "Chef is grilling & preparing your gourmet dishes"
                                        OrderStatus.READY -> if (order.orderType == OrderType.DINE_IN) "Fresh from the kitchen! Being brought to your table" else "Packed fresh! Driver is picking up your food"
                                        OrderStatus.SERVED_DELIVERED -> "Enjoy your meal! Thank you for ordering from TastyGo"
                                        OrderStatus.CANCELLED -> "This order was cancelled."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (order.status == OrderStatus.SERVED_DELIVERED) Color(0xFF065F46) else Color(0xFF7F1D1D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = WarmRedPrimary,
                        trackColor = WarmBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        // 2. Status Stepper
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Live Order Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val steps = listOf(
                        OrderStatus.RECEIVED to "Order Received",
                        OrderStatus.PREPARING to "Kitchen is Preparing",
                        OrderStatus.READY to "Food is Ready",
                        OrderStatus.SERVED_DELIVERED to (if (order.orderType == OrderType.DINE_IN) "Served to Table" else "Out for Delivery / Delivered")
                    )

                    steps.forEachIndexed { index, (stepStatus, label) ->
                        val isDone = currentStep >= index && order.status != OrderStatus.CANCELLED
                        val isCurrent = currentStep == index && order.status != OrderStatus.CANCELLED

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDone) WarmRedPrimary else Color(0xFFE5E7EB)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = if (isCurrent || isDone) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isCurrent) WarmRedPrimary else if (isDone) MaterialTheme.colorScheme.onSurface else Color.Gray
                                )
                                if (isCurrent) {
                                    Text(
                                        text = "In Progress...",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmRedPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Demo / Simulation advance status button
                    OutlinedButton(
                        onClick = {
                            val next = when (order.status) {
                                OrderStatus.RECEIVED -> OrderStatus.PREPARING
                                OrderStatus.PREPARING -> OrderStatus.READY
                                OrderStatus.READY -> OrderStatus.SERVED_DELIVERED
                                else -> OrderStatus.RECEIVED
                            }
                            viewModel.updateOrderStatus(order.id, next)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_simulate_order_step")
                    ) {
                        Text("Simulate Next Kitchen Stage", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 3. Order Breakdown Details
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ordered Items",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.quantity} × ${item.menuItem.name}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                if (item.selectedAddOns.isNotEmpty()) {
                                    Text(
                                        text = "+ " + item.selectedAddOns.joinToString(", ") { it.name },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "$${String.format("%.2f", item.totalPrice)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = WarmBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Paid",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
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

        // 4. Contact Restaurant Action
        item {
            Button(
                onClick = { /* Call action */ },
                colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_call_restaurant")
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Restaurant: (555) 829-8940", fontWeight = FontWeight.Bold)
            }
        }
    }
}
