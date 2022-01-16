package at.gepro.datawarehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DatawarehouseApplication

fun main(args: Array<String>) {
	runApplication<DatawarehouseApplication>(*args)
}
