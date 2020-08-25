package com.ivanilov.wildapp.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.adapters.GroupMenuAdapter
import com.ivanilov.wildapp.adapters.ProductMenuAdapter
import com.ivanilov.wildapp.firebase.RealtimeDatabase
import com.ivanilov.wildapp.models.Product
import com.ivanilov.wildapp.models.ProductInBasket
import com.ivanilov.wildapp.utils.InternetCheck
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.view.*


class MenuFragment : Fragment() {

    var numberSelectedGroup = 0
    var groupList: ArrayList<String>? = null
    var productList: ArrayList<Product>? = null
    var productInGroup: List<Product>? = null
    var groupAdapter: GroupMenuAdapter? = null
    var currentView: View? = null
    private var lastVisibleItemPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        progressBar.visibility = View.VISIBLE

        if ((activity as MainActivity).getGroupList().size != 0) {
            groupList = (activity as MainActivity).getGroupList()
        }

        if ((activity as MainActivity).getProductList().size != 0) {
            productList = (activity as MainActivity).getProductList()
        }


        currentView = view

        view.group_recycler_view.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)



        if (groupList == null) {
            RealtimeDatabase().getGroupList(this)
        } else {
            updateGroupAdapter(groupList!!)
        }

        view.products_recycler_view.layoutManager = LinearLayoutManager(context)

        lastVisibleItemPosition = (view.products_recycler_view.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if (!internet!!){
                    showError("Для работы приложения необходимо подключиться к интернету, пожалуйста подключитесь к сети")
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        if (groupList != null) {
            (activity as MainActivity).setGroupList(groupList!!)
        }

        if (productList != null){
            (activity as MainActivity).setProductList(productList!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("groupList", groupList)
    }

    fun groupSelected(nameGroup: String) {
        productInGroup = productList!!.filter { product -> product.group == nameGroup }
        val productAdapter = ProductMenuAdapter(productInGroup!!)

        currentView!!.products_recycler_view.adapter = productAdapter

        productAdapter.setOnClickListener(object : ProductMenuAdapter.OnClickListener {
            override fun onClick(position: Int, product: Product) {
                val cardBottomDialog = CardFragment()

                var listForAdd: ArrayList<Int> = ArrayList()
                product.add.forEach {
                    listForAdd.add(0)
                }

                val productInBasket = ProductInBasket(product, 1, listForAdd, product.cost.toInt())
                cardBottomDialog.productInBasket = productInBasket
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, cardBottomDialog, "cardBottomDialog")
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    fun updateGroupAdapter(groups: ArrayList<String>) {

        groupList = groups

        if (groupList != null) {

            if (productList == null) {
                RealtimeDatabase().getProductList(this)
            } else {
                updateProductAdapter(productList!!)
            }

            groupAdapter = GroupMenuAdapter(groupList!!)
            currentView!!.group_recycler_view.adapter = groupAdapter
            groupAdapter!!.setOnClickListener(object : GroupMenuAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    groupSelected(model)
                }
            })
        }
    }

    fun updateProductAdapter(products: ArrayList<Product>) {
        productList = products
        groupSelected(groupList!![0])
        currentView!!.progressBar.visibility = View.INVISIBLE

    }

    fun showError(textError: String) {

        val builder = AlertDialog.Builder(context)
        val alertView: View =
            layoutInflater.inflate(R.layout.fragment_dialog, null, false)

        alertView.text_dialog.text = textError
        builder.setView(alertView)
        val alert = builder.show()
        alertView.bt_ok.setOnClickListener {alert.dismiss()}


    }

}
