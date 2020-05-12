package com.morcinek.omise.ui.donation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.morcinek.omise.R
import com.morcinek.omise.core.ApiViewModel
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.BlockingDialog
import com.morcinek.omise.core.extensions.longSnackbar
import com.morcinek.omise.core.extensions.observe
import com.morcinek.omise.getApi
import kotlinx.android.synthetic.main.fragment_donation.view.*
import kotlinx.android.synthetic.main.button_header.view.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


class DonationFragment : BaseFragment(R.layout.fragment_donation) {

    private val viewModel by viewModel<DonationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            confirmButton.setOnClickListener { viewModel.postData() }
            amount.apply { 
                header.setText(R.string.amount_header)
                value.setText(R.string.value_not_set)
                setOnClickListener {  }
            }
            creditCard.apply {
                header.setText(R.string.credit_card_header)
                value.setText(R.string.value_not_set)
                setOnClickListener {  }
            }
            observe(viewModel.error) { longSnackbar(it.localizedMessage ?: "") }
        }
        observe(viewModel.progress, BlockingDialog(requireContext()))
    }
}

val donationModule = module {
    viewModel { DonationViewModel(getApi()) }
}

private class DonationViewModel(private val api: DonationsApi) : ApiViewModel() {

    val data = MutableLiveData<DonationResponse>()

    fun postData() = reloadData(data) { api.postData(DonationData(100, "Tomasz", "token")) }
}

