package com.anjithsasindran.materialcolorpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorPickerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {


    static final int COLOR_SELECTION_COMPLETE = 1;
    static final int COLOR_SELECTION_CANCELLED = 2;
    public static String colorname = null;
    public static final String TAG = "Colorpicker: ";
    public static Integer current = null;
    View colorView;
    SeekBar hueSeekBar, satSeekBar, valueSeekBar;
    TextView hueToolTip, satToolTip, valueToolTip;
    Button buttonSelector;
    Window window;
    Display display;
    int red, green, blue, hue, sat, value, seekBarLeft;
    float[] hsv = new float[3];

     Rect thumbRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        colorname = intent.getStringExtra("Prefname");
        current = intent.getIntExtra("Current", 0);
        Log.d(TAG, "Picker Started, received " + colorname + current.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.layout_color_picker);
        } else {
            setContentView(R.layout.layout_16);
        }

        display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        red = Color.red(current);
        green = Color.green(current);
        blue = Color.blue(current);
        Color.RGBToHSV(red, green, blue, hsv);
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        Log.d(TAG, "Color converted to RGB space " + String.valueOf(red) + String.valueOf(green) + String.valueOf(blue));
        Log.d(TAG, "Color converted to HSV space " + String.valueOf(hsv[0]) + " " + String.valueOf(hsv[1]) + " " + String.valueOf(hsv[2]));
        Log.d(TAG, "HSV As integers " + hue + " " + sat + " " + value);
        colorView = findViewById(R.id.colorView);
        window = getWindow();

        hueSeekBar = (SeekBar) findViewById(R.id.hueSeekBar);
        satSeekBar = (SeekBar) findViewById(R.id.satSeekBar);
        valueSeekBar = (SeekBar) findViewById(R.id.valueSeekBar);

        seekBarLeft = hueSeekBar.getPaddingLeft();

        hueToolTip = (TextView) findViewById(R.id.hueToolTip);
        satToolTip = (TextView) findViewById(R.id.satToolTip);
        valueToolTip = (TextView) findViewById(R.id.valueToolTip);

        buttonSelector = (Button) findViewById(R.id.buttonSelector);

        hueSeekBar.setOnSeekBarChangeListener(this);
        satSeekBar.setOnSeekBarChangeListener(this);
        valueSeekBar.setOnSeekBarChangeListener(this);

        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);

        //Setting View, Status bar & button color & hex codes

        colorView.setBackgroundColor(current);
        buttonSelector.setBackgroundColor(current);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() != Surface.ROTATION_90 && display.getRotation() != Surface.ROTATION_270)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        thumbRect = hueSeekBar.getThumb().getBounds();

        hueToolTip.setX(seekBarLeft + thumbRect.left);
        if (hue < 10)
            hueToolTip.setText("  " + hue);
        else if (hue < 100)
            hueToolTip.setText(" " + hue);
        else
            hueToolTip.setText(hue + "");

        thumbRect = satSeekBar.getThumb().getBounds();

        satToolTip.setX(seekBarLeft + thumbRect.left);
        if (sat < 10)
            satToolTip.setText("  " + sat);
        else if (sat < 100)
            satToolTip.setText(" " + sat);
        else
            satToolTip.setText(sat + "");

        thumbRect = valueSeekBar.getThumb().getBounds();

        valueToolTip.setX(seekBarLeft + thumbRect.left);
        if (value < 10)
            valueToolTip.setText("  " + value);
        else if (value < 100)
            valueToolTip.setText(" " + value);
        else
            valueToolTip.setText(value + "");

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.hueSeekBar) {

            hue = progress;
            Log.d(TAG, "HueBar updated " + hue + " as float " + (float) hue);
            Log.d(TAG, "HSV was " + hsv[0]);
            hsv[0] = (float) hue;
            Log.d(TAG, "We can read hsv as " + hsv[0]);
            thumbRect = seekBar.getThumb().getBounds();

            hueToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                hueToolTip.setText("  " + hue);
            else if (progress < 100)
                hueToolTip.setText(" " + hue);
            else
                hueToolTip.setText(hue + "");

        } else if (seekBar.getId() == R.id.satSeekBar) {

            sat = ((int) progress/100);
            Log.d(TAG, "SatBar updated " + progress + " as float " + sat);
            Log.d(TAG, "HSV was " + hsv[1]);
            hsv[1] = sat;
            Log.d(TAG, "We can read hsv as " + hsv[1]);
            thumbRect = seekBar.getThumb().getBounds();

            satToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress < 10)
                satToolTip.setText("  " + sat);
            else if (progress < 100)
                satToolTip.setText(" " + sat);
            else
                satToolTip.setText(sat + "");

        } else if (seekBar.getId() == R.id.valueSeekBar) {

            value = ((int) progress/100);
            Log.d(TAG, "ValBar updated " + progress + " as float " + value);
            Log.d(TAG, "HSV was " + hsv[2]);
            hsv[2] = value;
            Log.d(TAG, "We can read hsv as " + hsv[2]);
            thumbRect = seekBar.getThumb().getBounds();

            valueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress < 10)
                valueToolTip.setText("  " + value);
            else if (progress < 100)
                valueToolTip.setText(" " + value);
            else
                valueToolTip.setText(value + "");

        }
        Log.d(TAG, "HSV Components are " + hsv[0] + " " + hsv[1] + " " + hsv[2]);
        Log.d(TAG, "Trying to set color to " + Color.HSVToColor(hsv));
        colorView.setBackgroundColor(Color.HSVToColor(hsv));
        buttonSelector.setBackgroundColor(Color.HSVToColor(hsv));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() == Surface.ROTATION_0)
                window.setStatusBarColor(Color.HSVToColor(hsv));

        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }


    public void colorSelect(View view) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Name", colorname);
        returnIntent.putExtra("Color", Color.HSVToColor(hsv));
        Log.d(TAG,"Final output - " + colorname + " " + String.valueOf(hsv[0]) + String.valueOf(hsv[1]) + String.valueOf(hsv[2]));
        Log.d(TAG,"Final output (int) - " + getIntFromColor(red, green, blue));

        setResult(COLOR_SELECTION_COMPLETE, returnIntent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(COLOR_SELECTION_CANCELLED, returnIntent);
        finish();
    }


}