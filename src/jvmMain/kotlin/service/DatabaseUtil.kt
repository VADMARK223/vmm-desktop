package service

import org.jetbrains.exposed.sql.Database

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
fun databaseConnect() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/vmm",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}
