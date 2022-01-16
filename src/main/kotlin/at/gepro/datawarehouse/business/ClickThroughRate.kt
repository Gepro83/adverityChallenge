package at.gepro.datawarehouse.business

import at.gepro.datawarehouse.jpa.DataPoint

@JvmInline
value class ClickThroughRate(val value: Float) {

    companion object {
        fun of(datapoints: List<DataPoint>): ClickThroughRate =
                datapoints.map { calculateRate(it.clicks, it.impressions) }
                        .average()
                        .toFloat()
                        .takeUnless { it.isNaN() }
                        .let { ClickThroughRate(it ?: 0.0f) }


        private fun calculateRate(clicks: Long, impressions: Long): Float = clicks.toFloat() / impressions
    }
}
