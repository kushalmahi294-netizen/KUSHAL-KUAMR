package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.models.Address
import com.example.data.models.DiscountType
import com.example.data.models.OrderItem
import com.example.data.models.OrderStatus
import com.example.data.models.PaymentMethod
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        return list
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String {
        if (value == null) return "{}"
        val obj = JSONObject()
        value.forEach { (k, v) -> obj.put(k, v) }
        return obj.toString()
    }

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String> {
        if (value.isNullOrEmpty()) return emptyMap()
        val map = mutableMapOf<String, String>()
        val obj = JSONObject(value)
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = obj.optString(key, "")
        }
        return map
    }

    @TypeConverter
    fun fromOrderItems(items: List<OrderItem>?): String {
        if (items == null) return "[]"
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("productId", item.productId)
            obj.put("productName", item.productName)
            obj.put("productBrand", item.productBrand)
            obj.put("productImage", item.productImage)
            obj.put("price", item.price)
            obj.put("quantity", item.quantity)
            obj.put("selectedSize", item.selectedSize)
            obj.put("selectedColor", item.selectedColor)
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toOrderItems(value: String?): List<OrderItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<OrderItem>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                OrderItem(
                    productId = obj.optString("productId"),
                    productName = obj.optString("productName"),
                    productBrand = obj.optString("productBrand"),
                    productImage = obj.optString("productImage"),
                    price = obj.optDouble("price", 0.0),
                    quantity = obj.optInt("quantity", 1),
                    selectedSize = obj.optString("selectedSize", ""),
                    selectedColor = obj.optString("selectedColor", "")
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromAddress(address: Address?): String {
        if (address == null) return "{}"
        val obj = JSONObject()
        obj.put("id", address.id)
        obj.put("fullName", address.fullName)
        obj.put("phone", address.phone)
        obj.put("streetAddress", address.streetAddress)
        obj.put("city", address.city)
        obj.put("state", address.state)
        obj.put("pinCode", address.pinCode)
        obj.put("addressType", address.addressType)
        obj.put("isDefault", address.isDefault)
        return obj.toString()
    }

    @TypeConverter
    fun toAddress(value: String?): Address {
        if (value.isNullOrEmpty()) {
            return Address(fullName = "", phone = "", streetAddress = "", city = "", state = "", pinCode = "")
        }
        val obj = JSONObject(value)
        return Address(
            id = obj.optString("id"),
            fullName = obj.optString("fullName"),
            phone = obj.optString("phone"),
            streetAddress = obj.optString("streetAddress"),
            city = obj.optString("city"),
            state = obj.optString("state"),
            pinCode = obj.optString("pinCode"),
            addressType = obj.optString("addressType", "Home"),
            isDefault = obj.optBoolean("isDefault", false)
        )
    }

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = try {
        OrderStatus.valueOf(value)
    } catch (e: Exception) {
        OrderStatus.CONFIRMED
    }

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name

    @TypeConverter
    fun toPaymentMethod(value: String): PaymentMethod = try {
        PaymentMethod.valueOf(value)
    } catch (e: Exception) {
        PaymentMethod.COD
    }

    @TypeConverter
    fun fromDiscountType(type: DiscountType): String = type.name

    @TypeConverter
    fun toDiscountType(value: String): DiscountType = try {
        DiscountType.valueOf(value)
    } catch (e: Exception) {
        DiscountType.PERCENTAGE
    }
}
