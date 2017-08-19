package app.pinyas.javierportillo.pinyasapp

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.pinyeros_event_item.view.*

import java.util.ArrayList
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN



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
                itemView.pinyero_name.text = this.name



                //itemView.pinyero_name.setOnClickListener { itemClick(pinyeros_event) }


                itemView.setOnLongClickListener({ v ->
                    // Create a new ClipData.
                    // This is done in two steps to provide clarity. The convenience method
                    // ClipData.newPlainText() can create a plain text ClipData in one step.
                    itemClick(pinyeros_event)
                    // Create a new ClipData.Item from the ImageView object's tag

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    val dragData = ClipData.newPlainText ("uno","dos")

                    // Instantiates the drag shadow builder.
                    val myShadow = View.DragShadowBuilder(itemView.pinyero_name);

                    // Starts the drag

                    v.startDrag(dragData, // the data to be dragged
                            myShadow, // the drag shadow builder
                            null, // no need to use local data
                            0          // flags (not currently used, set to 0)
                    )
                })


                val mDragListen = canvasOnDrag()





                //itemView.setOnTouchListener(ChoiceTouchListener())
                //itemView.setOnDragListener(mDragListen)
            }
        }
    }
}