package com.morcinek.omise.ui.charities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import co.omise.android.models.Token
import co.omise.android.ui.OmiseActivity.Companion.EXTRA_TOKEN_OBJECT
import com.morcinek.omise.R
import com.morcinek.omise.core.ApiViewModel
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.extensions.loadImageWithProgressAndError
import com.morcinek.omise.core.extensions.longSnackbar
import com.morcinek.omise.core.extensions.observe
import com.morcinek.omise.core.itemCallback
import com.morcinek.omise.core.listAdapter
import com.morcinek.omise.getApi
import com.morcinek.omise.ui.lazyNavController
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.vh_charity.view.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


class CharitiesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by viewModel<CharitiesViewModel>()

    private val navController by lazyNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            refreshLayout.apply {
                observe(viewModel.progress) { isRefreshing = it }
                setOnRefreshListener { viewModel.reloadData() }
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = listAdapter(R.layout.vh_charity, itemCallback { areItemsTheSame { t1, t2 -> t1.id == t2.id } }) { _, item: CharityData ->
                    title.text = item.name
                    loadImageWithProgressAndError(image, item.logo_url)
                    setOnClickListener { navController.navigate(R.id.nav_donation) }
                }.apply {
                    observe(viewModel.data) { submitList(it.data) }
                }
            }
            observe(viewModel.error) { longSnackbar(it.localizedMessage ?: "") }
        }
        viewModel.reloadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> data?.getParcelableExtra<Token>(EXTRA_TOKEN_OBJECT)?.let {}
        }
    }
}

val charitiesModule = module {
    viewModel { CharitiesViewModel(getApi()) }
}

private class CharitiesViewModel(private val api: CharitiesApi) : ApiViewModel() {

    val data = MutableLiveData<CharitiesData>()

    fun reloadData() = requestData(data) { api.getData() }
}

