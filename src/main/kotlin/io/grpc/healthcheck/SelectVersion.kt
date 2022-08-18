package io.grpc.healthcheck

import io.grpc.healthcheck.models.Property
import io.grpc.healthcheck.models.VersionRequire
import java.sql.DriverManager
import java.sql.SQLException

class SelectVersion {


}

fun versionRequire (id: Int, flag: Boolean): String {
    var result = String()
    DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgresTest",
        "postgres",
        "admin")
        .use {
            conn ->
            conn.createStatement().use { statement ->
                val rs = statement.executeQuery(
                    "SELECT version_require($id, $flag)"
                )
                while (rs.next()) {
                    val value = rs.getString("version_require")
                    val obj = VersionRequire.VersionRequireInfo(value)
                    result = obj.versionRequire
                }
            }
        }
    return result
}

fun getVersion (): String {
    var result = String ()
    val sql = "SELECT value FROM property LIMIT 1"
    try {
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgresTest",
            "postgres",
            "admin"
        )
            .use { conn ->
                conn.createStatement().use { statement ->
                    val resultSet = statement.executeQuery(sql)
                    while (resultSet.next()) {
                        val value = resultSet.getString("value")
                        val obj = Property.PropertyInfo(value)
                        result = obj.value
                    }
                }
            }
    } catch (e: SQLException) {
        System.err.format("SQL State: %s\n%s", e.sqlState, e.message)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

object RowSelectVersion {
    @JvmStatic
    fun main (args: Array<String>) {
        System.out.println(getVersion())
    }
}