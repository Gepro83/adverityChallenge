package at.gepro.datawarehouse.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate


interface Repository : JpaRepository<DataPoint?, String?> {

    @Query("SELECT coalesce(SUM(dp.clicks), 0) FROM DataPoint dp WHERE dp.datasource = :datasource AND dp.day BETWEEN :from AND :to")
    fun sumOfClicksByDatasourceInDateRange(
            @Param("datasource") datasource: String,
            @Param("from") from: LocalDate,
            @Param("to") to: LocalDate
    ): Long

    @Query("SELECT coalesce(SUM(dp.clicks), 0) FROM DataPoint dp WHERE dp.day BETWEEN :from AND :to")
    fun sumOfClicksInDateRange(
            @Param("from") from: LocalDate,
            @Param("to") to: LocalDate
    ): Long


    fun findByDatasourceAndCampaign(datasource: String, campaign: String): List<DataPoint>

    fun findByDatasource(datasource: String): List<DataPoint>

    fun findByCampaign(campaign: String): List<DataPoint>

    fun findByDay(day: LocalDate): List<DataPoint>

}