package com.ivanilov.wildapp.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*


class ProductMenuAdapter(

    private val productList: MutableList<Product> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemView = holder.itemView

        itemView.setOnClickListener {
        }

        itemView.nameTextView.text = productList[position].name
        itemView.descriptionTextView.text = productList[position].description

        val imageView = itemView.imageViewPhoto

        Picasso.get().load(productList[position].imageUrl).into(imageView);

    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

}
