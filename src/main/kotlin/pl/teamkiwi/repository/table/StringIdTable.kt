package pl.teamkiwi.repository.table

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column
import pl.teamkiwi.repository.table.Constants.DEFAULT_CHARSET
import java.util.*

const val STRING_UUID_LENGTH = 60

/**
 * This class is a table, which id is [String] type.
 *
 * Default id value is equal to [UUID.randomUUID]
 */
open class StringIdTable(
    name: String = "",
    columnName: String = "id"
) : IdTable<String>(name) {

    override val id: Column<EntityID<String>> =
        varchar(columnName, STRING_UUID_LENGTH, DEFAULT_CHARSET)
            .primaryKey()
            .clientDefault { UUID.randomUUID().toString() }
            .entityId()

}

abstract class StringIdEntity(id: EntityID<String>) : Entity<String>(id)

abstract class StringIdEntityClass<out E: StringIdEntity>(
    table: IdTable<String>,
    entityType: Class<E>? = null
) : EntityClass<String, E>(table, entityType)

