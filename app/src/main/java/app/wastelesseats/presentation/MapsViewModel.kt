package app.wastelesseats.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.type.LatLng
import androidx.lifecycle.LiveData
import app.wastelesseats.util.MarkerData

class MapsViewModel: ViewModel() {

    var state by mutableStateOf(MapState())
    private val _markersFromFirestore = MutableLiveData<List<MarkerData>>()
    val markersFromFirestore: LiveData<List<MarkerData>> = _markersFromFirestore

    fun updateMarker(updatedMarker: MarkerData) {
        val currentMarkers = _markersFromFirestore.value.orEmpty().toMutableList()
        val index = currentMarkers.indexOfFirst { it.id == updatedMarker.id }

        if (index != -1) {
            currentMarkers[index] = updatedMarker
            _markersFromFirestore.value = currentMarkers
        }
    }


    fun setMarkers(markers: List<MarkerData>) {
        _markersFromFirestore.value = markers
    }

    fun onEvent(event: MapEvent){
        when(event){
            is MapEvent.ToggleDarkMode -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if(state.isDarkMode)(
                            MapStyleOptions(MapStyle.json)
                        ) else MapStyleOptions(MapStyle.jsonDark),
                    ),
                    isDarkMode = !state.isDarkMode
                )
            }
        }
    }
}