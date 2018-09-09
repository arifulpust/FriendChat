package com.arif.friendchat.Utils;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by mdmunirhossain on 5/22/18.
 * <p>
 * common variable format resID_text/background/hint/colorfilter
 */

public class ColorTheme {
public  static  String ColorPrimary="#0097A7";
public  static  String Gray="#7b7a7a";




    public static int getColor(String color) {
        return Color.parseColor(color);
    }

    public static GradientDrawable makeSelector(int colorSelected, int colorNotSelected, float leftTop, float rightTop, float rightBottom, float leftBottom) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{}  //others
        };
        int[] colors = new int[]{
                colorSelected,
                colorNotSelected,
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(colorNotSelected);
        gdDefault.setCornerRadii(new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom});
        gdDefault.setColor(colorStateList);
        return gdDefault;
    }

    public static GradientDrawable makeSelector(int colorSelected, int colorNotSelected) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{}  //others
        };
        int[] colors = new int[]{
                colorSelected,
                colorNotSelected,
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(colorNotSelected);
        gdDefault.setShape(GradientDrawable.OVAL);


        // gdDefault.setCornerRadii(new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom});
        gdDefault.setColor(colorStateList);
        return gdDefault;
    }

    public static ColorStateList makeTextSelector(int colorSelected, int colorNotSelected) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{}  //others
        };
        int[] colors = new int[]{
                colorSelected,
                colorNotSelected,
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;

    }

    public static GradientDrawable makeGradientBackground(int startColort, int endColor) {
        int[] colors = {startColort, endColor};


        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors); //#97712F this is the end color of gradient
        g.setGradientType(GradientDrawable.RADIAL_GRADIENT); // making it circular gradient
        g.setShape(GradientDrawable.OVAL);
        g.setGradientRadius(250);


        // gd.setShape(GradientDrawable.RECTANGLE);
        return g;
    }

    public static GradientDrawable makeCicularDrawableWithStroke(int background_Color,int stroke_color) {




        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(background_Color);
        shape.setStroke(2, stroke_color);
        shape.setCornerRadius(250);
        //shape.set

        //shape.
        // gd.setShape(GradientDrawable.RECTANGLE);
        return shape;
    }

    public static GradientDrawable makeGradientLineDrawable(int background_Color, int stroke_color, int stroke_width, int redius) {


        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(background_Color);
        shape.setStroke(stroke_width, stroke_color);
        shape.setCornerRadius(redius);
        // gd.setShape(GradientDrawable.RECTANGLE);
        return shape;

    }


    public static GradientDrawable makeGradientColor(int startColort, int endColor) {
        int[] colors = {startColort, endColor};
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors); //#97712F this is the end color of gradient

        gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);


        // Set the ActionBar background
        return gradient;
    }

    public static GradientDrawable makeGradientColorWithRadius(int startColort, int endColor,int radius)
    {
        int[] colors = {startColort, endColor};
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors); //#97712F this is the end color of gradient
        gradient.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return gradient;
    }


    public static void setCursorColor(EditText view, @ColorInt int color) {
        Field field = null;
        try {
            // Get the cursor resource id
            field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);
            //view.setCu
            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);


        } catch (Exception ignored) {

            Log.e("Cursor", "call" + ignored.getMessage());
        }
        }




}
