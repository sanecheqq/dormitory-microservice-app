package com.missclick3.repositories.dormitory_address

import java.util.*

interface DormitoryAddressRepository {
    suspend fun addAddress(address: String): Boolean
    suspend fun updateAddress(newAddress: String, id: UUID): Boolean
    suspend fun deleteAddress(id: UUID): Boolean
}