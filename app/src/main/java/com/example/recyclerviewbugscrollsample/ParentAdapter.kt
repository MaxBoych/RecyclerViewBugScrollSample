package com.example.recyclerviewbugscrollsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ParentAdapter(
    private var sections: List<Pair<String, List<String>>?>,
    private val context: Context
) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    fun updateData(data: List<Pair<String, List<String>>?>) {
        sections = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.section_content_layout, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.sectionRV)
        private val noContentLayout: RelativeLayout = itemView.findViewById(R.id.no_content_layout)
        private val noContent: TextView = itemView.findViewById(R.id.no_content_lbl)

        fun bind(items: List<String>?) {
            if (items == null || items.isEmpty()) {
                recyclerView.visibility = View.GONE
                noContentLayout.visibility = View.VISIBLE
                noContent.text = "No content"
            } else {
                recyclerView.visibility = View.VISIBLE
                noContentLayout.visibility = View.GONE
            }

            setupRecyclerView(recyclerView, items)
        }

        private fun setupRecyclerView(recyclerView: RecyclerView, items: List<String>?) {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                if (items != null) {
                    val userContentItemAdapter = ChildSectionAdapter(
                        items,
                        this@ParentAdapter.context
                    )
                    adapter = userContentItemAdapter
                }
                setRecycledViewPool(viewPool)

                val lineDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
                val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                lineDivider?.let { itemDecoration.setDrawable(it) }
                addItemDecoration(itemDecoration)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]

        section?.second.let { holder.bind(it?.toList()) }
    }

    override fun getItemCount(): Int {
        return sections.size
    }
}