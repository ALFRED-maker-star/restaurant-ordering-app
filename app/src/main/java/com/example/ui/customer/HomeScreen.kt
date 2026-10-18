package com.example.ui.customer

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MenuCategory
import com.example.data.model.MenuItem
import com.example.ui.RestaurantViewModel
import com.example.ui.components.SpiceIndicator
import com.example.ui.theme.GoldenYellowAccent
import com.example.ui.theme.GoldenYellowContainer
import com.example.ui.theme.OnGoldenYellowContainer
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmRedContainer
import com.example.ui.theme.WarmRedPrimary

@Composable
fun HomeScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val menuItems by viewModel.menuItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val allSearchItems by viewModel.allSearchItems.collectAsState()

    val chefSpecials = menuItems.filter { it.isChefSpecial }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_home"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Restaurant Hero Banner
        item {
            RestaurantHeroBanner()
        }

        // 2. Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = {
                        Text(
                            "Search gourmet burgers, pizzas, wings...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = WarmRedPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = WarmRedPrimary,
                        unfocusedBorderColor = WarmBorder
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_home")
                )
            }
        }

        // 3. Category Tabs
        item {
            Column(modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.testTag("row_categories")
                ) {
                    items(MenuCategory.values()) { category ->
                        val count = menuItems.count { it.category == category }
                        CategoryPill(
                            category = category,
                            itemCount = count,
                            isSelected = selectedCategory == category,
                            onClick = { viewModel.openCategoryMenu(category) }
                        )
                    }
                }
            }
        }

        // If searching, show search results
        if (searchQuery.isNotBlank()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Results for \"$searchQuery\"",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${allSearchItems.size} items",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (allSearchItems.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No delicious items found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try searching for burger, pizza, fries or wings",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(allSearchItems) { item ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        FoodItemCard(
                            item = item,
                            onClick = { viewModel.openItemDetail(item) },
                            onQuickAdd = { viewModel.quickAddToCart(item) }
                        )
                    }
                }
            }
        } else {
            // 4. Chef's Specials Showcase
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Chef's Specials",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = WarmRedContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "POPULAR",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = WarmRedPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "See Menu",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = WarmRedPrimary
                            ),
                            modifier = Modifier
                                .clickable { viewModel.openCategoryMenu(MenuCategory.MAINS) }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.testTag("row_chef_specials")
                    ) {
                        items(chefSpecials) { item ->
                            ChefSpecialCard(
                                item = item,
                                onClick = { viewModel.openItemDetail(item) },
                                onQuickAdd = { viewModel.quickAddToCart(item) }
                            )
                        }
                    }
                }
            }

            // 5. Category Spotlight: Mains & Starters
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Most Loved Dishes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = WarmRedPrimary
                        ),
                        modifier = Modifier
                            .clickable { viewModel.openCategoryMenu(MenuCategory.MAINS) }
                            .padding(4.dp)
                    )
                }
            }

            val popularItems = menuItems.take(5)
            items(popularItems) { item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    FoodItemCard(
                        item = item,
                        onClick = { viewModel.openItemDetail(item) },
                        onQuickAdd = { viewModel.quickAddToCart(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantHeroBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("card_restaurant_banner"),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_restaurant_hero),
                    contentDescription = "TastyGo Restaurant Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC000000)),
                                startY = 60f
                            )
                        )
                )

                // Rating & Delivery badge
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xDDFFFFFF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldenYellowAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "4.9 (420+)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xDDFFFFFF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = WarmRedPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "15-25 min",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Banner details
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "TastyGo Gourmet Kitchen",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Wood-fired Artisan Pizzas • Prime Smash Burgers • Craft Drinks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Promo announcement bar
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = GoldenYellowContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = OnGoldenYellowContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Promo: Code TASTY20 for 20% OFF! Free delivery over $30",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnGoldenYellowContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryPill(
    category: MenuCategory,
    itemCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) WarmRedPrimary else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        modifier = Modifier.testTag("cat_pill_${category.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                shape = CircleShape,
                color = if (isSelected) Color(0x33FFFFFF) else WarmRedContainer
            ) {
                Text(
                    text = "$itemCount",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) Color.White else WarmRedPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun ChefSpecialCard(
    item: MenuItem,
    onClick: () -> Unit,
    onQuickAdd: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(240.dp)
            .testTag("chef_special_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 12.dp),
                    color = GoldenYellowAccent,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Chef's Pick",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF451A03),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                SpiceIndicator(spiceLevel = item.defaultSpiceLevel)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", item.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = WarmRedPrimary
                        )
                    )

                    IconButton(
                        onClick = onQuickAdd,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(WarmRedPrimary)
                            .testTag("btn_quick_add_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add ${item.name} to cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    item: MenuItem,
    onClick: () -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("food_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image Thumbnail
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (!item.isAvailable) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAA000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sold Out",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpiceIndicator(spiceLevel = item.defaultSpiceLevel)
                    Text(
                        text = "${item.calories} kcal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", item.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = WarmRedPrimary
                        )
                    )

                    if (item.isAvailable) {
                        IconButton(
                            onClick = onQuickAdd,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(WarmRedPrimary)
                                .testTag("btn_add_card_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add to cart",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Text(
                                text = "Unavailable",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
