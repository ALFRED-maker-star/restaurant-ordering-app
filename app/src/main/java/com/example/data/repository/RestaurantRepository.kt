package com.example.data.repository

import com.example.R
import com.example.data.model.AddOn
import com.example.data.model.CartItem
import com.example.data.model.MenuCategory
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.OrderType
import com.example.data.model.PaymentMethod
import com.example.data.model.SpiceLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class RestaurantRepository {

    private val defaultAddOns = listOf(
        AddOn("ao_cheese", "Extra Melted Cheddar", 1.50),
        AddOn("ao_bacon", "Crispy Applewood Bacon", 2.00),
        AddOn("ao_avocado", "Fresh Sliced Avocado", 1.75),
        AddOn("ao_garlic_dip", "Truffle Garlic Aioli Dip", 0.99),
        AddOn("ao_jalapeno", "Pickled Jalapeños", 0.75)
    )

    private val initialMenuItems = listOf(
        MenuItem(
            id = "m1",
            name = "TastyGo Classic Double Smash",
            category = MenuCategory.MAINS,
            description = "Twin smashed prime beef patties, double melted cheddar, caramelized onions, crisp butter lettuce & secret house sauce on toasted brioche.",
            price = 14.99,
            imageRes = R.drawable.img_dish_burger,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.MILD,
            prepTimeMin = 14,
            calories = 780,
            isChefSpecial = true,
            rating = 4.9,
            reviewCount = 318,
            availableAddOns = defaultAddOns
        ),
        MenuItem(
            id = "m2",
            name = "Artisan Burrata & Pepperoni Pizza",
            category = MenuCategory.MAINS,
            description = "Hand-stretched sourdough crust, San Marzano tomato sauce, spicy cup-and-char pepperoni, topped with fresh creamy burrata & aromatic basil.",
            price = 16.50,
            imageRes = R.drawable.img_dish_pizza,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.MEDIUM,
            prepTimeMin = 18,
            calories = 920,
            isChefSpecial = true,
            rating = 4.9,
            reviewCount = 284,
            availableAddOns = defaultAddOns
        ),
        MenuItem(
            id = "m3",
            name = "Crispy Firecracker Buffalo Wings",
            category = MenuCategory.STARTERS,
            description = "Crisp-fried jumbo wings tossed in signature spicy honey-sriracha buffalo glaze. Served with cool ranch & celery sticks.",
            price = 11.99,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.HOT,
            prepTimeMin = 12,
            calories = 540,
            isChefSpecial = true,
            rating = 4.8,
            reviewCount = 192,
            availableAddOns = listOf(
                AddOn("ao_extra_ranch", "Extra Blue Cheese Ranch", 0.99),
                AddOn("ao_ghost_sauce", "Ghost Pepper Drizzle", 1.25)
            )
        ),
        MenuItem(
            id = "m4",
            name = "Truffle Parmesan Hand-Cut Fries",
            category = MenuCategory.STARTERS,
            description = "Crispy golden russet fries tossed with fragrant black truffle oil, aged parmesan reggiano, and fresh chopped parsley.",
            price = 7.99,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.NONE,
            prepTimeMin = 8,
            calories = 420,
            isChefSpecial = false,
            rating = 4.7,
            reviewCount = 165,
            availableAddOns = defaultAddOns
        ),
        MenuItem(
            id = "m5",
            name = "Smoky BBQ Bacon Cheddar Burger",
            category = MenuCategory.MAINS,
            description = "Wood-smoked bacon, crispy onion rings, sharp Wisconsin cheddar, and sweet hickory BBQ sauce on a toasted potato bun.",
            price = 15.99,
            imageRes = R.drawable.img_dish_burger,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.MILD,
            prepTimeMin = 15,
            calories = 840,
            isChefSpecial = false,
            rating = 4.8,
            reviewCount = 210,
            availableAddOns = defaultAddOns
        ),
        MenuItem(
            id = "m6",
            name = "Sparkling Yuzu Citrus Lemonade",
            category = MenuCategory.DRINKS,
            description = "Infused with Japanese yuzu juice, organic wildflower honey, fresh crushed mint, and sparkling mountain spring water.",
            price = 4.95,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.NONE,
            prepTimeMin = 4,
            calories = 110,
            isChefSpecial = false,
            rating = 4.7,
            reviewCount = 98,
            availableAddOns = listOf(
                AddOn("ao_boba", "Golden Popping Boba", 1.00),
                AddOn("ao_chia", "Chia Seeds", 0.75)
            )
        ),
        MenuItem(
            id = "m7",
            name = "Craft Amber Iced Cold Brew",
            category = MenuCategory.DRINKS,
            description = "Slow-steeped single-origin Ethiopian cold brew coffee served over clear artisan ice with vanilla bean sweet cold foam.",
            price = 5.75,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.NONE,
            prepTimeMin = 3,
            calories = 140,
            isChefSpecial = false,
            rating = 4.8,
            reviewCount = 135,
            availableAddOns = listOf(
                AddOn("ao_oat_milk", "Organic Oat Milk", 0.75),
                AddOn("ao_extra_shot", "Extra Espresso Shot", 1.25)
            )
        ),
        MenuItem(
            id = "m8",
            name = "Warm Molten Dark Chocolate Lava",
            category = MenuCategory.DESSERTS,
            description = "Rich Belgian dark chocolate cake with a velvety molten center. Served warm with a scoop of Madagascar vanilla gelato.",
            price = 8.99,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.NONE,
            prepTimeMin = 10,
            calories = 520,
            isChefSpecial = true,
            rating = 4.9,
            reviewCount = 245,
            availableAddOns = listOf(
                AddOn("ao_extra_gelato", "Extra Gelato Scoop", 2.00),
                AddOn("ao_berries", "Fresh Mixed Berries", 1.75)
            )
        ),
        MenuItem(
            id = "m9",
            name = "Cinnamon Churro Sundae Boat",
            category = MenuCategory.DESSERTS,
            description = "Fresh fried Mexican churros dusted in Saigon cinnamon sugar, dulce de leche caramel swirl, and toasted candied pecans.",
            price = 7.50,
            imageRes = R.drawable.img_restaurant_hero,
            isAvailable = true,
            defaultSpiceLevel = SpiceLevel.NONE,
            prepTimeMin = 8,
            calories = 490,
            isChefSpecial = false,
            rating = 4.6,
            reviewCount = 112,
            availableAddOns = defaultAddOns
        )
    )

    private val _menuItems = MutableStateFlow(initialMenuItems)
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val initialOrders = listOf(
        Order(
            id = "TG-8041",
            items = listOf(
                CartItem(
                    id = "c1",
                    menuItem = initialMenuItems[0],
                    quantity = 2,
                    spiceLevel = SpiceLevel.MEDIUM,
                    selectedAddOns = listOf(defaultAddOns[0], defaultAddOns[1]),
                    specialNotes = "Extra crispy fries please"
                ),
                CartItem(
                    id = "c2",
                    menuItem = initialMenuItems[5],
                    quantity = 2,
                    spiceLevel = SpiceLevel.NONE,
                    selectedAddOns = emptyList()
                )
            ),
            orderType = OrderType.DELIVERY,
            deliveryAddress = "742 Evergreen Terrace, Apt 4B",
            customerName = "Sarah Jenkins",
            customerPhone = "+1 (555) 782-9912",
            paymentMethod = PaymentMethod.CREDIT_CARD,
            status = OrderStatus.PREPARING,
            timestamp = System.currentTimeMillis() - 12 * 60 * 1000,
            subtotal = 39.88,
            deliveryFee = 2.99,
            discount = 0.0,
            total = 42.87,
            estimatedMinutes = 18
        ),
        Order(
            id = "TG-8040",
            items = listOf(
                CartItem(
                    id = "c3",
                    menuItem = initialMenuItems[1],
                    quantity = 1,
                    spiceLevel = SpiceLevel.HOT,
                    selectedAddOns = listOf(defaultAddOns[0])
                )
            ),
            orderType = OrderType.DINE_IN,
            tableNumber = "Table 14",
            customerName = "Marcus Sterling",
            customerPhone = "+1 (555) 431-8900",
            paymentMethod = PaymentMethod.GOOGLE_PAY,
            status = OrderStatus.READY,
            timestamp = System.currentTimeMillis() - 25 * 60 * 1000,
            subtotal = 18.00,
            deliveryFee = 0.0,
            discount = 0.0,
            total = 18.00,
            estimatedMinutes = 5
        ),
        Order(
            id = "TG-8039",
            items = listOf(
                CartItem(
                    id = "c4",
                    menuItem = initialMenuItems[2],
                    quantity = 1,
                    spiceLevel = SpiceLevel.HOT
                ),
                CartItem(
                    id = "c5",
                    menuItem = initialMenuItems[7],
                    quantity = 1,
                    spiceLevel = SpiceLevel.NONE
                )
            ),
            orderType = OrderType.DINE_IN,
            tableNumber = "Table 08",
            customerName = "Elena Rostova",
            customerPhone = "+1 (555) 902-1144",
            paymentMethod = PaymentMethod.CASH,
            status = OrderStatus.SERVED_DELIVERED,
            timestamp = System.currentTimeMillis() - 65 * 60 * 1000,
            subtotal = 20.98,
            deliveryFee = 0.0,
            discount = 0.0,
            total = 20.98,
            estimatedMinutes = 0
        )
    )

    private val _orders = MutableStateFlow(initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _activeTrackingOrder = MutableStateFlow<Order?>(initialOrders[0])
    val activeTrackingOrder: StateFlow<Order?> = _activeTrackingOrder.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Cart Actions
    fun addToCart(
        menuItem: MenuItem,
        quantity: Int = 1,
        spiceLevel: SpiceLevel = menuItem.defaultSpiceLevel,
        addOns: List<AddOn> = emptyList(),
        specialNotes: String = ""
    ) {
        val newItem = CartItem(
            id = UUID.randomUUID().toString(),
            menuItem = menuItem,
            quantity = quantity,
            spiceLevel = spiceLevel,
            selectedAddOns = addOns,
            specialNotes = specialNotes
        )
        _cartItems.update { current ->
            // Check if identical item configuration exists
            val existingIndex = current.indexOfFirst {
                it.menuItem.id == menuItem.id &&
                        it.spiceLevel == spiceLevel &&
                        it.selectedAddOns.map { a -> a.id }.sorted() == addOns.map { a -> a.id }.sorted() &&
                        it.specialNotes.trim() == specialNotes.trim()
            }
            if (existingIndex != -1) {
                current.toMutableList().apply {
                    val existing = this[existingIndex]
                    this[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
                }
            } else {
                current + newItem
            }
        }
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        _cartItems.update { current ->
            if (newQuantity <= 0) {
                current.filterNot { it.id == cartItemId }
            } else {
                current.map {
                    if (it.id == cartItemId) it.copy(quantity = newQuantity) else it
                }
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        _cartItems.update { it.filterNot { item -> item.id == cartItemId } }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Place Order
    fun placeOrder(
        orderType: OrderType,
        tableNumber: String,
        deliveryAddress: String,
        customerName: String,
        customerPhone: String,
        paymentMethod: PaymentMethod,
        specialInstructions: String,
        discountPercent: Double = 0.0
    ): Order {
        val currentCart = _cartItems.value
        val subtotal = currentCart.sumOf { it.totalPrice }
        val deliveryFee = if (orderType == OrderType.DELIVERY) {
            if (subtotal >= 30.0) 0.0 else 2.99
        } else 0.0
        val discountAmount = subtotal * discountPercent
        val total = (subtotal - discountAmount + deliveryFee).coerceAtLeast(0.0)

        val newOrder = Order(
            id = "TG-${(1000..9999).random()}",
            items = currentCart,
            orderType = orderType,
            tableNumber = if (orderType == OrderType.DINE_IN) tableNumber else "",
            deliveryAddress = if (orderType == OrderType.DELIVERY) deliveryAddress else "",
            customerName = customerName.ifBlank { "Alex Morgan" },
            customerPhone = customerPhone.ifBlank { "+1 (555) 234-5678" },
            paymentMethod = paymentMethod,
            specialInstructions = specialInstructions,
            status = OrderStatus.RECEIVED,
            timestamp = System.currentTimeMillis(),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discountAmount,
            total = total,
            estimatedMinutes = if (orderType == OrderType.DELIVERY) 30 else 18
        )

        _orders.update { listOf(newOrder) + it }
        _activeTrackingOrder.value = newOrder
        _cartItems.value = emptyList()
        return newOrder
    }

    fun reorder(order: Order) {
        _cartItems.update { current ->
            val reordered = order.items.map { item ->
                item.copy(id = UUID.randomUUID().toString())
            }
            current + reordered
        }
    }

    fun setActiveTrackingOrder(order: Order) {
        _activeTrackingOrder.value = order
    }

    // Admin & Kitchen order updates
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.update { list ->
            list.map {
                if (it.id == orderId) it.copy(status = newStatus) else it
            }
        }
        if (_activeTrackingOrder.value?.id == orderId) {
            _activeTrackingOrder.update { it?.copy(status = newStatus) }
        }
    }

    fun acceptOrder(orderId: String) {
        updateOrderStatus(orderId, OrderStatus.PREPARING)
    }

    fun rejectOrder(orderId: String) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    // Menu Management (Admin)
    fun toggleMenuItemAvailability(itemId: String) {
        _menuItems.update { list ->
            list.map {
                if (it.id == itemId) it.copy(isAvailable = !it.isAvailable) else it
            }
        }
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
        val fallbackImage = when (category) {
            MenuCategory.MAINS -> R.drawable.img_dish_burger
            MenuCategory.STARTERS -> R.drawable.img_restaurant_hero
            MenuCategory.DRINKS -> R.drawable.img_restaurant_hero
            MenuCategory.DESSERTS -> R.drawable.img_restaurant_hero
        }
        val newItem = MenuItem(
            id = "m_${System.currentTimeMillis()}",
            name = name,
            category = category,
            description = description,
            price = price,
            imageRes = fallbackImage,
            isAvailable = true,
            defaultSpiceLevel = spiceLevel,
            prepTimeMin = prepTimeMin,
            calories = calories,
            isChefSpecial = isChefSpecial,
            rating = 5.0,
            reviewCount = 1,
            availableAddOns = defaultAddOns
        )
        _menuItems.update { listOf(newItem) + it }
    }

    fun updateMenuItem(updated: MenuItem) {
        _menuItems.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    // Admin Auth
    fun loginAdmin(pin: String): Boolean {
        // Staff PINs: 1234 (Manager), 0000 (Kitchen Staff)
        val valid = pin.trim() == "1234" || pin.trim() == "0000" || pin.trim() == "admin"
        if (valid) {
            _isAdminLoggedIn.value = true
        }
        return valid
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    companion object {
        val instance by lazy { RestaurantRepository() }
    }
}
