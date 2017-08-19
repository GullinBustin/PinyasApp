package app.pinyas.javierportillo.pinyasapp

import android.content.ClipData
import android.content.ClipDescription
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.pinyeros_event_item.view.*
import org.json.JSONObject
import android.view.View.DragShadowBuilder
import android.view.MotionEvent
import android.annotation.SuppressLint
import android.view.View.OnTouchListener




class MainFragment : Fragment() {

    private var pinyrosEventList: ArrayList<PinyeroEvent>? = null
    private var pinyerosEventAdapter: PinyeroEventAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        pinyrosEventList = ArrayList<PinyeroEvent>()
        pinyerosEventAdapter = PinyeroEventAdapter(pinyrosEventList!!, this.context)  { pinyeroEvent ->

        }

        val driver_events = rootView.findViewById<RecyclerView>(R.id.pinyeros_list)
        driver_events.adapter = pinyerosEventAdapter
        driver_events.layoutManager = LinearLayoutManager(this.activity)

        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = "pinyas/pinyeros/"

        apiController.get(path) { response ->
            val dataArray = response!!.getJSONArray("data")
            for (i in 0..(dataArray!!.length() - 1)) {
                val pinyeroTemp = PinyeroEvent()
                pinyeroTemp.name = dataArray.getJSONObject(i).getString("name")
                pinyrosEventList!!.add(pinyeroTemp)

            }

        }

        val canvas = rootView.findViewById<MyView>(R.id.myView3)
        val filepath = "files/tres.json"

        apiController.get(filepath) { response ->
            Log.e("DragDrop Action", response.toString())
            canvas.fillCastell(response!!.getJSONArray("file"))

        }

        val mDragListen = canvasOnDrag()





       // canvas.setOnDragListener(mDragListen)

        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    }
}
