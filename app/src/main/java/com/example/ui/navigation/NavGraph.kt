package com.example.ui.navigation

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.screens.account.AccountScreen
import com.example.ui.screens.account.AddressesScreen
import com.example.ui.screens.account.NotificationsScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.SignupScreen
import com.example.ui.screens.cart.CartScreen
import com.example.ui.screens.checkout.CheckoutScreen
import com.example.ui.screens.checkout.OrderConfirmationScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.orders.OrderDetailScreen
import com.example.ui.screens.orders.OrdersListScreen
import com.example.ui.screens.products.ProductDetailScreen
import com.example.ui.screens.products.ProductListingScreen
import com.example.ui.screens.search.SearchScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.wishlist.WishlistScreen
import com.example.viewmodel.KushalViewModel
import kotlinx.coroutines.flow.collectLatest

object NavRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val SEARCH = "search"
    const val PRODUCT_LISTING = "product_listing"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val ORDER_CONFIRMATION = "order_confirmation/{orderId}"
    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{orderId}"
    const val WISHLIST = "wishlist"
    const val ACCOUNT = "account"
    const val ADDRESSES = "addresses"
    const val NOTIFICATIONS = "notifications"
    const val ADMIN = "admin"

    fun productDetail(productId: String) = "product_detail/$productId"
    fun orderConfirmation(orderId: String) = "order_confirmation/$orderId"
    fun orderDetail(orderId: String) = "order_detail/$orderId"
}

@Composable
fun KushalNavGraph(
    navController: NavHostController,
    viewModel: KushalViewModel
) {
    val context = LocalContext.current

    // Listen to ViewModel toast events
    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH,
        enterTransition = { fadeIn(animationSpec = tween(250)) },
        exitTransition = { fadeOut(animationSpec = tween(250)) }
    ) {
        // Splash Screen
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignup = { navController.navigate(NavRoutes.SIGNUP) },
                onContinueAsGuest = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Signup Screen
        composable(NavRoutes.SIGNUP) {
            SignupScreen(
                viewModel = viewModel,
                onSignupSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SIGNUP) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // Home Screen
        composable(NavRoutes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSearch = { navController.navigate(NavRoutes.SEARCH) },
                onNavigateToCart = { navController.navigate(NavRoutes.CART) },
                onNavigateToWishlist = { navController.navigate(NavRoutes.WISHLIST) },
                onNavigateToNotifications = { navController.navigate(NavRoutes.NOTIFICATIONS) },
                onNavigateToProfile = { navController.navigate(NavRoutes.ACCOUNT) },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(NavRoutes.productDetail(productId))
                },
                onNavigateToCategory = { categoryId ->
                    navController.navigate(NavRoutes.PRODUCT_LISTING)
                },
                onNavigateToAllProducts = {
                    viewModel.resetFilters()
                    navController.navigate(NavRoutes.PRODUCT_LISTING)
                }
            )
        }

        // Search Screen
        composable(NavRoutes.SEARCH) {
            SearchScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(NavRoutes.productDetail(productId))
                }
            )
        }

        // Product Listing Screen
        composable(NavRoutes.PRODUCT_LISTING) {
            ProductListingScreen(
                viewModel = viewModel,
                title = "Explore Catalog",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSearch = { navController.navigate(NavRoutes.SEARCH) },
                onNavigateToCart = { navController.navigate(NavRoutes.CART) },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(NavRoutes.productDetail(productId))
                }
            )
        }

        // Product Detail Screen
        composable(
            route = NavRoutes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(NavRoutes.CART) },
                onNavigateToCheckout = { navController.navigate(NavRoutes.CHECKOUT) },
                onNavigateToProductDetail = { nextProductId ->
                    navController.navigate(NavRoutes.productDetail(nextProductId))
                }
            )
        }

        // Cart Screen
        composable(NavRoutes.CART) {
            CartScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(NavRoutes.CHECKOUT) },
                onNavigateToExplore = {
                    viewModel.resetFilters()
                    navController.navigate(NavRoutes.PRODUCT_LISTING)
                }
            )
        }

        // Checkout Screen
        composable(NavRoutes.CHECKOUT) {
            CheckoutScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onOrderPlaced = { order ->
                    navController.navigate(NavRoutes.orderConfirmation(order.id)) {
                        popUpTo(NavRoutes.CHECKOUT) { inclusive = true }
                    }
                }
            )
        }

        // Order Confirmation Screen
        composable(
            route = NavRoutes.ORDER_CONFIRMATION,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val allOrders by viewModel.allOrders.collectAsState()
            val order = allOrders.find { it.id == orderId } ?: com.example.data.models.Order()

            OrderConfirmationScreen(
                order = order,
                onTrackOrder = {
                    navController.navigate(NavRoutes.orderDetail(order.id)) {
                        popUpTo(NavRoutes.HOME)
                    }
                },
                onContinueShopping = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // Orders List Screen
        composable(NavRoutes.ORDERS) {
            OrdersListScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(NavRoutes.orderDetail(orderId))
                }
            )
        }

        // Order Detail & Timeline Tracking Screen
        composable(
            route = NavRoutes.ORDER_DETAIL,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Wishlist Screen
        composable(NavRoutes.WISHLIST) {
            WishlistScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(NavRoutes.productDetail(productId))
                },
                onNavigateToExplore = {
                    viewModel.resetFilters()
                    navController.navigate(NavRoutes.PRODUCT_LISTING)
                }
            )
        }

        // Account Screen
        composable(NavRoutes.ACCOUNT) {
            AccountScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOrders = { navController.navigate(NavRoutes.ORDERS) },
                onNavigateToWishlist = { navController.navigate(NavRoutes.WISHLIST) },
                onNavigateToAddresses = { navController.navigate(NavRoutes.ADDRESSES) },
                onNavigateToNotifications = { navController.navigate(NavRoutes.NOTIFICATIONS) },
                onNavigateToAdmin = { navController.navigate(NavRoutes.ADMIN) },
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // Addresses Management Screen
        composable(NavRoutes.ADDRESSES) {
            AddressesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Notifications Screen
        composable(NavRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Admin Dashboard Screen
        composable(NavRoutes.ADMIN) {
            AdminDashboardScreen(
                viewModel = viewModel,
                onNavigateBackToStore = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.ADMIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
