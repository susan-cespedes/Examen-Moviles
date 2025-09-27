package com.calyrsoft.ucbp1.features.profile.data

import com.calyrsoft.ucbp1.features.profile.domain.model.Profile
import com.calyrsoft.ucbp1.features.profile.domain.model.value.*
import com.calyrsoft.ucbp1.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl : ProfileRepository {
    override fun getProfile(): Flow<Result<Profile>> = flow {
        // Simulamos datos de perfil por ahora

        val profile = Profile(
            id = ProfileId("user_123"),
            name = ProfileName("Usuario Demo"),
            email = ProfileEmail("usuario@ejemplo.com"),
            avatarUrl = ProfileAvatarUrl("drawable/profile")
        )
        emit(Result.success(profile))
    }
}