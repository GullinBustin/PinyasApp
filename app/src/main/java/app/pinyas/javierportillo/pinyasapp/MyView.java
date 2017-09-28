package app.pinyas.javierportillo.pinyasapp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View {
    Paint paint = new Paint();
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = -1;
    private float mPosX, mPosY;
    private JSONArray castell;
    private double[] draggedPoint = {-1,-1};
    private boolean isDragged = false;
    private String dragName = "";

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for(int i=0; castell != null && i<castell.length(); i++) {

            double x = 0,y=0,w=0,h=0,a=0;
            String name = "";

            try {
                JSONObject posicio = castell.getJSONObject(i);
                JSONArray rect = posicio.getJSONArray("rect");
                x = rect.getJSONArray(0).getDouble(0);
                y = rect.getJSONArray(0).getDouble(1);
                w = rect.getJSONArray(1).getDouble(0);
                h = rect.getJSONArray(1).getDouble(1);
                a = rect.getDouble(2);

                if(isDragged && checkRectIsTouched(draggedPoint, new double[] {x,y}, new double[]{w,h}, a)) {
                    name = dragName;
                }else{
                    JSONArray nomPos = posicio.getJSONArray("pos");
                    for (int nameNum = 0; nameNum < nomPos.length(); nameNum++) {
                        if (nameNum != 0) {
                            name += "_";
                        }
                        name += nomPos.getString(nameNum);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(w < h) {
                a+=90;
                double temp = w;
                w = h;
                h = temp;
            }
            if(a > 80) a-=180;
            if(a < -100) a+=180;

            paint.setColor(Color.YELLOW);
            //paint.setStrokeWidth(3);
            canvas.save();
            canvas.translate((float) x,(float) y);
            canvas.rotate((float) a);
            canvas.drawRect((float) (-w/2), (float) (-h/2), (float) (w/2), (float) (h/2), paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText(name, (float) (-w/2), (float) (h/2), paint);
            canvas.restore();

        }
/*
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(mPosX-25, mPosY-25, mPosX+25, mPosY+25, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(mLastTouchX-25, mLastTouchY-25, mLastTouchX+25, mLastTouchY+25, paint );
        paint.setColor(Color.YELLOW);
        canvas.drawRect(33, 33, 77, 60, paint );
*/

        paint.setColor(Color.RED);
        //canvas.drawCircle(mLastTouchX, mLastTouchY, 10, paint);
        canvas.drawCircle((float)draggedPoint[0], (float)draggedPoint[1], 10, paint);

    }


    private String checkAllRects(double[] screenPoint ){

        String touched = "nothing";


        for(int i=0; castell != null && i<castell.length(); i++) {


            try {


                JSONObject posicio = castell.getJSONObject(i);
                JSONArray rect = posicio.getJSONArray("rect");
                double[] corner = { rect.getJSONArray(0).getDouble(0),
                                    rect.getJSONArray(0).getDouble(1)};
                double[] size = {rect.getJSONArray(1).getDouble(0),
                                rect.getJSONArray(1).getDouble(1)};
                double angle = rect.getDouble(2);





                if(checkRectIsTouched(screenPoint, corner, size, angle)){
                    String name = "";

                    JSONArray nomPos = posicio.getJSONArray("pos");
                    for(int nameNum = 0; nameNum < nomPos.length(); nameNum++) {
                        if(nameNum != 0){
                            name += "_";
                        }
                        name += nomPos.getString(nameNum);

                    }

                    touched = name;
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return touched;
    }

    private boolean checkRectIsTouched(double[] screenPoint, double[] rectCorner, double[] rectSize, double rectAngle){

        if(rectSize[0] < rectSize[1]) {
            rectAngle+=90;
            double temp = rectSize[0];
            rectSize[0] = rectSize[1];
            rectSize[1] = temp;
        }

        if(rectAngle > 80) rectAngle-=180;
        if(rectAngle < -100) rectAngle+=180;

        double[] rectCenter = {rectCorner[0] , rectCorner[1]};
        double[] screenPointRotated = {Math.cos(-rectAngle*Math.PI/180.)*(screenPoint[0]-rectCenter[0]) - Math.sin(-rectAngle*Math.PI/180.)*(screenPoint[1]-rectCenter[1]),
                                       Math.sin(-rectAngle*Math.PI/180.)*(screenPoint[0]-rectCenter[0]) + Math.cos(-rectAngle*Math.PI/180.)*(screenPoint[1]-rectCenter[1])};

        boolean touched = (Math.abs(screenPointRotated[0])<rectSize[0]/2.) && (Math.abs(screenPointRotated[1])<rectSize[1]/2.);

        return touched;
    }

    public void fillCastell(JSONArray newCastell){
        castell = newCastell;
    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        Log.e("DragDrop Action", "yeah?");
        return true;
    }
*/
    @Override
    public boolean onDragEvent(DragEvent event) {
        int action = event.getAction();
        isDragged = false;
        if(action == DragEvent.ACTION_DROP ) {
            ClipData a = event.getClipData();
            if (a != null) {
                ClipData.Item b = a.getItemAt(0);
                CharSequence c = b.getText();
                String d = c.toString();
                Log.e("DragDrop Drp Item", d);
            }
        }
        if(action == DragEvent.ACTION_DRAG_LOCATION ) {
            draggedPoint = new double[]{event.getX(),event.getY()};
            isDragged = true;
            ClipDescription desc = event.getClipDescription();
            ClipData a = event.getClipData();
            if (desc != null){
                String descText = desc.getLabel().toString();
                //Log.e("DragDrop Location Desc", descText);
                //Log.e("DragDrop Location Desc", draggedPoint[0]+"   "+draggedPoint[1]);

                dragName = descText;

            }
            if (a != null) {
                ClipData.Item b = a.getItemAt(0);
                CharSequence c = b.getText();
                String d = c.toString();
                Log.e("DragDrop Location item", d);
            }
            this.invalidate();
        }
        return true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        //mScaleDetector.onTouchEvent(ev);

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);



                //Log.e("DragDrop Action", "DOWN" + mActivePointerId );

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                double[] screenPoint = {x,y};

                String touched = checkAllRects(screenPoint);

                Log.e("DragDrop Touched", touched );
                //Log.e("DragDrop Action", "MOVE" + mActivePointerId );

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = -1;
                //Log.e("DragDrop Action", "UP" + mActivePointerId );

                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
                //Log.e("DragDrop Action", "CANCEL" + mActivePointerId );

                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                //Log.e("DragDrop Action", "POINTER_UP" + mActivePointerId );

                break;
            }
        }
        return true;
    }
}
