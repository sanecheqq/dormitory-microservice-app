package com.missclick3.repositories.certificates

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.messages.dtos.CertificateDTO
import com.missclick3.model.FluoroCertificate
import com.missclick3.model.FluoroCertificatesTable
import com.missclick3.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FluoroCertificateRepositoryImpl : CertificateRepository<FluoroCertificate> {
    override suspend fun createNewCertificate(certificateDTO: CertificateDTO, userId: UUID): Boolean {
        return try {
            dbQuery {
                deleteCertificateByUserId(userId)
                FluoroCertificate.new {
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

    override suspend fun getCertificateByUserId(userId: UUID): FluoroCertificate? {
        return dbQuery {
            FluoroCertificate.find { FluoroCertificatesTable.userId eq userId }.singleOrNull()
        }
    }

    override suspend fun deleteCertificateByUserId(userId: UUID) {
        dbQuery {
            val certificate = FluoroCertificate
                .find { FluoroCertificatesTable.userId eq userId }.singleOrNull()
            certificate?.delete()
        }
    }

    override suspend fun deleteCertificateByCertId(certId: UUID) {
        dbQuery {
            val certificate = FluoroCertificate.findById(certId)
            certificate?.delete()
        }
    }
}