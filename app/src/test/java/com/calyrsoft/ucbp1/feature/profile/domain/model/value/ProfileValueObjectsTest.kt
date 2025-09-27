package com.calyrsoft.ucbp1.feature.profile.domain.model.value

import com.calyrsoft.ucbp1.features.profile.domain.model.value.ProfileAvatarUrl
import com.calyrsoft.ucbp1.features.profile.domain.model.value.ProfileEmail
import com.calyrsoft.ucbp1.features.profile.domain.model.value.ProfileId
import com.calyrsoft.ucbp1.features.profile.domain.model.value.ProfileName
import org.junit.Assert
import org.junit.Test

class ProfileValueObjectsTest {

    @Test
    fun `ProfileId acepta un string válido`() {
        val id = ProfileId("user_123")
        Assert.assertEquals("user_123", id.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `ProfileName no acepta nombre vacío`() {
        ProfileName("")
    }

    @Test
    fun `ProfileEmail acepta un correo válido`() {
        val email = ProfileEmail("test@ucb.edu.bo")
        Assert.assertEquals("test@ucb.edu.bo", email.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `ProfileEmail lanza excepción si no contiene arroba`() {
        ProfileEmail("correo_invalido")
    }

    @Test
    fun `ProfileAvatarUrl devuelve valor por defecto si es null`() {
        val avatar = ProfileAvatarUrl(null)
        Assert.assertEquals("https://example.com/default-avatar.png", avatar.orDefault())
    }
}