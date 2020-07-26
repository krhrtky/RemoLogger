package gateways.repositories

import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.ninja_squad.dbsetup_kotlin.dbSetup
import com.remoLogger.entities.sensorRecord.CreatedAt
import com.remoLogger.entities.sensorRecord.Humidity
import com.remoLogger.entities.sensorRecord.Id
import com.remoLogger.entities.sensorRecord.Illuminace
import com.remoLogger.entities.sensorRecord.SensorRecord
import com.remoLogger.entities.sensorRecord.Temperature
import com.remoLogger.gateways.repositories.SensorRecords
import com.remoLogger.gateways.repositories.SensorRecordsRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.db.api.Assertions.assertThat
import org.assertj.db.type.Changes
import org.assertj.db.type.DateTimeValue
import org.assertj.db.type.Source
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.sql.Timestamp

object SensorRecordsRepositorySpec: Spek({
    val repository = SensorRecordsRepository()
    val source = Source("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1", "", "")
    val dbSource = DriverManagerDestination("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1", "", "")

    describe(".create") {
        beforeEachGroup {
            Database.connect(
                "jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
                driver = "org.h2.Driver"
            )
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SensorRecords)
            }
        }

        context("success save.") {
            it("should save row values.") {

                val changes = Changes(source)
                changes.setStartPointNow()

                val dateTime = DateTime.now()

                val sensorRecord = SensorRecord(
                    id = Id("test-id"),
                    temperature = Temperature(25.0.toFloat()),
                    humidity = Humidity(70.0.toFloat()),
                    illuminance = Illuminace(130.toFloat()),
                    createdAt = CreatedAt(dateTime)
                )

                transaction {
                    repository.create(sensorRecord)
                }

                changes.setEndPointNow()

                assertThat(changes).hasNumberOfChanges(1)
                    .change()
                    .isCreation
                    .isOnTable(SensorRecords.tableName)
                    .rowAtEndPoint()
                    .hasValues(
                        "test-id",
                        25.0.toFloat(),
                        70.0.toFloat(),
                        130.toFloat(),
                        DateTimeValue.from(Timestamp(dateTime.millis))
                    )
            }
        }
    }

    describe(".find") {
        beforeEachGroup {
            Database.connect(
                "jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
                driver = "org.h2.Driver"
            )
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(SensorRecords)
            }
        }

        beforeEachTest {
            dbSetup(dbSource) {
                deleteAllFrom(SensorRecords.tableName)
            }
        }

        context("table has 2 data.") {
            beforeGroup {
                dbSetup(dbSource) {
                    deleteAllFrom(SensorRecords.tableName)
                    insertInto(SensorRecords.tableName) {
                        columns(
                            SensorRecords.id.name,
                            SensorRecords.temperature.name,
                            SensorRecords.humidity.name,
                            SensorRecords.illuminance.name,
                            SensorRecords.createdAt.name
                        )
                        (1..2).forEach {
                            values(
                                "test-id-${it}",
                                (20 + it).toLong(),
                                (60 + it).toLong(),
                                (100 + it).toLong(),
                                DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS")
                            )
                        }
                    }
                }.launch()
            }

            it("should return list has 2 items.") {

                val result = repository.find()

                assertThat(result).hasSize(2)
            }
        }

        context("table has no data.") {
            beforeGroup {
                dbSetup(dbSource) {
                    deleteAllFrom(SensorRecords.tableName)
                }.launch()
            }
            it("should return empty list.") {

                val result = repository.find()

                assertThat(result).isEmpty()
            }
        }
    }
})


