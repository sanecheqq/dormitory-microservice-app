package com.missclick3.services.dormitory_address

import java.util.*

interface DormitoryAddressService {
    suspend fun addAddress(address: String): Boolean
    suspend fun updateAddress(newAddress: String, id: UUID): Boolean
    suspend fun deleteAddress(id: UUID): Boolean
}