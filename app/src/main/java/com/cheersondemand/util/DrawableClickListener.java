package com.cheersondemand.util;

/**
 * Created by GAURAV on 5/28/2018.
 */

public interface DrawableClickListener {
    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}
