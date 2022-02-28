package com.nenasa.dysgraphia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nenasa.R;
import com.nenasa.Services.HTTP;
import com.nenasa.Services.SharedPreference;

public class Tracing extends Activity {

    DrawingView drawingView ;
    RelativeLayout DrawView;
    private Paint mPaint;
    ImageView letters_transparent, letters_filled;
    int transparent_count = 0;
    int painted_count = 0;
    int touch_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent();
        String letter = myIntent.getStringExtra("letter");

        setContentView(R.layout.dysgraphia_tracing);
        letters_filled = (ImageView)findViewById(R.id.letters_filled);
        letters_transparent = (ImageView)findViewById(R.id.letters_transparent);
        if (letter.equals("1")){
            letters_filled.setImageResource(R.drawable.letter_23_filled);
            letters_transparent.setImageResource(R.drawable.letter_23);
        } else if (letter.equals("2")){
            letters_filled.setImageResource(R.drawable.letter_40_filled);
            letters_transparent.setImageResource(R.drawable.letter_40);
        } else if (letter.equals("3")){
            letters_filled.setImageResource(R.drawable.letter_7_filled);
            letters_transparent.setImageResource(R.drawable.letter_7);
        }
        DrawView = (RelativeLayout) findViewById(R.id.DrawView);
        drawingView = new DrawingView(this);
        DrawView.addView(drawingView);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            //Log.d("start xy==>", x+","+y);
        }
        private void touch_move(float x, float y) {
            //Log.d("move xy==>", x+","+y);
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if ((dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            /*  circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);*/
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            //Log.d("end xy", mX+","+mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            letters_transparent.setDrawingCacheEnabled(true);
            letters_transparent.buildDrawingCache();
            Bitmap bm = letters_transparent.getDrawingCache();
            int color = bm.getPixel((int) event.getX(), (int) event.getY());
            if (color == Color.TRANSPARENT) {
                transparent_count++;
                //Log.d("color", "Transparent");
            } else {
                painted_count++;
                //Log.d("color", "Painted: "+color);
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_count++;
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    float per = ((float)transparent_count/((float)transparent_count+(float)painted_count))*100;
                    Log.d("Counts", "\nTouchCount: "+String.valueOf(touch_count)+"\ntCount: "+String.valueOf(transparent_count)+"\npCount: "+String.valueOf(painted_count)+"\nPercentage: "+String.valueOf(per));
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    public void openHome(View view){
        Intent myIntent = new Intent(this, Home.class);
        this.startActivity(myIntent);
        finish();
    }

    public void finishTracing(View view){
        float per = ((float)transparent_count/((float)transparent_count+(float)painted_count))*100;
        final int coins = (int) (per / 10);
        Toast.makeText(getApplicationContext(),"Score: "+String.valueOf(Math.round(per))+"%\nCoins: "+String.valueOf(coins),Toast.LENGTH_SHORT).show();
        try {
            SharedPreference sp = new SharedPreference(this);
            int score =  Integer.parseInt(sp.getPreference("dysgraphia_score")) + coins;
            sp.setPreference("dysgraphia_score", Integer.toString(score));
            HTTP http = new HTTP(this, this);
            http.request("/update_scores","{\"user_id\":\""+ sp.getPreference("user_id") +"\", \"game\":\"dysgraphia\", \"score\":\""+ score +"\"}");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Intent myIntent = new Intent(this, Home.class);
        this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {}
}