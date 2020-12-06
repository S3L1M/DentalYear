package com.example.dentalyear.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.dentalyear.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity()
//    , AdapterView.OnItemSelectedListener
{
    private var selectedCountry: String = ""
    private lateinit var prefs: SharedPreferences
    val countryList: List<String?> = listOf(
        "United States",
        "Canada",
        "Australia"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences(
            getString(R.string.string_preference_file_key),
            Context.MODE_PRIVATE
        )

        initSpinner()
        activity_settings_back_image_view.setOnClickListener {
            handleBackButton()
        }
    }

    private fun initSpinner() {

        activity_settings_spinner_country
            .setSelection(
                countryList.indexOf(
                    prefs.getString(HomeFragment.COUNTRY_KEY, countryList[0])
                )
            )

        activity_settings_spinner_country!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    // Save the result to sharedPreference
                    with(prefs.edit()) {
                        putString(HomeFragment.COUNTRY_KEY, countryList[position])
                        apply()

                    }
                    selectedCountry = countryList[position].toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        activity_settings_spinner_country!!.item = countryList as List<Nothing>
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handleBackButton()
    }

    private fun handleBackButton() {
        val intent = Intent()
        intent.putExtra(HomeFragment.SELECTED_COUNTRY_RESULT, selectedCountry)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}