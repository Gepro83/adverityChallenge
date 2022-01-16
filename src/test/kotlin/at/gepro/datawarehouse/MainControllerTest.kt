package at.gepro.datawarehouse

import at.gepro.datawarehouse.jpa.DataPoint
import at.gepro.datawarehouse.jpa.Repository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate


@SpringBootTest
class MainControllerTest {

    @Autowired
    private lateinit var repository: Repository

    @Autowired
    private lateinit var mainController: MainController

    private val testDataset = listOf(
            DataPoint(
                    datasource = "google",
                    campaign = "some campaing",
                    day = LocalDate.of(2020, 10, 1),
                    clicks = 3L,
                    impressions = 10L
            ),
            DataPoint(
                    datasource = "google",
                    campaign = "some campaing",
                    day = LocalDate.of(2020, 10, 2),
                    clicks = 7L,
                    impressions = 10L
            ),
            DataPoint(
                    datasource = "google",
                    campaign = "some campaing",
                    day = LocalDate.of(2020, 10, 3),
                    clicks = 1L,
                    impressions = 10L
            ),
            DataPoint(
                    datasource = "google",
                    campaign = "some campaing",
                    day = LocalDate.of(2020, 10, 4),
                    clicks = 5L,
                    impressions = 10L
            ),
            DataPoint(
                    datasource = "twitter",
                    campaign = "some campaing",
                    day = LocalDate.of(2020, 10, 6),
                    clicks = 12L,
                    impressions = 10L
            ),
    )

    @BeforeEach
    fun loadTestData() {
        repository.saveAll(testDataset)
    }

    @AfterEach
    fun resetRepository() {
        repository.deleteAll()
    }

    @Test
    fun `totalClicks for datasource and daterange`() {
        assertEquals(
                11L,
                mainController.totalClicks(
                        "google",
                        from = LocalDate.of(2020, 10, 1),
                        to = LocalDate.of(2020, 10, 3)
                )
        )
    }

    @Test
    fun `expect 0 clicks for unknown datasource`() {
        assertEquals(
                0L,
                mainController.totalClicks(
                        "not in dataset",
                        from = LocalDate.of(2020, 10, 1),
                        to = LocalDate.of(2020, 10, 3)
                )
        )
    }

    @Test
    fun `expect 0 clicks for from after to`() {
        assertEquals(
                0L,
                mainController.totalClicks(
                        "google",
                        from = LocalDate.of(2020, 10, 3),
                        to = LocalDate.of(2020, 10, 1)
                )
        )
    }


}
