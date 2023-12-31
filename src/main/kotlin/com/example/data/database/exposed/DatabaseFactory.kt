package com.example.data.database.exposed

import com.example.data.database.exposed.table.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(appConfig: ApplicationConfig) {
        Database.connect(hikari(appConfig))

        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(ContactRequests)
            SchemaUtils.create(Contacts)
            SchemaUtils.create(BlockedUsers)
            SchemaUtils.create(Chats)
            SchemaUtils.create(Messages)
            SchemaUtils.create(UsersSeenMessage)
            SchemaUtils.create(MessagePictureUrls)
            SchemaUtils.create(Participants)
        }
    }


    private fun hikari(appConfig: ApplicationConfig): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = appConfig.property("storage.driverClassName").getString()
        config.jdbcUrl = appConfig.property("storage.jdbcURL").getString()
        config.password = appConfig.property("storage.password").getString()
        config.username = appConfig.property("storage.user").getString()
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

    suspend fun drop() {
        dbQuery {
            SchemaUtils.drop(
                Users,
                ContactRequests,
                Contacts,
                BlockedUsers,
                Chats,
                Messages,
                MessagePictureUrls,
                Participants
            )
        }
    }

}