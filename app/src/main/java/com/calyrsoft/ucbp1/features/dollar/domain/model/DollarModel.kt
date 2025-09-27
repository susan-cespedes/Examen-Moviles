package com.calyrsoft.ucbp1.features.dollar.domain.model

data class DollarModel(
    val dollarOfficialCompra: String? = null,
    val dollarParallelCompra: String? = null,
    val dollarOfficialVenta: String? = null,
    val dollarParallelVenta: String? = null,
    val timestamp:Long=0L
)
