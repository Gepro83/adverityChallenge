package at.gepro.datawarehouse

import at.gepro.datawarehouse.business.ClickThroughRate
import at.gepro.datawarehouse.jpa.DataPoint
import at.gepro.datawarehouse.jpa.Repository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val LOG = LoggerFactory.getLogger(MainController::class.java)

@Controller
class MainController {

    @Autowired
    private lateinit var repository: Repository

    @GetMapping("/")
    fun index(model: Model): String? {
        model.addAttribute("message", "${repository.count()} datapoints in database")
        return "index"
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.originalFilename + "!")

        with(file.inputStream.bufferedReader()) {
            val headerLine: String? = readLine()
            if (headerLine  == "Datasource,Campaign,Daily,Clicks,Impressions") {
                val entities = readLines().mapNotNull { line ->
                    val split = line.split(',')
                    if (split.size != 5)
                        null
                    else
                        try {
                            DataPoint(
                                    datasource = split[0],
                                    campaign = split[1],
                                    day = LocalDate.parse(split[2], DateTimeFormatter.ofPattern("MM/dd/yy")),
                                    clicks = split[3].toLong(),
                                    impressions = split[4].toLong(),
                            )
                        } catch(e: Exception) {
                            LOG.warn("could not deserialize $split", e)
                            null
                        }
                }

                repository.saveAll(entities)

            }
        }

        return "redirect:/"
    }

    @PostMapping(path= ["/totalClicks"])
    @ResponseBody
    fun totalClicks(
            @RequestParam datasource: String,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate
    ): Long {

        return repository.totalClicksForDatasourceInDateRange(
                datasource = datasource,
                from = from,
                to = to
        )
    }

    @PostMapping(path= ["/clickThroughRate"])
    @ResponseBody
    fun clickThroughRate(
            @RequestParam datasource: String,
            @RequestParam campaign: String,
    ): ClickThroughRate {
        return ClickThroughRate.of(repository.findByDatasourceAndCampaign(datasource, campaign))
    }

    @PostMapping(path= ["/totalImpressions"])
    @ResponseBody
    fun totalImpressions(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate): Long =
            repository.findByDay(date).sumOf { it.impressions }


}