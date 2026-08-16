package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.models.Address
import com.example.data.models.DiscountType
import com.example.data.models.OrderItem
import com.example.data.models.OrderStatus
import com.example.data.models.PaymentMethod

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String,
    val categoryId: String,
    val categoryName: String,
    val subcategory: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int,
    val rating: Double,
    val reviewCount: Int,
    val stock: Int,
    val description: String,
    val specifications: Map<String, String>,
    val images: List<String>,
    val sizes: List<String>,
    val colors: List<String>,
    val isFeatured: Boolean,
    val isBestSeller: Boolean,
    val isTrending: Boolean,
    val isNewArrival: Boolean,
    val isDeal: Boolean,
    val isActive: Boolean,
    val createdAt: Long
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String,
    val subcategories: List<String>,
    val description: String
)

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val productName: String,
    val productBrand: String,
    val productImage: String,
    val price: Double,
    val originalPrice: Double,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String,
    val maxStock: Int
)

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val productName: String,
    val productBrand: String,
    val productImage: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Double,
    val inStock: Boolean
)

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phone: String,
    val streetAddress: String,
    val city: String,
    val state: String,
    val pinCode: String,
    val addressType: String,
    val isDefault: Boolean
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val items: List<OrderItem>,
    val subtotal: Double,
    val discount: Double,
    val couponCode: String,
    val deliveryCharge: Double,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val paymentStatus: String,
    val orderStatus: OrderStatus,
    val shippingAddress: Address,
    val createdAt: Long,
    val estimatedDeliveryDate: String,
    val trackingNumber: String,
    val returnRequested: Boolean,
    val returnReason: String
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val userName: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val date: String,
    val isVerifiedPurchase: Boolean,
    val isApproved: Boolean,
    val imageUrl: String
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val discountType: DiscountType,
    val discountAmount: Double,
    val minOrderValue: Double,
    val maxDiscount: Double,
    val expiryDate: String,
    val usageLimit: Int,
    val timesUsed: Int,
    val isActive: Boolean,
    val description: String
)

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val categoryId: String,
    val buttonText: String,
    val bgGradientHexStart: Long,
    val bgGradientHexEnd: Long,
    val isActive: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean,
    val type: String
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val isAdmin: Boolean,
    val memberSince: String
)
