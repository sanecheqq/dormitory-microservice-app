package com.missclick3.data.dao.fluoro_certificate

import com.missclick3.data.dao.DatabaseSingleton.dbQuery
import com.missclick3.data.models.FluoroCertificate
import com.missclick3.data.models.FluoroCertificates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FluoroDaoImpl : FluoroDao {
    private fun resultRowToCerificate(row: ResultRow) = FluoroCertificate(
        id = row[FluoroCertificates.id].toString(),
        startDate = row[FluoroCertificates.startDate].toString(),
        expireDate = row[FluoroCertificates.expireDate].toString()
    )

    override suspend fun upsertFluoro(certificate: FluoroCertificate, userId: UUID): Boolean =
        try {
            dbQuery {
                FluoroCertificates.upsert(FluoroCertificates.startDate, FluoroCertificates.expireDate, FluoroCertificates.userId) { row ->
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    row[startDate] = LocalDate.parse(certificate.startDate, formatter)
                    row[expireDate] = LocalDate.parse(certificate.expireDate, formatter)
                    row[FluoroCertificates.userId] = userId
                }
            }
            true
        } catch (e: Exception) {
            false
        }

    override suspend fun deleteFluoro(fluId: UUID) {
        dbQuery {
            FluoroCertificates
                .deleteWhere { FluoroCertificates.id eq fluId }
        }
    }

    override suspend fun deleteFluoroByUserId(userId: UUID) {
        dbQuery {
            FluoroCertificates
                .deleteWhere { FluoroCertificates.userId eq userId }
        }
    }

    override suspend fun getFluoroByUserId(userId: UUID): FluoroCertificate? = dbQuery {
        FluoroCertificates
            .selectAll()
            .where { FluoroCertificates.userId eq userId }
            .map(::resultRowToCerificate)
            .singleOrNull()
    }
}