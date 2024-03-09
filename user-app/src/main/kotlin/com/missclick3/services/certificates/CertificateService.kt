package com.missclick3.services.certificates

import com.missclick3.messages.dtos.CertificateDTO
import java.util.*

interface CertificateService<T> {
    suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID) : Boolean

    suspend fun getCertificateByUserId(userId: UUID) : T?

    suspend fun deleteCertificateByUserId(userId: UUID)

    suspend fun deleteCertificateByCertId(certId: UUID)
}