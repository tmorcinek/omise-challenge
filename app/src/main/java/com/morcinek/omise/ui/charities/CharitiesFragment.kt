package com.morcinek.omise.ui.charities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import co.omise.android.ui.OmiseActivity.Companion.EXTRA_PKEY
import co.omise.android.ui.OmiseActivity.Companion.EXTRA_TOKEN_OBJECT
import com.morcinek.omise.BuildConfig
import com.morcinek.omise.R
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.extensions.loadImageWithProgressAndError
import com.morcinek.omise.core.extensions.observe
import com.morcinek.omise.core.extensions.startActivityForResult
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
                setOnClickListener {
                    startActivityForResult<CreditCardActivity> { putExtra(EXTRA_PKEY, BuildConfig.PKEY) }
                }
            }.apply {
                observe(viewModel.data) {
                    submitList(it.data)
                    view.progressBar.hide()
                }
            }
        }
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

private class CharitiesViewModel(val summaryApi: CharitiesApi) : ViewModel() {

    val data = liveData(Dispatchers.IO) { emit(summaryApi.getData()) }
}

