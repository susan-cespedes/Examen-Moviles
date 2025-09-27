package com.calyrsoft.ucbp1.features.profile.domain.model.value

@JvmInline
value class ProfileName(val value: String) {
    init {
        require(value.isNotBlank()) { "No se permite el nombre vacío" }
    }
}