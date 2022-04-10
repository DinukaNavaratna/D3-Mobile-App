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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nenasa.Nenasa;
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
    String number = "";
    String type = "";
    int time_spent = 0;
    String level_name = "";
    Nenasa nenasa = new Nenasa();
    String treatment = "false";
    String treatment_suffix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent();
        if (myIntent.hasExtra("letter")) {
            number = myIntent.getStringExtra("letter");
            type = "letter";
            level_name = "Easy";
        } else if (myIntent.hasExtra("word")) {
            number = myIntent.getStringExtra("word");
            type = "word";
            level_name = "Medium";
        } else {
            Nenasa nenasa = new Nenasa();
            nenasa.showDialogBox(this, "error", "Type Error", "System couldn't detect the type of the tracing!", "redirect", new Intent(this, Home.class), treatment);
        }

        treatment = myIntent.getStringExtra("treatment").toString();
        if(treatment == "true")
            treatment_suffix = "_treatment";

        setContentView(R.layout.dysgraphia_tracing);
        letters_filled = (ImageView)findViewById(R.id.letters_filled);
        letters_transparent = (ImageView)findViewById(R.id.letters_transparent);
        String uri_filled = "@drawable/"+type+"_"+number+"_filled";
        int imageResource_filled = getResources().getIdentifier(uri_filled, null, getPackageName());
        letters_filled.setImageResource(imageResource_filled);
        String uri = "@drawable/"+type+"_"+number;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        letters_transparent.setImageResource(imageResource);
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

        long maxCounter;
        if(treatment == "true") {
            maxCounter = 60000;
        } else {
            maxCounter = 600000;
        }
        long diff = 1000;
        new CountDownTimer(maxCounter, diff) {
            public void onTick(long millisUntilFinished) {
                long diff = maxCounter - millisUntilFinished;
                time_spent++;
                int countdown = (int)(millisUntilFinished/1000);
                if(countdown == 10) {
                    Toast.makeText(getApplicationContext(),"Time left: 10 seconds",Toast.LENGTH_SHORT).show();
                }
                if(countdown == 5) {
                    Toast.makeText(getApplicationContext(),"Time left: 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
            public void onFinish() {
                time_spent++;
                finishTracing("time");
            }
        }.start();
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
        Intent intent = openHome();
        this.startActivity(intent);
        finish();
    }

    private Intent openHome(){
        Intent myIntent;
        if (type == "letter")
            myIntent = new Intent(this, Level_01.class);
        else if (type == "word")
            myIntent = new Intent(this, Level_02.class);
        else
            myIntent = new Intent(this, Home.class);
        return myIntent;
    }

    public void finishTracing(View view){
        finishTracing("");
    }

    public void finishTracing(String error){
        float per = ((float)transparent_count/((float)transparent_count+(float)painted_count))*100;
        final int coins = (int) (per / 10);
        SharedPreference sp = new SharedPreference(this);
        String user_id = sp.getPreference("user_id");
        try {
            sp.setPreference("dysgraphia_"+type+"_"+number+treatment_suffix, Integer.toString((int)per));
            int score_type =  Integer.parseInt(sp.getPreference("dysgraphia_score_"+type+treatment_suffix)) + coins;
            sp.setPreference("dysgraphia_score_"+type+treatment_suffix, Integer.toString(score_type));
            int score =  Integer.parseInt(sp.getPreference("dysgraphia_score"+treatment_suffix)) + coins;
            sp.setPreference("dysgraphia_score"+treatment_suffix, Integer.toString(score));
            HTTP http = new HTTP(this, this);
            //http.request("/update_scores","{\"userid\":\""+ user_id +"\", \"game\":\"dysgraphia\", \"score\":\""+ score +"\"}");
            http.request(
                    "/insert_scores",
                    "{\"user_id\":\"" + user_id + "\", \"game\":\"dysgraphia\", \"score\":\""+ coins +"\", \"query\":\"INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('"
                            +user_id+"','"+level_name+"', "+time_spent+",'"+per+"','"+level_name+"_"+number+"',"+coins+")\"}"
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Intent intent = openHome();
        if(error == "time"){
            nenasa.showDialogBox(this, "info", "Ran Out of Time", "Coins: " + coins + "\nScore: " + (int) per + "%\nDuration: " + time_spent + " seconds", "redirect", intent, treatment);
        } else {
            nenasa.showDialogBox(this, "info", "Your Score", "Coins: " + coins + "\nScore: " + (int) per + "%\nDuration: " + time_spent + " seconds", "redirect", intent, treatment);
        }
    }

    @Override
    public void onBackPressed() {}
}