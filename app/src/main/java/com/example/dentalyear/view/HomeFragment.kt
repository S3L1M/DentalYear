package com.example.dentalyear.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dentalyear.R
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.utils.HomeTopBarClickListener
import com.example.dentalyear.utils.Status
import com.example.dentalyear.utils.Utility
import com.example.dentalyear.view.adapter.HomeAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeTopBarClickListener {

    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: MainViewModel
    private var usaPrompt: HomeModel? = null
    private var australiaPrompt: HomeModel? = null
    private var canadaPrompt: HomeModel? = null
    private val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private lateinit var datePickerBuilder: MaterialDatePicker.Builder<Long>
    private lateinit var datePicker: MaterialDatePicker<Long>
    private var currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    private val daysRemainingInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    private lateinit var currentCountry: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init recyclerView
        initRecyclerViewAdapter()

        // change view visibility | By default Invisible
        changeViewVisibility(View.INVISIBLE)

        // Define viewModel
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // get shared preference
        val prefs = requireActivity().getSharedPreferences(
            getString(R.string.string_preference_file_key),
            Context.MODE_PRIVATE
        )
        currentCountry = prefs.getString("KEY", Utility.UNITED_STATES_KEY).toString()

        // init datePicker
        initDatePicker()

        /*
        * Init Listeners
         */
        // prompts listener
        viewModel.getPrompts(formatDate(calendar.time))?.observe(viewLifecycleOwner, { prompts ->
            if (prompts != null) {
                when (prompts.status) {
                    Status.LOADING -> {
                        home_fragment_progress_bar.visibility = View.VISIBLE
                        changeViewVisibility(View.INVISIBLE)
                        Log.d("HomeFragment", "Loading")
                    }
                    Status.SUCCESS -> {
                        Log.d("HomeFragment", "Success")
                        prepareData(prompts.data!!)
                        // change visibility to visible
                        home_fragment_progress_bar.visibility = View.INVISIBLE
                        changeViewVisibility(View.VISIBLE)
                        // update date textview
                        updateDateData()
                        // Set adapter based on the country
                        setAdapterData()
                    }
                    Status.ERROR -> {
                        Log.d("HomeFragment", "Error: ${prompts.message}")
                        home_fragment_progress_bar.visibility = View.INVISIBLE
                    }
                }
            }
        })

        // Listen for date picker | positive (OK) & negative (CANCEL)
        datePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            viewModel.refreshPrompts(formatDate(Date(it)))
        }
        datePicker.addOnNegativeButtonClickListener {
            Log.d("HomeFragment", "Negative Called")
        }

        // Listen for dots image view (settings)
        home_fragment_dots_image_view.setOnClickListener {
            val intent = Intent(requireActivity(), SettingsActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun initRecyclerViewAdapter() {
        adapter = HomeAdapter(requireContext(), this)
        home_recycler_view.adapter = adapter
    }

    private fun changeViewVisibility(visibility: Int) {
        home_recycler_view.visibility = visibility
        home_fragment_title_logo.visibility = visibility
        home_fragment_dots_image_view.visibility = visibility
        home_fragmnet_dental_logo.visibility = visibility
    }

    private fun prepareData(prompts: List<HomeModel>) {
        for (prompt in prompts) {
            when (prompt.promptCountry.toLowerCase(Locale.ROOT)) {
                Utility.UNITED_STATES -> {
                    usaPrompt = prompt
                }
                Utility.AUSTRALIA -> {
                    australiaPrompt = prompt
                }
                Utility.CANADA -> {
                    canadaPrompt = prompt
                }

            }
        }
    }

    private fun setAdapterData() {
        when (currentCountry) {
            Utility.UNITED_STATES_KEY -> {
                adapter.setData(usaPrompt!!, Utility.UNITED_STATES_KEY)
            }
            Utility.CANADA_KEY -> {
                adapter.setData(canadaPrompt!!, Utility.CANADA_KEY)
            }
            Utility.AUSTRALIA_KEY -> {
                adapter.setData(australiaPrompt!!, Utility.AUSTRALIA_KEY)
            }
        }
    }

    override fun datePickerClicked() {
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        updateDateData()
    }

    override fun leftArrowClicked(imageView: ImageView) {
        val originalDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        when {
            currentDay - originalDay > 1 -> {
                calendar.add(Calendar.DATE, -1)
                viewModel.refreshPrompts(formatDate(calendar.time))
            }
            currentDay - originalDay == 1 -> {
                calendar.add(Calendar.DATE, -1)
                viewModel.refreshPrompts(formatDate(calendar.time))
            }
            else -> {
            }
        }
        datePickerBuilder.setSelection(calendar.timeInMillis)
        datePickerBuilder.build()
        updateDateData()
    }

    override fun rightArrowClicked(imageView: ImageView) {
        when {
            currentDay + 1 < daysRemainingInMonth -> {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                viewModel.refreshPrompts(formatDate(calendar.time))
            }
            currentDay + 1 == daysRemainingInMonth -> {
                calendar.add(Calendar.DATE, 1)
                viewModel.refreshPrompts(formatDate(calendar.time))
            }
            else -> {
            }
        }
        datePickerBuilder.setSelection(calendar.timeInMillis)
        datePickerBuilder.build()
        updateDateData()
    }

    private fun updateDateData() {
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        adapter.setCurrentDate(
            formatDate(calendar.time, "EEEE"),
            formatDate(calendar.time, "MMMM dd")
        )
    }

    private fun formatDate(date: Date, format: String = "yyyyMMdd"): String {
        return SimpleDateFormat(format).format(date)
    }

    private fun initDatePicker() {
        datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setTitleText("Choose Date")
        // get current date in millis
        val todayInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        // By default select the current day
        datePickerBuilder.setSelection(todayInMillis)

        /*
        *set calendar constraints
        *
        * Send todayInMillis to the lower bound and higher bound
        * to make the user choose ONLY from the current month
        *
        * Also add validator to make the first day
        * the user could choose is the current day
         */
        datePickerBuilder.setCalendarConstraints(
            setupConstraintsBuilder(
                todayInMillis,
                todayInMillis
            )
                .setValidator(MyDateValidator())
                .build()
        )
        datePicker = datePickerBuilder.build()
    }


    private fun setupConstraintsBuilder(
        boundMin: Long,
        boundMax: Long
    ): CalendarConstraints.Builder {
        val constraintsBuilder = CalendarConstraints.Builder()

        // Set the bound of the date  | today => to the end of the month
        constraintsBuilder.setStart(boundMin)
        constraintsBuilder.setEnd(boundMax)

        // Always open the picker at the current date
        constraintsBuilder.setOpenAt(boundMin)

        return constraintsBuilder
    }

    @Parcelize
    class MyDateValidator : CalendarConstraints.DateValidator {
        override fun isValid(date: Long): Boolean {
            return date >= System.currentTimeMillis()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("HomeFragment", "HERE 1")
        if (requestCode == 100) {
            Log.d("HomeFragment", "HERE 2")
            if (resultCode == Activity.RESULT_OK && data != null) {
                Log.d("HomeFragment", "HERE 3")
                currentCountry = data.getStringExtra("DATA").toString()
                Log.d("HomeFragment", "Country: $currentCountry")
                setAdapterData()
            }
        }
    }
}