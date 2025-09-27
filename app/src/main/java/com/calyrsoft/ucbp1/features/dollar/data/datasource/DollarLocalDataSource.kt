package com.calyrsoft.ucbp1.features.dollar.data.datasource

import com.calyrsoft.ucbp1.features.dollar.data.database.dao.IDollarDao
import com.calyrsoft.ucbp1.features.dollar.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.dollar.data.mapper.toModel
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel

class DollarLocalDataSource(
    private val dao: IDollarDao
) {
    suspend fun insert(dollar: DollarModel) {
        dao.insert(dollar.toEntity())
    }

    suspend fun getAllOrderedByDate(): List<DollarModel> {
        return dao.getAllOrderedByDate().map { it.toModel() }
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}
