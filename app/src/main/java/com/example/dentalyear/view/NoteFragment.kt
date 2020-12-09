package com.example.dentalyear.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.database.NoteModel
import com.example.dentalyear.utils.NoteItemClickListener
import com.example.dentalyear.utils.Utility
import com.example.dentalyear.utils.Utility.Companion.formatDate
import com.example.dentalyear.view.adapter.NoteAdapter
import com.example.dentalyear.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_video.*
import java.util.*


class NoteFragment : Fragment(), NoteItemClickListener {

    companion object {
        const val MODE_TYPE = "mode_type"
        const val NOTE_DATA = "note_data"
    }

    private lateinit var adapter: NoteAdapter
    private var noteList = mutableListOf<NoteModel>()
    private var goalList = mutableListOf<NoteModel>()
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define viewModel
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // init recyclerView
        initRecyclerView()


        /*
        *
        * Init Listeners
        *
         */
        // Listen for notes
        viewModel.getAllNotes()?.observe(viewLifecycleOwner, { notes ->
            Log.d("NoteFragment", "Inside getAllNotes() 1")
            if (notes.isNullOrEmpty())
                return@observe

            Log.d("NoteFragment", "Inside getAllNotes() 2")
            // Prepare notes | get Notes and Goals
            prepareNotes(notes)

            if (fragment_note_tabLayout.selectedTabPosition == 0) {
                Log.d("NoteFragment", "Inside getAllNotes() 6")
                setAdapterData(noteList)
            } else {
                Log.d("NoteFragment", "Inside getAllNotes() 7")
                setAdapterData(goalList)
            }
        })

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition
                when (getNoteType()) {
                    Utility.NOTE_NOTES -> removeNote(noteList[position])
                    Utility.NOTE_GOALS -> removeNote(goalList[position])
                }
                adapter.removeNote(position)
                Toast.makeText(requireContext(), "Note removed Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Assign swipe listener for recycler view
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(fragment_note_recycler_view)

        // Listen for Tab layout
        fragment_note_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setAdapterData(noteList)
                    1 -> setAdapterData(goalList)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // Listen for FAB
        fragment_note_fab.setOnClickListener {
            openAddNoteActivity(
                NoteModel(
                    noteTitle = "",
                    noteContent = "",
//                    noteDate = formatDate(SimpleDateFormat("yyyy-MM-dd").parse("2014-2-10"), "MMM dd, yyyy"),
                    noteDate = formatDate(Date(), "MMM dd, yyyy"),
                    noteType = getNoteType()
                ), Utility.NOTE_CREATING_MODE
            )
        }
    }

    private fun initRecyclerView() {
        adapter = NoteAdapter(this)
        fragment_note_recycler_view.adapter = adapter
    }

    private fun prepareNotes(notes: List<NoteModel>) {
        noteList = mutableListOf()
        goalList = mutableListOf()
        Log.d("NoteFragment", "Inside getAllNotes() 3")
        for (note in notes) {
            if (note.noteType == Utility.NOTE_NOTES) {
                Log.d("NoteFragment", "Note: ${note.noteTitle}")
                noteList.add(note)
            } else if (note.noteType == Utility.NOTE_GOALS) {
                Log.d("NoteFragment", "Goal: ${note.noteTitle}")
                goalList.add(note)
            }
        }
        Log.d("NoteFragment", "noteList size: ${noteList.size}")
        Log.d("NoteFragment", "goalList size: ${goalList.size}")
    }

    private fun setAdapterData(notes: List<NoteModel>) {
        adapter.setData(notes as MutableList<NoteModel>)
    }

    private fun openAddNoteActivity(
        note: NoteModel,
        type: Int
    ) {
        val intent = Intent(requireContext(), AddNoteActivity::class.java)
        intent.putExtra(NOTE_DATA, note)
        intent.putExtra(MODE_TYPE, type)

        this.startActivity(intent)
    }

    private fun removeNote(note: NoteModel) {
        viewModel.deleteNote(note)
    }

    private fun getNoteType(): Int {
        return if (fragment_note_tabLayout.selectedTabPosition == Utility.NOTE_NOTES)
            Utility.NOTE_NOTES
        else
            Utility.NOTE_GOALS
    }

    override fun noteItemClicked(note: NoteModel) {
        openAddNoteActivity(
            note,
            Utility.NOTE_EDITING_MODE
        )
    }
}