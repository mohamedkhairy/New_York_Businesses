package com.example.newyorkbusinesses.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.newyorkbusinesses.R
import com.example.newyorkbusinesses.adapter.HomeAdapter
import com.example.newyorkbusinesses.model.Businesse
import com.example.newyorkbusinesses.ui.businessDetails.DetailsFragment
import com.example.newyorkbusinesses.ui.businessDetails.DetailsFragment.Companion.BUISNESS_KEY
import com.example.newyorkbusinesses.ui.main.MainActivity
import com.example.newyorkbusinesses.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class BusinessFragment: Fragment() {

    private val viewModel: BusinessViewmodel by viewModel<BusinessViewmodel>()
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: Group
    private lateinit var loading: Group
    private lateinit var loading2: ProgressBar
    private lateinit var retry: Button





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.business_fragment, container, false)
        setHasOptionsMenu(true)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        with(view) {
            recyclerView = findViewById(R.id.home_rv)
            loading = findViewById<Group>(R.id.home_loading)
            loading2 = findViewById(R.id.item_progress)
            emptyView = findViewById(R.id.search_empty)
            retry = findViewById(R.id.retry)
        }
        loading2.showView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        businessObserver()
        requestStateObserver()
        setListener()
    }

    private fun setListener() {
        retry.setOnClickListener {
            viewModel.refreshAllList()
        }
    }


    private fun configureRecyclerView(){
        adapter = HomeAdapter(){
            gotoBusinessDetails(it)
        }
        recyclerView.adapter = adapter
    }

    private fun gotoBusinessDetails(businesse: Businesse){
        (activity as MainActivity).addFragment(lazy{
            val detailsFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putString(BUISNESS_KEY , businesse.id)
            detailsFragment.arguments = bundle
            detailsFragment },
            DetailsFragment.TAG)
    }

    private fun businessObserver() {
        viewModel.getLiveBusinessList()?.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun requestStateObserver() {
        viewModel.getLiveRequestState().observe(this, Observer {

            when(it){
                RequestState.RUNNING ->{
                    loading2.showView()
                    emptyView.hideView()

                }

                RequestState.SUCCESS ->{
                    recyclerView.showView()
                    loading2.hideView()
                    emptyView.hideView()
                }

                RequestState.FAILED ->{
                    loading2.hideView()
                    emptyView.showView()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        configureMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun configureMenu(menu: Menu) {

        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.onSearchTextChange {
            loading2.showView()
            viewModel.searchForBusiness(it)
        }
    }

    companion object{
        const val TAG = "BusinessFragment"
    }
}