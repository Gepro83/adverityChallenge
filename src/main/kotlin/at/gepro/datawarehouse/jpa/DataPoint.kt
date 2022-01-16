package at.gepro.datawarehouse.jpa

import java.time.LocalDate
import javax.persistence.*

@Entity
class DataPoint(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val datasource: String,

        val campaign: String,

        val day: LocalDate,

        val clicks: Long,

        val impressions: Long

)