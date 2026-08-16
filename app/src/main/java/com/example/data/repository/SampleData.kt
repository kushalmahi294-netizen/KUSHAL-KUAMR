package com.example.data.repository

import com.example.data.models.Address
import com.example.data.models.Category
import com.example.data.models.Coupon
import com.example.data.models.DiscountType
import com.example.data.models.NotificationItem
import com.example.data.models.OrderItem
import com.example.data.models.OrderStatus
import com.example.data.models.PaymentMethod
import com.example.data.models.Product
import com.example.data.models.PromoBanner
import com.example.data.models.Review
import com.example.data.models.UserProfile

object SampleData {

    val categories = listOf(
        Category("cat_electronics", "Electronics", "Devices", listOf("Smartphones", "Audio", "Laptops", "Smartwatches", "TV & Home")),
        Category("cat_fashion", "Fashion", "Checkroom", listOf("Men's Wear", "Women's Wear", "Footwear", "Watches", "Bags")),
        Category("cat_jewellery", "Jewellery", "Diamond", listOf("Rings", "Necklaces", "Earrings", "Bracelets", "Gold & Silver")),
        Category("cat_beauty", "Beauty", "Face", listOf("Skincare", "Makeup", "Haircare", "Fragrances", "Bath & Body")),
        Category("cat_home", "Home & Kitchen", "Kitchen", listOf("Appliances", "Cookware", "Decor", "Bedding", "Furniture")),
        Category("cat_grocery", "Grocery", "LocalGroceryStore", listOf("Beverages", "Organic Staples", "Snacks", "Gourmet", "Cooking Oils")),
        Category("cat_stationery", "Stationery", "EditNote", listOf("Notebooks", "Pens & Writing", "Desk Organizers", "Art Supplies")),
        Category("cat_mobile_acc", "Mobile Accessories", "Smartphone", listOf("Cases & Covers", "Power Banks", "Chargers & Cables", "Mounts & Stands")),
        Category("cat_clothing", "Clothing", "DryCleaning", listOf("T-Shirts", "Shirts", "Pants & Trousers", "Winterwear", "Activewear")),
        Category("cat_other", "Other Products", "Category", listOf("Travel Gear", "Fitness & Wellness", "Gifts", "Smart Gadgets"))
    )

