package my.com.syazli.outflow.data.mapper

import my.com.syazli.outflow.data.local.entity.CategoryEntity
import my.com.syazli.outflow.domain.model.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        id,
        name,
        type
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id,
        name,
        type
    )
}