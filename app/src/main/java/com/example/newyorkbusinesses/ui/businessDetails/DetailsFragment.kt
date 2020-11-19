package com.example.newyorkbusinesses.ui.businessDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import arrow.core.Either
import com.example.newyorkbusinesses.R
import com.example.newyorkbusinesses.model.BusinessDetails
import com.example.newyorkbusinesses.utils.hideView
import com.example.newyorkbusinesses.utils.loadAsyncImage
import com.example.newyorkbusinesses.utils.showView
import org.koin.android.viewmodel.ext.android.viewModel


class DetailsFragment: Fragment() {


    private lateinit var imageView: ImageView
    private lateinit var name: TextView
    private lateinit var category: TextView
    private lateinit var price: TextView
    private lateinit var reviewCount: TextView
    private lateinit var rate: TextView
    private lateinit var address: TextView
    private lateinit var phone: TextView
    private lateinit var frame: FrameLayout

    private lateinit var loading: Group
    private lateinit var errorView: Group

    private val detailsViewModel: DetailsViewModel by viewModel<DetailsViewModel>()

    private val businessId: String? by lazy {
        arguments?.getString(BUISNESS_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.details_fragment, container, false)
        initView(view)
        setHasOptionsMenu(false)
        return view
    }

    private fun initView(view: View) {
        with(view){
            imageView = findViewById(R.id.header_image)
            name = findViewById(R.id.info_name)
            category = findViewById(R.id.info_category)
            price = findViewById(R.id.info_price)
            reviewCount = findViewById(R.id.info_review_count)
            rate = findViewById(R.id.info_rating)
            address = findViewById(R.id.info_address)
            loading = findViewById(R.id.details_loading)
            errorView = findViewById(R.id.error_view)
            phone = findViewById(R.id.phone)
            frame = findViewById(R.id.frame)

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callDetails()
        observeDetails()
    }


    override fun onDestroy() {
        super.onDestroy()
        setHasOptionsMenu(true)
    }

    private fun callDetails(){
        loading.showView()
        businessId?.let {
            detailsViewModel.getDetails(it)
            return
        }
    }

    private fun observeDetails(){
        detailsViewModel.getLiveDetails().observe(this, Observer {
            loading.hideView()
            when(it){
                is Either.Right ->{
                    it.b.t?.let {data ->
                        handleRightSide(data)
                    }
                }
                is Either.Left ->{
                    handleLeftSide()
                }
            }
        })
    }

    private fun handleLeftSide() {
       errorView.showView()
    }

    private fun handleRightSide(data: BusinessDetails) {
        errorView.hideView()
        imageView.loadAsyncImage(data.imageUrl)
        name.text = getString(R.string.name , data.name)
        category.text = getString(R.string.category , data.categories.first().alias)
        price.text = getString(R.string.price , data.price)
        rate.text = getString(R.string.rating , data.rating.toString())
        reviewCount.text = getString(R.string.review_count , data.reviewCount.toString())
        address.text = getString(R.string.name , data.location.address1)

        data.phone?.let {
            frame.showView()
            phone.text = getString(R.string.call , data.phone)
            phone.setOnClickListener { callPhoneNumber(data.phone) }
        }
    }

    private fun callPhoneNumber(num: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$num")
        startActivity(intent)
    }




    companion object{
        const val TAG = "DetailsFragment"
        const val BUISNESS_KEY = "business_key"
    }

}