    val products = listOf(
        Product(
            id = "prod_1",
            name = "AeroSound Pro Wireless ANC Headphones",
            brand = "SonicX",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            subcategory = "Audio",
            price = 149.99,
            originalPrice = 249.99,
            rating = 4.8,
            reviewCount = 342,
            stock = 45,
            description = "Experience industry-leading active noise cancellation, studio-grade 40mm titanium drivers, 50-hour ultra-long battery life, and ultra-plush memory foam earcups for all-day comfort.",
            specifications = mapOf(
                "Bluetooth Version" to "5.3 with multipoint",
                "Battery Life" to "Up to 50 Hours",
                "Fast Charging" to "10 min charge for 5 hours",
                "Noise Cancellation" to "Active Hybrid ANC with Transparency",
                "Weight" to "250g",
                "Warranty" to "1 Year Manufacturer Warranty"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80",
                "https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800&q=80",
                "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=800&q=80"
            ),
            colors = listOf("Midnight Black", "Platinum Silver", "Navy Blue"),
            isFeatured = true,
            isBestSeller = true,
            isTrending = true,
            isDeal = true
        ),
        Product(
            id = "prod_2",
            name = "UltraVision 4K Smart OLED Display 55\"",
            brand = "Lumina",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            subcategory = "TV & Home",
            price = 799.00,
            originalPrice = 1199.00,
            rating = 4.9,
            reviewCount = 188,
            stock = 12,
            description = "Infinite contrast with self-lit OLED pixels, 120Hz refresh rate, Dolby Vision IQ, and immersive Dolby Atmos surround sound.",
            specifications = mapOf(
                "Screen Size" to "55 Inches",
                "Resolution" to "4K Ultra HD (3840 x 2160)",
                "Refresh Rate" to "120 Hz Native",
                "HDR Support" to "Dolby Vision, HDR10+, HLG",
                "Audio Output" to "60W 2.2 Channel Dolby Atmos"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=800&q=80",
                "https://images.unsplash.com/photo-1509281373149-e957c6296406?w=800&q=80"
            ),
            colors = listOf("Titanium Slate"),
            isFeatured = true,
            isBestSeller = false,
            isTrending = true
        ),
        Product(
            id = "prod_3",
            name = "Zenith Men's Tailored Slim Fit Suit Blazer",
            brand = "Avenue Bespoke",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            subcategory = "Men's Wear",
            price = 129.50,
            originalPrice = 199.00,
            rating = 4.6,
            reviewCount = 95,
            stock = 28,
            description = "Impeccably crafted from a breathable wool-blend fabric with a modern slim silhouette, notch lapel, and subtle textured weave.",
            specifications = mapOf(
                "Material" to "70% Wool, 30% Silk Blend",
                "Fit" to "Modern Slim Fit",
                "Care" to "Dry Clean Only",
                "Pockets" to "2 Flap pockets, 1 Breast pocket, 2 Internal"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=800&q=80",
                "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=800&q=80"
            ),
            sizes = listOf("38R", "40R", "42R", "44R"),
            colors = listOf("Charcoal Grey", "Royal Navy", "Classic Black"),
            isFeatured = true,
            isTrending = true
        ),
        Product(
            id = "prod_4",
            name = "Floral Breeze Tiered Chiffon Maxi Dress",
            brand = "Aura Bloom",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            subcategory = "Women's Wear",
            price = 64.99,
            originalPrice = 99.99,
            rating = 4.7,
            reviewCount = 210,
            stock = 40,
            description = "Flowy tiered silhouette featuring hand-painted floral motifs, delicate smocked bodice, and flutter sleeves. Perfect for summer brunches and evening soirées.",
            specifications = mapOf(
                "Fabric" to "100% Breathable Chiffon with soft viscose lining",
                "Length" to "Maxi (52 inches)",
                "Neckline" to "Sweetheart with tie accent"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=800&q=80",
                "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=800&q=80"
            ),
            sizes = listOf("XS", "S", "M", "L", "XL"),
            colors = listOf("Blush Floral", "Sage Meadow", "Lavender Sky"),
            isBestSeller = true,
            isNewArrival = true
        ),
        Product(
            id = "prod_5",
            name = "Solitaire 1.5ct Moissanite Halo Ring in 18K Gold",
            brand = "Kushal Fine Jewels",
            categoryId = "cat_jewellery",
            categoryName = "Jewellery",
            subcategory = "Rings",
            price = 289.00,
            originalPrice = 450.00,
            rating = 4.9,
            reviewCount = 145,
            stock = 15,
            description = "A brilliant round-cut 1.5ct lab-created Moissanite center stone encased in a pave-set halo of shimmering accents on solid 18K gold band with certificate of authenticity.",
            specifications = mapOf(
                "Metal" to "18K Solid White/Yellow Gold",
                "Main Stone" to "1.5ct DEF Color VVS1 Clarity Moissanite",
                "Certification" to "GRA Certified Gemstone",
                "Hypoallergenic" to "100% Nickel-free & Lead-free"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=800&q=80",
                "https://images.unsplash.com/photo-1603561591411-07134e71a2a9?w=800&q=80"
            ),
            sizes = listOf("US 5", "US 6", "US 7", "US 8", "US 9"),
            colors = listOf("18K Yellow Gold", "18K White Gold", "18K Rose Gold"),
            isFeatured = true,
            isBestSeller = true
        ),
        Product(
            id = "prod_6",
            name = "Radiance 20% Vitamin C + Hyaluronic Glow Serum",
            brand = "Botanica Pure",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            subcategory = "Skincare",
            price = 24.99,
            originalPrice = 42.00,
            rating = 4.8,
            reviewCount = 520,
            stock = 85,
            description = "Clinical strength 20% stable Vitamin C serum infused with Botanical Hyaluronic Acid, Ferulic Acid, and Vitamin E to visibly brighten dark spots, even tone, and boost collagen.",
            specifications = mapOf(
                "Volume" to "30ml / 1.0 fl oz",
                "Skin Type" to "All skin types including sensitive",
                "Cruelty-Free" to "Yes, Leaping Bunny Certified",
                "Key Actives" to "20% Ethyl Ascorbic Acid, 2% Hyaluronic Acid"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&q=80",
                "https://images.unsplash.com/photo-1608248597359-009d73507d30?w=800&q=80"
            ),
            isDeal = true,
            isBestSeller = true,
            isTrending = true
        ),
        Product(
            id = "prod_7",
            name = "Barista Master Precision Espresso & Cappuccino Machine",
            brand = "CremaCraft",
            categoryId = "cat_home",
            categoryName = "Home & Kitchen",
            subcategory = "Appliances",
            price = 219.00,
            originalPrice = 320.00,
            rating = 4.7,
            reviewCount = 160,
            stock = 18,
            description = "20-bar Italian high pressure pump with thermoblock rapid heating, commercial steam wand for microfoam latte art, and dual-shot stainless steel filter basket.",
            specifications = mapOf(
                "Pressure" to "20 Bar Italian Pump",
                "Water Tank" to "1.8L Removable Reservoir",
                "Power" to "1350 Watts",
                "Accessories" to "Portafilter, Tamper, 2 Stainless Filters, Milk Pitcher"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=800&q=80",
                "https://images.unsplash.com/photo-1570968915860-54d5c301fa9f?w=800&q=80"
            ),
            colors = listOf("Brushed Stainless Steel", "Matte Black", "Vintage Cream"),
            isFeatured = true,
            isTrending = true
        ),
        Product(
            id = "prod_8",
            name = "Single-Origin Ethiopian Yirgacheffe Whole Bean Coffee 1kg",
            brand = "Kushal Reserve",
            categoryId = "cat_grocery",
            categoryName = "Grocery",
            subcategory = "Beverages",
            price = 28.50,
            originalPrice = 38.00,
            rating = 4.9,
            reviewCount = 275,
            stock = 60,
            description = "Artisan small-batch roasted 100% Arabica beans boasting delicate floral jasmine aromas, bright bergamot acidity, and a smooth honeyed peach finish.",
            specifications = mapOf(
                "Roast Profile" to "Medium-Light Roast",
                "Process" to "Washed Heirloom Varietals",
                "Altitude" to "1900 - 2200m Above Sea Level",
                "Net Weight" to "1000g (2.2 lbs) Nitrogen-Flushed Valve Bag"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=800&q=80",
                "https://images.unsplash.com/photo-1587734195503-904fca47e0e9?w=800&q=80"
            ),
            isDeal = true,
            isNewArrival = true
        ),
        Product(
            id = "prod_9",
            name = "Heritage Full-Grain Leather Refillable Journal",
            brand = "Artisan Guild",
            categoryId = "cat_stationery",
            categoryName = "Stationery",
            subcategory = "Notebooks",
            price = 34.00,
            originalPrice = 50.00,
            rating = 4.8,
            reviewCount = 112,
            stock = 35,
            description = "Handmade from rustic saddle brown crazy-horse leather that develops rich patina over time. Includes 240 pages of 120gsm fountain-pen friendly bleedproof cotton paper.",
            specifications = mapOf(
                "Cover" to "100% Full Grain Buffalo Leather",
                "Paper" to "240 Pages / 120 GSM Acid-Free Cotton",
                "Size" to "A5 (8.5 x 5.8 inches)",
                "Binding" to "Hand-stitched Coptic spine with leather strap"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800&q=80",
                "https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=800&q=80"
            ),
            colors = listOf("Rustic Brown", "Midnight Espresso", "Vintage Tan"),
            isTrending = true
        ),
        Product(
            id = "prod_10",
            name = "MagPower 20,000mAh Ultra-Fast 65W Power Bank",
            brand = "VoltCore",
            categoryId = "cat_mobile_acc",
            categoryName = "Mobile Accessories",
            subcategory = "Power Banks",
            price = 49.99,
            originalPrice = 79.99,
            rating = 4.7,
            reviewCount = 410,
            stock = 50,
            description = "High-density aviation-grade battery with 65W Power Delivery to fast charge laptops, tablets, and phones simultaneously. Smart digital LED power meter display.",
            specifications = mapOf(
                "Capacity" to "20,000mAh / 74Wh",
                "Max Output" to "65W USB-C PD 3.0 + 22.5W USB-A QC 4.0",
                "Ports" to "2x USB-C, 1x USB-A",
                "Display" to "Smart Real-time Voltage/Current OLED"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=800&q=80",
                "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=800&q=80"
            ),
            colors = listOf("Space Grey", "Stealth Black"),
            isFeatured = true,
            isDeal = true,
            isBestSeller = true
        ),
        Product(
            id = "prod_11",
            name = "Supima Signature Heavyweight Organic Cotton Tee",
            brand = "Kushal Basics",
            categoryId = "cat_clothing",
            categoryName = "Clothing",
            subcategory = "T-Shirts",
            price = 22.00,
            originalPrice = 35.00,
            rating = 4.6,
            reviewCount = 330,
            stock = 120,
            description = "Premium 240 GSM long-staple organic Supima cotton. Pre-shrunk, drop-shoulder relaxed cut, reinforced ribbed collar that never sags.",
            specifications = mapOf(
                "Material" to "100% US Grown Supima Organic Cotton",
                "Fabric Weight" to "240 GSM Heavyweight Jersey",
                "Fit" to "Modern Relaxed Fit"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&q=80",
                "https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=800&q=80"
            ),
            sizes = listOf("S", "M", "L", "XL", "XXL"),
            colors = listOf("Off White", "Vintage Black", "Forest Green", "Dusty Rose"),
            isBestSeller = true,
            isNewArrival = true
        ),
        Product(
            id = "prod_12",
            name = "ThermoShield Smart UV-Self Cleaning Water Bottle 750ml",
            brand = "HydroPure",
            categoryId = "cat_other",
            categoryName = "Other Products",
            subcategory = "Smart Gadgets",
            price = 45.00,
            originalPrice = 65.00,
            rating = 4.8,
            reviewCount = 88,
            stock = 30,
            description = "Built-in UV-C sterilization cap neutralizes 99.9% of bacteria and viruses in 60 seconds. Double-wall vacuum insulation keeps liquids cold for 24h or hot for 12h.",
            specifications = mapOf(
                "Capacity" to "750 ml (25 oz)",
                "Material" to "Food Grade 18/8 Stainless Steel",
                "Battery" to "Magnetic USB rechargeable (1 month per charge)",
                "Sterilization" to "UV-C LED automatically triggers every 2 hours"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=800&q=80",
                "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=800&q=80"
            ),
            colors = listOf("Obsidian Black", "Arctic White", "Sage Green"),
            isNewArrival = true
        )
    )

