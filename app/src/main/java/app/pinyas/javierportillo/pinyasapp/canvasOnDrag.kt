package app.pinyas.javierportillo.pinyasapp

import android.annotation.SuppressLint
import android.widget.Toast
import android.support.v4.graphics.drawable.DrawableCompat.clearColorFilter
import android.view.DragEvent
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnDragListener
import kotlinx.android.synthetic.main.pinyeros_event_item.view.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN




/**
 * Created by Javier on 31/07/2017.
 */

class canvasOnDrag : View.OnDragListener {

    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    override fun onDrag(v: View, event: DragEvent): Boolean {


        val action = event.action

        // Handles each of the expected events
        when (action) {

            DragEvent.ACTION_DRAG_STARTED -> {

                Log.e("DragDrop Action", "ACTION_DRAG_STARTE")

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false
            }

            DragEvent.ACTION_DRAG_ENTERED -> {

                // Applies a green tint to the View. Return true; the return value is ignored.

                Log.e("DragDrop Action", "ACTION_DRAG_ENTERED")


                return true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {


                Log.e("DragDrop Action", "ACTION_DRAG_LOCATION")

                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {

                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                Log.e("DragDrop Action", "ACTION_DRAG_EXITED")


                return true
            }

            DragEvent.ACTION_DROP -> {

                val item = event.clipData.getItemAt(0)

                // Gets the text data from the item.
                val dragData = item.text
                // Gets the item containing the dragged data
                Log.e("DragDrop Action", "ACTION_DROP")

                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {

                // Turns off any color tinting
                Log.e("DragDrop Action", "ACTION_DRAG_ENDED")

                // returns true; the value is ignored.
                return true
            }

        // An unknown action type was received.
            else -> Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
        }

        return false
    }
};


/**
 * ChoiceTouchListener will handle touch events on draggable views

 */
class ChoiceTouchListener : View.OnTouchListener {

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            /*
             * Drag details: we only need default behavior
             * - clip data could be set to pass data as part of drag
             * - shadow can be tailored
             */
            val data = ClipData.newPlainText("43535", "345345345")
            val shadowBuilder = View.DragShadowBuilder(view)
            //start dragging the item touched
            view.startDrag(data, shadowBuilder, null, 0)
            return true
        } else {
            return false
        }
    }
}