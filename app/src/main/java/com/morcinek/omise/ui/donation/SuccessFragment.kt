package com.morcinek.omise.ui.donation

import android.os.Bundle
import android.view.View
import com.morcinek.omise.R
import com.morcinek.omise.core.BaseFragment
import com.morcinek.omise.core.extensions.getParcelable
import com.morcinek.omise.core.extensions.loadImageWithProgressAndError
import com.morcinek.omise.ui.charities.CharityData
import com.morcinek.omise.ui.lazyNavController
import kotlinx.android.synthetic.main.fragment_success.view.*


class SuccessFragment : BaseFragment(R.layout.fragment_success) {

    private val navController by lazyNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            getParcelable<CharityData>().let {
                successMessage.text = getString(R.string.success_message, it.name)
                loadImageWithProgressAndError(image, it.logo_url)
            }
            confirmButton.setOnClickListener { navController.navigateUp() }
        }
    }
}
