package com.ivanilov.wildapp.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.adapters.CardAddAdapter
import com.ivanilov.wildapp.models.ProductInBasket
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_buttom_fragment.*

class CardFragment() : BottomSheetDialogFragment() {

    var productInBasket: ProductInBasket? = null
    var positionInBasket: Int = 100
    var orderFragment: OrderFragment? = null

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentView = inflater.inflate(R.layout.card_buttom_fragment, container, false)

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_add.layoutManager = LinearLayoutManager(context)

        Picasso.get().load(productInBasket!!.imageUrl).into(iv_product_image);
        tv_name.text = productInBasket!!.name
        tv_description.text = productInBasket!!.description
        tv_count.text = productInBasket!!.count.toString()

        val addAdapter = CardAddAdapter(productInBasket!!.add, productInBasket!!.selectedAdd, this)
        rv_add.adapter = addAdapter

        bt_send_order.text = productInBasket!!.cost.toString() + " ₽"
        (activity as MainActivity).nav_view.visibility = View.GONE

        bt_back.setOnClickListener {
            dismiss()
        }

        bt_plu.setOnClickListener {
            productInBasket!!.count += 1
            tv_count.text = productInBasket!!.count.toString()
            updateCost()

        }
        bt_minus.setOnClickListener {
            if (productInBasket!!.count != 1) {
                productInBasket!!.count -= 1
                tv_count.text = productInBasket!!.count.toString()
                updateCost()
            }
        }
        bt_send_order.setOnClickListener {

            if (orderFragment != null) {
                (activity as MainActivity).updateProductInBasket(
                    productInBasket!!,
                    positionInBasket
                )
                orderFragment!!.updateAdapter()
            } else {
                (activity as MainActivity).addProductToBasket(productInBasket!!)
            }
            dismiss()

        }
    }

    fun updateAddInProduct(selectedListAdd: ArrayList<Int>) {
        productInBasket!!.selectedAdd = selectedListAdd
        updateCost()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun updateCost() {
        var addCost = 0
        productInBasket!!.selectedAdd.forEachIndexed() { index, item ->
            if (item != 0 && productInBasket!!.add[index].costOption.size != 0) {
                addCost += (productInBasket!!.add[index].costOption[item]).toInt()
            }
        }

        bt_send_order.text =
            ((productInBasket!!.cost + addCost).toInt() * productInBasket!!.count).toString() + " ₽"

    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).nav_view.visibility = View.VISIBLE
    }


}