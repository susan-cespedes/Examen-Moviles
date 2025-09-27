package com.calyrsoft.ucbp1.features.dollar.data.datasource

import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealTimeRemoteDataSource(
    private val database: FirebaseDatabase
) {
    fun getDollarUpdates(): Flow<DollarModel> = callbackFlow {
        val ref: DatabaseReference = database.getReference("dollar")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dollarOfficialCompra = snapshot.child("dollarOfficialCompra").value?.toString()
                val dollarParallelCompra = snapshot.child("dollarParallelCompra").value?.toString()
                val dollarOfficialVenta = snapshot.child("dollarOfficialVenta").value?.toString()
                val dollarParallelVenta = snapshot.child("dollarParallelVenta").value?.toString()

                val model = DollarModel(
                    dollarOfficialCompra = dollarOfficialCompra,
                    dollarParallelCompra = dollarParallelCompra,
                    dollarOfficialVenta = dollarOfficialVenta,
                    dollarParallelVenta = dollarParallelVenta,
                    timestamp = System.currentTimeMillis()
                )

                trySend(model)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }
}