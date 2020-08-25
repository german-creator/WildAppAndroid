package com.ivanilov.wildapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.activities.OrderFragment
import com.ivanilov.wildapp.models.ProductInBasket
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_order.view.*


class OrderAdapter(

    private val productList: List<ProductInBasket>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OrderAdapter.OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemView = holder.itemView
        val productInBasket = productList[position]

        itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, productInBasket)
            }
        }

        itemView.tv_name.text = productInBasket.name

        val imageView = itemView.iv_photo

        Picasso.get().load(productList[position].imageUrl).into(imageView);

        itemView.tv_add_0.text = ""
        itemView.tv_add_1.text = ""
        itemView.tv_add_2.text = ""
        itemView.tv_add_3.text = ""

        var addTvList: ArrayList<TextView> = ArrayList()

        addTvList.add(itemView.tv_add_0)
        addTvList.add(itemView.tv_add_1)
        addTvList.add(itemView.tv_add_2)
        addTvList.add(itemView.tv_add_3)

        var addCost: Int = 0

        productInBasket.selectedAdd.forEachIndexed { index, item ->
            when (index) {
                0 -> if (item != 0) {
                    addTvList[0].text = productInBasket.add[0].option[item]
                    if (productInBasket.add[index].costOption.size != 0)  addCost += productInBasket.add[index].costOption[item].toInt() * productInBasket.count

                }

                1 -> if (item != 0) {
                    if (productInBasket.add[index].costOption.size != 0)  addCost += productInBasket.add[index].costOption[item].toInt() * productInBasket.count

                    if (addTvList[0].text == "") {
                        addTvList[0].text = productInBasket.add[index].option[item]
                    } else {
                        addTvList[1].text = productInBasket.add[index].option[item]
                    }
                }

                2 -> if (item != 0) {
                    if (productInBasket.add[index].costOption.size != 0)  addCost += productInBasket.add[index].costOption[item].toInt() * productInBasket.count

                    if (addTvList[0].text == "") {
                        addTvList[0].text = productInBasket.add[index].option[item]
                    } else {
                        if (addTvList[1].text == "") {
                            addTvList[1].text = productInBasket.add[index].option[item]
                        } else {
                            addTvList[2].text = productInBasket.add[index].option[item]
                        }
                    }
                }

                3 -> if (item != 0) {
                    if (productInBasket.add[index].costOption.size != 0)  addCost += productInBasket.add[index].costOption[item].toInt() * productInBasket.count

                    if (addTvList[0].text == "") {
                        addTvList[0].text = productInBasket.add[index].option[item]
                    } else {
                        if (addTvList[1].text == "") {
                            addTvList[1].text = productInBasket.add[index].option[item]
                        } else {
                            if (addTvList[2].text == "") {
                                addTvList[2].text = productInBasket.add[index].option[item]
                            } else {
                                addTvList[3].text = productInBasket.add[index].option[item]
                            }
                        }
                    }
                }
            }
        }

        if (productInBasket.count != 1) {
            itemView.tv_count.visibility = View.VISIBLE
            itemView.tv_count.text = productInBasket.count.toString() + " шт"
            itemView.tv_cost.text = ((productInBasket.cost.toInt() + addCost)* productInBasket.count).toString() + " ₽"
        } else {
            itemView.tv_count.visibility = View.GONE
            itemView.tv_cost.text = (productInBasket.cost.toInt() + addCost).toString() + " ₽"

        }



    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    interface OnClickListener {
        fun onClick(position: Int, product: ProductInBasket)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


}
