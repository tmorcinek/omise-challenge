package com.morcinek.omise.ui.donation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import co.omise.android.ui.OmiseActivity
import com.morcinek.omise.BuildConfig
import com.morcinek.omise.R
import com.morcinek.omise.core.ApiViewModel
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.BlockingDialog
import com.morcinek.omise.core.extensions.*
import com.morcinek.omise.getApi
import com.morcinek.omise.ui.lazyNavController
import kotlinx.android.synthetic.main.button_header.view.*
import kotlinx.android.synthetic.main.fragment_donation.view.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


class DonationFragment : BaseFragment(R.layout.fragment_donation) {

    private val viewModel by viewModel<DonationViewModel>()

    private val navController by lazyNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            nameTextInputLayout.editText?.apply {
                setText(viewModel.name.value)
                doOnTextChanged { text, _, _, _ -> viewModel.name.postValue(text.toString()) }
            }
            amount.apply {
                header.setText(R.string.amount_header)
                setOnClickListener {
                    singleChoiceSelector(
                        R.string.amount_header,
                        viewModel.amount.value,
                        resources.getIntArray(R.array.Donations).asList(),
                        { getString(R.string.amount_value, this) },
                        { viewModel.amount.postValue(it) }
                    )
                }
                observe(viewModel.amount) { value.text = getString(R.string.amount_value, it) }
            }
            creditCard.apply {
                header.setText(R.string.card_number_header)
                value.setText(R.string.card_number_empty)
                setOnClickListener { startActivityForResult<CreditCardActivity> { putExtra(OmiseActivity.EXTRA_PKEY, BuildConfig.PKEY) } }
                observe(viewModel.token) { it?.card?.let { card -> value.text = getString(R.string.card_number_value, card.lastDigits) } }
            }
            confirmButton.apply {
                setOnClickListener { viewModel.postData() }
                observe(viewModel.isButtonEnabled) { isEnabled = it }
            }
            observe(viewModel.error) { longSnackbar(it.localizedMessage ?: "") }
            observe(viewModel.response) { if (it.success) navController.navigateUp() else longSnackbar(it.error_message) }
        }
        observe(viewModel.progress, BlockingDialog(requireContext()))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> data?.getParcelableExtra<Token>(OmiseActivity.EXTRA_TOKEN_OBJECT)?.let { viewModel.token.postValue(it) }
        }
    }
}

val donationModule = module {
    viewModel { DonationViewModel(getApi()) }
}

private class DonationViewModel(private val api: DonationsApi) : ApiViewModel() {

    val amount = MutableLiveData(0)
    val name = MutableLiveData("")
    val token = MutableLiveData<Token?>(null)

    val response = MutableLiveData<DonationResponse>()

    val isButtonEnabled = combine(name.map { it.isNotBlank() }, amount.map { it > 0 }, token.map { it != null }).map { it.first && it.second && it.third }

    fun postData() = requestData(response) { api.postData(DonationRequest(amount.value!!, name.value!!, token.value!!.id!!)) }
}
