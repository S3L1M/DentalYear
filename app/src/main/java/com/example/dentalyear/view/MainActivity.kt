package com.example.dentalyear.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.dentalyear.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var nav: MeowBottomNavigation
    private lateinit var mHomeTextView: TextView
    private lateinit var mNotesTextView: TextView
    private lateinit var mExhibitsTextView: TextView
    private lateinit var mVideoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()

        // get TextViews by IDs
        mHomeTextView = findViewById(R.id.tv_home)
        mNotesTextView = findViewById(R.id.tv_notes)
        mExhibitsTextView = findViewById(R.id.tv_exhibits)
        mVideoTextView = findViewById(R.id.tv_video)

        // Build nav bar
        nav = findViewById(R.id.bottom_nav)
        nav.add(MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24))
        nav.add(MeowBottomNavigation.Model(2, R.drawable.ic_baseline_edit_24))
        nav.add(MeowBottomNavigation.Model(3, R.drawable.ic_baseline_shopping_cart_24))
        nav.add(MeowBottomNavigation.Model(4, R.drawable.ic_baseline_play_arrow_24))

        // onClickMenuListener event
        nav.setOnClickMenuListener {
            when (it.id) {
                1 -> showHome()
                2 -> showNotes()
                3 -> showExhibits()
                4 -> showVideo()
            }
        }

        // Select Home
        nav.show(1)
        setSelectedTextColor(mHomeTextView)

    }

    private fun showHome() {
        setSelectedTextColor(mHomeTextView)

        // Inflate Home fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<HomeFragment>(R.id.nav_host_fragment)
        }
    }

    private fun showNotes() {
        setSelectedTextColor(mNotesTextView)

        // Inflate Notes fragment
    }

    private fun showExhibits() {
        setSelectedTextColor(mExhibitsTextView)

        // Inflate Exhibits fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ExhibitFragment>(R.id.nav_host_fragment)
        }
    }

    private fun showVideo() {
        setSelectedTextColor(mVideoTextView)

        // Inflate Video fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<VideoFragment>(R.id.nav_host_fragment)
        }
    }

    private fun setSelectedTextColor(tv: TextView) {
        val defaultColor: Int = getColor(android.R.color.tab_indicator_text)
        mHomeTextView.setTextColor(defaultColor)
        mNotesTextView.setTextColor(defaultColor)
        mExhibitsTextView.setTextColor(defaultColor)
        mVideoTextView.setTextColor(defaultColor)
        tv.setTextColor(getColor(R.color.colorNavSelected))
    }
}