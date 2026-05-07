package my.com.syazli.outflow.domain.model

data class Transactions(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String,
    val date: Long,
    val note: String
)