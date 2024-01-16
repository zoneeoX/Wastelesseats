package app.wastelesseats.util

import android.content.Context
import android.os.Build
import android.service.autofill.UserData
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel(): ViewModel() {
    private val _recentlyAddedItems = MutableStateFlow<List<MarkerData>>(emptyList())
    val recentlyAddedItems: StateFlow<List<MarkerData>> = _recentlyAddedItems
    private val _categoryItems = MutableStateFlow<List<MarkerData>>(emptyList())
    val categoryItems: StateFlow<List<MarkerData>> = _categoryItems

    fun getMarkersByCategory(category: String) {
        val query = Firebase.firestore
            .collection("markers")
            .whereEqualTo("category", category)

        getMarkers(query) { markerDataList ->
            viewModelScope.launch {
                _categoryItems.value = markerDataList
            }
        }
    }

    fun getItemById(itemId: String, onDataReceived: (MarkerData?) -> Unit) {
        viewModelScope.launch {
            try {
                val documentSnapshot = Firebase.firestore
                    .collection("markers")
                    .document(itemId)
                    .get()
                    .await()

                val markerData = documentSnapshot.toObject(MarkerData::class.java)
                onDataReceived(markerData)
            } catch (e: Exception) {
                onDataReceived(null)
                // Handle exceptions if needed
            }
        }
    }

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




    fun updateStatusToPending(
        itemId: String,
        onSuccess: () -> Unit,

    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("markers")
            .document(itemId)

        try {
            fireStoreRef.update("status", "Pending")
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener { e ->
                    //
                }
        } catch (e: Exception) {
            //
        }
    }
     fun updateStatusToSold(
        itemId: String,

        ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("markers")
            .document(itemId)

        try {
            fireStoreRef.update("status", "Sold")
                .addOnSuccessListener {
                    //
                }
                .addOnFailureListener { e ->
                    //
                }
        } catch (e: Exception) {
            //
        }

    }

    fun deleteItem(
        itemId: String,

        ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("markers")
            .document(itemId)

        try {
            fireStoreRef.delete()
                .addOnSuccessListener {
                    //
                }
                .addOnFailureListener { e ->
                    //
                }
        } catch (e: Exception) {
            //
        }
    }



    fun getMarkers(query: Query, onDataReceived: (List<MarkerData>) -> Unit) {
        query.get()
            .addOnSuccessListener { querySnapshot ->
                val markerDataList = querySnapshot.toObjects<MarkerData>()
                onDataReceived(markerDataList)
            }
            .addOnFailureListener { e ->
                // Handle failure TODO later
            }
    }


    fun getRecentlyAddedItems() {
        val query = Firebase.firestore
            .collection("markers")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)

        getMarkers(query) { markerDataList ->
            viewModelScope.launch {
                _recentlyAddedItems.value = markerDataList
            }
        }
    }





    fun initiateBuy(markerData: MarkerData, onSuccess: () -> Unit) {
        updateStatusToPending(markerData.id, onSuccess = onSuccess)
    }

    fun getUserUploadedItems(
        userId: String,
        onDataReceived: (List<MarkerData>) -> Unit
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
