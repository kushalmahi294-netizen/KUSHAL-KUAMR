package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.models.Address
import com.example.data.models.CartItem
import com.example.data.models.Category
import com.example.data.models.Coupon
import com.example.data.models.DiscountType
import com.example.data.models.NotificationItem
import com.example.data.models.Order
import com.example.data.models.OrderItem
import com.example.data.models.OrderStatus
import com.example.data.models.PaymentMethod
import com.example.data.models.Product
import com.example.data.models.PromoBanner
import com.example.data.models.Review
import com.example.data.models.UserProfile
import com.example.data.models.WishlistItem
import com.example.data.repository.KushalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class SortOption(val displayName: String) {
    POPULARITY("Popularity"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    RATING("Customer Rating"),
    DISCOUNT("Highest Discount"),
    NEWEST("Newest Arrivals")
}

data class ProductFilterState(
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val selectedSubcategory: String? = null,
    val selectedBrand: String? = null,
    val minPrice: Double = 0.0,
    val maxPrice: Double = 2000.0,
    val minRating: Double = 0.0,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.POPULARITY
)

data class CartSummary(
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val appliedCoupon: Coupon? = null,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val itemCount: Int = 0,
    val savings: Double = 0.0
)

data class AdminStats(
    val totalRevenue: Double = 0.0,
    val totalOrders: Int = 0,
    val totalProducts: Int = 0,
    val totalCustomers: Int = 48,
    val pendingOrdersCount: Int = 0,
    val deliveredOrdersCount: Int = 0,
    val lowStockCount: Int = 0
)

class KushalViewModel(
    private val repository: KushalRepository
) : ViewModel() {

    // Auth state
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    // Base data flows
    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBanners: StateFlow<List<PromoBanner>> = repository.allBanners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItem>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAddresses: StateFlow<List<Address>> = repository.allAddresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCoupons: StateFlow<List<Coupon>> = repository.allCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationItem>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAdminReviews: StateFlow<List<Review>> = repository.allAdminReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter & Search State
    private val _filterState = MutableStateFlow(ProductFilterState())
    val filterState: StateFlow<ProductFilterState> = _filterState.asStateFlow()

    // Search History
    private val _searchHistory = MutableStateFlow(listOf("Headphones", "Coffee", "Maxi dress", "Gold ring", "Power bank"))
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    // Applied Coupon
    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    // Selected Checkout Address
    private val _selectedCheckoutAddressId = MutableStateFlow<String?>(null)
    val selectedCheckoutAddressId: StateFlow<String?> = _selectedCheckoutAddressId.asStateFlow()

    // UI Feedback messages
    private val _toastEvents = MutableSharedFlow<String>()
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    // Recently viewed product IDs
    private val _recentlyViewedProductIds = MutableStateFlow<List<String>>(emptyList())
    val recentlyViewedProducts: StateFlow<List<Product>> = combine(
        allProducts,
        _recentlyViewedProductIds
    ) { products, ids ->
        ids.mapNotNull { id -> products.find { it.id == id } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Products
    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        _filterState
    ) { products, filter ->
        var list = products.filter { it.isActive }

        if (filter.searchQuery.isNotBlank()) {
            val q = filter.searchQuery.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                        it.brand.lowercase().contains(q) ||
                        it.categoryName.lowercase().contains(q) ||
                        it.subcategory.lowercase().contains(q) ||
                        it.description.lowercase().contains(q)
            }
        }

        if (filter.selectedCategoryId != null) {
            list = list.filter { it.categoryId == filter.selectedCategoryId }
        }

        if (filter.selectedSubcategory != null) {
            list = list.filter { it.subcategory.equals(filter.selectedSubcategory, ignoreCase = true) }
        }

        if (filter.selectedBrand != null) {
            list = list.filter { it.brand.equals(filter.selectedBrand, ignoreCase = true) }
        }

        list = list.filter { it.price in filter.minPrice..filter.maxPrice }

        if (filter.minRating > 0) {
            list = list.filter { it.rating >= filter.minRating }
        }

        if (filter.inStockOnly) {
            list = list.filter { it.stock > 0 }
        }

        when (filter.sortOption) {
            SortOption.POPULARITY -> list.sortedByDescending { it.reviewCount }
            SortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> list.sortedByDescending { it.discountPercent }
            SortOption.NEWEST -> list.sortedByDescending { it.createdAt }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart Summary
    val cartSummary: StateFlow<CartSummary> = combine(
        cartItems,
        _appliedCoupon
    ) { items, coupon ->
        val subtotal = items.sumOf { it.price * it.quantity }
        val originalTotal = items.sumOf { it.originalPrice * it.quantity }
        val productSavings = (originalTotal - subtotal).coerceAtLeast(0.0)

        var couponDiscount = 0.0
        if (coupon != null && subtotal >= coupon.minOrderValue) {
            couponDiscount = if (coupon.discountType == DiscountType.PERCENTAGE) {
                (subtotal * (coupon.discountAmount / 100.0)).coerceAtMost(coupon.maxDiscount)
            } else {
                coupon.discountAmount.coerceAtMost(subtotal)
            }
        }

        val deliveryFee = if (subtotal > 50.0 || subtotal == 0.0) 0.0 else 9.99
        val total = (subtotal - couponDiscount + deliveryFee).coerceAtLeast(0.0)
        val itemCount = items.sumOf { it.quantity }

        CartSummary(
            subtotal = subtotal,
            discount = couponDiscount,
            appliedCoupon = coupon,
            deliveryFee = deliveryFee,
            total = total,
            itemCount = itemCount,
            savings = productSavings + couponDiscount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    // Admin Stats
    val adminStats: StateFlow<AdminStats> = combine(
        allOrders,
        allProducts
    ) { orders, products ->
        val totalRev = orders.filter { it.orderStatus != OrderStatus.CANCELLED }.sumOf { it.totalAmount }
        val pendingCount = orders.count { it.orderStatus == OrderStatus.PENDING || it.orderStatus == OrderStatus.CONFIRMED || it.orderStatus == OrderStatus.PACKED }
        val deliveredCount = orders.count { it.orderStatus == OrderStatus.DELIVERED }
        val lowStock = products.count { it.stock in 1..15 }

        AdminStats(
            totalRevenue = totalRev,
            totalOrders = orders.size,
            totalProducts = products.size,
            totalCustomers = 56,
            pendingOrdersCount = pendingCount,
            deliveredOrdersCount = deliveredCount,
            lowStockCount = lowStock
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminStats())

    // Toast emitter
    fun showToast(message: String) {
        viewModelScope.launch {
            _toastEvents.emit(message)
        }
    }

    // Filter operations
    fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(searchQuery = query) }
        if (query.isNotBlank() && !_searchHistory.value.contains(query.trim())) {
            _searchHistory.update { listOf(query.trim()) + it.take(8) }
        }
    }

    fun clearSearchQuery() {
        _filterState.update { it.copy(searchQuery = "") }
    }

    fun removeSearchHistoryItem(item: String) {
        _searchHistory.update { it.filter { h -> h != item } }
    }

    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }

    fun selectCategoryFilter(categoryId: String?) {
        _filterState.update { it.copy(selectedCategoryId = categoryId, selectedSubcategory = null) }
    }

    fun selectSubcategoryFilter(subcategory: String?) {
        _filterState.update { it.copy(selectedSubcategory = subcategory) }
    }

    fun selectBrandFilter(brand: String?) {
        _filterState.update { it.copy(selectedBrand = brand) }
    }

    fun updateSortOption(sortOption: SortOption) {
        _filterState.update { it.copy(sortOption = sortOption) }
    }

    fun updatePriceRange(min: Double, max: Double) {
        _filterState.update { it.copy(minPrice = min, maxPrice = max) }
    }

    fun updateMinRating(rating: Double) {
        _filterState.update { it.copy(minRating = rating) }
    }

    fun toggleInStockOnly(inStock: Boolean) {
        _filterState.update { it.copy(inStockOnly = inStock) }
    }

    fun resetFilters() {
        _filterState.value = ProductFilterState()
    }

    // Cart Operations
    fun addToCart(product: Product, quantity: Int = 1, selectedSize: String = "", selectedColor: String = "") {
        viewModelScope.launch {
            repository.addToCart(product, quantity, selectedSize, selectedColor)
            showToast("Added \"${product.name}\" to Cart!")
        }
    }

    fun updateCartQuantity(id: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(id, quantity)
        }
    }

    fun removeFromCart(id: String, name: String = "Item") {
        viewModelScope.launch {
            repository.removeFromCart(id)
            showToast("Removed $name from Cart")
        }
    }

    fun saveForLater(item: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(item.id)
            val product = repository.getProductById(item.productId)
            if (product != null) {
                repository.toggleWishlist(product)
                showToast("Moved \"${item.productName}\" to Wishlist")
            }
        }
    }

    fun applyCoupon(code: String) {
        viewModelScope.launch {
            val subtotal = cartSummary.value.subtotal
            val (valid, message) = repository.validateCoupon(code, subtotal)
            if (valid) {
                val coupon = allCoupons.value.find { it.code.equals(code.trim(), ignoreCase = true) }
                _appliedCoupon.value = coupon
                showToast(message)
            } else {
                showToast(message)
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        showToast("Coupon removed")
    }

    // Wishlist Operations
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val currentlyIn = wishlistItems.value.any { it.productId == product.id }
            repository.toggleWishlist(product)
            if (currentlyIn) {
                showToast("Removed from Wishlist")
            } else {
                showToast("Saved to Wishlist ❤️")
            }
        }
    }

    fun removeWishlistItem(id: String) {
        viewModelScope.launch {
            repository.removeWishlistItem(id)
            showToast("Removed from Wishlist")
        }
    }

    fun moveWishlistToCart(item: WishlistItem) {
        viewModelScope.launch {
            val product = repository.getProductById(item.productId)
            if (product != null) {
                repository.addToCart(product)
                repository.removeWishlistItem(item.id)
                showToast("Moved to Cart!")
            }
        }
    }

    // Product Tracking
    fun markProductViewed(productId: String) {
        _recentlyViewedProductIds.update { current ->
            (listOf(productId) + current.filter { it != productId }).take(10)
        }
    }

    // Address Operations
    fun selectCheckoutAddress(id: String) {
        _selectedCheckoutAddressId.value = id
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            repository.addAddress(address)
            showToast("Address saved successfully!")
        }
    }

    fun setDefaultAddress(id: String) {
        viewModelScope.launch {
            repository.setDefaultAddress(id)
            showToast("Default address updated")
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            repository.deleteAddress(id)
            showToast("Address deleted")
        }
    }

    // Checkout & Order Placement
    fun placeOrder(
        shippingAddress: Address,
        paymentMethod: PaymentMethod,
        onSuccess: (Order) -> Unit
    ) {
        viewModelScope.launch {
            val items = cartItems.value.map {
                OrderItem(
                    productId = it.productId,
                    productName = it.productName,
                    productBrand = it.productBrand,
                    productImage = it.productImage,
                    price = it.price,
                    quantity = it.quantity,
                    selectedSize = it.selectedSize,
                    selectedColor = it.selectedColor
                )
            }
            if (items.isEmpty()) {
                showToast("Your cart is empty!")
                return@launch
            }

            val summary = cartSummary.value
            val order = Order(
                items = items,
                subtotal = summary.subtotal,
                discount = summary.discount,
                couponCode = summary.appliedCoupon?.code ?: "",
                deliveryCharge = summary.deliveryFee,
                totalAmount = summary.total,
                paymentMethod = paymentMethod,
                paymentStatus = if (paymentMethod == PaymentMethod.COD) "Pending COD" else "Paid Online",
                orderStatus = OrderStatus.CONFIRMED,
                shippingAddress = shippingAddress,
                estimatedDeliveryDate = "Delivery by " + java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(System.currentTimeMillis() + 86400000L * 4)
            )

            repository.placeOrder(order)
            _appliedCoupon.value = null
            showToast("Order #${order.id} placed successfully! 🎉")
            onSuccess(order)
        }
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            showToast("Order status updated to ${status.displayName}")
        }
    }

    fun requestOrderReturn(orderId: String, reason: String) {
        viewModelScope.launch {
            repository.requestOrderReturn(orderId, reason)
            showToast("Return request submitted for Order #$orderId")
        }
    }

    // Reviews
    fun addReview(productId: String, rating: Int, title: String, comment: String, userName: String) {
        viewModelScope.launch {
            val review = Review(
                productId = productId,
                userName = userName.ifBlank { userProfile.value.name },
                rating = rating,
                title = title,
                comment = comment,
                date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                isVerifiedPurchase = true,
                isApproved = true
            )
            repository.addReview(review)
            showToast("Thank you! Your review has been published.")
        }
    }

    fun setReviewApproval(reviewId: String, approved: Boolean) {
        viewModelScope.launch {
            repository.setReviewApproval(reviewId, approved)
            showToast(if (approved) "Review approved" else "Review unapproved")
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            repository.deleteReview(reviewId)
            showToast("Review removed")
        }
    }

    // Admin Product Management
    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.addProduct(product)
            showToast("Product \"${product.name}\" created!")
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
            showToast("Product updated successfully!")
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            repository.deleteProduct(id)
            showToast("Product removed")
        }
    }

    // Admin Categories
    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
            showToast("Category \"${category.name}\" added!")
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            repository.deleteCategory(id)
            showToast("Category deleted")
        }
    }

    // Admin Coupons
    fun addCoupon(coupon: Coupon) {
        viewModelScope.launch {
            repository.addCoupon(coupon)
            showToast("Coupon ${coupon.code} created!")
        }
    }

    fun deleteCoupon(code: String) {
        viewModelScope.launch {
            repository.deleteCoupon(code)
            showToast("Coupon $code deleted")
        }
    }

    // Admin Banners
    fun addBanner(banner: PromoBanner) {
        viewModelScope.launch {
            repository.addBanner(banner)
            showToast("Banner added!")
        }
    }

    fun deleteBanner(id: String) {
        viewModelScope.launch {
            repository.deleteBanner(id)
            showToast("Banner removed")
        }
    }

    // Profile & Auth
    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val updated = userProfile.value.copy(name = name, email = email, phone = phone)
            repository.updateUserProfile(updated)
            showToast("Profile updated successfully")
        }
    }

    fun toggleAdminMode() {
        _isAdminMode.update { !it }
        val mode = if (_isAdminMode.value) "Admin Panel Activated" else "Switched to Customer Store"
        showToast(mode)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    fun logout() {
        _isLoggedIn.value = false
        showToast("Logged out successfully")
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun getReviewsForProduct(productId: String) = repository.getReviewsForProduct(productId)
}

class KushalViewModelFactory(private val repository: KushalRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KushalViewModel::class.java)) {
            return KushalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
