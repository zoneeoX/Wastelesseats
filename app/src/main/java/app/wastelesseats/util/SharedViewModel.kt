package app.wastelesseats.util

import android.content.Context
import android.os.Build
import android.service.autofill.UserData
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(): ViewModel() {
    fun saveData(
        userData: MarkerData,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("markers")
            .document(userData.id)

        try {
            fireStoreRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserUploadedItems(
        userId: String,
        onDataReceived: (List<MarkerData>) -> Unit // Callback for handling retrieved items
    ) {
        val fireStoreRef = Firebase.firestore
            .collection("markers")
            .whereEqualTo("userId", userId)

        fireStoreRef.get()
            .addOnSuccessListener { querySnapshot ->
                val uploadedItems = querySnapshot.toObjects<MarkerData>()
                onDataReceived(uploadedItems)
            }
            .addOnFailureListener { e ->
                // Handle failure TODO later
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun retrieveData(
        context: Context,
        data: (List<UserData>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("markers")

        try {
            fireStoreRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val userDataList = mutableListOf<UserData>()
                    for (document in querySnapshot.documents) {
                        val userData = document.toObject(UserData::class.java)
                        userData?.let {
                            userDataList.add(it)
                        }
                    }
                    data(userDataList)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
