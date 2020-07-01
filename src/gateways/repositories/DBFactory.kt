package com.remoLogger.gateways.repositories

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class DBFactory {
    companion object {
        fun init(dataSource: DataSource = HikariDataSource(
            HikariConfig().apply {
                val config = HoconApplicationConfig(ConfigFactory.load())
                jdbcUrl = config.propertyOrNull("ktor.db.jdbcUrl")?.getString()
                driverClassName = config.propertyOrNull("ktor.db.driverClassName")?.getString()
                username = config.propertyOrNull("ktor.db.username")?.getString()
                password = config.propertyOrNull("ktor.db.password")?.getString()
                maximumPoolSize = config.propertyOrNull("ktor.db.maximumPoolSize")?.getString()?.toInt() ?: 10
            }
        )) {
            Database.connect(dataSource)
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SensorRecords)
            }
        }
    }
}

