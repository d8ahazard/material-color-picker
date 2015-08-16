package com.anjithsasindran.materialcolorpicker;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.anjithsasindran.materialcolorpicker.R;

public class XColorPickerPreference extends Preference {
    private int myTheme;
    private ImageView myView;

    public XColorPickerPreference(Context context) {
        super(context);
    }

    public XColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onBindView(View rootView) {
        super.onBindView(rootView);
        myView = (ImageView) rootView.findViewById(R.id.button);
        setMyColor(myTheme);
    }

    public void setMyColor(int themecolor) {
        myTheme = themecolor;
        Log.d("XColorPicker: ", "Color passed " + myTheme);
        if (myView != null) {
            Log.d("XColorPicker: ", "It works!" + " " + myView + " and color of " + myTheme);
            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(10);
            sd.setIntrinsicWidth(10);
            sd.getPaint().setColor(myTheme);
            myView.setBackground(sd);
        } else {
            Log.d("XColorPicker: ", "Close, but no cigar!" + " " + myView + " and color of " + myTheme);

        }
    }
}
