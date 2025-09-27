package com.calyrsoft.ucbp1.features.profile.domain.model


import com.calyrsoft.ucbp1.features.profile.domain.model.value.*

data class Profile(
    val id: ProfileId,
    val name: ProfileName,
    val email: ProfileEmail,
    val avatarUrl: ProfileAvatarUrl = ProfileAvatarUrl(null)
)
