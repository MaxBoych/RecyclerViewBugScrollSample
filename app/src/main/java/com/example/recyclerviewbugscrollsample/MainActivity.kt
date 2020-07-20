package com.example.recyclerviewbugscrollsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var contentAdapter: ParentAdapter

    private var contentSections: MutableList<Pair<String, List<String>>?> = mutableListOf(
        null, null, null, null, null, null, null, null, null, null
        /*Pair("Section0", mutableListOf()),
        Pair("Section1", mutableListOf()),
        Pair("Section2", mutableListOf()),
        Pair("Section3", mutableListOf()),
        Pair("Section4", mutableListOf()),
        Pair("Section5", mutableListOf()),
        Pair("Section6", mutableListOf()),
        Pair("Section7", mutableListOf()),
        Pair("Section8", mutableListOf()),
        Pair("Section9", mutableListOf())*/
    )

    //private var items: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRV()
    }

    private fun setupRV() {

        setRVData()

        parentRV.apply {
            val llManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            contentAdapter = ParentAdapter(
                mutableListOf(),
                context
            )

            contentAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    if (positionStart == 0) {
                        llManager.scrollToPosition(0);
                    }
                }
            })

            layoutManager = llManager

            adapter = contentAdapter

            isNestedScrollingEnabled = false
            //setHasFixedSize(true)

            val recyclerItemDecoration = RecyclerItemDecoration(
                context,
                resources.getDimensionPixelSize(R.dimen.header_height),
                true,
                getSectionCallback(contentSections)
            )
            addItemDecoration(recyclerItemDecoration)
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            contentAdapter.updateData(contentSections)
            parentRV.layoutManager?.scrollToPosition(0)
            contentAdapter.notifyDataSetChanged()
            /*for (i in contentSections.size..1) {
                contentAdapter.notifyItemChanged(i)
            }*/
        }
    }

    private fun setRVData() {
        /*contentSections[0].second.addAll(listOf())
        contentSections[1].second.addAll(listOf())
        contentSections[2].second.addAll(listOf())
        contentSections[3].second.addAll(listOf())
        contentSections[4].second.addAll(listOf())
        contentSections[5].second.addAll(generate12items())
        contentSections[6].second.addAll(listOf())
        contentSections[7].second.addAll(listOf())
        contentSections[8].second.addAll(generate12items())
        contentSections[9].second.addAll(generate12items())*/


        contentSections[0] = Pair("Section 0", listOf())
        contentSections[1] = Pair("Section 1", listOf())
        contentSections[2] = Pair("Section 2", listOf())
        contentSections[3] = Pair("Section 3", listOf())
        contentSections[4] = Pair("Section 4", listOf())
        contentSections[5] = Pair("Section 5", generate12items())
        contentSections[6] = Pair("Section 6", listOf())
        contentSections[7] = Pair("Section 7", listOf())
        contentSections[8] = Pair("Section 8", generate12items())
        contentSections[9] = Pair("Section 9", generate12items())
    }

    private fun generate12items(): List<String> {
        val list: MutableList<String> = mutableListOf()
        for (i in 0..11) {
            list.add("Element $i")
        }
        return list
    }

    private fun getSectionCallback(list: MutableList<Pair<String, List<String>>?>): SectionCallback? {
        return object : SectionCallback {

            override fun isSection(pos: Int): Boolean {
                return pos == 0 || contentSections[pos]?.first !== contentSections[pos - 1]?.first
            }

            override fun getSectionHeaderName(pos: Int): String? {
                return list[pos]?.first
            }

            override fun getSectionItemsAmount(pos: Int): Int? {
                return list[pos]?.second?.size
            }
        }
    }
}