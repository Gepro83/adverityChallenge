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

) {

        override fun toString(): String =
                "DataPoint(id=$id, datasource='$datasource', campaign='$campaign', day=$day, clicks=$clicks, impressions=$impressions)"

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as DataPoint

                if (id != other.id) return false
                if (datasource != other.datasource) return false
                if (campaign != other.campaign) return false
                if (day != other.day) return false
                if (clicks != other.clicks) return false
                if (impressions != other.impressions) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id.hashCode()
                result = 31 * result + datasource.hashCode()
                result = 31 * result + campaign.hashCode()
                result = 31 * result + day.hashCode()
                result = 31 * result + clicks.hashCode()
                result = 31 * result + impressions.hashCode()
                return result
        }


}