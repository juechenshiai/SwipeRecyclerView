package com.jessehu.swiperecyclerview.menu;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

/**
 * 菜单属性
 *
 * @author JesseHu
 * @date 2019/10/16
 */
public class MenuItem {
    private String title;
    private int textColor = Color.BLACK;
    private int textSize;
    private int bgColor = Color.WHITE;
    private Drawable bgDrawable;
    private Drawable icon;
    private int iconSize;
    private int iconGravity = Gravity.START;
    private int width;
    private int padding;
    private int iconPadding;

    public MenuItem() {
    }

    public MenuItem(String title, int textColor, int textSize, int bgColor, Drawable bgDrawable, Drawable icon, int iconSize, int iconGravity, int width, int padding, int iconPadding) {
        this.title = title;
        this.textColor = textColor;
        this.textSize = textSize;
        this.bgColor = bgColor;
        this.bgDrawable = bgDrawable;
        this.icon = icon;
        this.iconSize = iconSize;
        this.iconGravity = iconGravity;
        this.width = width;
        this.padding = padding;
        this.iconPadding = iconPadding;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public Drawable getBgDrawable() {
        return bgDrawable;
    }

    public void setBgDrawable(Drawable bgDrawable) {
        this.bgDrawable = bgDrawable;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public int getIconGravity() {
        return iconGravity;
    }

    public void setIconGravity(int iconGravity) {
        this.iconGravity = iconGravity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getIconPadding() {
        return iconPadding;
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
    }
}