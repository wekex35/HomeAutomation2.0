package com.digiads.akshhomeautomation.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.digiads.akshhomeautomation.R;

import org.json.JSONArray;

public class devicetype1 extends LinearLayout {
    private TextView buttonName;
    private Switch toggleswitch;
    JSONArray buttons;
    int mSpinnerValues;
    private static boolean showall = false;
    private static int values = 1;




    public devicetype1(Context context) {
        super(context);
        initializeViews(context);
    }

    public devicetype1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public devicetype1(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i =0;i<values;i++) {
           inflater.inflate(R.layout.merge_buttons, this);
        }
            inflater.inflate(R.layout.merge_textview, this);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses 
        // built-in images so you don't need to add images, but in 
        // a real application your images should be in the 
        // application package so they are always available.

    }
}
