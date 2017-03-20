package ugram.util

import java.time.Instant

fun calculateNbPages(total: Int, perPage: Int) = Math.ceil(total.toDouble()/perPage.toDouble()).toInt()
fun nowTimestamp() = Instant.now().epochSecond.toInt()