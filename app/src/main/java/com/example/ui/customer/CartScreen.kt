package com.example.ui.customer

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.RestaurantViewModel
import com.example.ui.Screen
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.GoldenYellowContainer
import com.example.ui.theme.OnGoldenYellowContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary

@Composable
fun CartScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val promoDiscountPercent by viewModel.promoDiscountPercent.collectAsState()
    val promoMessage by viewModel.promoMessage.collectAsState()
    var promoInput by remember { mutableStateOf(viewModel.promoCode.value) }

    val discountAmount = subtotal * promoDiscountPercent
    val deliveryFee = if (subtotal >= 30.0 || subtotal == 0.0) 0.0 else 2.99
    val finalTotal = (subtotal - discountAmount + deliveryFee).coerceAtLeast(0.0)

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("screen_cart_empty"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = WarmRedContainer,
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.RemoveShoppingCart,
                            contentDescription = null,
                            tint = WarmRedPrimary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Looks like you haven't added any appetizing meals yet. Explore our menu!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("btn_empty_browse_menu")
                ) {
                    Text("Browse Tasty Menu", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_cart")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 140.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Free delivery progress banner
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (subtotal >= 30.0) Color(0xFFD1FAE5) else GoldenYellowContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeliveryDining,
                            contentDescription = null,
                            tint = if (subtotal >= 30.0) Color(0xFF065F46) else OnGoldenYellowContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (subtotal >= 30.0) {
                                "🎉 You've unlocked FREE Delivery on this order!"
                            } else {
                                "Add $${String.format("%.2f", 30.0 - subtotal)} more for FREE Delivery!"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (subtotal >= 30.0) Color(0xFF065F46) else OnGoldenYellowContainer
                        )
                    }
                }
            }

            // Items in Cart
            items(cartItems, key = { it.id }) { cartItem ->
                CartItemRow(
                    cartItem = cartItem,
                    onIncrease = { viewModel.updateCartQuantity(cartItem.id, cartItem.quantity + 1) },
                    onDecrease = { viewModel.updateCartQuantity(cartItem.id, cartItem.quantity - 1) },
                    onRemove = { viewModel.removeFromCart(cartItem.id) }
                )
            }

            // Promo Code Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = WarmRedPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Have a Promo Code?",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = promoInput,
                                onValueChange = { promoInput = it },
                                placeholder = { Text("E.g. TASTY20") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WarmRedPrimary,
                                    unfocusedBorderColor = WarmBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_promo_code")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    viewModel.promoCode.value = promoInput
                                    viewModel.applyPromoCode(promoInput)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("btn_apply_promo")
                            ) {
                                Text("Apply")
                            }
                        }

                        if (promoMessage != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = promoMessage ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (promoDiscountPercent > 0.0) SuccessGreen else WarmRedPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Bill Breakdown Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Summary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        SummaryRow(label = "Subtotal", amount = subtotal)
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

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = WarmBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$${String.format("%.2f", finalTotal)}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = WarmRedPrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Bottom Fixed Checkout Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total to Pay",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.2f", finalTotal)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = WarmRedPrimary
                        )
                    )
                }

                Button(
                    onClick = { viewModel.navigateTo(Screen.CHECKOUT) },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("btn_proceed_checkout")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Checkout",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${cartItem.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Image(
                painter = painterResource(id = cartItem.menuItem.imageRes),
                contentDescription = cartItem.menuItem.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = cartItem.menuItem.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("btn_remove_${cartItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove item",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Customizations badges
                Text(
                    text = "Spice: ${cartItem.spiceLevel.label}",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmRedPrimary
                )

                if (cartItem.selectedAddOns.isNotEmpty()) {
                    Text(
                        text = "+ " + cartItem.selectedAddOns.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (cartItem.specialNotes.isNotBlank()) {
                    Text(
                        text = "Note: ${cartItem.specialNotes}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", cartItem.totalPrice)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = WarmRedPrimary
                        )
                    )

                    // Quantity Stepper
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.background,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = onDecrease,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = WarmRedPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "${cartItem.quantity}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = onIncrease,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = WarmRedPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    amount: Double,
    isFree: Boolean = false,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDiscount) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (isFree) "FREE" else (if (isDiscount) "-$${String.format("%.2f", -amount)}" else "$${String.format("%.2f", amount)}"),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isDiscount || isFree) FontWeight.Bold else FontWeight.Medium,
                color = if (isDiscount || isFree) SuccessGreen else MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
