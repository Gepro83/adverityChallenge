package at.gepro.datawarehouse.util

import at.gepro.datawarehouse.jpa.DataPoint
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object CsvParser {

    private val LOG = LoggerFactory.getLogger(CsvParser::class.java)
    private const val EXPECTED_HEADER = "Datasource,Campaign,Daily,Clicks,Impressions"

    fun execute(inputStream: InputStream): List<DataPoint> {
        with(inputStream.bufferedReader()) {
            if (readLine() != EXPECTED_HEADER)
                return emptyList()

            return readLines()
                    .mapNotNull { line -> line.toDataPoint() }
        }
    }

    private fun String.toDataPoint(): DataPoint? {
        val split = split(',')

        if (split.size != 5)
            return null

        return try {
                DataPoint(
                        datasource = split[0].trim(),
                        campaign = split[1].trim(),
                        day = LocalDate.parse(split[2].trim(), DateTimeFormatter.ofPattern("MM/dd/yy")),
                        clicks = split[3].trim().toLong(),
                        impressions = split[4].trim().toLong(),
                )
            } catch (e: Exception) {
                LOG.warn("could not deserialize $split", e)
                null
        }
    }
}