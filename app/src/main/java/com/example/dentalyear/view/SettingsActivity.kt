package com.example.dentalyear.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.dentalyear.R
import com.example.dentalyear.utils.Utility
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var selectedCountry: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs =
            this.getSharedPreferences(
                getString(R.string.string_preference_file_key),
                Context.MODE_PRIVATE
            )
        selectedCountry = prefs.getString("KEY", Utility.UNITED_STATES_KEY).toString()
        activity_settings_spinner_dropdown.setSelection(getSelectionIndex())

        ArrayAdapter.createFromResource(
            this,
            R.array.country_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activity_settings_spinner_dropdown.adapter = adapter
        }
        activity_settings_spinner_dropdown.onItemSelectedListener = this

        activity_settings_back_image_view.setOnClickListener {
            Log.d("HomeFragment", "Back button pressed")
            // Save the result to sharedPreference
            with(prefs.edit()) {
                putString("KEY", selectedCountry)
                apply()
            }
            val intent = Intent()
            intent.putExtra("DATA", selectedCountry)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun getSelectionIndex(): Int {
        return when (selectedCountry) {
            Utility.UNITED_STATES_KEY -> 0
            Utility.CANADA_KEY -> 1
            Utility.AUSTRALIA_KEY -> 2
            else->0
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedCountry = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}