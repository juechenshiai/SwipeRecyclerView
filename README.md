SwipeRecyclerView
---

[![](https://api.bintray.com/packages/juechenshiai/maven/SwipeRecyclerView/images/download.svg)](https://bintray.com/juechenshiai/maven/SwipeRecyclerView)

侧滑菜单很多，对于重复造轮子的问题，主要是没有找到合适的，顺便练练手

[旧版文档 BaseOldSwipeAdapter](./README_OLD.md)

* 左滑菜单(水平排列)
    * 支持定义多个菜单
    * 支持定义每个菜单的宽度、背景、图标等
    * 支持图标大小、字体大小、字体颜色等设置
* 事件监听
    * 菜单点击事件
    * item点击事件
    * 侧滑状态(包括展开关闭起始状态，展开关闭结束状态)

### 添加library
Gradle
```
// SwipeRecyclerView基于recyclerview，因此必须添加recyclerview，版本随意
implementation 'androidx.recyclerview:recyclerview:1.0.0'
// 1.0.0以上版本才有新的Adapter，0.0.4以及以下版本只有旧版Adapter
implementation 'com.jessehu.swiperecyclerview:SwipeRecyclerView:0.2.0'
```

### 使用
#### 1. 布局中添加SwipeRecyclerView
```XML
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.jessehu.swiperecyclerview.SwipeRecyclerView
        android:id="@+id/srv_list"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
#### 2. 创建菜单
创建菜单有两种方式：
  1. 使用默认菜单，只需要配置菜单属性即可
  2. 使用自定义菜单，可以自定义菜单，但是高度只能为ItemView的高度，需要在adapter中创建

##### 2.1 创建默认菜单属性
```java
MenuItem menuItem = new MenuItem();
// 设置菜单宽度
menuItem.setWidth(300);
// 设置菜单的内边距，仅在菜单宽度自适应的情况下生效，如果设置了Width或者Adapter设置了MenuWidth都将无效
menuItem.setPadding(20);
// 设置菜单名称
menuItem.setTitle("菜单");
// 设置标题字体颜色
menuItem.setTextColor(Color.BLUE);
// 设置菜单标题字体大小
menuItem.setTextSize(10);
// 设置菜单背景色
menuItem.setBgColor(Color.YELLOW);
// 设置菜单背景，如果设置了BgDrawable，BgColor将无效
menuItem.setBgDrawable(getResources().getDrawable(R.drawable.bg_menu));
// 设置图标
menuItem.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
// 设置图标和标题的间距
menuItem.setIconPadding(20);
// 设置图标位置
menuItem.setIconGravity(Gravity.BOTTOM);
// 设置图标大小，如果不设置，默认为菜单文字高度
menuItem.setIconSize(50);
```
以上属性可以根据自身需要选择设置  
菜单高度由ItemView的高度决定

#### 3. 继承BaseSwipeAdapter
```java
/**
 * 示例,类似于RecyclerView.Adapter的使用
 */
public class SwipeAdapter extends BaseSwipeAdapter<SwipeAdapter.ContentViewHolder> {
    private List<String> contentData;

    public SwipeAdapter(Context context, List<String> contentData) {
        super(context);
        this.contentData = contentData;
    }

    @Override
    public ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item的布局，不包含menu
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new ContentViewHolder(itemView);
    }

    @Override
    public void onBindContentViewHolder(ContentViewHolder holder, int position) {
        holder.titleTv.setText(contentData.get(position));
    }

    @Override
    public int getItemCount() {
        return contentData.size();
    }

    public class ContentViewHolder extends BaseSwipeAdapter.BaseViewHolder {
        private TextView titleTv;

        // ItemView不包含menu
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.tv_title);
        }
    }
}
```
类似于RecyclerView.Adapter的使用  
`BaseSwipeAdapter` 对应 `RecyclerView.Adapter`  
`onCreateContentViewHolder()` 对应 `onCreateViewHolder()`  
`onBindContentViewHolder()` 对应 `onBindViewHolder()`  
`BaseSwipeAdapter.BaseViewHolder` 对应 `RecyclerView.ViewHolder`

##### 3.1 创建自定义菜单
在adapter中重写setCustomMenuCount(int)和createCustomMenuView(int)
```java
@Override
public View createCustomMenuView(int menuPosition) {
    // 创建对应位置的菜单
    switch (menuPosition) {
       case 0:
            TextView textView = new TextView(mContext);
            textView.setText("123654");
            textView.setGravity(Gravity.CENTER);
            textView.setWidth(150);
            textView.setTextColor(Color.RED);
            textView.setBackgroundColor(Color.BLUE);
            return textView;
        case 1:
            return mInflater.inflate(R.layout.item_menu_view, null);
        default:
            return super.createCustomMenuView(menuPosition);
    }
}

