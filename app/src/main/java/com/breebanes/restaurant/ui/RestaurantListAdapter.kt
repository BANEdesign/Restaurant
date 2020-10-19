package com.breebanes.restaurant.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breebanes.restaurant.R
import com.breebanes.restaurant.dataaccess.Restaurant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import java.util.*

class RestaurantListAdapter(private val shops: List<Restaurant>,
                            private val favShops: List<String>? = null) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    var onItemClick: ((Restaurant) -> Unit)? = null
    var onFavClick: ((Restaurant) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int = shops.count()

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(shops[position])
    }

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
                itemView.setOnClickListener {
                    onItemClick?.invoke(shops[adapterPosition])
                }
                itemView.favoriteButton.setOnClickListener {
                    onFavClick?.invoke(shops[adapterPosition])
                }
            }
        val imageView: ImageView = itemView.restaurantImageView
        val name: TextView = itemView.itemName
        val description: TextView = itemView.itemDescription
        val deliveryFee: TextView = itemView.deliveryFee
        val status: TextView = itemView.status
        val favoriteButton: TextView = itemView.favoriteButton

        fun bind(shop: Restaurant) {
            Glide
                .with(itemView)
                .load(shop.coverImgUrl)
                .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ic_launcher_foreground))
                .into(imageView)
            name.text = shop.name
            description.text = shop.description
            deliveryFee.text = "Fee $" + "${shop.deliveryFee}"
            if (shop.status.toLowerCase(Locale.US).contains("closed"))
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            status.text = shop.status
            favoriteButton.apply {
                if (favShops != null
                    && favShops.contains(shops[adapterPosition].id)) {
                    text = "Unfavorite"
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.design_default_color_primary_dark))
                }
            }
        }
    }
}