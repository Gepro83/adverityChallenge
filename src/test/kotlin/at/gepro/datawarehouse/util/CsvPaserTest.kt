package at.gepro.datawarehouse.util

import at.gepro.datawarehouse.jpa.DataPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CsvPaserTest {

    @Test
    fun `empty stream to empty list`() {
        assertEquals(
                emptyList<DataPoint>(),
                CsvParser.execute("".byteInputStream())
        )
    }

    @Test
    fun `header row with 2 datapoints`() {
        val input = """
            |Datasource,Campaign,Daily,Clicks,Impressions
            |Google,Some Campaing,11/23/20,456,789
            |Twitter,Some other Campaing,11/24/19,11,22
        """.trimMargin()

        assertEquals(
                listOf(
                        DataPoint(
                                datasource = "Google",
                                campaign = "Some Campaing",
                                day = LocalDate.of(2020, 11, 23),
                                clicks = 456,
                                impressions = 789
                        ),
                        DataPoint(
                                datasource = "Twitter",
                                campaign = "Some other Campaing",
                                day = LocalDate.of(2019, 11, 24),
                                clicks = 11,
                                impressions = 22
                        ),
                ),
                CsvParser.execute(input.byteInputStream())
        )
    }

    @Test
    fun `trim whitespaces`() {
        val input = """
            |Datasource,Campaign,Daily,Clicks,Impressions
            |Google ,Some Campaing,11/23/20  ,456,789
        """.trimMargin()

        assertEquals(
                listOf(
                        DataPoint(
                                datasource = "Google",
                                campaign = "Some Campaing",
                                day = LocalDate.of(2020, 11, 23),
                                clicks = 456,
                                impressions = 789
                        )
                ),
                CsvParser.execute(input.byteInputStream())
        )
    }

    @Test
    fun `skip row with malformed date`() {
        val input = """
            |Datasource,Campaign,Daily,Clicks,Impressions
            |Google,Some Campaing,11/23/20,456,789
            |Twitter,Some other Campaing,11/24/19ER,11,22
        """.trimMargin()

        assertEquals(
                listOf(
                        DataPoint(
                                datasource = "Google",
                                campaign = "Some Campaing",
                                day = LocalDate.of(2020, 11, 23),
                                clicks = 456,
                                impressions = 789
                        )
                ),
                CsvParser.execute(input.byteInputStream())
        )
    }

    @Test
    fun `skip row with malformed number`() {
        val input = """
            |Datasource,Campaign,Daily,Clicks,Impressions
            |Google,Some Campaing,11/23/20,456,789ER
            |Twitter,Some other Campaing,11/24/19,11,22
        """.trimMargin()

        assertEquals(
                listOf(
                        DataPoint(
                                datasource = "Twitter",
                                campaign = "Some other Campaing",
                                day = LocalDate.of(2019, 11, 24),
                                clicks = 11,
                                impressions = 22
                        )
                ),
                CsvParser.execute(input.byteInputStream())
        )
    }

    @Test
    fun `skip row with too few entries`() {
        val input = """
            |Datasource,Campaign,Daily,Clicks,Impressions
            |Google,Some Campaing,11/23/20,456
            |Twitter,Some other Campaing,11/24/19,11,22
        """.trimMargin()

        assertEquals(
                listOf(
                        DataPoint(
                                datasource = "Twitter",
                                campaign = "Some other Campaing",
                                day = LocalDate.of(2019, 11, 24),
                                clicks = 11,
                                impressions = 22
                        )
                ),
                CsvParser.execute(input.byteInputStream())
        )
    }


    @Test
    fun `expect empty list when header not present`() {
        val input = """
            |Google,Some Campaing,11/23/20,456,56
            |Twitter,Some other Campaing,11/24/19,11,22
        """.trimMargin()

        assertEquals(
                emptyList<DataPoint>(),
                CsvParser.execute(input.byteInputStream())
        )
    }

}