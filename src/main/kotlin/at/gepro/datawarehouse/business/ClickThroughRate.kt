package at.gepro.datawarehouse.business

import at.gepro.datawarehouse.jpa.DataPoint

@JvmInline
value class ClickThroughRate(val value: Float) {

    companion object {
        fun of(datapoints: List<DataPoint>): ClickThroughRate =
                datapoints.map { it.clicks to it.impressions }
                        .reduceOrNull { accumulator, element -> accumulator + element}
                        ?.let { calculateRate(it.first, it.second) }
                        ?: ClickThroughRate(0.0f)

        private operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) =
                first + other.first to second + other.second

        private fun calculateRate(clicks: Long, impressions: Long) =
                ClickThroughRate(clicks.toFloat() / impressions)

    }
}