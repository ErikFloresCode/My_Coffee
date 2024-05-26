package com.example.mycoffee

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DataBaseActivity : AppCompatActivity() {
    var dbHandler:BaseDatos?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_base)
        /*variables y funciones para lugares*/
        val buttoncrear= findViewById<Button>(R.id.button_crear)
        val buttonlistar=findViewById<Button>(R.id.button_listar)
        //val Texto=findViewById<EditText>(R.id.listas)
        val textViewLugares = findViewById<TextView>(R.id.texto_box)
        val nombre=findViewById<EditText>(R.id.text_nombre)
        val descripcion=findViewById<EditText>(R.id.text_descripcion)
        val latitud=findViewById<EditText>(R.id.text_latitud)
        val longitud=findViewById<EditText>(R.id.text_longitud)
        val eliminar = findViewById<Button>(R.id.button_eliminar)


        buttoncrear.setOnClickListener(){
            dbHandler = BaseDatos(this)
            var success: Boolean = false
            val lugares: Lugares = Lugares()
            lugares.nombre = nombre.text.toString()
            lugares.descripcion = descripcion.text.toString()
            lugares.latitud =latitud.text.toString().toDouble()
            lugares.longitud = longitud.text.toString().toDouble()
            success = dbHandler?.addLugar(lugares) as Boolean

        }

        buttonlistar.setOnClickListener() {
            val listTasks = dbHandler?.lugar ?: emptyList()

            if (listTasks.isNotEmpty()) {
                val lugaresTexto = StringBuilder()
                for (lugares in listTasks) {
                    Log.d("Datos", "---> ${lugares.nombre}")
                    lugaresTexto.append("Nombre: ${lugares.nombre}\n")
                    lugaresTexto.append("Descripción: ${lugares.descripcion}\n")
                    lugaresTexto.append("Latitud: ${lugares.latitud}\n")
                    lugaresTexto.append("Longitud: ${lugares.longitud}\n\n")
                }
                textViewLugares.text = lugaresTexto.toString()
            } else {
                Toast.makeText(this, "No hay lugares para mostrar", Toast.LENGTH_LONG).show()
                textViewLugares.text = ""
            }
        }

        eliminar.setOnClickListener {
            val deleted = dbHandler?.deleteAllLugares() // Aquí pasas el ID del lugar que quieres eliminar
            if (deleted == true) {
                Toast.makeText(this, "Se elimino todo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
        /*Variables y funciones para productos*/
        val buttonCrearProducto = findViewById<Button>(R.id.button_crearproducto)
        val buttonListarProducto = findViewById<Button>(R.id.button_listarproducto)
        val textViewProductos = findViewById<TextView>(R.id.texto_boxproductos)
        val nombreProducto = findViewById<EditText>(R.id.text_nombreproducto)
        val descripcionProducto = findViewById<EditText>(R.id.text_descripcionproducto)
        val precioProducto = findViewById<EditText>(R.id.text_precio)
        val statusProducto = findViewById<EditText>(R.id.text_status)
        val eliminarProducto = findViewById<Button>(R.id.button_eliminarproducto)

        buttonCrearProducto.setOnClickListener(){
            dbHandler = BaseDatos(this)
            var success: Boolean = false
            val producto: Products = Products()
            producto.nombre = nombreProducto.text.toString()
            producto.descripcion = descripcionProducto.text.toString()
            producto.precio = precioProducto.text.toString().toFloat()
            producto.status = statusProducto.text.toString().toInt()
            success = dbHandler?.addProduct(producto) as Boolean

            if (success) {
                Toast.makeText(this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al crear producto", Toast.LENGTH_SHORT).show()
            }
        }

        buttonListarProducto.setOnClickListener {
            val listTasks = dbHandler?.productos ?: emptyList()

            if (listTasks.isNotEmpty()) {
                val productosTexto = StringBuilder()
                for (producto in listTasks) {
                    Log.d("Datos", "---> ${producto.nombre}")
                    productosTexto.append("Nombre: ${producto.nombre}\n")
                    productosTexto.append("Descripción: ${producto.descripcion}\n")
                    productosTexto.append("Precio: ${producto.precio}\n")
                    productosTexto.append("Status: ${producto.status}\n\n")
                }
                textViewProductos.text = productosTexto.toString()
            } else {
                Toast.makeText(this, "No hay productos para mostrar", Toast.LENGTH_LONG).show()
                textViewProductos.text = ""
            }
        }

        eliminarProducto.setOnClickListener {
            val deleted = dbHandler?.deleteAllProducts()
            if (deleted == true) {
                Toast.makeText(this, "Se eliminaron todos los productos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar productos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}