    val banners = listOf(
        PromoBanner(
            id = "banner_1",
            title = "Mega Super Saver Sale!",
            subtitle = "Up to 50% Off Top Electronics & Mobile Gear",
            categoryId = "cat_electronics",
            buttonText = "Explore Deals",
            bgGradientHexStart = 0xFF0F172A,
            bgGradientHexEnd = 0xFF1E40AF
        ),
        PromoBanner(
            id = "banner_2",
            title = "New Festive Fashion & Jewellery",
            subtitle = "Exclusive Collections for the Modern Trendsetter",
            categoryId = "cat_fashion",
            buttonText = "Shop Fashion",
            bgGradientHexStart = 0xFF831843,
            bgGradientHexEnd = 0xFFDB2777
        ),
        PromoBanner(
            id = "banner_3",
            title = "Gourmet Coffee & Kitchen Pro",
            subtitle = "Upgrade Your Morning Routine with Free Delivery",
            categoryId = "cat_home",
            buttonText = "Discover More",
            bgGradientHexStart = 0xFF78350F,
            bgGradientHexEnd = 0xFFD97706
        )
    )

    val coupons = listOf(
        Coupon(
            code = "WELCOME50",
            discountType = DiscountType.PERCENTAGE,
            discountAmount = 50.0,
            minOrderValue = 100.0,
            maxDiscount = 50.0,
            expiryDate = "2026-12-31",
            description = "50% off on your first purchase (Max ₹50)"
        ),
        Coupon(
            code = "KUSHAL20",
            discountType = DiscountType.PERCENTAGE,
            discountAmount = 20.0,
            minOrderValue = 80.0,
            maxDiscount = 150.0,
            expiryDate = "2026-12-31",
            description = "20% off on electronics and fashion orders above ₹80"
        ),
        Coupon(
            code = "SAVE100",
            discountType = DiscountType.FIXED,
            discountAmount = 100.0,
            minOrderValue = 300.0,
            maxDiscount = 100.0,
            expiryDate = "2026-12-31",
            description = "Flat ₹100 off on premium orders above ₹300"
        ),
        Coupon(
            code = "FREESHIP",
            discountType = DiscountType.FIXED,
            discountAmount = 15.0,
            minOrderValue = 40.0,
            maxDiscount = 15.0,
            expiryDate = "2026-12-31",
            description = "Free standard shipping on all orders over ₹40"
        )
    )

