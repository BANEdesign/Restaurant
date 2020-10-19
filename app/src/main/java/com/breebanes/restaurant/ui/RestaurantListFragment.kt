package com.breebanes.restaurant.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.breebanes.restaurant.R
import com.breebanes.restaurant.utils.CoroutinesDispatcherProvider
import com.breebanes.restaurant.viewmodel.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurant_list.*

class RestaurantListFragment : Fragment() {
    lateinit var viewModel: RestaurantViewModel
    lateinit var listAdapter: RestaurantListAdapter
    lateinit var navController: NavController
    var shopList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)
        viewModel = RestaurantViewModel(CoroutinesDispatcherProvider())
        listAdapter = RestaurantListAdapter(listOf())

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity?.getPreferences(MODE_PRIVATE)
        prefs?.let {
            //var set = listOf<String>()
            val set = it.getStringSet(REST_ID, setOf(""))
            if (set != null && set.size > 1) {
                shopList = set.toMutableList()
            }
        }

        viewModel.restaurantList.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE
            restaurantRecyclerView.apply {
                layoutManager = LinearLayoutManager(view.context)
                listAdapter = RestaurantListAdapter(it, shopList)
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))
                listAdapter.onItemClick = { restaurant ->
                    navController.navigate(R.id.action_fragmentSearch_to_restaurantDetailFragment,
                        bundleOf(SHOP_ID to restaurant.id))
                }
                listAdapter.onFavClick = { shop ->
                    shopList.add(shop.id)
                }
            }
        })

        viewModel.fetchRestaurants(LAT, LONG)
        progressBar.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        saveToPrefrences()
    }

    private fun saveToPrefrences() {
        activity?.let {
            //save rest id
            val prefs: SharedPreferences = it.getPreferences(MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            prefsEditor.putStringSet(REST_ID, shopList.toSet())
            prefsEditor.commit()
        }
    }

    companion object {
        const val LAT = "37.422740"
        const val LONG = "-122.139956"
        const val REST_ID = "id"
        val TAG = RestaurantListFragment::class.java.simpleName
    }
}