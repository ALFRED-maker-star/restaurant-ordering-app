package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.MenuCategory
import com.example.data.model.OrderType
import com.example.data.model.PaymentMethod
import com.example.data.model.SpiceLevel
import com.example.data.repository.RestaurantRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("TastyGo", appName)
    }

    @Test
    fun `restaurant repository cart and order flow`() {
        val repository = RestaurantRepository()
        val firstItem = repository.menuItems.value.first()
        assertNotNull(firstItem)

        // Add to cart
        repository.addToCart(firstItem, quantity = 2, spiceLevel = SpiceLevel.HOT)
        assertEquals(1, repository.cartItems.value.size)
        assertEquals(2, repository.cartItems.value.first().quantity)

        // Place order
        val order = repository.placeOrder(
            orderType = OrderType.DINE_IN,
            tableNumber = "Table 05",
            deliveryAddress = "",
            customerName = "Taylor",
            customerPhone = "+15551234567",
            paymentMethod = PaymentMethod.CREDIT_CARD,
            specialInstructions = "Extra hot sauce"
        )
        assertNotNull(order)
        assertEquals(0, repository.cartItems.value.size)
        assertTrue(repository.orders.value.any { it.id == order.id })
    }

    @Test
    fun `admin login with staff PIN`() {
        val repository = RestaurantRepository()
        val managerLogin = repository.loginAdmin("1234")
        assertTrue(managerLogin)
        assertTrue(repository.isAdminLoggedIn.value)

        repository.logoutAdmin()
        assertEquals(false, repository.isAdminLoggedIn.value)
    }
}
