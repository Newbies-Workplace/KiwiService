package pl.teamkiwi.repository

import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import pl.teamkiwi.repository.table.Users

class Exposed {

    companion object Feature : ApplicationFeature<Application, ExposedConfiguration, Exposed> {

        override val key: AttributeKey<Exposed>
            get() = AttributeKey("Exposed")

        override fun install(
            pipeline: Application,
            configure: ExposedConfiguration.() -> Unit
        ): Exposed {
            val application = ExposedConfiguration.create()
            application.apply(configure)
            return Exposed()
        }

    }
}

class ExposedConfiguration private constructor() {

    private val database = Database

    fun connectWithConfig(configuration: DatabaseConfiguration) {
        database.connect(
            url = configuration.url,
            driver = configuration.driver,
            user = configuration.user,
            password = configuration.password
        )
    }

    /**
     * Creates database schemas if they do not exists.
     */
    fun createSchemas() {
        transaction {
            SchemaUtils.create(
                Users
            )
        }
    }

    companion object {
        fun create(): ExposedConfiguration {
            return ExposedConfiguration()
        }
    }
}

data class DatabaseConfiguration(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)