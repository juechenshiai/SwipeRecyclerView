package com.jessehu.swiperecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * MenuView
 *
 * @author JesseHu
 * @date 2019/9/11
 */
public class MenuView extends AppCompatTextView {
    private boolean isRedrawed = false;

    public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable[] drawables = getCompoundDrawables();
        Drawable leftDrawable = drawables[0];
        Drawable topDrawable = drawables[1];
        Drawable rightDrawable = drawables[2];
        Drawable bottomDrawable = drawables[3];

        // setCompoundDrawables会重新绘制，防止展开状态不断绘制(死循环)
        if (!isRedrawed) {
            isRedrawed = true;
            if (leftDrawable != null) {
                // 左边图标
                int iconWidth = leftDrawable.copyBounds().width();
                int iconHeight = leftDrawable.copyBounds().height();
                int left =
                        ((int) (getWidth() - getPaddingLeft() * 2 - iconWidth - getPaint().measureText(getText().toString())) / 2);
                leftDrawable.setBounds(left, 0, left + iconWidth, iconHeight);
                // 重新绘制图标位置
                setCompoundDrawables(leftDrawable, null, null, null);

            } else if (topDrawable != null) {
                // 上边图标
                int iconHeight = topDrawable.copyBounds().height();
                int iconWidth = topDrawable.copyBounds().width();
                Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
                float fontHeight = fontMetrics.descent - fontMetrics.ascent;
                int top = ((int) (getHeight() - iconHeight - fontHeight) / 2);
                topDrawable.setBounds(0, top, iconWidth, iconHeight + top);

                setCompoundDrawables(null, topDrawable, null, null);
            } else if (rightDrawable != null) {
                // 右边图标
                int iconWidth = rightDrawable.copyBounds().width();
                int iconHeight = rightDrawable.copyBounds().height();
                int right =
                        ((int) (getWidth() - getPaddingLeft() * 2 - iconWidth - getPaint().measureText(getText().toString())) / 2);
                rightDrawable.setBounds(-right, 0, -right + iconWidth, iconHeight);
                setCompoundDrawables(null, null, rightDrawable, null);

            } else if (bottomDrawable != null) {
                // 下边图标
                int iconHeight = bottomDrawable.copyBounds().height();
                int iconWidth = bottomDrawable.copyBounds().width();
                Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
                float height = fontMetrics.descent - fontMetrics.ascent;
                int bottom = ((int) (getHeight() - iconHeight - height) / 2);
                bottomDrawable.setBounds(0, -bottom, iconWidth, iconHeight - bottom);
                setCompoundDrawables(null, null, null, bottomDrawable);

            }
        }

    }
}
