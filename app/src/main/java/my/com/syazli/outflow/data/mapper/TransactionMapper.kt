package my.com.syazli.outflow.data.mapper

import my.com.syazli.outflow.data.local.entity.TransactionEntity
import my.com.syazli.outflow.domain.model.Transactions

fun TransactionEntity.toDomain(): Transactions {
    return Transactions(
        id,
        title,
        amount,
        type,
        date,
        note
    )
}

fun Transactions.toEntity(): TransactionEntity {
    return TransactionEntity(
        id,
        title,
        amount,
        type,
        date,
        note
    )
}