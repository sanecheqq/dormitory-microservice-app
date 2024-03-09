package com.missclick3.services.dormitory_address

import com.missclick3.repositories.dormitory_address.DormitoryAddressRepository
import com.missclick3.services.dormitory_address.DormitoryAddressService
import java.util.*

class DormitoryAddressServiceImpl(
    dormitoryAddressRepository: DormitoryAddressRepository
) : DormitoryAddressService {
    private val repository = dormitoryAddressRepository

    override suspend fun addAddress(address: String): Boolean {
        return repository.addAddress(address)
    }

    override suspend fun updateAddress(newAddress: String, id: UUID): Boolean {
        return repository.updateAddress(newAddress, id)
    }

    override suspend fun deleteAddress(id: UUID): Boolean {
        return repository.deleteAddress(id)
    }
}