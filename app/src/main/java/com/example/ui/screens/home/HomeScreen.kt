package com.example.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Category
import com.example.data.models.Product
import com.example.ui.components.ProductCard
import com.example.ui.components.PromoBannerCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StoreHeader
import com.example.ui.theme.SaleRed
import com.example.viewmodel.KushalViewModel

@Composable
fun HomeScreen(
    viewModel: KushalViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToAllProducts: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    val banners by viewModel.allBanners.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val recentlyViewed by viewModel.recentlyViewedProducts.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val unreadNotifs = notifications.count { !it.isRead }
    val featuredProducts = products.filter { it.isFeatured }
    val bestSellers = products.filter { it.isBestSeller }
    val deals = products.filter { it.isDeal || it.discountPercent >= 30 }
    val newArrivals = products.filter { it.isNewArrival }
    val trending = products.filter { it.isTrending }

    Scaffold(
        topBar = {
            StoreHeader(
                onSearchClick = onNavigateToSearch,
                onCartClick = onNavigateToCart,
                onWishlistClick = onNavigateToWishlist,
                onNotificationsClick = onNavigateToNotifications,
                onProfileClick = onNavigateToProfile,
                onAdminToggle = { viewModel.toggleAdminMode() },
                cartCount = cartItems.sumOf { it.quantity },
                wishlistCount = wishlistItems.size,
                unreadNotifCount = unreadNotifs,
                isAdminMode = isAdminMode
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_screen_list"),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Admin Mode Active Banner
            if (isAdminMode) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "👑 Admin Dashboard is active (Switch via top icon or Account)",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Promotional Banners Carousel
            if (banners.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(banners.filter { it.isActive }) { banner ->
                            Box(modifier = Modifier.width(320.dp)) {
                                PromoBannerCard(
                                    banner = banner,
                                    onBannerClick = {
                                        if (banner.categoryId.isNotBlank()) {
                                            viewModel.selectCategoryFilter(banner.categoryId)
                                            onNavigateToCategory(banner.categoryId)
                                        } else {
                                            onNavigateToAllProducts()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Category Icons Row
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Shop by Category",
                    subtitle = "Explore our wide collection of curated departments",
                    onViewAllClick = onNavigateToAllProducts
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        CategoryItemCircle(
                            category = category,
                            onClick = {
                                viewModel.selectCategoryFilter(category.id)
                                onNavigateToCategory(category.id)
                            }
                        )
                    }
                }
            }

            // Flash Deals / Super Savers
            if (deals.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        color = SaleRed.copy(alpha = 0.08f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalOffer,
                                        contentDescription = null,
                                        tint = SaleRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Top Deals & Discounts",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SaleRed
                                        )
                                    )
                                }
                                Surface(
                                    color = SaleRed,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "UP TO 50% OFF",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(deals) { product ->
                                    Box(modifier = Modifier.width(190.dp)) {
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
                    }
                }
            }

            // Featured Products Section
            if (featuredProducts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(
                        title = "Featured Products",
                        subtitle = "Handpicked best sellers for you",
                        onViewAllClick = onNavigateToAllProducts
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(featuredProducts) { product ->
                            Box(modifier = Modifier.width(190.dp)) {
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
            }

            // Trending Now
            if (trending.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(
                        title = "Trending Now 🔥",
                        subtitle = "What other shoppers are loving today",
                        onViewAllClick = onNavigateToAllProducts
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(trending) { product ->
                            Box(modifier = Modifier.width(190.dp)) {
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
            }

            // Best Sellers
            if (bestSellers.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(
                        title = "Best Sellers 🏆",
                        subtitle = "Highest rated customer favorites",
                        onViewAllClick = onNavigateToAllProducts
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(bestSellers) { product ->
                            Box(modifier = Modifier.width(190.dp)) {
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
            }

            // New Arrivals
            if (newArrivals.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(
                        title = "New Arrivals ✨",
                        subtitle = "Fresh additions to the Kushal Store catalog",
                        onViewAllClick = onNavigateToAllProducts
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(newArrivals) { product ->
                            Box(modifier = Modifier.width(190.dp)) {
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
            }

            // Recently Viewed
            if (recentlyViewed.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(
                        title = "Recently Viewed 👀",
                        subtitle = "Pick up where you left off"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(recentlyViewed) { product ->
                            Box(modifier = Modifier.width(190.dp)) {
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
            }

            // Trust & Features Footer
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Why Shop at Kushal Store?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FeatureBadgeItem(
                                icon = Icons.Default.Verified,
                                title = "100% Genuine",
                                desc = "Direct from brand"
                            )
                            FeatureBadgeItem(
                                icon = Icons.Default.LocalShipping,
                                title = "Fast Delivery",
                                desc = "Free on ₹50+"
                            )
                            FeatureBadgeItem(
                                icon = Icons.Default.Security,
                                title = "7-Day Return",
                                desc = "Easy instant refunds"
                            )
                            FeatureBadgeItem(
                                icon = Icons.Default.SupportAgent,
                                title = "24/7 Help",
                                desc = "Priority support"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItemCircle(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
            .testTag("home_category_${category.id}")
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getCategoryIcon(category.name),
                contentDescription = category.name,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FeatureBadgeItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(74.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
            textAlign = TextAlign.Center
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center
        )
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName.lowercase()) {
        "electronics" -> Icons.Default.Devices
        "fashion" -> Icons.Default.Checkroom
        "jewellery" -> Icons.Default.Diamond
        "beauty" -> Icons.Default.Face
        "home & kitchen" -> Icons.Default.Kitchen
        "grocery" -> Icons.Default.LocalGroceryStore
        "stationery" -> Icons.Default.EditNote
        "mobile accessories" -> Icons.Default.Smartphone
        "clothing" -> Icons.Default.DryCleaning
        else -> Icons.Default.Category
    }
}
