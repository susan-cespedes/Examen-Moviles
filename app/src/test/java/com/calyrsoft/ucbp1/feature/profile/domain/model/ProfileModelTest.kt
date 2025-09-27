import com.calyrsoft.ucbp1.features.profile.domain.model.Profile
import com.calyrsoft.ucbp1.features.profile.domain.model.value.*
import org.junit.Assert.*
import org.junit.Test

class ProfileModelTest {

    @Test
    fun `Profile se construye correctamente con value objects válidos`() {
        val profile = Profile(
            id = ProfileId("user_123"),
            name = ProfileName("Juan Perez"),
            email = ProfileEmail("juan@ucb.edu.bo"),
            avatarUrl = ProfileAvatarUrl("https://img.com/avatar.jpg")
        )

        assertEquals("user_123", profile.id.value)
        assertEquals("Juan Perez", profile.name.value)
        assertEquals("juan@ucb.edu.bo", profile.email.value)
        assertEquals("https://img.com/avatar.jpg", profile.avatarUrl.value)
    }
}
