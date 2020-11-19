package com.example.newyorkbusinesses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newyorkbusinesses.R
import com.example.newyorkbusinesses.model.Businesse
import com.example.newyorkbusinesses.utils.loadAsyncImage
import kotlinx.android.synthetic.main.business_card_item.view.*

class HomeAdapter(val clickListener: (Businesse) -> Unit) :
    PagedListAdapter<Businesse, HomeAdapter.DataViewHolder>(diffCallback) {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(businesse: Businesse?) {
            businesse?.let {data ->
                itemView.apply {
                    name.text = data.name
                    category.text = data.categories.first().title
                    price.text = data.price
                    review_count.text = data.reviewCount.toString()
                    item_cover.loadAsyncImage(data.imageUrl)
                }
                itemView.setOnClickListener { clickListener(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.business_card_item, parent, false)
        )

    override fun getItemCount(): Int = super.getItemCount()

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Businesse>() {
            override fun areItemsTheSame(oldItem: Businesse, newItem: Businesse): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Businesse, newItem: Businesse): Boolean =
                oldItem == newItem
        }
    }


}
