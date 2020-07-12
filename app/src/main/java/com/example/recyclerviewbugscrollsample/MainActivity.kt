package com.example.recyclerviewbugscrollsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var contentSections: MutableList<Pair<String, List<String>?>?> = mutableListOf(
        null, null, null, null, null, null, null, null, null, null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRV()
    }

    private fun setupRV() {

        setRVData()

        parentRV.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            val contentAdapter = ParentAdapter(
                contentSections,
                context
            )
            adapter = contentAdapter

            isNestedScrollingEnabled = false

            val recyclerItemDecoration = RecyclerItemDecoration(
                context,
                resources.getDimensionPixelSize(R.dimen.header_height),
                true,
                getSectionCallback(contentSections)
            )
            addItemDecoration(recyclerItemDecoration)
        }
    }

    private fun setRVData() {
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

    private fun getSectionCallback(list: MutableList<Pair<String, List<String>?>?>): SectionCallback? {
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