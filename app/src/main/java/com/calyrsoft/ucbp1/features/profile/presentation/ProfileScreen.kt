package com.calyrsoft.ucbp1.features.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.calyrsoft.ucbp1.core.AuthManager
import com.calyrsoft.ucbp1.navigation.Screen
import com.calyrsoft.ucbp1.ui.theme.PinkPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                // Avatar/Photo
                state.avatarUrl?.let { avatar ->
                    val resAvatarUrl = LocalContext.current.resources.getIdentifier(
                        avatar,
                        "drawable",
                        LocalContext.current.packageName
                        )
                    Image(
                        painter = rememberAsyncImagePainter(resAvatarUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(
                    text = "Bienvenido a Profile",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.userName,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))
               // En ProfileScreen.kt, agrega este botón después de la información del perfil:


               //  Botón para ir a la pantalla del dólar
                Button(
                    onClick = {
                        navController.navigate(Screen.Dollar.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ver Tipo de Cambio")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Esta es la pantalla de perfil. Aquí podrás ver y gestionar tu información personal.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.Movie.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ir a Movies")
                }



            }
        }
    }
}