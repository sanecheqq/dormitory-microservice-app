package com.missclick3.data.dao.certificate

import com.missclick3.data.models.Certificate
import java.util.UUID

interface CertificateDao {
    suspend fun getCertificateByUserId(userId: UUID)

    suspend fun addCertificate(certificate: Certificate)

    suspend fun deleteCertificate(certificateId: UUID)
}