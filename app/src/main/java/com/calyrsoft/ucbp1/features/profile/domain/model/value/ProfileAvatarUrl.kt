package com.calyrsoft.ucbp1.features.profile.domain.model.value

@JvmInline
value class ProfileAvatarUrl(val value: String?) {
    fun orDefault(): String = value ?: "https://example.com/default-avatar.png"
}