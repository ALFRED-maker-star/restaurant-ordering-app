package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class MenuCategory(val displayName: String, val iconName: String) {
    STARTERS("Starters", "starter"),
    MAINS("Mains", "main"),
    DRINKS("Drinks", "drink"),
    DESSERTS("Desserts", "dessert")
}

enum class SpiceLevel(val label: String, val pepperCount: Int) {
    NONE("No Spice", 0),
    MILD("Mild", 1),
    MEDIUM("Medium", 2),
    HOT("Hot", 3),
    EXTRA_HOT("Extra Hot", 4)
}

data class AddOn(
    val id: String,
    val name: String,
    val price: Double
)

data class MenuItem(
    val id: String,
    val name: String,
    val category: MenuCategory,
    val description: String,
    val price: Double,
    @DrawableRes val imageRes: Int,
    val isAvailable: Boolean = true,
    val defaultSpiceLevel: SpiceLevel = SpiceLevel.MILD,
    val prepTimeMin: Int = 15,
    val calories: Int = 450,
    val isChefSpecial: Boolean = false,
    val rating: Double = 4.8,
    val reviewCount: Int = 124,
    val availableAddOns: List<AddOn> = emptyList()
)

data class CartItem(
    val id: String,
    val menuItem: MenuItem,
    val quantity: Int,
    val spiceLevel: SpiceLevel = SpiceLevel.MILD,
    val selectedAddOns: List<AddOn> = emptyList(),
    val specialNotes: String = ""
) {
    val unitPrice: Double
        get() = menuItem.price + selectedAddOns.sumOf { it.price }

    val totalPrice: Double
        get() = unitPrice * quantity
}

enum class OrderType(val displayName: String) {
    DELIVERY("Delivery"),
    DINE_IN("Dine-in")
}

enum class PaymentMethod(val displayName: String) {
    CREDIT_CARD("Credit/Debit Card"),
    GOOGLE_PAY("Google Pay"),
    CASH("Cash on Delivery / Table")
}

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    RECEIVED("Order Received", 0),
    PREPARING("Preparing", 1),
    READY("Ready", 2),
    SERVED_DELIVERED("Served / Delivered", 3),
    CANCELLED("Cancelled", -1)
}

data class Order(
    val id: String,
    val items: List<CartItem>,
    val orderType: OrderType,
    val tableNumber: String = "",
    val deliveryAddress: String = "",
    val customerName: String = "Alex Morgan",
    val customerPhone: String = "+1 (555) 349-8291",
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val specialInstructions: String = "",
    val status: OrderStatus = OrderStatus.RECEIVED,
    val timestamp: Long = System.currentTimeMillis(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val estimatedMinutes: Int = 25
)
