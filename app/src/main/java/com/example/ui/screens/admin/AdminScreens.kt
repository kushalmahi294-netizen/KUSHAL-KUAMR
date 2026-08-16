package com.example.ui.screens.admin

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.Category
import com.example.data.models.Coupon
import com.example.data.models.DiscountType
import com.example.data.models.Order
import com.example.data.models.OrderStatus
import com.example.data.models.Product
import com.example.data.models.PromoBanner
import com.example.data.models.Review
import com.example.ui.screens.orders.OrderStatusBadge
import com.example.ui.theme.SaleRed
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.KushalViewModel
import java.util.UUID

@Composable
fun AdminDashboardScreen(
    viewModel: KushalViewModel,
    onNavigateBackToStore: () -> Unit
) {
    val adminStats by viewModel.adminStats.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val allCoupons by viewModel.allCoupons.collectAsState()
    val allBanners by viewModel.allBanners.collectAsState()
    val allReviews by viewModel.allAdminReviews.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Products", "Orders", "Categories", "Coupons", "Banners", "Reviews")

    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showCouponDialog by remember { mutableStateOf(false) }
    var showBannerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👑", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Kushal Store Admin",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                                Text("Store Operations & Inventory Hub", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp))
                            }
                        }

                        Button(
                            onClick = onNavigateBackToStore,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Customer Store", fontSize = 12.sp)
                        }
                    }

                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        edgePadding = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        title,
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTabIndex) {
                0 -> AdminOverviewTab(
                    stats = adminStats,
                    products = allProducts,
                    orders = allOrders,
                    onNavigateToTab = { selectedTabIndex = it }
                )
                1 -> AdminProductsTab(
                    products = allProducts,
                    onAddClick = {
                        editingProduct = null
                        showProductDialog = true
                    },
                    onEditClick = { prod ->
                        editingProduct = prod
                        showProductDialog = true
                    },
                    onDeleteClick = { viewModel.deleteProduct(it) }
                )
                2 -> AdminOrdersTab(
                    orders = allOrders,
                    onUpdateStatus = { id, status -> viewModel.updateOrderStatus(id, status) }
                )
                3 -> AdminCategoriesTab(
                    categories = allCategories,
                    onAddClick = { showCategoryDialog = true },
                    onDeleteClick = { viewModel.deleteCategory(it) }
                )
                4 -> AdminCouponsTab(
                    coupons = allCoupons,
                    onAddClick = { showCouponDialog = true },
                    onDeleteClick = { viewModel.deleteCoupon(it) }
                )
                5 -> AdminBannersTab(
                    banners = allBanners,
                    onAddClick = { showBannerDialog = true },
                    onDeleteClick = { viewModel.deleteBanner(it) }
                )
                6 -> AdminReviewsTab(
                    reviews = allReviews,
                    onApprove = { id, approved -> viewModel.setReviewApproval(id, approved) },
                    onDelete = { viewModel.deleteReview(it) }
                )
            }
        }

        // Product Form Dialog (Add / Edit)
        if (showProductDialog) {
            ProductFormDialog(
                product = editingProduct,
                categories = allCategories,
                onDismiss = { showProductDialog = false },
                onSave = { prod ->
                    if (editingProduct != null) {
                        viewModel.updateProduct(prod)
                    } else {
                        viewModel.addProduct(prod)
                    }
                    showProductDialog = false
                }
            )
        }

        // Category Form Dialog
        if (showCategoryDialog) {
            CategoryFormDialog(
                onDismiss = { showCategoryDialog = false },
                onSave = { cat ->
                    viewModel.addCategory(cat)
                    showCategoryDialog = false
                }
            )
        }

        // Coupon Form Dialog
        if (showCouponDialog) {
            CouponFormDialog(
                onDismiss = { showCouponDialog = false },
                onSave = { cp ->
                    viewModel.addCoupon(cp)
                    showCouponDialog = false
                }
            )
        }

        // Banner Form Dialog
        if (showBannerDialog) {
            BannerFormDialog(
                categories = allCategories,
                onDismiss = { showBannerDialog = false },
                onSave = { bn ->
                    viewModel.addBanner(bn)
                    showBannerDialog = false
                }
            )
        }
    }
}

