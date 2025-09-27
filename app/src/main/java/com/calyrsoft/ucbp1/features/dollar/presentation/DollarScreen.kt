// DollarScreen.kt
package com.calyrsoft.ucbp1.features.dollar.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.calyrsoft.ucbp1.core.presentation.components.TopAppBarWithBack
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.ui.theme.PinkPrimary
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarScreen(
    navController: NavController,
    viewModelDollar: DollarViewModel = koinViewModel()
) {
    val state by viewModelDollar.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = "Cotización del Dólar",
                navController = navController,
                actions = {
                    IconButton(
                        onClick = { viewModelDollar.loadHistory() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (val stateValue = state) {
                is DollarViewModel.DollarUIState.Error -> {
                    Text(
                        text = stateValue.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                DollarViewModel.DollarUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(32.dp)
                    )
                }

                is DollarViewModel.DollarUIState.Success -> {
                    // Card con valores actuales
                    CurrentDollarCard(dollar = stateValue.data)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Historial
                    Text(
                        text = "Historial de Cambios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DollarHistoryList(history = stateValue.history)
                }
            }
        }
    }
}



@Composable
fun DollarValueItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = color.copy(alpha = 0.8f)
        )

        Text(
            text = "$$value",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun DollarHistoryList(history: List<DollarModel>) {
    if (history.isEmpty()) {
        Text(
            text = "No hay historial disponible",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(history) { dollar ->
                DollarHistoryItem(dollar = dollar)
            }
        }
    }
}
@Composable
fun CurrentDollarCard(dollar: DollarModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = PinkPrimary,
            contentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Valor Actual",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila 1 -> Oficial y Paralelo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DollarValueItem(
                    title = "Dólar Oficial Compra",
                    value = dollar.dollarOfficialCompra ?: "N/A",
                    color = Color.White
                )

                DollarValueItem(
                    title = "Dólar Paralelo Compra",
                    value = dollar.dollarParallelCompra ?: "N/A",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila 2 -> USDT y USDC
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DollarValueItem(
                    title = "Dolar Oficial Venta",
                    value = dollar.dollarOfficialVenta ?: "N/A",
                    color = Color.White
                )

                DollarValueItem(
                    title = "Dolar Paralelo Venta",
                    value = dollar.dollarParallelVenta ?: "N/A",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha de actualización
            Text(
                text = "Actualizado: ${getCurrentTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun DollarHistoryItem(dollar: DollarModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = formatDate(dollar.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Historial con los 4 valores
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Oficial: $${dollar.dollarOfficialCompra ?: "N/A"}")
                Text(text = "Paralelo: $${dollar.dollarParallelCompra ?: "N/A"}")
                Text(text = "USDT: $${dollar.dollarOfficialVenta ?: "N/A"}")
                Text(text = "USDC: $${dollar.dollarParallelVenta ?: "N/A"}")
            }
        }
    }
}



// Funciones de utilidad
private fun getCurrentTime(): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
}

private fun formatDate(timestamp: Long): String {
    return try {
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    } catch (e: Exception) {
        "Fecha inválida"
    }
}