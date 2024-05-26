package com.example.mycoffee.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mycoffee.BaseDatos
import com.example.mycoffee.R
import com.example.mycoffee.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHandler: BaseDatos

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHandler = BaseDatos(requireContext())

        loadProducts()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadProducts() {
        val productosList = dbHandler.productos
        val productContainer = binding.productContainer

        productContainer.removeAllViews()  // Limpiar cualquier vista anterior

        if (productosList.isNotEmpty()) {
            for (producto in productosList) {
                val productView = LayoutInflater.from(requireContext()).inflate(R.layout.product_item, productContainer, false)

                val productName = productView.findViewById<TextView>(R.id.product_name)
                val productDescription = productView.findViewById<TextView>(R.id.product_description)
                val productPrice = productView.findViewById<TextView>(R.id.product_price)
                val productImage = productView.findViewById<ImageView>(R.id.product_image)

                productName.text = producto.nombre
                productDescription.text = producto.descripcion
                productPrice.text = producto.precio.toString()

                productContainer.addView(productView)
            }
        } else {
            val noProductView = TextView(requireContext())
            noProductView.text = "No hay productos"
            productContainer.addView(noProductView)
        }
    }
}