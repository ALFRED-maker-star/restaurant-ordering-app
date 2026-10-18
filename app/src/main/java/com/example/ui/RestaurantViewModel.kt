package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AddOn
import com.example.data.model.CartItem
import com.example.data.model.MenuCategory
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.PaymentMethod
import com.example.data.model.SpiceLevel
import com.example.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class Screen {
    HOME,
    CATEGORY_MENU,
    ITEM_DETAIL,
    CART,
    CHECKOUT,
    ORDER_TRACKING,
    ORDER_HISTORY,
    // Admin screens
    ADMIN_LOGIN,
    ADMIN_DASHBOARD,
    ADMIN_MENU_MANAGEMENT,
    ADMIN_INCOMING_ORDERS,
    ADMIN_ORDER_DETAIL
}

class RestaurantViewModel(
    private val repository: RestaurantRepository = RestaurantRepository.instance
) : ViewModel() {

    // Navigation State
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Backstack history for back-navigation
    private val screenBackStack = mutableListOf<Screen>(Screen.HOME)

    // Screen-specific selection states
    private val _selectedCategory = MutableStateFlow(MenuCategory.MAINS)
    val selectedCategory: StateFlow<MenuCategory> = _selectedCategory.asStateFlow()

    private val _selectedMenuItem = MutableStateFlow<MenuItem?>(null)
    val selectedMenuItem: StateFlow<MenuItem?> = _selectedMenuItem.asStateFlow()

    private val _selectedAdminOrder = MutableStateFlow<Order?>(null)
    val selectedAdminOrder: StateFlow<Order?> = _selectedAdminOrder.asStateFlow()

    // Search and filter states
    val searchQuery = MutableStateFlow("")
    val filterDietary = MutableStateFlow("ALL") // "ALL", "SPECIALS", "SPICY", "AVAILABLE"

    // Menu Items
    val menuItems = repository.menuItems
    val cartItems = repository.cartItems
    val orders = repository.orders
    val activeTrackingOrder = repository.activeTrackingOrder
    val isAdminLoggedIn = repository.isAdminLoggedIn

    // Promo code
    val promoCode = MutableStateFlow("")
    val promoDiscountPercent = MutableStateFlow(0.0)
    val promoMessage = MutableStateFlow<String?>(null)

    // Filtered menu items for Home and Category Menu
    val filteredMenuItems: StateFlow<List<MenuItem>> = combine(
        menuItems,
        searchQuery,
        _selectedCategory,
        filterDietary
    ) { items, query, cat, filter ->
        items.filter { item ->
            val matchesCategory = item.category == cat
            val matchesSearch = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                "SPECIALS" -> item.isChefSpecial
                "SPICY" -> item.defaultSpiceLevel != SpiceLevel.NONE
                "AVAILABLE" -> item.isAvailable
                else -> true
            }
            // If searching actively, match across all categories; otherwise match category
            if (query.isNotBlank()) matchesSearch && matchesFilter else matchesCategory && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All matching search items (across whole menu)
    val allSearchItems: StateFlow<List<MenuItem>> = combine(
        menuItems,
        searchQuery
    ) { items, query ->
        if (query.isBlank()) {
            items.filter { it.isChefSpecial }
        } else {
            items.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.category.displayName.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart calculations
    val cartItemCount: StateFlow<Int> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSubtotal: StateFlow<Double> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Admin Dashboard Stats
    val ordersTodayCount: StateFlow<Int> = orders.combine(orders) { list, _ ->
        list.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val revenueToday: StateFlow<Double> = orders.combine(orders) { list, _ ->
        list.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.total }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val pendingOrdersCount: StateFlow<Int> = orders.combine(orders) { list, _ ->
        list.count { it.status == OrderStatus.RECEIVED || it.status == OrderStatus.PREPARING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.size > 1) {
            screenBackStack.removeAt(screenBackStack.size - 1)
            _currentScreen.value = screenBackStack.last()
            return true
        } else {
            if (_currentScreen.value != Screen.HOME) {
                _currentScreen.value = Screen.HOME
                return true
            }
        }
        return false
    }

    fun openCategoryMenu(category: MenuCategory) {
        _selectedCategory.value = category
        navigateTo(Screen.CATEGORY_MENU)
    }

    fun openItemDetail(item: MenuItem) {
        _selectedMenuItem.value = item
        navigateTo(Screen.ITEM_DETAIL)
    }

    fun openAdminOrderDetail(order: Order) {
        _selectedAdminOrder.value = order
        navigateTo(Screen.ADMIN_ORDER_DETAIL)
    }

    fun openOrderTracking(order: Order) {
        repository.setActiveTrackingOrder(order)
        navigateTo(Screen.ORDER_TRACKING)
    }

    // Cart operations
    fun quickAddToCart(menuItem: MenuItem) {
        repository.addToCart(menuItem = menuItem, quantity = 1)
    }

    fun addToCartWithCustomization(
        menuItem: MenuItem,
        quantity: Int,
        spiceLevel: SpiceLevel,
        addOns: List<AddOn>,
        specialNotes: String
    ) {
        repository.addToCart(
            menuItem = menuItem,
            quantity = quantity,
            spiceLevel = spiceLevel,
            addOns = addOns,
            specialNotes = specialNotes
        )
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        repository.updateCartQuantity(cartItemId, newQuantity)
    }

    fun removeFromCart(cartItemId: String) {
        repository.removeFromCart(cartItemId)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun applyPromoCode(code: String) {
        val trimmed = code.trim().uppercase()
        when (trimmed) {
            "TASTY20" -> {
                promoDiscountPercent.value = 0.20
                promoMessage.value = "20% Discount applied!"
            }
            "TASTY50" -> {
                promoDiscountPercent.value = 0.50
                promoMessage.value = "50% Welcome treat applied!"
            }
            "FREESHIP" -> {
                promoDiscountPercent.value = 0.10
                promoMessage.value = "Free delivery credit applied!"
            }
            else -> {
                promoDiscountPercent.value = 0.0
                promoMessage.value = "Invalid or expired promo code"
            }
        }
    }

    // Checkout & Order Placement
    fun submitOrder(
        orderType: OrderType,
        tableNumber: String,
        deliveryAddress: String,
        customerName: String,
        customerPhone: String,
        paymentMethod: PaymentMethod,
        specialInstructions: String
    ): Order {
        val order = repository.placeOrder(
            orderType = orderType,
            tableNumber = tableNumber,
            deliveryAddress = deliveryAddress,
            customerName = customerName,
            customerPhone = customerPhone,
            paymentMethod = paymentMethod,
            specialInstructions = specialInstructions,
            discountPercent = promoDiscountPercent.value
        )
        // Reset promo
        promoCode.value = ""
        promoDiscountPercent.value = 0.0
        promoMessage.value = null
        navigateTo(Screen.ORDER_TRACKING)
        return order
    }

    fun reorder(order: Order) {
        repository.reorder(order)
        navigateTo(Screen.CART)
    }

    // Admin operations
    fun loginAdmin(pin: String): Boolean {
        val success = repository.loginAdmin(pin)
        if (success) {
            navigateTo(Screen.ADMIN_DASHBOARD)
        }
        return success
    }

    fun logoutAdmin() {
        repository.logoutAdmin()
        navigateTo(Screen.HOME)
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        repository.updateOrderStatus(orderId, newStatus)
    }

    fun acceptOrder(orderId: String) {
        repository.acceptOrder(orderId)
    }

    fun rejectOrder(orderId: String) {
        repository.rejectOrder(orderId)
    }

    fun toggleAvailability(itemId: String) {
        repository.toggleMenuItemAvailability(itemId)
    }

    fun addMenuItem(
        name: String,
        category: MenuCategory,
        description: String,
        price: Double,
        spiceLevel: SpiceLevel,
        calories: Int,
        prepTimeMin: Int,
        isChefSpecial: Boolean
    ) {
        repository.addMenuItem(
            name = name,
            category = category,
            description = description,
            price = price,
            spiceLevel = spiceLevel,
            calories = calories,
            prepTimeMin = prepTimeMin,
            isChefSpecial = isChefSpecial
        )
    }

    fun updateMenuItem(item: MenuItem) {
        repository.updateMenuItem(item)
    }
}
