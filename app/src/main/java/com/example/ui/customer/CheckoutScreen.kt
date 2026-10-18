package com.example.ui.customer

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderType
import com.example.data.model.PaymentMethod
import com.example.ui.RestaurantViewModel
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedPrimary

@Composable
fun CheckoutScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val promoDiscountPercent by viewModel.promoDiscountPercent.collectAsState()

    var orderType by remember { mutableStateOf(OrderType.DELIVERY) }
    var tableNumber by remember { mutableStateOf("Table 07") }
    var deliveryAddress by remember { mutableStateOf("452 Oak Avenue, Apt 3B") }
    var customerName by remember { mutableStateOf("Alex Morgan") }
    var customerPhone by remember { mutableStateOf("+1 (555) 782-9912") }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }
    var specialInstructions by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val discountAmount = subtotal * promoDiscountPercent
    val deliveryFee = if (orderType == OrderType.DELIVERY) {
        if (subtotal >= 30.0 || subtotal == 0.0) 0.0 else 2.99
    } else 0.0
    val finalTotal = (subtotal - discountAmount + deliveryFee).coerceAtLeast(0.0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_checkout")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Order Type Switcher (Dine-in vs Delivery)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "How would you like to receive your food?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                onClick = { orderType = OrderType.DELIVERY },
                                shape = RoundedCornerShape(12.dp),
                                color = if (orderType == OrderType.DELIVERY) WarmRedPrimary else MaterialTheme.colorScheme.background,
                                border = if (orderType == OrderType.DELIVERY) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("toggle_delivery")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeliveryDining,
                                        contentDescription = null,
                                        tint = if (orderType == OrderType.DELIVERY) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Delivery",
                                        fontWeight = FontWeight.Bold,
                                        color = if (orderType == OrderType.DELIVERY) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Surface(
                                onClick = { orderType = OrderType.DINE_IN },
                                shape = RoundedCornerShape(12.dp),
                                color = if (orderType == OrderType.DINE_IN) WarmRedPrimary else MaterialTheme.colorScheme.background,
                                border = if (orderType == OrderType.DINE_IN) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("toggle_dine_in")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = if (orderType == OrderType.DINE_IN) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Dine-in",
                                        fontWeight = FontWeight.Bold,
                                        color = if (orderType == OrderType.DINE_IN) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Table / Location Info
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (orderType == OrderType.DINE_IN) "Dine-in Details" else "Delivery Address",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (orderType == OrderType.DINE_IN) {
                            OutlinedTextField(
                                value = tableNumber,
                                onValueChange = { tableNumber = it },
                                label = { Text("Table Number") },
                                placeholder = { Text("E.g. Table 04, Patio 2") },
                                leadingIcon = {
                                    Icon(Icons.Default.TableBar, contentDescription = null, tint = WarmRedPrimary)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_table_number"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WarmRedPrimary,
                                    unfocusedBorderColor = WarmBorder
                                )
                            )
                        } else {
                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text("Street Address & Apartment") },
                                placeholder = { Text("123 Main St, Apt 4B") },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = WarmRedPrimary)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_delivery_address"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WarmRedPrimary,
                                    unfocusedBorderColor = WarmBorder
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customerName,
                                onValueChange = { customerName = it },
                                label = { Text("Your Name") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = WarmRedPrimary)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_customer_name"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WarmRedPrimary,
                                    unfocusedBorderColor = WarmBorder
                                )
                            )

                            OutlinedTextField(
                                value = customerPhone,
                                onValueChange = { customerPhone = it },
                                label = { Text("Phone") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = WarmRedPrimary)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_customer_phone"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WarmRedPrimary,
                                    unfocusedBorderColor = WarmBorder
                                )
                            )
                        }
                    }
                }
            }

            // 3. Payment Method
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethod.values().forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = method }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == method,
                                    onClick = { paymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = WarmRedPrimary),
                                    modifier = Modifier.testTag("radio_pay_${method.name.lowercase()}")
                                )

                                val icon = when (method) {
                                    PaymentMethod.CREDIT_CARD -> Icons.Default.CreditCard
                                    PaymentMethod.GOOGLE_PAY -> Icons.Default.Payment
                                    PaymentMethod.CASH -> Icons.Default.Money
                                }

                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = WarmRedPrimary,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = method.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Special Instructions
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Instructions for Restaurant & Courier",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = specialInstructions,
                            onValueChange = { specialInstructions = it },
                            placeholder = { Text("E.g. Ring doorbell, leave at front porch, extra napkins please") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_checkout_notes"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WarmRedPrimary,
                                unfocusedBorderColor = WarmBorder
                            ),
                            maxLines = 2
                        )
                    }
                }
            }

            // 5. Total Recap
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        SummaryRow(label = "Items (${cartItems.sumOf { it.quantity }})", amount = subtotal)
                        SummaryRow(
                            label = "Delivery Fee",
                            amount = deliveryFee,
                            isFree = deliveryFee == 0.0
                        )
                        if (discountAmount > 0.0) {
                            SummaryRow(
                                label = "Promo Discount (${(promoDiscountPercent * 100).toInt()}%)",
                                amount = -discountAmount,
                                isDiscount = true
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = WarmBorder)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Final Total",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$${String.format("%.2f", finalTotal)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = WarmRedPrimary
                                )
                            )
                        }
                    }
                }
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: "",
                        color = WarmRedPrimary,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        // Bottom Place Order Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        if (orderType == OrderType.DELIVERY && deliveryAddress.isBlank()) {
                            errorMessage = "Please enter a valid delivery address"
                            return@Button
                        }
                        if (orderType == OrderType.DINE_IN && tableNumber.isBlank()) {
                            errorMessage = "Please specify your table number"
                            return@Button
                        }
                        errorMessage = null
                        viewModel.submitOrder(
                            orderType = orderType,
                            tableNumber = tableNumber,
                            deliveryAddress = deliveryAddress,
                            customerName = customerName,
                            customerPhone = customerPhone,
                            paymentMethod = paymentMethod,
                            specialInstructions = specialInstructions
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_place_order")
                ) {
                    Text(
                        text = "Place Order • $${String.format("%.2f", finalTotal)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                }
            }
        }
    }
}
