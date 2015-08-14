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
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    TextView redToolTip, greenToolTip, blueToolTip;
    Button buttonSelector;
    Window window;
    Display display;
    int red, green, blue, seekBarLeft;
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
        Log.d(TAG, "Color converted to " + String.valueOf(red) + String.valueOf(green) + String.valueOf(blue));

        colorView = findViewById(R.id.colorView);
        window = getWindow();

        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView) findViewById(R.id.redToolTip);
        greenToolTip = (TextView) findViewById(R.id.greenToolTip);
        blueToolTip = (TextView) findViewById(R.id.blueToolTip);

        buttonSelector = (Button) findViewById(R.id.buttonSelector);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

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

        thumbRect = redSeekBar.getThumb().getBounds();

        redToolTip.setX(seekBarLeft + thumbRect.left);
        if (red < 10)
            redToolTip.setText("  " + red);
        else if (red < 100)
            redToolTip.setText(" " + red);
        else
            redToolTip.setText(red + "");

        thumbRect = greenSeekBar.getThumb().getBounds();

        greenToolTip.setX(seekBarLeft + thumbRect.left);
        if (green < 10)
            greenToolTip.setText("  " + green);
        else if (red < 100)
            greenToolTip.setText(" " + green);
        else
            greenToolTip.setText(green + "");

        thumbRect = blueSeekBar.getThumb().getBounds();

        blueToolTip.setX(seekBarLeft + thumbRect.left);
        if (blue < 10)
            blueToolTip.setText("  " + blue);
        else if (blue < 100)
            blueToolTip.setText(" " + blue);
        else
            blueToolTip.setText(blue + "");

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = seekBar.getThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                redToolTip.setText("  " + red);
            else if (progress < 100)
                redToolTip.setText(" " + red);
            else
                redToolTip.setText(red + "");

        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress < 10)
                greenToolTip.setText("  " + green);
            else if (progress < 100)
                greenToolTip.setText(" " + green);
            else
                greenToolTip.setText(green + "");

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress < 10)
                blueToolTip.setText("  " + blue);
            else if (progress < 100)
                blueToolTip.setText(" " + blue);
            else
                blueToolTip.setText(blue + "");

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));
        buttonSelector.setBackgroundColor(Color.rgb(red, green, blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() == Surface.ROTATION_0)
                window.setStatusBarColor(Color.rgb(red, green, blue));

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

    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param   Number  h       The hue
     * @param   Number  s       The saturation
     * @param   Number  l       The lightness
     * @return  Array           The RGB representation
     */
    public int hslToRgb(int h,int  s,int l){

        int r;
        int g;
        int b;

        if(s == 0){
            r = g = b = l; // achromatic
        }else{


            int q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            int p = 2 * l - q;
            r = hue2rgb(p, q, h + 1/3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1/3);
        }

        return (Color.rgb((Math.round(r * 255)), (Math.round(g * 255)), (Math.round(b * 255))));
    }

    public int hue2rgb(int p,int q,int t){
        if(t < 0) t += 1;
        if(t > 1) t -= 1;
        if(t < 1/6) return (p + (q - p) * 6 * t);
        if(t < 1/2) return q;
        if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
        return p;
    }

    public void colorSelect(View view) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Name", colorname);
        returnIntent.putExtra("Color", getIntFromColor(red, green, blue));
        Log.d(TAG,"Final output - " + colorname + " " + red + " " + green + " " + blue);
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