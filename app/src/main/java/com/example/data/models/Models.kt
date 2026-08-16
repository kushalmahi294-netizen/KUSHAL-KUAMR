package com.example.data.models

import java.util.UUID

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PACKED("Packed"),
    SHIPPED("Shipped"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

enum class PaymentMethod(val title: String) {
    COD("Cash on Delivery"),
    UPI("UPI / Google Pay / PhonePe"),
    CREDIT_CARD("Credit / Debit Card"),
    NET_BANKING("Net Banking");

    val displayName: String get() = title
}

enum class DiscountType {
    PERCENTAGE,
    FIXED
}

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val brand: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val subcategory: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val discountPercent: Int = if (originalPrice > price && originalPrice > 0) (((originalPrice - price) / originalPrice) * 100).toInt() else 0,
    val rating: Double = 4.5,
    val reviewCount: Int = 120,
    val stock: Int = 25,
    val description: String = "",
    val specifications: Map<String, String> = emptyMap(),
    val images: List<String> = emptyList(),
    val sizes: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isBestSeller: Boolean = false,
    val isTrending: Boolean = false,
    val isNewArrival: Boolean = false,
    val isDeal: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class Category(
    val id: String,
    val name: String,
    val iconName: String = "category",
    val subcategories: List<String> = emptyList(),
    val description: String = ""
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val productName: String,
    val productBrand: String,
    val productImage: String,
    val price: Double,
    val originalPrice: Double,
    val quantity: Int = 1,
    val selectedSize: String = "",
    val selectedColor: String = "",
    val maxStock: Int = 99
)

data class WishlistItem(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val productName: String,
    val productBrand: String,
    val productImage: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Double,
    val inStock: Boolean = true
)

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String = "",
    val phone: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val pinCode: String = "",
    val addressType: String = "Home", // Home, Work, Other
    val isDefault: Boolean = false
) {
    val phoneNumber: String get() = phone
    val addressLine: String get() = streetAddress
}

data class OrderItem(
    val productId: String,
    val productName: String,
    val productBrand: String,
    val productImage: String,
    val price: Double,
    val quantity: Int,
    val selectedSize: String = "",
    val selectedColor: String = ""
)

data class Order(
    val id: String = "KS-${(System.currentTimeMillis() % 1000000).toString().padStart(6, '0')}",
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val couponCode: String = "",
    val deliveryCharge: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    val paymentStatus: String = "Paid",
    val orderStatus: OrderStatus = OrderStatus.CONFIRMED,
    val shippingAddress: Address = Address(),
    val createdAt: Long = System.currentTimeMillis(),
    val estimatedDeliveryDate: String = "3-5 Business Days",
    val trackingNumber: String = "TRK-${UUID.randomUUID().toString().take(8).uppercase()}",
    val returnRequested: Boolean = false,
    val returnReason: String = ""
) {
    val formattedDate: String get() = "Aug 15, 2026"
}

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val userName: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val date: String,
    val isVerifiedPurchase: Boolean = true,
    val isApproved: Boolean = true,
    val imageUrl: String = ""
)

data class Coupon(
    val code: String,
    val discountType: DiscountType,
    val discountAmount: Double,
    val minOrderValue: Double = 0.0,
    val maxDiscount: Double = 1000.0,
    val expiryDate: String = "2026-12-31",
    val usageLimit: Int = 500,
    val timesUsed: Int = 12,
    val isActive: Boolean = true,
    val description: String = ""
)

data class PromoBanner(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val categoryId: String = "",
    val buttonText: String = "Shop Now",
    val bgGradientHexStart: Long = 0xFF1E3A8A,
    val bgGradientHexEnd: Long = 0xFF3B82F6,
    val isActive: Boolean = true
)

data class UserProfile(
    val id: String = "user_1",
    val name: String = "Kushal Khare",
    val email: String = "kushalkhare30@gmail.com",
    val phone: String = "+91 98765 43210",
    val isAdmin: Boolean = false,
    val loyaltyPoints: Int = 450,
    val memberSince: String = "August 2026"
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timeAgo: String = "2 hrs ago",
    val isRead: Boolean = false,
    val type: String = "Order" // Order, Promo, System
) {
    val date: String get() = timeAgo
}
