package com.ivanilov.wildapp.ui.menu

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import kotlinx.android.synthetic.main.item_group_menu.view.*


class GroupMenuAdapter(

    private val groupList: MutableList<String> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_menu, parent, false)
        )

    override fun getItemCount(): Int = groupList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemView = holder.itemView

        itemView.setOnClickListener {
            selectedItem = position
            this.notifyDataSetChanged()
        }
        if (position == selectedItem) {
            holder.itemView.setBackgroundResource(R.drawable.shape_menu_selected)
            holder.itemView.textView.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorLead
                )
            )
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_menu_unselected)
            holder.itemView.textView.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorDarkGrey
                )
            )
        }
        holder.itemView.textView.text = groupList[position]
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

}
