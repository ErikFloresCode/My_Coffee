package com.example.mycoffee.ui.locations

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mycoffee.BaseDatos
import com.example.mycoffee.Lugares
import com.example.mycoffee.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    var dbHandler: BaseDatos?=null

    private val callback = OnMapReadyCallback { googleMap ->
        // Obtenemos los datos de la base de datos
        val dbHandler = BaseDatos(requireContext())
        val listTasks: List<Lugares> = dbHandler.lugar
        for(lugares in listTasks) {
            Log.d("Datos", "--->" + lugares.nombre)
            if(lugares.latitud != null && lugares.longitud != null) {
                val position = LatLng(lugares.latitud, lugares.longitud)
                googleMap.addMarker(MarkerOptions().position(position).title(lugares.nombre))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f)) // 14 es el nivel de zoom
            } else {
                Log.d("Datos", "Latitud o Longitud no válidos para " + lugares.nombre)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}