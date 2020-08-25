package com.ivanilov.wildapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import com.ivanilov.wildapp.activities.CardFragment
import com.ivanilov.wildapp.models.Add
import kotlinx.android.synthetic.main.item_card_add.view.*


public class CardAddAdapter(

    private val addList: ArrayList<Add> = ArrayList(),
    private val selectedAddList: ArrayList<Int> = ArrayList(),
    private val fragment: CardFragment

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_add, parent, false)
        )

    override fun getItemCount(): Int = addList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemView = holder.itemView
        val add = addList[position]

        itemView.tv_name_add.text = add.name


        var itemListForSpinner: ArrayList<String> = ArrayList()
        addList[position].option.forEachIndexed {index, item ->
            val s = addList[position].costOption
            if (s.size != 0){
                if (s[index] != 0.toLong()){
                    itemListForSpinner.add(item + " " + s[index].toString() + " ₽")
                } else{
                    itemListForSpinner.add(item)
                }
            } else{

                itemListForSpinner.add(item)
            }
        }

        val adapter = ArrayAdapter<String>(itemView.context, R.layout.spinner_item, itemListForSpinner)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        itemView.sp_choice_add.adapter = adapter
        itemView.sp_choice_add.setSelection(selectedAddList[position])

        itemView.sp_choice_add.onItemSelectedListener   = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, spinnerPosition: Int, id: Long) {

                selectedAddList[position] = spinnerPosition
                fragment.updateAddInProduct(selectedAddList)
            }

        }


    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    }


}
