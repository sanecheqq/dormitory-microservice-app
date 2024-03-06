package com.missclick3.data.dao.stds_certificate

import com.missclick3.data.dao.DatabaseSingleton.dbQuery
import com.missclick3.data.models.STDsCertificate
import com.missclick3.data.models.STDsCertificates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class STDsDaoImpl : STDsDao {
    private fun resultRowToCertificate(row: ResultRow) = STDsCertificate(
        id = row[STDsCertificates.id].toString(),
        startDate = row[STDsCertificates.startDate].toString(),
        expireDate = row[STDsCertificates.expireDate].toString()
    )

    override suspend fun upsertSTDs(certificate: STDsCertificate, userId: UUID): Boolean =
        try {
            dbQuery {
                STDsCertificates.upsert(
                    STDsCertificates.startDate,
                    STDsCertificates.expireDate,
                    STDsCertificates.userId
                ) { row->
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    row[startDate] = LocalDate.parse(certificate.startDate, formatter)
                    row[expireDate] = LocalDate.parse(certificate.expireDate, formatter)
                    row[STDsCertificates.userId] = userId
                }
            }
            true
        } catch (e: Exception) {
            false
        }

    override suspend fun deleteSTDs(stdsId: UUID) {
        dbQuery {
            STDsCertificates
                .deleteWhere { STDsCertificates.id eq stdsId }
        }
    }

    override suspend fun deleteSTDsByUserId(userId: UUID) {
        dbQuery {
            STDsCertificates
                .deleteWhere { STDsCertificates.userId eq userId }
        }
    }

    override suspend fun getSTDsByUserId(userId: UUID): STDsCertificate? = dbQuery {
        STDsCertificates
            .selectAll()
            .where {STDsCertificates.userId eq userId}
            .map(::resultRowToCertificate)
            .singleOrNull()
    }
}