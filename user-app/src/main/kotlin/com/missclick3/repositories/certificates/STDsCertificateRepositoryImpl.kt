package com.missclick3.repositories.certificates

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.messages.dtos.CertificateDTO
import com.missclick3.model.STDsCertificate
import com.missclick3.model.STDsCertificatesTable
import com.missclick3.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class STDsCertificateRepositoryImpl : CertificateRepository<STDsCertificate> {
    override suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID): Boolean {
        return try {
            dbQuery {
                deleteCertificateByUserId(userId)
                STDsCertificate.new {
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    startDate = LocalDate.parse(certificateDTO.startDate, formatter)
                    expireDate = LocalDate.parse(certificateDTO.expireDate, formatter)
                    user = User.findById(userId)!!
                }
            }
            true
        }   catch (e: Exception) {
            false
        }
    }

    override suspend fun getCertificateByUserId(userId: UUID): STDsCertificate? {
        return dbQuery {
            STDsCertificate.find { STDsCertificatesTable.userId eq userId }.singleOrNull()
        }
    }

    override suspend fun deleteCertificateByUserId(userId: UUID) {
        dbQuery {
            val certificate = STDsCertificate
                .find { STDsCertificatesTable.userId eq userId }.singleOrNull()
            certificate?.delete()
        }
    }

    override suspend fun deleteCertificateByCertId(certId: UUID) {
        dbQuery {
            val certificate = STDsCertificate.findById(certId)
            certificate?.delete()
        }
    }
}