// ---------------- OVERVIEW TAB ----------------
@Composable
fun AdminOverviewTab(
    stats: com.example.viewmodel.AdminStats,
    products: List<Product>,
    orders: List<Order>,
    onNavigateToTab: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Metric Cards 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Total Revenue",
                        value = "₹${String.format("%.2f", stats.totalRevenue)}",
                        icon = Icons.Default.LocalOffer,
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Total Orders",
                        value = "${stats.totalOrders} Placed",
                        icon = Icons.Default.ShoppingBag,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Products Catalog",
                        value = "${stats.totalProducts} Items",
                        icon = Icons.Default.Inventory,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Active Customers",
                        value = "${stats.totalCustomers} Users",
                        icon = Icons.Default.People,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Low Stock Alert Section
        val lowStockProducts = products.filter { it.stock in 1..15 }
        if (lowStockProducts.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = SaleRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Low Stock Alert (${lowStockProducts.size} Items)",
                                fontWeight = FontWeight.Bold,
                                color = SaleRed
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        lowStockProducts.take(4).forEach { lp ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(lp.name, maxLines = 1, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                                Text("Only ${lp.stock} left in stock", color = SaleRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        // Recent Orders
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Orders", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                TextButton(onClick = { onNavigateToTab(2) }) { Text("View All Orders") }
            }
        }

        items(orders.take(5)) { ord ->
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Order #${ord.id}", fontWeight = FontWeight.Bold)
                        Text("${ord.items.size} items • ${ord.shippingAddress.fullName}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${String.format("%.2f", ord.totalAmount)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        OrderStatusBadge(ord.orderStatus)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
            Text(title, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

// ---------------- PRODUCTS TAB ----------------
@Composable
fun AdminProductsTab(
    products: List<Product>,
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Product Inventory (${products.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Product")
                }
            }
        }

        items(products) { prod ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = prod.images.firstOrNull() ?: "",
                        contentDescription = prod.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(prod.name, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("${prod.categoryName} • ${prod.brand}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("₹${String.format("%.2f", prod.price)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Stock: ${prod.stock}", style = MaterialTheme.typography.bodySmall.copy(color = if (prod.stock <= 10) SaleRed else SuccessGreen, fontWeight = FontWeight.SemiBold))
                        }
                    }

                    Row {
                        IconButton(onClick = { onEditClick(prod) }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { onDeleteClick(prod.id) }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SaleRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// ---------------- ORDERS TAB ----------------
@Composable
fun AdminOrdersTab(
    orders: List<Order>,
    onUpdateStatus: (String, OrderStatus) -> Unit
) {
    var selectedFilter by remember { mutableStateOf<OrderStatus?>(null) }
    val filtered = if (selectedFilter == null) orders else orders.filter { it.orderStatus == selectedFilter }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Order Fulfillment", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { selectedFilter = null },
                        label = { Text("All (${orders.size})") }
                    )
                }
                items(OrderStatus.values()) { st ->
                    FilterChip(
                        selected = selectedFilter == st,
                        onClick = { selectedFilter = if (selectedFilter == st) null else st },
                        label = { Text("${st.displayName} (${orders.count { it.orderStatus == st }})") }
                    )
                }
            }
        }

        items(filtered) { order ->
            var showStatusDropdown by remember { mutableStateOf(false) }

            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Order #${order.id}", fontWeight = FontWeight.Bold)
                            Text("Customer: ${order.shippingAddress.fullName} (${order.shippingAddress.phoneNumber})", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("Address: ${order.shippingAddress.city}, ${order.shippingAddress.state}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                        OrderStatusBadge(order.orderStatus)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total: ₹${String.format("%.2f", order.totalAmount)} (${order.paymentMethod.displayName})",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Box {
                            OutlinedButton(
                                onClick = { showStatusDropdown = true },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Update Status", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            DropdownMenu(
                                expanded = showStatusDropdown,
                                onDismissRequest = { showStatusDropdown = false }
                            ) {
                                OrderStatus.values().forEach { st ->
                                    DropdownMenuItem(
                                        text = { Text(st.displayName) },
                                        onClick = {
                                            onUpdateStatus(order.id, st)
                                            showStatusDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- CATEGORIES TAB ----------------
@Composable
fun AdminCategoriesTab(
    categories: List<Category>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Catalog Categories (${categories.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Button(onClick = onAddClick, shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Category")
                }
            }
        }

        items(categories) { cat ->
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(cat.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        if (cat.subcategories.isNotEmpty()) {
                            Text("Subcategories: ${cat.subcategories.joinToString(", ")}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }

                    IconButton(onClick = { onDeleteClick(cat.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SaleRed)
                    }
                }
            }
        }
    }
}

// ---------------- COUPONS TAB ----------------
@Composable
fun AdminCouponsTab(
    coupons: List<Coupon>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Promotions & Coupons (${coupons.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Button(onClick = onAddClick, shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create Coupon")
                }
            }
        }

        items(coupons) { cp ->
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(cp.code, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text(cp.description, style = MaterialTheme.typography.bodyMedium)
                        Text("Min Order: ₹${cp.minOrderValue.toInt()} • Max Discount: ₹${cp.maxDiscount.toInt()} • Expiry: ${cp.expiryDate}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }

                    IconButton(onClick = { onDeleteClick(cp.code) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SaleRed)
                    }
                }
            }
        }
    }
}

// ---------------- BANNERS TAB ----------------
@Composable
fun AdminBannersTab(
    banners: List<PromoBanner>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Home Banners (${banners.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Button(onClick = onAddClick, shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Banner")
                }
            }
        }

        items(banners) { bn ->
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(bn.title, fontWeight = FontWeight.Bold)
                        Text(bn.subtitle, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Text("Button: ${bn.buttonText}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary))
                    }

                    IconButton(onClick = { onDeleteClick(bn.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SaleRed)
                    }
                }
            }
        }
    }
}

// ---------------- REVIEWS MODERATION TAB ----------------
@Composable
fun AdminReviewsTab(
    reviews: List<Review>,
    onApprove: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Customer Reviews Moderation (${reviews.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        items(reviews) { r ->
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(r.userName, fontWeight = FontWeight.Bold)
                        Text("${r.rating} ★", color = StarGold, fontWeight = FontWeight.Bold)
                    }
                    Text(r.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                    Text(r.comment, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { onApprove(r.id, !r.isApproved) }) {
                            Text(if (r.isApproved) "Unapprove" else "Approve")
                        }
                        IconButton(onClick = { onDelete(r.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SaleRed)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- FORM DIALOGS ----------------
@Composable
fun ProductFormDialog(
    product: Product?,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var brand by remember { mutableStateOf(product?.brand ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var originalPrice by remember { mutableStateOf(product?.originalPrice?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "50") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var selectedCategoryId by remember { mutableStateOf(product?.categoryId ?: categories.firstOrNull()?.id ?: "") }
    var imageUrl by remember { mutableStateOf(product?.images?.firstOrNull() ?: "") }
    var sizesText by remember { mutableStateOf(product?.sizes?.joinToString(", ") ?: "") }
    var colorsText by remember { mutableStateOf(product?.colors?.joinToString(", ") ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product != null) "Edit Product" else "Add New Product", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                    OutlinedTextField(value = originalPrice, onValueChange = { originalPrice = it }, label = { Text("Original Price (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                }

                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Inventory Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Primary Image URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = sizesText, onValueChange = { sizesText = it }, label = { Text("Sizes (comma-separated, e.g. S, M, L)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = colorsText, onValueChange = { colorsText = it }, label = { Text("Colors (comma-separated, e.g. Black, Blue)") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Text("Category", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategoryId == cat.id,
                            onClick = { selectedCategoryId = cat.id },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = price.toDoubleOrNull() ?: 0.0
                    val op = originalPrice.toDoubleOrNull() ?: p
                    val st = stock.toIntOrNull() ?: 10
                    val catObj = categories.find { it.id == selectedCategoryId }
                    val catName = catObj?.name ?: "General"
                    val discount = if (op > p) (((op - p) / op) * 100).toInt() else 0

                    val sizesList = sizesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val colorsList = colorsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val imgs = if (imageUrl.isNotBlank()) listOf(imageUrl) else (product?.images ?: listOf("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80"))

                    val updatedOrNew = (product ?: Product(id = UUID.randomUUID().toString())).copy(
                        name = name.ifBlank { "New Product" },
                        brand = brand.ifBlank { "Kushal" },
                        price = p,
                        originalPrice = op,
                        discountPercent = discount,
                        stock = st,
                        description = description.ifBlank { "Quality product from Kushal Store." },
                        categoryId = selectedCategoryId,
                        categoryName = catName,
                        images = imgs,
                        sizes = sizesList,
                        colors = colorsList
                    )
                    onSave(updatedOrNew)
                }
            ) {
                Text("Save Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CategoryFormDialog(
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subcategoriesText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Category", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Category Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = subcategoriesText, onValueChange = { subcategoriesText = it }, label = { Text("Subcategories (comma-separated)") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val subs = subcategoriesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val cat = Category(
                            id = name.lowercase().replace(" ", "_"),
                            name = name,
                            iconName = "category",
                            subcategories = subs
                        )
                        onSave(cat)
                    }
                }
            ) {
                Text("Add Category")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CouponFormDialog(
    onDismiss: () -> Unit,
    onSave: (Coupon) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("20") }
    var minOrder by remember { mutableStateOf("50") }
    var maxDiscount by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Discount Coupon", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = code, onValueChange = { code = it.uppercase() }, label = { Text("Coupon Code (e.g. FESTIVE30)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Short Description") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = discountPercent, onValueChange = { discountPercent = it }, label = { Text("Discount Percentage (%)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = minOrder, onValueChange = { minOrder = it }, label = { Text("Minimum Order Value (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = maxDiscount, onValueChange = { maxDiscount = it }, label = { Text("Max Discount Cap (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (code.isNotBlank()) {
                        val cp = Coupon(
                            code = code,
                            discountType = DiscountType.PERCENTAGE,
                            discountAmount = discountPercent.toDoubleOrNull() ?: 20.0,
                            minOrderValue = minOrder.toDoubleOrNull() ?: 50.0,
                            maxDiscount = maxDiscount.toDoubleOrNull() ?: 100.0,
                            description = description.ifBlank { "Get $discountPercent% off on eligible orders" },
                            expiryDate = "Dec 31, 2026"
                        )
                        onSave(cp)
                    }
                }
            ) {
                Text("Create Coupon")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun BannerFormDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (PromoBanner) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var buttonText by remember { mutableStateOf("Shop Now") }
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Promotion Banner", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Banner Headline") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle / Offer Detail") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = buttonText, onValueChange = { buttonText = it }, label = { Text("Action Button Text") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Text("Link to Department:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategoryId == cat.id,
                            onClick = { selectedCategoryId = cat.id },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val bn = PromoBanner(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            subtitle = subtitle.ifBlank { "Special seasonal discounts available" },
                            buttonText = buttonText.ifBlank { "Shop Now" },
                            categoryId = selectedCategoryId,
                            bgGradientHexStart = 0xFF1E3A8A,
                            bgGradientHexEnd = 0xFF3B82F6
                        )
                        onSave(bn)
                    }
                }
            ) {
                Text("Add Banner")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
