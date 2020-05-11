package com.morcinek.omise.ui.charities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.morcinek.omise.R
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.extensions.loadImageWithProgressAndError
import com.morcinek.omise.core.extensions.observe
import com.morcinek.omise.core.itemCallback
import com.morcinek.omise.core.listAdapter
import com.morcinek.omise.getApi
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.vh_charity.view.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


class CharitiesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by viewModel<CharitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.progressBar.show()
        view.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter(R.layout.vh_charity, itemCallback { areItemsTheSame { t1, t2 -> t1.id == t2.id } }) { _, item: CharityData ->
                title.text = item.name
                loadImageWithProgressAndError(image, item.logo_url)
            }.apply {
                observe(viewModel.data) {
                    submitList(it.data)
                    view.progressBar.hide()
                }
            }
        }
    }
}

val charitiesModule = module {
    viewModel { CharitiesViewModel(getApi()) }
}

private class CharitiesViewModel(val summaryApi: CharitiesApi) : ViewModel() {

    val data = liveData(Dispatchers.IO) { emit(summaryApi.getData()) }
}

