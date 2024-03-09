package com.missclick3.repositories.certificates

import com.missclick3.messages.dtos.CertificateDTO
import java.util.*

interface CertificateRepository<T> {
    suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID) : Boolean

    suspend fun getCertificateByUserId(userId: UUID) : T?

    suspend fun deleteCertificateByUserId(userId: UUID)

    suspend fun deleteCertificateByCertId(certId: UUID)
}