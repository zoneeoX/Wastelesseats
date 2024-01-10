package app.wastelesseats.presentation

sealed class MapEvent {
    data object ToggleDarkMode: MapEvent()
}