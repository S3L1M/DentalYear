package com.example.dentalyear.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dentalyear.R
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_exhibit_details.*

@AndroidEntryPoint
class ExhibitDetailsActivity : AppCompatActivity() {
    private lateinit var exhibit: ExhibitModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibit_details)

        intent?.let {
            exhibit = it.getParcelableExtra(ExhibitFragment.EXHIBIT_KEY)!!
            Glide.with(this).load(exhibit.acf.sponsorLogo)
                .into(activity_exhibit_details_logo_image_view)
            activity_exhibit_details_description_text_view.text = exhibit.sponsorMessage
        }

        activity_exhibit_details_back_image_view.setOnClickListener {
            finish()
        }

        activity_exhibit_details_see_more_button.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(exhibit.sponsorLink))
            )
        }

        activity_exhibit_details_want_to_be_here_button.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(Utility.SPONSOR_LINK))
            )
        }

    }
}