    val addresses = listOf(
        Address(
            id = "addr_1",
            fullName = "Kushal Khare",
            phone = "+91 98765 43210",
            streetAddress = "Flat 402, Royal Palms Residency, 10th Main Road",
            city = "Bengaluru",
            state = "Karnataka",
            pinCode = "560034",
            addressType = "Home",
            isDefault = true
        ),
        Address(
            id = "addr_2",
            fullName = "Kushal Khare",
            phone = "+91 98765 43210",
            streetAddress = "Tech Park Tower B, 6th Floor, Whitefield",
            city = "Bengaluru",
            state = "Karnataka",
            pinCode = "560066",
            addressType = "Work",
            isDefault = false
        )
    )

    val reviews = listOf(
        Review(
            id = "rev_1",
            productId = "prod_1",
            userName = "Arjun Mehta",
            rating = 5,
            title = "Incredible Noise Cancellation & Audio!",
            comment = "The sound stage is super wide and ANC easily drowns out plane noise during travel. Very comfortable for long work hours.",
            date = "Aug 12, 2026",
            isVerifiedPurchase = true,
            isApproved = true
        ),
        Review(
            id = "rev_2",
            productId = "prod_1",
            userName = "Priya Sharma",
            rating = 5,
            title = "Worth every rupee",
            comment = "Build quality feels very premium and the battery lasts for days. Fast pairing with my Android phone!",
            date = "Aug 10, 2026",
            isVerifiedPurchase = true,
            isApproved = true
        ),
        Review(
            id = "rev_3",
            productId = "prod_6",
            userName = "Sneha Patel",
            rating = 5,
            title = "Visible glow within 1 week",
            comment = "Lightweight, non-sticky and doesn't oxidize fast. My skin looks noticeably brighter.",
            date = "Aug 14, 2026",
            isVerifiedPurchase = true,
            isApproved = true
        )
    )

