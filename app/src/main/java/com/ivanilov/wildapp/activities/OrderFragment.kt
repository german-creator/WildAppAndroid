package com.ivanilov.wildapp.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.adapters.OrderAdapter
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import com.ivanilov.wildapp.models.Order
import com.ivanilov.wildapp.models.ProductInBasket
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_dialog_sent_order.view.*
import kotlinx.android.synthetic.main.fragment_order.*


class OrderFragment : Fragment() {

    lateinit var orderAdapter: OrderAdapter
    lateinit var basketList: ArrayList<ProductInBasket>
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        var itemListForSpinner: ArrayList<String> = arrayListOf(
            "Через...",
            "5 минут",
            "10 минут",
            "15 минут",
            "20 минут",
            "25 минут",
            "30 минут"
        )
        val adapter = context?.let {
            ArrayAdapter<String>(
                it,
                R.layout.spinner_item_order,
                itemListForSpinner
            )
        }
        adapter!!.setDropDownViewResource(R.layout.spinner_dropdown_item)

        sp_choice_time.adapter = adapter

        rv_order_item.layoutManager = LinearLayoutManager(context)
        if ((activity as MainActivity).getProductsInBasket().size != 0) {
            basketList = (activity as MainActivity).getProductsInBasket()
            orderAdapter = OrderAdapter(basketList)
            rv_order_item.adapter = orderAdapter
            updateTopLabel((activity as MainActivity).getProductsInBasket())
        }

        if (::orderAdapter.isInitialized) {
            orderAdapter.setOnClickListener(object : OrderAdapter.OnClickListener {
                override fun onClick(position: Int, product: ProductInBasket) {
                    val cardBottomDialog = CardFragment()

                    cardBottomDialog.productInBasket = product
                    cardBottomDialog.positionInBasket = position
                    cardBottomDialog.orderFragment = this@OrderFragment
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, cardBottomDialog, "cardBottomDialog")
                        .addToBackStack(null)
                        .commit()
                }
            })

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_order_item)

        bt_send_order.setOnClickListener {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_comment.windowToken, 0)

            if (auth.currentUser == null) {
                showError("Для заказа напитков необходимо зарегестрироваться")
            } else {
                if (!this::basketList.isInitialized || basketList.size == 0) {
                    showError("Добавьте напитки в заказ")
                } else {
                    if (sp_choice_time.selectedItemPosition == 0) {
                        showError("Выберите время")
                    } else {

                        var builder = AlertDialog.Builder(context)
                        val alertView: View =
                            layoutInflater.inflate(R.layout.fragment_dialog_sent_order, null, false)

                        builder.setView(alertView)
                        val dialog = builder.show()

                        alertView.bt_send.setOnClickListener {
                            val order = Order(
                                basketList,
                                et_comment.text.toString(),
                                sp_choice_time.selectedItem.toString()
                            )

                            RealtimeDatabase().pushOrderToDatabase(order)
                            rv_order_item.adapter = null


                            alertView.iv_success.animate().alpha(1.0f).setDuration(1000)
                            alertView.tv_title_dialog_send.animate().alpha(0.0f).setDuration(1000)
                            alertView.tv_message_dialog_send.animate().alpha(0.0f).setDuration(1000)
                            alertView.bt_send.animate().alpha(0.0f).setDuration(1000)

                            (activity as MainActivity).clearBasket()
                            basketList = ArrayList()
                            updateAdapter()
                            sp_choice_time.setSelection(0)
                            et_comment.setText("")

                            Handler().postDelayed({
                                dialog.dismiss()

                            }, 2000)


                        }

                    }
                }
            }
        }
    }

    fun updateAdapter() {
        if (::orderAdapter.isInitialized) {
            if (basketList.size == 0){
                orderAdapter = OrderAdapter(basketList)
            }
            orderAdapter.notifyDataSetChanged()
        }
        updateTopLabel((activity as MainActivity).getProductsInBasket())
        (activity as MainActivity).updateBadge()

    }

    fun updateTopLabel(basketList: ArrayList<ProductInBasket>) {
        var count = 0
        var cost = 0
        var costNextWord: String


        basketList.forEach {
            count += it.count
            var addCost = 0
            for (j in 0..it.add.size - 1) {
                if (it.add[j].costOption.size != 0) {
                    addCost += it.add[j].costOption[it.selectedAdd[j]].toInt() * it.count
                }
            }
            cost += it.cost.toInt() * it.count + addCost
        }


        when (count) {
            1 -> costNextWord = "напиток"
            2, 3, 4 -> costNextWord = "напитка"
            else -> costNextWord = "напитков"


        }
        tv_order_title.text = "В заказе $count $costNextWord \nна $cost ₽"
    }

    fun showError(error: String) {
        val builder = AlertDialog.Builder(context)
        val alertView: View =
            layoutInflater.inflate(R.layout.fragment_dialog, null, false)

        alertView.text_dialog.text = error
        builder.setView(alertView)
        val alert = builder.show()
        alertView.bt_ok.setOnClickListener {alert.dismiss()}

    }

    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                basketList.removeAt(viewHolder.adapterPosition)
                updateAdapter()
            }
        }


}

