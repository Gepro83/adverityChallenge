package at.gepro.datawarehouse.business

import at.gepro.datawarehouse.jpa.DataPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ClickThroughRateTest {

    @Test
    fun `expect ctr 0 for no datapoints`() {
        assertEquals(0.0f, ClickThroughRate.of(emptyList()).value)
    }

    @Test
    fun `ctr of single datapoint is ratio of clicks to impressions`() {
        val datapoints = listOf(
                DataPoint(
                        datasource = "doesnt matter",
                        campaign = "doesnt matter",
                        day = LocalDate.now(),
                        clicks = 25L,
                        impressions = 100L
                )
        )

        assertEquals(0.25f, ClickThroughRate.of(datapoints).value)
    }

    @Test
    fun `ctr of multiple datapoints is sum of clicks divided by sum of impressions`() {
        val datapoints = listOf(
                DataPoint(
                        datasource = "doesnt matter",
                        campaign = "doesnt matter",
                        day = LocalDate.now(),
                        clicks = 10L,
                        impressions = 100L
                ),
                DataPoint(
                        datasource = "doesnt matter",
                        campaign = "doesnt matter",
                        day = LocalDate.now(),
                        clicks = 20,
                        impressions = 100L
                ),
                DataPoint(
                        datasource = "doesnt matter",
                        campaign = "doesnt matter",
                        day = LocalDate.now(),
                        clicks = 1L,
                        impressions = 110L
                ),
        )

        assertEquals(0.10f, ClickThroughRate.of(datapoints).value)
    }
}