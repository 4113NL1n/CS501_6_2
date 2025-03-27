package com.example.c6_2

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.collections.firstOrNull
import kotlin.collections.forEach

@Composable
fun MainScreen(modifier: Modifier, location : Location){

    val defaultLocation = remember { mutableStateOf(LatLng(location.latitude, location.longitude)) }
    val customLocation = remember { mutableStateOf(mutableStateListOf<LatLng>()) }
    val customGonLocation = remember { mutableStateOf(mutableStateListOf<LatLng>()) }

    val polylineColor = remember { mutableStateOf(Color.Blue) }
    val polygonColor = remember { mutableStateOf(Color.Green) }
    val polyLineWidth = remember { mutableStateOf(8f) }


    val isItPolyline = remember { mutableStateOf(true) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation.value, 12f)
    }

    LaunchedEffect(Unit) {
        if(customLocation.value.isEmpty()){
            customLocation.value.add(defaultLocation.value)
            customGonLocation.value.add(defaultLocation.value)
        }
    }
    Column (
        modifier = modifier.fillMaxHeight()
    ){
        GoogleMap(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                if(isItPolyline.value){
                    customLocation.value.add(latLng)
                }else{
                    customGonLocation.value.add(latLng)
                }
            }
        ) {
            Polyline(
                points = customLocation.value.toList(),
                clickable = true,
                color = polylineColor.value,
                width =  polyLineWidth.value)

            Polygon(
                points = customGonLocation.value.toList(),
                clickable = true,
                fillColor = polygonColor.value,
                strokeColor = polylineColor.value,
                strokeWidth = polyLineWidth.value

            )
            customLocation.value.forEach { latlng ->
                Marker(
                    state = MarkerState(position = latlng)
                )
            }
        }
        Button(onClick = {
            isItPolyline.value = false
        }) {
            Text(text = "Polygon")
        }
        Button(onClick = {
            isItPolyline.value = true
        }) {
            Text(text = "Polyline")
        }


    }

}