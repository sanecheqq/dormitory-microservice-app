package com.missclick3.services.certificates

import com.missclick3.messages.dtos.CertificateDTO
import com.missclick3.model.FluoroCertificate
import com.missclick3.model.STDsCertificate
import com.missclick3.repositories.certificates.CertificateRepository
import com.missclick3.services.certificates.CertificateService
import java.util.*

class STDsCertificateServiceImpl(
    certificateRepository: CertificateRepository<STDsCertificate>
) : CertificateService<STDsCertificate> {
    private val repository = certificateRepository
    private fun certToCertDTO(stDsCertificate: STDsCertificate) = CertificateDTO(
        id = stDsCertificate.id.toString(),
        startDate = stDsCertificate.startDate.toString(),
        expireDate = stDsCertificate.expireDate.toString()
    )
    override suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID): Boolean {
        return repository.createNewCertificate(certificateDTO, userId)
    }

    override suspend fun getCertificateByUserId(userId: UUID): CertificateDTO? {
        val certificate = repository.getCertificateByUserId(userId) ?: return null
        return certToCertDTO(certificate)
    }

    override suspend fun deleteCertificateByUserId(userId: UUID) {
        repository.deleteCertificateByUserId(userId)
    }

    override suspend fun deleteCertificateByCertId(certId: UUID) {
        repository.deleteCertificateByCertId(certId)
    }
}