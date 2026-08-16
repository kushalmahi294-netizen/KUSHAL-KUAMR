package com.example.data.repository

import com.example.data.local.AddressEntity
import com.example.data.local.BannerEntity
import com.example.data.local.CartEntity
import com.example.data.local.CategoryEntity
import com.example.data.local.CouponEntity
import com.example.data.local.KushalDao
import com.example.data.local.NotificationEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserProfileEntity
import com.example.data.models.Address
import com.example.data.models.CartItem
import com.example.data.models.Category
import com.example.data.models.Coupon
import com.example.data.models.DiscountType
import com.example.data.models.NotificationItem
import com.example.data.models.Order
import com.example.data.models.OrderStatus
import com.example.data.models.PaymentMethod
import com.example.data.models.Product
import com.example.data.models.PromoBanner
import com.example.data.models.Review
import com.example.data.models.UserProfile
import com.example.data.models.WishlistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class KushalRepository(
    private val dao: KushalDao,
    private val appScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    init {
        appScope.launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingProducts = dao.getAllProducts().firstOrNull()
        if (existingProducts.isNullOrEmpty()) {
            // Seed Categories
            val catEntities = SampleData.categories.map {
                CategoryEntity(
                    id = it.id,
                    name = it.name,
                    iconName = it.iconName,
                    subcategories = it.subcategories,
                    description = it.description
                )
            }
            dao.insertCategories(catEntities)

            // Seed Products
            val prodEntities = SampleData.products.map { it.toEntity() }
            dao.insertProducts(prodEntities)

            // Seed Banners
            val bannerEntities = SampleData.banners.map {
                BannerEntity(
                    id = it.id,
                    title = it.title,
                    subtitle = it.subtitle,
                    categoryId = it.categoryId,
                    buttonText = it.buttonText,
                    bgGradientHexStart = it.bgGradientHexStart,
                    bgGradientHexEnd = it.bgGradientHexEnd,
                    isActive = it.isActive
                )
            }
            dao.insertBanners(bannerEntities)

            // Seed Coupons
            val couponEntities = SampleData.coupons.map {
                CouponEntity(
                    code = it.code,
                    discountType = it.discountType,
                    discountAmount = it.discountAmount,
                    minOrderValue = it.minOrderValue,
                    maxDiscount = it.maxDiscount,
                    expiryDate = it.expiryDate,
                    usageLimit = it.usageLimit,
                    timesUsed = it.timesUsed,
                    isActive = it.isActive,
                    description = it.description
                )
            }
            dao.insertCoupons(couponEntities)

            // Seed Addresses
            val addressEntities = SampleData.addresses.map { it.toEntity() }
            addressEntities.forEach { dao.insertAddress(it) }

            // Seed Reviews
            val reviewEntities = SampleData.reviews.map {
                ReviewEntity(
                    id = it.id,
                    productId = it.productId,
                    userName = it.userName,
                    rating = it.rating,
                    title = it.title,
                    comment = it.comment,
                    date = it.date,
                    isVerifiedPurchase = it.isVerifiedPurchase,
                    isApproved = it.isApproved,
                    imageUrl = it.imageUrl
                )
            }
            dao.insertReviews(reviewEntities)

            // Seed Orders
            val orderEntities = SampleData.initialOrders.map { it.toEntity() }
            orderEntities.forEach { dao.insertOrder(it) }

            // Seed Notifications
            val notifEntities = SampleData.notifications.map {
                NotificationEntity(
                    id = it.id,
                    title = it.title,
                    message = it.message,
                    timeAgo = it.timeAgo,
                    isRead = it.isRead,
                    type = it.type
                )
            }
            dao.insertNotifications(notifEntities)

            // Seed Default User
            val user = SampleData.defaultUser
            dao.insertUserProfile(
                UserProfileEntity(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    isAdmin = user.isAdmin,
                    memberSince = user.memberSince
                )
            )
        }
    }

    // Products
    val allProducts: Flow<List<Product>> = dao.getAllProducts().map { list -> list.map { it.toDomain() } }

    val allCategories: Flow<List<Category>> = dao.getAllCategories().map { list ->
        list.map { Category(it.id, it.name, it.iconName, it.subcategories, it.description) }
    }

    suspend fun getProductById(id: String): Product? = withContext(Dispatchers.IO) {
        dao.getProductById(id)?.toDomain()
    }

    suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        dao.insertProduct(product.toEntity())
    }

    suspend fun updateProduct(product: Product) = withContext(Dispatchers.IO) {
        dao.updateProduct(product.toEntity())
    }

    suspend fun deleteProduct(id: String) = withContext(Dispatchers.IO) {
        dao.deleteProduct(id)
    }

    // Categories
    suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        dao.insertCategory(CategoryEntity(category.id, category.name, category.iconName, category.subcategories, category.description))
    }

    suspend fun deleteCategory(id: String) = withContext(Dispatchers.IO) {
        dao.deleteCategory(id)
    }

    // Cart
    val cartItems: Flow<List<CartItem>> = dao.getCartItems().map { list ->
        list.map {
            CartItem(
                id = it.id,
                productId = it.productId,
                productName = it.productName,
                productBrand = it.productBrand,
                productImage = it.productImage,
                price = it.price,
                originalPrice = it.originalPrice,
                quantity = it.quantity,
                selectedSize = it.selectedSize,
                selectedColor = it.selectedColor,
                maxStock = it.maxStock
            )
        }
    }

    suspend fun addToCart(
        product: Product,
        quantity: Int = 1,
        selectedSize: String = "",
        selectedColor: String = ""
    ) = withContext(Dispatchers.IO) {
        val currentCart = dao.getCartItems().firstOrNull() ?: emptyList()
        val existing = currentCart.find {
            it.productId == product.id && it.selectedSize == selectedSize && it.selectedColor == selectedColor
        }
        if (existing != null) {
            val updated = existing.copy(quantity = (existing.quantity + quantity).coerceAtMost(product.stock))
            dao.updateCartItem(updated)
        } else {
            val newItem = CartEntity(
                id = UUID.randomUUID().toString(),
                productId = product.id,
                productName = product.name,
                productBrand = product.brand,
                productImage = product.images.firstOrNull() ?: "",
                price = product.price,
                originalPrice = product.originalPrice,
                quantity = quantity.coerceAtMost(product.stock),
                selectedSize = selectedSize,
                selectedColor = selectedColor,
                maxStock = product.stock
            )
            dao.insertCartItem(newItem)
        }
    }

    suspend fun updateCartItemQuantity(id: String, quantity: Int) = withContext(Dispatchers.IO) {
        val currentCart = dao.getCartItems().firstOrNull() ?: emptyList()
        val item = currentCart.find { it.id == id } ?: return@withContext
        if (quantity <= 0) {
            dao.deleteCartItem(id)
        } else {
            dao.updateCartItem(item.copy(quantity = quantity.coerceAtMost(item.maxStock)))
        }
    }

    suspend fun removeFromCart(id: String) = withContext(Dispatchers.IO) {
        dao.deleteCartItem(id)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        dao.clearCart()
    }

    // Wishlist
    val wishlistItems: Flow<List<WishlistItem>> = dao.getWishlistItems().map { list ->
        list.map {
            WishlistItem(
                id = it.id,
                productId = it.productId,
                productName = it.productName,
                productBrand = it.productBrand,
                productImage = it.productImage,
                price = it.price,
                originalPrice = it.originalPrice,
                rating = it.rating,
                inStock = it.inStock
            )
        }
    }

    fun isProductInWishlist(productId: String): Flow<Boolean> = dao.isInWishlist(productId)

    suspend fun toggleWishlist(product: Product) = withContext(Dispatchers.IO) {
        val inWishlist = dao.isInWishlist(product.id).firstOrNull() ?: false
        if (inWishlist) {
            dao.deleteWishlistItemByProductId(product.id)
        } else {
            dao.insertWishlistItem(
                com.example.data.local.WishlistEntity(
                    id = UUID.randomUUID().toString(),
                    productId = product.id,
                    productName = product.name,
                    productBrand = product.brand,
                    productImage = product.images.firstOrNull() ?: "",
                    price = product.price,
                    originalPrice = product.originalPrice,
                    rating = product.rating,
                    inStock = product.stock > 0
                )
            )
        }
    }

    suspend fun removeWishlistItem(id: String) = withContext(Dispatchers.IO) {
        dao.deleteWishlistItem(id)
    }

    // Addresses
    val allAddresses: Flow<List<Address>> = dao.getAllAddresses().map { list -> list.map { it.toDomain() } }

    suspend fun addAddress(address: Address) = withContext(Dispatchers.IO) {
        if (address.isDefault) {
            dao.clearDefaultAddress()
        }
        dao.insertAddress(address.toEntity())
    }

    suspend fun setDefaultAddress(id: String) = withContext(Dispatchers.IO) {
        dao.clearDefaultAddress()
        dao.setDefaultAddress(id)
    }

    suspend fun deleteAddress(id: String) = withContext(Dispatchers.IO) {
        dao.deleteAddress(id)
    }

    // Orders
    val allOrders: Flow<List<Order>> = dao.getAllOrders().map { list -> list.map { it.toDomain() } }

    suspend fun getOrderById(id: String): Order? = withContext(Dispatchers.IO) {
        dao.getOrderById(id)?.toDomain()
    }

    suspend fun placeOrder(order: Order) = withContext(Dispatchers.IO) {
        dao.insertOrder(order.toEntity())
        dao.clearCart()

        // Create order notification
        dao.insertNotification(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                title = "Order Placed Successfully 🎉",
                message = "Order #${order.id} for ₹${String.format("%.2f", order.totalAmount)} has been placed.",
                timeAgo = "Just now",
                isRead = false,
                type = "Order"
            )
        )
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) = withContext(Dispatchers.IO) {
        dao.updateOrderStatus(orderId, status)
        dao.insertNotification(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                title = "Order Status Update",
                message = "Order #$orderId is now ${status.displayName}.",
                timeAgo = "Just now",
                isRead = false,
                type = "Order"
            )
        )
    }

    suspend fun requestOrderReturn(orderId: String, reason: String) = withContext(Dispatchers.IO) {
        dao.requestOrderReturn(orderId, reason)
    }

    // Reviews
    fun getReviewsForProduct(productId: String): Flow<List<Review>> = dao.getReviewsForProduct(productId).map { list ->
        list.map {
            Review(
                id = it.id,
                productId = it.productId,
                userName = it.userName,
                rating = it.rating,
                title = it.title,
                comment = it.comment,
                date = it.date,
                isVerifiedPurchase = it.isVerifiedPurchase,
                isApproved = it.isApproved,
                imageUrl = it.imageUrl
            )
        }
    }

    val allAdminReviews: Flow<List<Review>> = dao.getAllReviews().map { list ->
        list.map {
            Review(
                id = it.id,
                productId = it.productId,
                userName = it.userName,
                rating = it.rating,
                title = it.title,
                comment = it.comment,
                date = it.date,
                isVerifiedPurchase = it.isVerifiedPurchase,
                isApproved = it.isApproved,
                imageUrl = it.imageUrl
            )
        }
    }

    suspend fun addReview(review: Review) = withContext(Dispatchers.IO) {
        dao.insertReview(
            ReviewEntity(
                id = review.id,
                productId = review.productId,
                userName = review.userName,
                rating = review.rating,
                title = review.title,
                comment = review.comment,
                date = review.date,
                isVerifiedPurchase = review.isVerifiedPurchase,
                isApproved = review.isApproved,
                imageUrl = review.imageUrl
            )
        )
    }

    suspend fun setReviewApproval(reviewId: String, approved: Boolean) = withContext(Dispatchers.IO) {
        dao.setReviewApproval(reviewId, approved)
    }

    suspend fun deleteReview(reviewId: String) = withContext(Dispatchers.IO) {
        dao.deleteReview(reviewId)
    }

    // Coupons
    val allCoupons: Flow<List<Coupon>> = dao.getAllCoupons().map { list ->
        list.map {
            Coupon(
                code = it.code,
                discountType = it.discountType,
                discountAmount = it.discountAmount,
                minOrderValue = it.minOrderValue,
                maxDiscount = it.maxDiscount,
                expiryDate = it.expiryDate,
                usageLimit = it.usageLimit,
                timesUsed = it.timesUsed,
                isActive = it.isActive,
                description = it.description
            )
        }
    }

    suspend fun validateCoupon(code: String, subtotal: Double): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val coupon = dao.getCouponByCode(code.uppercase().trim())
        if (coupon == null) {
            return@withContext Pair(false, "Invalid coupon code")
        }
        if (!coupon.isActive) {
            return@withContext Pair(false, "This coupon is currently inactive")
        }
        if (subtotal < coupon.minOrderValue) {
            return@withContext Pair(false, "Minimum order value of ₹${coupon.minOrderValue} required")
        }
        if (coupon.timesUsed >= coupon.usageLimit) {
            return@withContext Pair(false, "Coupon usage limit reached")
        }
        Pair(true, "Coupon applied successfully!")
    }

    suspend fun addCoupon(coupon: Coupon) = withContext(Dispatchers.IO) {
        dao.insertCoupon(
            CouponEntity(
                code = coupon.code.uppercase().trim(),
                discountType = coupon.discountType,
                discountAmount = coupon.discountAmount,
                minOrderValue = coupon.minOrderValue,
                maxDiscount = coupon.maxDiscount,
                expiryDate = coupon.expiryDate,
                usageLimit = coupon.usageLimit,
                timesUsed = coupon.timesUsed,
                isActive = coupon.isActive,
                description = coupon.description
            )
        )
    }

    suspend fun deleteCoupon(code: String) = withContext(Dispatchers.IO) {
        dao.deleteCoupon(code)
    }

    // Banners
    val allBanners: Flow<List<PromoBanner>> = dao.getAllBanners().map { list ->
        list.map {
            PromoBanner(
                id = it.id,
                title = it.title,
                subtitle = it.subtitle,
                categoryId = it.categoryId,
                buttonText = it.buttonText,
                bgGradientHexStart = it.bgGradientHexStart,
                bgGradientHexEnd = it.bgGradientHexEnd,
                isActive = it.isActive
            )
        }
    }

    suspend fun addBanner(banner: PromoBanner) = withContext(Dispatchers.IO) {
        dao.insertBanner(
            BannerEntity(
                id = banner.id,
                title = banner.title,
                subtitle = banner.subtitle,
                categoryId = banner.categoryId,
                buttonText = banner.buttonText,
                bgGradientHexStart = banner.bgGradientHexStart,
                bgGradientHexEnd = banner.bgGradientHexEnd,
                isActive = banner.isActive
            )
        )
    }

    suspend fun deleteBanner(id: String) = withContext(Dispatchers.IO) {
        dao.deleteBanner(id)
    }

    // Notifications
    val allNotifications: Flow<List<NotificationItem>> = dao.getAllNotifications().map { list ->
        list.map {
            NotificationItem(
                id = it.id,
                title = it.title,
                message = it.message,
                timeAgo = it.timeAgo,
                isRead = it.isRead,
                type = it.type
            )
        }
    }

    suspend fun markAllNotificationsRead() = withContext(Dispatchers.IO) {
        dao.markAllNotificationsAsRead()
    }

    // User Profile
    val userProfile: Flow<UserProfile> = dao.getUserProfile().map {
        if (it == null) {
            SampleData.defaultUser
        } else {
            UserProfile(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone,
                isAdmin = it.isAdmin,
                memberSince = it.memberSince
            )
        }
    }

    suspend fun updateUserProfile(user: UserProfile) = withContext(Dispatchers.IO) {
        dao.insertUserProfile(
            UserProfileEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                phone = user.phone,
                isAdmin = user.isAdmin,
                memberSince = user.memberSince
            )
        )
    }

    // Mappers
    private fun Product.toEntity(): ProductEntity = ProductEntity(
        id = id,
        name = name,
        brand = brand,
        categoryId = categoryId,
        categoryName = categoryName,
        subcategory = subcategory,
        price = price,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        rating = rating,
        reviewCount = reviewCount,
        stock = stock,
        description = description,
        specifications = specifications,
        images = images,
        sizes = sizes,
        colors = colors,
        isFeatured = isFeatured,
        isBestSeller = isBestSeller,
        isTrending = isTrending,
        isNewArrival = isNewArrival,
        isDeal = isDeal,
        isActive = isActive,
        createdAt = createdAt
    )

    private fun ProductEntity.toDomain(): Product = Product(
        id = id,
        name = name,
        brand = brand,
        categoryId = categoryId,
        categoryName = categoryName,
        subcategory = subcategory,
        price = price,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        rating = rating,
        reviewCount = reviewCount,
        stock = stock,
        description = description,
        specifications = specifications,
        images = images,
        sizes = sizes,
        colors = colors,
        isFeatured = isFeatured,
        isBestSeller = isBestSeller,
        isTrending = isTrending,
        isNewArrival = isNewArrival,
        isDeal = isDeal,
        isActive = isActive,
        createdAt = createdAt
    )

    private fun Address.toEntity(): AddressEntity = AddressEntity(
        id = id,
        fullName = fullName,
        phone = phone,
        streetAddress = streetAddress,
        city = city,
        state = state,
        pinCode = pinCode,
        addressType = addressType,
        isDefault = isDefault
    )

    private fun AddressEntity.toDomain(): Address = Address(
        id = id,
        fullName = fullName,
        phone = phone,
        streetAddress = streetAddress,
        city = city,
        state = state,
        pinCode = pinCode,
        addressType = addressType,
        isDefault = isDefault
    )

    private fun Order.toEntity(): OrderEntity = OrderEntity(
        id = id,
        items = items,
        subtotal = subtotal,
        discount = discount,
        couponCode = couponCode,
        deliveryCharge = deliveryCharge,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        orderStatus = orderStatus,
        shippingAddress = shippingAddress,
        createdAt = createdAt,
        estimatedDeliveryDate = estimatedDeliveryDate,
        trackingNumber = trackingNumber,
        returnRequested = returnRequested,
        returnReason = returnReason
    )

    private fun OrderEntity.toDomain(): Order = Order(
        id = id,
        items = items,
        subtotal = subtotal,
        discount = discount,
        couponCode = couponCode,
        deliveryCharge = deliveryCharge,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        orderStatus = orderStatus,
        shippingAddress = shippingAddress,
        createdAt = createdAt,
        estimatedDeliveryDate = estimatedDeliveryDate,
        trackingNumber = trackingNumber,
        returnRequested = returnRequested,
        returnReason = returnReason
    )
}
