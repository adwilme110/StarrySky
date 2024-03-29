package com.itbomb.space.backgroundlib.drawable;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;


import com.itbomb.space.R;

import org.xmlpull.v1.XmlPullParserException;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT;

public class GradientDrawableCreator implements ICreateDrawable {

    private TypedArray typedArray;

    GradientDrawableCreator(TypedArray typedArray) {
        this.typedArray = typedArray;
    }

    @Override
    public Drawable create() throws XmlPullParserException {
//        GradientDrawable drawable = new GradientDrawable();
        float[] cornerRadius = new float[8];
        float sizeWidth = 0;
        float sizeHeight = 0;
        float strokeWidth = -1;
        float strokeDashWidth = 0;
        int strokeColor = 0;
        float strokeGap = 0;
        float centerX = 0;
        float centerY = 0;
        int centerColor = 0;
        int startColor = 0;
        int endColor = 0;
        int gradientType = LINEAR_GRADIENT;
        int gradientAngle = 0;
        int shape = 0;
        int solidColor = -2;
        float cornerRadiusAll = 0.0f;
        float gradientRadius = 0.0f;
        boolean useLevel = false;


        Rect padding = new Rect();
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.background_bl_shape) {
                shape = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.background_bl_solid_color) {
                solidColor = typedArray.getColor(attr, 0);
            } else if (attr == R.styleable.background_bl_corners_radius) {
                cornerRadiusAll = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_corners_bottomLeftRadius) {
                cornerRadius[6] = typedArray.getDimension(attr, 0);
                cornerRadius[7] = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_corners_bottomRightRadius) {
                cornerRadius[4] = typedArray.getDimension(attr, 0);
                cornerRadius[5] = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_corners_topLeftRadius) {
                cornerRadius[0] = typedArray.getDimension(attr, 0);
                cornerRadius[1] = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_corners_topRightRadius) {
                cornerRadius[2] = typedArray.getDimension(attr, 0);
                cornerRadius[3] = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_angle) {
                gradientAngle = typedArray.getInteger(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_centerX) {
                centerX = typedArray.getFloat(attr, -1);
            } else if (attr == R.styleable.background_bl_gradient_centerY) {
                centerY = typedArray.getFloat(attr, -1);
            } else if (attr == R.styleable.background_bl_gradient_centerColor) {
                centerColor = typedArray.getColor(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_endColor) {
                endColor = typedArray.getColor(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_startColor) {
                startColor = typedArray.getColor(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_gradientRadius) {
                gradientRadius = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_type) {
                gradientType = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.background_bl_gradient_useLevel) {
                useLevel = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.background_bl_padding_left) {
                padding.left = (int) typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_padding_top) {
                padding.top = (int) typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_padding_right) {
                padding.right = (int) typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_padding_bottom) {
                padding.bottom = (int) typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_size_width) {
                sizeWidth = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_size_height) {
                sizeHeight = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_stroke_width) {
                strokeWidth = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_stroke_color) {
                strokeColor = typedArray.getColor(attr, 0);
            } else if (attr == R.styleable.background_bl_stroke_dashWidth) {
                strokeDashWidth = typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.background_bl_stroke_dashGap) {
                strokeGap = typedArray.getDimension(attr, 0);
            }
        }
        int[] colors = null;
        if (typedArray.hasValue(R.styleable.background_bl_gradient_startColor) &&
                typedArray.hasValue(R.styleable.background_bl_gradient_endColor)) {
            if (typedArray.hasValue(R.styleable.background_bl_gradient_centerColor)) {
                colors = new int[3];
                colors[0] = startColor;
                colors[1] = centerColor;
                colors[2] = endColor;
            } else {
                colors = new int[2];
                colors[0] = startColor;
                colors[1] = endColor;
            }
        }

        GradientDrawable.Orientation mOrientation = null;
        if (gradientType == LINEAR_GRADIENT &&
                typedArray.hasValue(R.styleable.background_bl_gradient_angle)) {
            gradientAngle %= 360;
            if (gradientAngle % 45 != 0) {
                throw new XmlPullParserException(typedArray.getPositionDescription()
                        + "<gradient> tag requires 'angle' attribute to "
                        + "be a multiple of 45");
            }
            mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
            switch (gradientAngle) {
                case 0:
                    mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
                    break;
                case 45:
                    mOrientation = GradientDrawable.Orientation.BL_TR;
                    break;
                case 90:
                    mOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
                    break;
                case 135:
                    mOrientation = GradientDrawable.Orientation.BR_TL;
                    break;
                case 180:
                    mOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
                    break;
                case 225:
                    mOrientation = GradientDrawable.Orientation.TR_BL;
                    break;
                case 270:
                    mOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
                    break;
                case 315:
                    mOrientation = GradientDrawable.Orientation.TL_BR;
                    break;
            }
        }
        if (mOrientation == null){
            mOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
        }
        GradientDrawable drawable = new GradientDrawable(mOrientation,colors);
        drawable.setShape(shape);
        if (solidColor != -2) {
            drawable.setColor(solidColor);
        }
        drawable.setCornerRadius(cornerRadiusAll);
        drawable.setGradientRadius(gradientRadius);
        drawable.setGradientType(gradientType);
        drawable.setUseLevel(useLevel);

        if (hasSetRadius(cornerRadius)) {
            drawable.setCornerRadii(cornerRadius);
        }
        if (typedArray.hasValue(R.styleable.background_bl_size_width) &&
                typedArray.hasValue(R.styleable.background_bl_size_height)) {
            drawable.setSize((int) sizeWidth, (int) sizeHeight);
        }
        if (typedArray.hasValue(R.styleable.background_bl_stroke_width)) {
            int start = 0;
            ArrayList<Integer> stateList = new ArrayList<>();
            ArrayList<Integer> colorList = new ArrayList<>();
            if (typedArray.hasValue(R.styleable.background_bl_stroke_color)) {
                drawable.setStroke((int) strokeWidth, strokeColor, strokeDashWidth, strokeGap);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (typedArray.hasValue(R.styleable.background_bl_pressed_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unPressed_stroke_color)) {
                    stateList.add(android.R.attr.state_pressed);
                    stateList.add(-android.R.attr.state_pressed);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_pressed_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unPressed_stroke_color, 0));
                }
                if (typedArray.hasValue(R.styleable.background_bl_checkable_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unCheckable_stroke_color)) {
                    stateList.add(android.R.attr.state_checkable);
                    stateList.add(-android.R.attr.state_checkable);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_checkable_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unCheckable_stroke_color, 0));
                }
                if (typedArray.hasValue(R.styleable.background_bl_checked_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unChecked_stroke_color)) {
                    stateList.add(android.R.attr.state_checked);
                    stateList.add(-android.R.attr.state_checked);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_checked_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unChecked_stroke_color, 0));
                }
                if (typedArray.hasValue(R.styleable.background_bl_enabled_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unEnabled_stroke_color)) {
                    stateList.add(android.R.attr.state_enabled);
                    stateList.add(-android.R.attr.state_enabled);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_enabled_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unEnabled_stroke_color, 0));
                }
                if (typedArray.hasValue(R.styleable.background_bl_selected_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unSelected_stroke_color)) {
                    stateList.add(android.R.attr.state_selected);
                    stateList.add(-android.R.attr.state_selected);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_selected_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unSelected_stroke_color, 0));
                }
                if (typedArray.hasValue(R.styleable.background_bl_focused_stroke_color) &&
                        typedArray.hasValue(R.styleable.background_bl_unFocused_stroke_color)) {
                    stateList.add(android.R.attr.state_focused);
                    stateList.add(-android.R.attr.state_focused);
                    colorList.add(typedArray.getColor(R.styleable.background_bl_focused_stroke_color, 0));
                    colorList.add(typedArray.getColor(R.styleable.background_bl_unFocused_stroke_color, 0));
                }
                int[][] state = new int[stateList.size()][];
                int[] color = new int[stateList.size()];
                for (int iState : stateList) {
                    state[start] = new int[]{iState};
                    color[start] = colorList.get(start);
                    start++;
                }

                ColorStateList colorStateList = new ColorStateList(state, color);
                drawable.setStroke((int) strokeWidth, colorStateList, strokeDashWidth, strokeGap);
                stateList = null;
                colorList = null;
            }
        }
        if (typedArray.hasValue(R.styleable.background_bl_gradient_centerX) &&
                typedArray.hasValue(R.styleable.background_bl_gradient_centerY)) {
            drawable.setGradientCenter(centerX, centerY);
        }

        if (typedArray.hasValue(R.styleable.background_bl_padding_left) &&
                typedArray.hasValue(R.styleable.background_bl_padding_top) &&
                typedArray.hasValue(R.styleable.background_bl_padding_right) &&
                typedArray.hasValue(R.styleable.background_bl_padding_bottom)) {
            try {
                Field paddingField = drawable.getClass().getField("mPadding");
                paddingField.setAccessible(true);
                paddingField.set(drawable, padding);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return drawable;
    }

    private boolean hasSetRadius(float[] radius) {
        boolean hasSet = false;
        for (float f : radius) {
            if (f != 0.0f) {
                hasSet = true;
                break;
            }
        }
        return hasSet;
    }
}
