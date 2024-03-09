package com.missclick3.repositories.dormitory_address

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.model.DormitoryAddress
import com.missclick3.repositories.dormitory_address.DormitoryAddressRepository
import java.util.*

class DormitoryAddressRepositoryImpl : DormitoryAddressRepository {
    override suspend fun addAddress(address: String): Boolean {
        return try {
            dbQuery {
                DormitoryAddress.new {
                    this.address = address
                }
            }
            true
        }
        catch (e: Exception) {
            false
        }
    }

    override suspend fun updateAddress(newAddress: String, id: UUID): Boolean {
        return try {
            dbQuery {
                val dormAddress = DormitoryAddress.findById(id)
                dormAddress?.address = newAddress
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteAddress(id: UUID): Boolean {
        return try {
            dbQuery {
                DormitoryAddress.findById(id)?.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}