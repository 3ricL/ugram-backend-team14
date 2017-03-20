package ugram.models

data class Pagination<out T>(
        val items: List<T>,
        val totalPages: Int,
        val totalEntries: Int
)