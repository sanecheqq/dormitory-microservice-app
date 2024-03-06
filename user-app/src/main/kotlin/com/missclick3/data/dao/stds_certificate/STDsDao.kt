package com.missclick3.data.dao.stds_certificate

import com.missclick3.data.models.STDsCertificate
import java.util.*

interface STDsDao {
    suspend fun upsertSTDs(certificate: STDsCertificate, userId: UUID): Boolean

    suspend fun deleteSTDs(stdsId: UUID)

    suspend fun deleteSTDsByUserId(userId: UUID)

    suspend fun getSTDsByUserId(userId: UUID): STDsCertificate?
}