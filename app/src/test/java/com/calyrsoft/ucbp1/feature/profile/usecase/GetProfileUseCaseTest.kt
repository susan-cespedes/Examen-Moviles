import com.calyrsoft.ucbp1.features.profile.domain.model.Profile
import com.calyrsoft.ucbp1.features.profile.domain.model.value.*
import com.calyrsoft.ucbp1.features.profile.domain.repository.ProfileRepository
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GetProfileUseCaseTest {

    private val fakeRepository = object : ProfileRepository {
        override fun getProfile() = flowOf(
            Result.success(
                Profile(
                    id = ProfileId("user_123"),
                    name = ProfileName("Demo"),
                    email = ProfileEmail("demo@ucb.edu.bo"),
                    avatarUrl = ProfileAvatarUrl(null)
                )
            )
        )
    }

    @Test
    fun `GetProfileUseCase retorna perfil exitoso`() = runBlocking {
        val useCase = GetProfileUseCase(fakeRepository)
        val result = useCase().first() // recolectamos el primer valor
        assertTrue(result.isSuccess)
        assertEquals("Demo", result.getOrNull()?.name?.value)
    }
}
