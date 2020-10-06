package com.breebanes.restaurant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.breebanes.restaurant.R
import com.breebanes.restaurant.utils.CoroutinesDispatcherProvider
import com.breebanes.restaurant.viewmodel.RestaurantViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_restaurant_detail.view.*

const val SHOP_ID = "shopId"

class RestaurantDetailFragment : Fragment() {

    lateinit var viewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = RestaurantViewModel(CoroutinesDispatcherProvider())
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.restaurant.observe(viewLifecycleOwner, Observer { restaurant ->
            view.progressBar.visibility = View.GONE
            Glide
                .with(view)
                .load(restaurant.coverImgUrl)
                .placeholder(ContextCompat.getDrawable(view.context, R.drawable.ic_launcher_foreground))
                .into(view.detailImageView)
            view.detailName.text = restaurant.name
            view.detailDescription.text = restaurant.description
            view.detailDeliveryFee.text = "Fee $"+"${restaurant.deliveryFee}"
        })

        arguments?.let { bundle ->
            val orderId = bundle.getString(SHOP_ID, "")
            viewModel.fetchRestaurantById(orderId)
            view.progressBar.visibility = View.VISIBLE
        }
    }
}