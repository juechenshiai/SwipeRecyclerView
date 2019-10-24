package com.jessehu.swiperecyclerview.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jessehu.swiperecyclerview.R;
import com.jessehu.swiperecyclerview.impl.OnItemClickListener;
import com.jessehu.swiperecyclerview.impl.OnMenuItemClickListener;
import com.jessehu.swiperecyclerview.menu.MenuItem;
import com.jessehu.swiperecyclerview.menu.MenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseSwipeAdapter
 *
 * @author JesseHu
 * @date 2019/10/15
 */
public abstract class BaseSwipeAdapter<VH extends BaseSwipeAdapter.BaseViewHolder> extends RecyclerView.Adapter<BaseSwipeAdapter.ItemViewHolder> {
    protected LayoutInflater mInflater;
    private List<MenuItem> menuItems;
    private int mMenuWidth;
    protected Context mContext;
    private OnMenuItemClickListener mMenuItemClickListener;
    private OnItemClickListener mItemClickListener;

    public BaseSwipeAdapter(Context context) {
        this.mContext = context;
        menuItems = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 设置菜单属性
     *
     * @param menus 菜单
     */
    public void setMenus(List<MenuItem> menus) {
        this.menuItems.addAll(menus);
    }

    /**
     * 统一设置菜单宽度，如果菜单没有单独设置宽度则使用该宽度，不支持自定义menu
     *
     * @param width 菜单宽度
     */
    public void setMenuWidth(int width) {
        this.mMenuWidth = width;
    }

    @NonNull
    @Override
    public BaseSwipeAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_layout, parent, false);
        FrameLayout contentContainer = itemView.findViewById(R.id.fl_content_layout);
        LinearLayout menuContainer = itemView.findViewById(R.id.ll_menu_layout);

        // 设置菜单容器的宽度，否则为0
        ViewGroup.LayoutParams layoutParams = menuContainer.getLayoutParams();
        int menuContainerWidth = 0;

        int size = menuItems.size();
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

            // 高度为MATCH_PARENT,即使用ItemView的高度作为menu的高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            titleTv.setLayoutParams(params);
            menuContainerWidth += width;

            menuContainer.addView(titleTv);
        }

        // 设置自定义菜单
        int customMenuCount = setCustomMenuCount(viewType);
        for (int i = 0; i < customMenuCount; i++) {
            View customMenuView = createCustomMenuView(i);
            // 制定测量规则,UNSPECIFIED获取最小宽度，AT_MOST以及EXACTLY无法获取宽高，因此只能使用minWidth控制menu的最小宽度，或者使用布局内部的子view来控制宽度
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            // 调用measure方法以获取宽高
            customMenuView.measure(measureSpec, measureSpec);
            int measuredWidth = customMenuView.getMeasuredWidth();

            // 高度为MATCH_PARENT,即使用ItemView的高度作为menu的高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(measuredWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            customMenuView.setLayoutParams(params);
            menuContainerWidth += measuredWidth;

            menuContainer.addView(customMenuView);
        }

        layoutParams.width = menuContainerWidth;
        menuContainer.setLayoutParams(layoutParams);

        VH viewHolder = onCreateContentViewHolder(contentContainer, viewType);
        contentContainer.addView(viewHolder.itemView);
        ItemViewHolder itemViewHolder = new ItemViewHolder(itemView);
        itemViewHolder.setViewHolder(viewHolder);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseSwipeAdapter.ItemViewHolder holder, int position) {
        onBindContentViewHolder((VH) holder.mHolder, position);
    }

    /**
     * 添加列表item的holder
     *
     * @param parent   contentContainer
     * @param viewType viewType
     * @return VH
     */
    public abstract VH onCreateContentViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * 绑定数据，交由用户自己处理数据与view之间的关系
     *
     * @param holder   VH
     * @param position Position
     */
    public abstract void onBindContentViewHolder(VH holder, int position);

    /**
     * 设置自定义菜单view，非必须
     *
     * @param menuPosition 菜单的position，具体的数量由 {@link #setCustomMenuCount(int)} 决定
     * @return 菜单视图
     */
    public View createCustomMenuView(int menuPosition) {
        return null;
    }

    /**
     * 设置自定义menu的数量，非必须，如果需要设置自定义菜单必须添加
     *
     * @param viewType 同onCreateViewHolder的viewType
     * @return 自定义menu的数量
     */
    public int setCustomMenuCount(int viewType) {
        return 0;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    class ItemViewHolder extends BaseViewHolder {
        private VH mHolder;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout menuContainer = itemView.findViewById(R.id.ll_menu_layout);
            for (int i = 0; i < menuContainer.getChildCount(); i++) {
                View menuView = menuContainer.getChildAt(i);
                final int menuPosition = i;
                menuView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 菜单item点击事件
                        if (mMenuItemClickListener != null) {
                            mMenuItemClickListener.onClick(view, getLayoutPosition(), menuPosition);
                        }
                    }
                });
            }
            FrameLayout contentContainer = itemView.findViewById(R.id.fl_content_layout);
            contentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // item点击事件
                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(view, getLayoutPosition());
                    }
                }
            });
        }

        void setViewHolder(VH mHolder) {
            this.mHolder = mHolder;
        }
    }
}
