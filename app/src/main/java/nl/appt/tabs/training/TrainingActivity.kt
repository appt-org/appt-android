package nl.appt.tabs.training

import android.os.Bundle
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import nl.appt.R
import nl.appt.adapters.headerAdapterDelegate
import nl.appt.adapters.itemAdapterDelegate
import nl.appt.databinding.ViewListBinding
import nl.appt.extensions.addItemDecoration
import nl.appt.extensions.setText
import nl.appt.extensions.setTitle
import nl.appt.model.Course
import nl.appt.tabs.training.actions.ActionsActivity
import nl.appt.tabs.training.gestures.GesturesActivity
import nl.appt.widgets.TextActivity
import nl.appt.widgets.ToolbarActivity

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
class TrainingActivity : ToolbarActivity() {

    private lateinit var binding: ViewListBinding

    override fun getToolbarTitle() = getString(R.string.tab_training)

    override fun getLayoutId() = R.layout.view_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ViewListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onViewCreated()
        setListAdapter()
    }

    private fun setListAdapter(){
        val adapter = ListDelegationAdapter(
            headerAdapterDelegate(),
            itemAdapterDelegate<Course> { course ->
                when (course) {
                    Course.TALKBACK_GESTURES -> {
                        startActivity<GesturesActivity>()
                    }
                    Course.TALKBACK_ENABLE -> {
                        startActivity<TextActivity> {
                            setTitle(getString(R.string.talkback_enable_title))
                            setText(getString(R.string.talkback_enable_text))
                        }
                    }
                    Course.TALKBACK_ACTIONS -> {
                        startActivity<ActionsActivity>()
                    }
                }
            }
        )
        adapter.items = gestures
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration()
    }

    companion object {
        private val gestures = listOf(
            "TalkBack",
            Course.TALKBACK_GESTURES,
            Course.TALKBACK_ENABLE,
            Course.TALKBACK_ACTIONS
        )
    }
}