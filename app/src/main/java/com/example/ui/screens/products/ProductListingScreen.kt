package com.example.ui.screens.products

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.models.Category
import com.example.ui.components.EmptyStateView
import com.example.ui.components.ProductCard
import com.example.ui.theme.StarGold
import com.example.viewmodel.KushalViewModel
import com.example.viewmodel.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListingScreen(
    viewModel: KushalViewModel,
    title: String = "All Products",
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit
) {
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Distinct brands from products
    val allBrands = remember(allProducts) {
        allProducts.map { it.brand }.distinct().sorted()
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("product_listing_back_button")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${filteredProducts.size} Items Available",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        BadgedBox(
                            badge = {
                                val count = cartItems.sumOf { it.quantity }
                                if (count > 0) {
                                    Badge { Text(count.toString()) }
                                }
                            }
                        ) {
                            IconButton(onClick = onNavigateToCart) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Sort & Filter Action Bar
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sort Button & Menu
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showSortMenu = true }
                                .padding(vertical = 4.dp)
                                .testTag("sort_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sort: ${filterState.sortOption.displayName}",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(option.displayName)
                                            if (filterState.sortOption == option) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    },
                                    onClick = {
                                        viewModel.updateSortOption(option)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Filter Button
                    OutlinedButton(
                        onClick = { showFilterBottomSheet = true },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("filter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filters", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Products Grid
            if (filteredProducts.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Default.FilterList,
                    title = "No products found",
                    description = "No items matched your current filter criteria. Try adjusting price range or removing filters.",
                    actionText = "Reset All Filters",
                    onActionClick = { viewModel.resetFilters() }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onNavigateToProductDetail(product.id) },
                            onAddToCart = { viewModel.addToCart(product) },
                            onToggleWishlist = { viewModel.toggleWishlist(product) },
                            isWishlisted = wishlistItems.any { it.productId == product.id }
                        )
                    }
                }
            }
        }

        // Filter Bottom Sheet
        if (showFilterBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterBottomSheet = false },
                sheetState = sheetState
            ) {
                FilterBottomSheetContent(
                    categories = allCategories,
                    brands = allBrands,
                    filterState = filterState,
                    onCategorySelect = { viewModel.selectCategoryFilter(it) },
                    onBrandSelect = { viewModel.selectBrandFilter(it) },
                    onPriceChange = { min, max -> viewModel.updatePriceRange(min, max) },
                    onRatingSelect = { viewModel.updateMinRating(it) },
                    onInStockToggle = { viewModel.toggleInStockOnly(it) },
                    onReset = { viewModel.resetFilters() },
                    onApply = { showFilterBottomSheet = false }
                )
            }
        }
    }
}

@Composable
fun FilterBottomSheetContent(
    categories: List<Category>,
    brands: List<String>,
    filterState: com.example.viewmodel.ProductFilterState,
    onCategorySelect: (String?) -> Unit,
    onBrandSelect: (String?) -> Unit,
    onPriceChange: (Double, Double) -> Unit,
    onRatingSelect: (Double) -> Unit,
    onInStockToggle: (Boolean) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    var maxPriceSlider by remember { mutableStateOf(filterState.maxPrice.toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Filter Products",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(onClick = onReset) {
                Text("Reset All")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Price Range
        Text(
            "Max Price: ₹${maxPriceSlider.toInt()}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Slider(
            value = maxPriceSlider,
            onValueChange = {
                maxPriceSlider = it
                onPriceChange(0.0, it.toDouble())
            },
            valueRange = 20f..1500f,
            steps = 29
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Categories Filter
        Text(
            "Department",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = filterState.selectedCategoryId == null,
                    onClick = { onCategorySelect(null) },
                    label = { Text("All") }
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = filterState.selectedCategoryId == cat.id,
                    onClick = { onCategorySelect(if (filterState.selectedCategoryId == cat.id) null else cat.id) },
                    label = { Text(cat.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Brands Filter
        Text(
            "Brand",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = filterState.selectedBrand == null,
                    onClick = { onBrandSelect(null) },
                    label = { Text("All Brands") }
                )
            }
            items(brands) { brand ->
                FilterChip(
                    selected = filterState.selectedBrand == brand,
                    onClick = { onBrandSelect(if (filterState.selectedBrand == brand) null else brand) },
                    label = { Text(brand) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Filter
        Text(
            "Minimum Customer Rating",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(0.0, 4.0, 4.5, 4.8).forEach { r ->
                FilterChip(
                    selected = filterState.minRating == r,
                    onClick = { onRatingSelect(r) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (r == 0.0) "Any" else "$r+")
                            if (r > 0.0) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // In Stock Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("In Stock Only", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                Text("Hide out of stock items", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            Switch(
                checked = filterState.inStockOnly,
                onCheckedChange = onInStockToggle
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("apply_filters_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Apply Filters", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
