package com.missclick3.services.certificates

import com.missclick3.messages.dtos.CertificateDTO
import com.missclick3.model.FluoroCertificate
import com.missclick3.repositories.certificates.CertificateRepository
import com.missclick3.services.certificates.CertificateService
import java.util.*

class FluoroCertificateServiceImpl(
    certificateRepository: CertificateRepository<FluoroCertificate>
) : CertificateService<FluoroCertificate> {
    private val repository = certificateRepository

    override suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID): Boolean {
        return repository.createNewCertificate(certificateDTO, userId)
    }

    override suspend fun getCertificateByUserId(userId: UUID): FluoroCertificate? {
        return repository.getCertificateByUserId(userId)
    }

    override suspend fun deleteCertificateByUserId(userId: UUID) {
        repository.deleteCertificateByUserId(userId)
    }

    override suspend fun deleteCertificateByCertId(certId: UUID) {
        repository.deleteCertificateByCertId(certId)
    }
}