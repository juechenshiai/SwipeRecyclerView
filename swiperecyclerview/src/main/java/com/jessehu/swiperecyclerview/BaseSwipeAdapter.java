package com.jessehu.swiperecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseSwipeAdapter
 *
 * @author JesseHu
 * @date 2019/9/10
 */
public abstract class BaseSwipeAdapter<T> extends RecyclerView.Adapter<BaseSwipeAdapter.SwipeHolder> {
    private LayoutInflater mInflater;
    private List<MenuItem> menuItems = new ArrayList<>();
    private Context mContext;
    private int mLayoutId;
    private int mMenuWidth;
    private List<T> mContentData;
    private OnMenuItemClickListener mMenuItemClickListener;
    private OnItemClickListener mItemClickListener;

    public BaseSwipeAdapter(Context mContext, int mLayoutId, List<T> contentData) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        if (contentData == null) {
            contentData = new ArrayList<>();
        }
        this.mContentData = contentData;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置菜单标题
     *
     * @param menuTitles 菜单标题
     */
    public void setMenus(List<MenuItem> menuTitles) {
        this.menuItems.addAll(menuTitles);
    }

    /**
     * 设置菜单宽度
     *
     * @param width 菜单宽度
     */
    public void setMenuWidth(int width) {
        this.mMenuWidth = width;
    }

    @NonNull
    @Override
    public BaseSwipeAdapter.SwipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_layout, parent, false);
        FrameLayout contentContainer = itemView.findViewById(R.id.fl_content_layout);
        LinearLayout menuContainer = itemView.findViewById(R.id.ll_menu_layout);

        // 设置内容视图
        View contentLayout = mInflater.inflate(mLayoutId, null, false);
        contentContainer.addView(contentLayout);

        int size = menuItems.size();
        // 设置菜单容器的宽度，否则为0
        ViewGroup.LayoutParams layoutParams = menuContainer.getLayoutParams();

        int menuContainerWidth = 0;

        // 设置侧滑菜单
        for (int i = 0; i < size; i++) {
            MenuItem menuItem = menuItems.get(i);
            String title = menuItem.getTitle();
            Drawable icon = menuItem.getIcon();
            int textColor = menuItem.getTextColor();
            int bgColor = menuItem.getBgColor();
            int iconSize = menuItem.getIconSize();
            int iconGravity = menuItem.getIconGravity();
            Drawable bgDrawable = menuItem.getBgDrawable();
            int textSize = menuItem.getTextSize();
            int width = menuItem.getWidth();
            int padding = menuItem.getPadding();
            int iconPadding = menuItem.getIconPadding();

            MenuView titleTv = new MenuView(mContext);
            titleTv.setTag(i);
            titleTv.setText(title);
            titleTv.setGravity(Gravity.CENTER);

            if (bgDrawable != null) {
                titleTv.setBackground(bgDrawable);
            } else {
                titleTv.setBackgroundColor(bgColor);
            }
            if (textSize != 0) {
                titleTv.setTextSize(textSize);
            }
            titleTv.setTextColor(textColor);
            titleTv.setCompoundDrawablePadding(iconPadding);

            if (icon != null) {
                if (iconSize <= 0) {
                    // 如果没有设置icon高度则icon高度为文字高度
                    Paint.FontMetrics fontMetrics = titleTv.getPaint().getFontMetrics();
                    iconSize = (int) (fontMetrics.descent - fontMetrics.ascent);
                }
                icon.setBounds(0, 0, iconSize, iconSize);

                switch (iconGravity) {
                    case Gravity.START:
                    case Gravity.LEFT:
                        titleTv.setCompoundDrawables(icon, null, null, null);
                        break;
                    case Gravity.TOP:
                        titleTv.setCompoundDrawables(null, icon, null, null);
                        break;
                    case Gravity.END:
                    case Gravity.RIGHT:
                        titleTv.setCompoundDrawables(null, null, icon, null);
                        break;
                    case Gravity.BOTTOM:
                        titleTv.setCompoundDrawables(null, null, null, icon);
                        break;
                    default:
                }
            } else {
                iconSize = 0;
            }

            if (width == 0) {
                if (mMenuWidth != 0) {
                    width = mMenuWidth;
                } else {
                    // padding仅在没有设置任何宽度的时候生效，限制了宽度的情况下，意义不大
                    titleTv.setPadding(padding, 0, padding, 0);
                    width = (int) (titleTv.getPaint().measureText(title) + 0.5) + iconSize + padding * 2 + iconPadding;
                }
            }
            titleTv.setWidth(width);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            titleTv.setLayoutParams(params);
            menuContainerWidth += width;

            menuContainer.addView(titleTv);
        }

        layoutParams.width = menuContainerWidth;
        menuContainer.setLayoutParams(layoutParams);
        return new SwipeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseSwipeAdapter.SwipeHolder holder, int position) {
        T data = mContentData.get(position);
        onBindViewHolder(holder, data, position);
    }

    /**
     * 绑定数据，交由用户自己处理数据与view之间的关系
     *
     * @param holder   BaseSwipeAdapter.SwipeHolder
     * @param data     单条数据
     * @param position Position
     */
    public abstract void onBindViewHolder(@NonNull BaseSwipeAdapter.SwipeHolder holder, T data, int position);

    @Override
    public int getItemCount() {
        return mContentData.size();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class SwipeHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;

        public SwipeHolder(@NonNull View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
            LinearLayout menuContainer = itemView.findViewById(R.id.ll_menu_layout);
            for (int i = 0; i < menuContainer.getChildCount(); i++) {
                View menuView = menuContainer.getChildAt(i);
                menuView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 菜单点击事件
                        if (mMenuItemClickListener != null) {
                            mMenuItemClickListener.onClick(view, getLayoutPosition(), (Integer) view.getTag());
                        }
                    }
                });
            }
            FrameLayout contentContainer = itemView.findViewById(R.id.fl_content_layout);
            contentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(view, getLayoutPosition());
                    }
                }
            });
        }

        public View getView(int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViews.put(id, view);
            }
            return view;
        }
    }

    public interface OnMenuItemClickListener {
        /**
         * 菜单点击回调
         *
         * @param view         菜单对应的view
         * @param itemPosition ItemView在RecyclerView中的位置
         * @param menuPosition 菜单位置
         */
        void onClick(View view, int itemPosition, int menuPosition);
    }

    public interface OnItemClickListener {
        /**
         * ItemView点击回调
         *
         * @param view     自定义content的layout，即初始化adapter时传入的layout
         * @param position 当前点击的item的position
         */
        void onClick(View view, int position);
    }

    public static class MenuItem {
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

        private int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
    }
}