    val initialOrders = listOf(
        com.example.data.models.Order(
            id = "KS-894210",
            items = listOf(
                OrderItem(
                    productId = "prod_1",
                    productName = "AeroSound Pro Wireless ANC Headphones",
                    productBrand = "SonicX",
                    productImage = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80",
                    price = 149.99,
                    quantity = 1,
                    selectedColor = "Midnight Black"
                )
            ),
            subtotal = 149.99,
            discount = 20.0,
            couponCode = "KUSHAL20",
            deliveryCharge = 0.0,
            totalAmount = 129.99,
            paymentMethod = PaymentMethod.UPI,
            paymentStatus = "Paid",
            orderStatus = OrderStatus.SHIPPED,
            shippingAddress = addresses.first(),
            createdAt = System.currentTimeMillis() - 86400000 * 2,
            estimatedDeliveryDate = "Tomorrow, by 8 PM",
            trackingNumber = "TRK-982341A"
        )
    )

    val notifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Order Shipped! 🚚",
            message = "Your order #KS-894210 has been shipped via Express Logistics. Expected delivery tomorrow.",
            timeAgo = "2 hours ago",
            isRead = false,
            type = "Order"
        ),
        NotificationItem(
            id = "notif_2",
            title = "Flash Sale Weekend 🎉",
            message = "Use code KUSHAL20 to get extra 20% off on all electronics & apparel.",
            timeAgo = "1 day ago",
            isRead = true,
            type = "Promo"
        ),
        NotificationItem(
            id = "notif_3",
            title = "Welcome to Kushal Store ✨",
            message = "Thank you for joining Kushal Store! Enjoy seamless shopping and fast delivery.",
            timeAgo = "3 days ago",
            isRead = true,
            type = "System"
        )
    )

    val defaultUser = UserProfile(
        id = "user_1",
        name = "Kushal Khare",
        email = "kushalkhare30@gmail.com",
        phone = "+91 98765 43210",
        isAdmin = false,
        memberSince = "August 2026"
    )
}
