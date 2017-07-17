package app.pinyas.javierportillo.pinyasapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pinyeros_event_item.view.*

import java.util.ArrayList

internal class PinyeroEventAdapter (private val arrayList: ArrayList<PinyeroEvent>,
                                    private val context: Context,
                                    val itemClick: (PinyeroEvent) -> Unit)
    : RecyclerView.Adapter<PinyeroEventAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinyeroEventAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.pinyeros_event_item, parent, false)
        return ViewHolder(v, itemClick)
    }

    override fun onBindViewHolder(holder: PinyeroEventAdapter.ViewHolder, position: Int) {
        holder.bindForecast(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(view: View, val itemClick: (PinyeroEvent) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindForecast(pinyeros_event: PinyeroEvent) {
            with(pinyeros_event) {
                itemView.pinyero_name.text = pinyeros_event.name
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}