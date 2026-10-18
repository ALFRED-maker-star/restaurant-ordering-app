package com.example.ui.customer

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MenuCategory
import com.example.ui.RestaurantViewModel
import com.example.ui.theme.WarmRedPrimary

@Composable
fun CategoryMenuScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filterDietary by viewModel.filterDietary.collectAsState()
    val filteredItems by viewModel.filteredMenuItems.collectAsState()
    val allItems by viewModel.menuItems.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_category_menu")
    ) {
        // Category Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategory.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategory.ordinal]),
                    color = WarmRedPrimary,
                    height = 3.dp
                )
            },
            divider = {}
        ) {
            MenuCategory.values().forEach { category ->
                val count = allItems.count { it.category == category }
                Tab(
                    selected = selectedCategory == category,
                    onClick = { viewModel.openCategoryMenu(category) },
                    text = {
                        Text(
                            text = "${category.displayName} ($count)",
                            fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategory == category) WarmRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.testTag("tab_${category.name.lowercase()}")
                )
            }
        }

        // Dietary & Preference Filters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            val filterOptions = listOf(
                "ALL" to "All Dishes",
                "SPECIALS" to "Chef's Specials",
                "SPICY" to "Spicy Only",
                "AVAILABLE" to "In Stock"
            )
            items(filterOptions) { (key, label) ->
                FilterChip(
                    selected = filterDietary == key,
                    onClick = { viewModel.filterDietary.value = key },
                    label = { Text(label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WarmRedPrimary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("chip_filter_$key")
                )
            }
        }

        // Items List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedCategory.displayName} Selection",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${filteredItems.size} items",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (filteredItems.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No items match current filter",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try switching the filter to 'All Dishes'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredItems) { item ->
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
