package at.gepro.datawarehouse

import at.gepro.datawarehouse.jpa.DataPoint
import at.gepro.datawarehouse.jpa.Repository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate


@SpringBootTest
class MainControllerTest {

	@Autowired
	private lateinit var repository: Repository

	@Autowired
	private lateinit var MainController: MainController

	@Test
	fun `totalClicks`(){
		repository.save(DataPoint(0, "some source", "some campaing", LocalDate.now(), 0L, 10L))
	}

}
