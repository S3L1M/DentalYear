package com.example.dentalyear.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dentalyear.R
import com.example.dentalyear.data.model.ExhibitModel
import kotlinx.android.synthetic.main.activity_exhibit_details.*

class ExhibitDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibit_details)

        intent?.let {
            val data = it.getParcelableExtra<ExhibitModel>(ExhibitFragment.EXHIBIT_KEY)
            Glide.with(this).load(data?.acf?.sponsorLogo)
                .into(activity_exhibit_details_logo_image_view)
            activity_exhibit_details_description_text_view.text = data?.sponsorMessage
        }

        activity_exhibit_details_back_image_view.setOnClickListener {
            finish()
        }
    }
}