@Override
public int setCustomMenuCount(int viewType) {
    // 菜单数量
    return 2;
}
```
> 注意：
> 1. 菜单宽度通过设置自定义View的RootView的最小宽度(setMinWidth)，如果RootView支持setWidth也可以设置
> 2. 宽度也可以通过ViewGroup中的子View的宽度来撑开ViewGroup作为菜单宽度
> 3. 如果菜单的RootView不支持setWidth，在XML布局中设置layout_width无效，但是可以通过设置layout_minWidth

#### 4. 设置Adapter
```java
SwipeAdapter swipeAdapter = new SwipeAdapter(mContext, contents);
// 设置菜单
swipeAdapter.setMenus(List<MenuItem>);
// 设置菜单宽度，如果在创建菜单的时候没有设置宽度，将会使用该宽度，如果菜单宽度都一样，可以使用该属性统一设置
swipeAdapter.setMenuWidth(200);
SwipeRecyclerView listView = findViewById(R.id.srv_list);
listView.setAdapter(swipeAdapter);
```

#### 5. 设置监听事件
```java
// 设置菜单点击事件
swipeAdapter.setOnMenuItemClickListener(new OnMenuItemClickListener() {
    @Override
    public void onClick(View view, int itemPosition, int menuPosition) {
        if (view instanceof MenuView) {
            Toast.makeText(mContext, mTitles.get(menuPosition) + mContents.get(itemPosition), Toast.LENGTH_SHORT).show();
        } else {
                Toast.makeText(mContext, "菜单" + (menuPosition + 1) + mContents.get(itemPosition), Toast.LENGTH_SHORT).show();
        }
    }
});

// 设置item点击事件
swipeAdapter.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onClick(View view, int position) {
        listView.closeMenu();
        Toast.makeText(mContext, contents.get(position), Toast.LENGTH_SHORT).show();
    }
});

// 设置菜单滑动状态监听
listView.setOnMenuStatusListener(new SwipeRecyclerView.OnMenuStatusListener() {
    @Override
    public void onOpenStart(View itemView, List<View> menuViewList, int position) {
        Log.i(TAG, "onOpenStart: " + contents.get(position));
        itemView.setBackground(getResources().getDrawable(R.drawable.bg_open));
    }

    @Override
    public void onOpenFinish(View itemView, List<View> menuViewList, int position) {
        Log.i(TAG, "onOpenFinish: " + contents.get(position));
    }

    @Override
    public void onCloseStart(View itemView, List<View> menuViewList, int position) {
        Log.i(TAG, "onCloseStart: " + contents.get(position));
    }

    @Override
    public void onCloseFinish(View itemView, List<View> menuViewList, int position) {
        Log.i(TAG, "onCloseFinish: " + contents.get(position));
        itemView.setBackground(getResources().getDrawable(R.drawable.bg_normal));
    }
});
```
注意：点击事件是Adapter回调的而侧滑状态是SwipeRecyclerView回调的  
在进行其他操作之前，请先关闭已打开的菜单`listView.closeMenu()`

更多请查看demo

最后送上丑图一张  
![丑图](screenshot/1.gif)