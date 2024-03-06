package com.missclick3.data.dao.fluoro_certificate

import com.missclick3.data.models.FluoroCertificate
import java.util.*

interface FluoroDao {
    suspend fun insertNewFluoro(certificate: FluoroCertificate, userId: UUID): Boolean

    suspend fun deleteFluoro(fluId: UUID)

    suspend fun getFluoroByUserId(userId: UUID): FluoroCertificate?

    suspend fun deleteFluoroByUserId(userId: UUID)
}