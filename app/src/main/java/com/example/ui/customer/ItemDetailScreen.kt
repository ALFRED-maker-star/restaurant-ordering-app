package com.example.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AddOn
import com.example.data.model.SpiceLevel
import com.example.ui.RestaurantViewModel
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary
import kotlinx.coroutines.launch

@Composable
fun ItemDetailScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val item = viewModel.selectedMenuItem.collectAsState().value ?: return

    var quantity by remember { mutableIntStateOf(1) }
    var selectedSpiceLevel by remember { mutableStateOf(item.defaultSpiceLevel) }
    val selectedAddOns = remember { mutableStateListOf<AddOn>() }
    var specialInstructions by remember { mutableStateOf("") }
    var showAddedToast by remember { mutableStateOf(false) }

    val singleItemPrice = item.price + selectedAddOns.sumOf { it.price }
    val totalPrice = singleItemPrice * quantity

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_item_detail")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            // 1. Large Food Photo Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x99000000), Color.Transparent, Color(0x66000000))
                            )
                        )
                )

                // Top back button
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xAAFFFFFF))
                        .align(Alignment.TopStart)
                        .testTag("btn_detail_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                // Chef special badge
                if (item.isChefSpecial) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GoldenYellowAccent,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "Chef's Special",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF451A03),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 2. Info Section
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$${String.format("%.2f", item.price)}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = WarmRedPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats row (rating, time, calories)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldenYellowAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.rating} (${item.reviewCount} reviews)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = WarmRedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.prepTimeMin} min prep",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = "${item.calories} kcal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Customization: Spice Level
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Choose Spice Level",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Selected: ${selectedSpiceLevel.label}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = WarmRedPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SpiceLevel.values().forEach { level ->
                                val isSelected = selectedSpiceLevel == level
                                Surface(
                                    onClick = { selectedSpiceLevel = level },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) WarmRedPrimary else MaterialTheme.colorScheme.background,
                                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("spice_opt_${level.name.lowercase()}")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else if (level == SpiceLevel.NONE) Color.Gray else WarmRedPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = level.label.split(" ").first(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Customization: Add-ons
                if (item.availableAddOns.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Customize & Add-ons",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Optional extras to make your dish even better",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            item.availableAddOns.forEach { addOn ->
                                val isChecked = selectedAddOns.any { it.id == addOn.id }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isChecked) {
                                                selectedAddOns.removeAll { it.id == addOn.id }
                                            } else {
                                                selectedAddOns.add(addOn)
                                            }
                                        }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedAddOns.add(addOn)
                                            } else {
                                                selectedAddOns.removeAll { it.id == addOn.id }
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = WarmRedPrimary),
                                        modifier = Modifier.testTag("cb_addon_${addOn.id}")
                                    )
                                    Text(
                                        text = addOn.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "+$${String.format("%.2f", addOn.price)}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = WarmRedPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 5. Special Instructions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Special Instructions",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = specialInstructions,
                            onValueChange = { specialInstructions = it },
                            placeholder = { Text("E.g. Sauce on the side, well done, allergy alert...") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_special_notes"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WarmRedPrimary,
                                unfocusedBorderColor = WarmBorder
                            ),
                            maxLines = 3
                        )
                    }
                }
            }
        }

        // Bottom Sticky Action Bar: Quantity Stepper + Add to Cart Button
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (showAddedToast) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD1FAE5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF065F46),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Added $quantity × ${item.name} to Cart!",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Stepper (- / +)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.background,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                enabled = quantity > 1,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("btn_qty_minus")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease quantity",
                                    tint = if (quantity > 1) WarmRedPrimary else Color.LightGray
                                )
                            }

                            Text(
                                text = "$quantity",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .testTag("txt_quantity")
                            )

                            IconButton(
                                onClick = { if (quantity < 20) quantity++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("btn_qty_plus")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase quantity",
                                    tint = WarmRedPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Add to Cart Button
                    Button(
                        onClick = {
                            viewModel.addToCartWithCustomization(
                                menuItem = item,
                                quantity = quantity,
                                spiceLevel = selectedSpiceLevel,
                                addOns = selectedAddOns.toList(),
                                specialNotes = specialInstructions
                            )
                            showAddedToast = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmRedPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_add_to_cart_detail")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add • $${String.format("%.2f", totalPrice)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
