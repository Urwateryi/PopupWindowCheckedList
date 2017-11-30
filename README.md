# PopupWindowCheckedList

### 前言
最近项目有如下需求：

![image](https://raw.githubusercontent.com/Urwateryi/MarkDownPic/master/PopupWindowCheckList/%E9%9C%80%E6%B1%82.jpg)

想了想，正好没有这样的弹窗，就用PopupWindow自己做一个吧

先上效果：

![image](https://raw.githubusercontent.com/Urwateryi/MarkDownPic/master/PopupWindowCheckList/pop.gif)

### 分析
有个title，一个底部按钮，中间是一个列表，这里使用ListView来做，内部的item使用CheckedTextView

可以通过外部设置列表为单选还是多选
```java

mPopup.setTagTxt(getString(R.string.choose_drawback_reason_of_refuse))//设置顶部title的内容
                .setButtomTxt(getString(R.string.cancel))//设置底部按钮内容
                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);//单选

/**
     * 设置ListView的选择模式
     * 多选：AbsListView.CHOICE_MODE_MULTIPLE
     * 单选：AbsListView.CHOICE_MODE_SINGLE
     *
     * @param choiceMode
     * @return
     */
    public PopupWindowCheckChoose setChoiceMode(int choiceMode) {
        if (mListView != null) {
            mListView.setChoiceMode(choiceMode);
        }
        return this;
    }
```

选中了某个/些item后，获取选中的item的position列表

```java
mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mLisenter != null) {
                ArrayList<Integer> positionList=new ArrayList<>();

                //获取选中的item的position列表
                SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
                for (int i=0;i<mList.size();i++){
                    if (checkedItemPositions.get(i)){
                        positionList.add(i);
                    }
                }

                mLisenter.onItemClick(positionList);
            }
        });
```

### 具体实现

PopupWindowCheckChoose 

```java
package com.example.popwindowcheckedtextview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Description:从底部弹出的单选或多选的popupwindow
 * <p>
 * Author: zoey
 * Time: 2017/11/29 0029
 */
public class PopupWindowCheckChoose extends PopupWindow {

    private Activity mContext;

    /**
     * 列表和按钮的监听事件
     */
    private onEventLisenter mLisenter;

    /**
     * 父View
     */
    private View mView;
    /**
     * 顶部标题
     */
    private TextView mTvTag;
    /**
     * 底部按钮
     */
    private TextView mTvButtom;
    /**
     * 选择ListView
     */
    private ListView mListView;

    /**
     * 顶部标题内容
     */
    private String mTagTxt;
    /**
     * 底部按钮内容
     */
    private String mButtomTxt;

    /**
     * 数据源
     */
    private ArrayList<String> mList = new ArrayList<>();
    /**
     * 适配器
     */
    private CheckAdapter mAdapter;

    /**
     * 设置顶部内容
     *
     * @param tagTxt
     * @return
     */
    public PopupWindowCheckChoose setTagTxt(String tagTxt) {
        mTagTxt = tagTxt;
        if (mTvTag != null) {
            mTvTag.setText(mTagTxt);
        }
        return this;
    }

    /**
     * 设置底部按钮内容
     *
     * @param buttomTxt
     * @return
     */
    public PopupWindowCheckChoose setButtomTxt(String buttomTxt) {
        mButtomTxt = buttomTxt;
        if (mTvButtom != null) {
            mTvButtom.setText(mButtomTxt);
        }
        return this;
    }

    /**
     * 设置ListView的选择模式
     * 多选：AbsListView.CHOICE_MODE_MULTIPLE
     * 单选：AbsListView.CHOICE_MODE_SINGLE
     *
     * @param choiceMode
     * @return
     */
    public PopupWindowCheckChoose setChoiceMode(int choiceMode) {
        if (mListView != null) {
            mListView.setChoiceMode(choiceMode);
        }
        return this;
    }

    public PopupWindowCheckChoose(Activity context, ArrayList<String> list) {
        mContext = context;
        mList = list;

        initView();
        initLisenter();
    }

    /**
     * 展示Pop
     *
     * @param localAt
     */
    public void showPop(View localAt) {
        if (!this.isShowing()) {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
            lp.alpha = 0.7f;
            mContext.getWindow().setAttributes(lp);

            showAtLocation(localAt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        if (this != null && this.isShowing()) {
            super.dismiss();
            //消失后，背景变回原来的颜色
            WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
            lp.alpha = 1f;
            mContext.getWindow().setAttributes(lp);
        }
    }

    /**
     * 初始化控件
     */
    public void initView() {
        mView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.lay_popup_check_choose_buttom, null);

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_anim_style);
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.T_mini_black)));

        mTvTag = mView.findViewById(R.id.tv_tag);
        mListView = mView.findViewById(R.id.lv_list);
        mTvButtom = mView.findViewById(R.id.tv_buttom);

        mAdapter = new CheckAdapter(mList);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 对控件设置监听事件
     */
    public void initLisenter() {
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mLisenter != null) {
                ArrayList<Integer> positionList=new ArrayList<>();

                //获取选中的item的position列表
                SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
                for (int i=0;i<mList.size();i++){
                    if (checkedItemPositions.get(i)){
                        positionList.add(i);
                    }
                }

                mLisenter.onItemClick(positionList);
            }
        });

        mTvButtom.setOnClickListener(v -> {
            if (mLisenter != null) {
                mLisenter.onClickButtom();
                dismiss();
            }
        });
    }

    public interface onEventLisenter {
        void onItemClick(ArrayList<Integer> positionList);//返回选中item的位置集合

        default void onClickButtom() {
        }//底部按钮点击事件
    }

    public void setOnEventLisenter(onEventLisenter lisenter) {
        mLisenter = lisenter;
    }

    class CheckAdapter extends BaseAdapter {

        ArrayList<String> list;

        public CheckAdapter(ArrayList<String> list) {
            this.list = list;
        }

        public ArrayList<String> getList() {
            return list;
        }

        public void setList(ArrayList<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            String bean = list.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lv_pop_choose, parent, false);
                holder = new ViewHolder();

                holder.ctvContent = convertView.findViewById(R.id.ctv_content);
                convertView.setTag(holder);

                //对于listview，注意添加这一行，即可在item上使用高度
                AutoUtils.autoPadding(convertView);
                AutoUtils.autoMargin(convertView);
                AutoUtils.autoTextSize(convertView);
                AutoUtils.autoSize(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            boolean check = ((ListView) parent).isItemChecked(position);
            holder.ctvContent.setChecked(check);

            holder.ctvContent.setText(bean);
            return convertView;
        }
    }

    class ViewHolder {
        CheckedTextView ctvContent;
    }
}


```

TestActivity

```java
package com.example.popwindowcheckedtextview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

/**
 * Description:
 * <p>
 * Time: 2017/11/30 0030
 */
public class TestActivity extends AutoLayoutActivity {

    private TextView mTvContent;
    private RelativeLayout mRlContent;
    private PopupWindowCheckChoose mPopup;
    private ArrayList<String> mList=new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTvContent = findViewById(R.id.tv_content);
        mRlContent = findViewById(R.id.rl_content);

        mList=getPopList();
        mPopup = new PopupWindowCheckChoose(this, mList);
        mPopup.setTagTxt(getString(R.string.choose_drawback_reason_of_refuse))//设置顶部title的内容
                .setButtomTxt(getString(R.string.cancel))//设置底部按钮内容
                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);//单选

        mRlContent.setOnClickListener(v -> {
            mPopup.showPop(mTvContent);
        });

        //单选
        mPopup.setOnEventLisenter(positionList -> {
            mPopup.dismiss();
            mTvContent.setText(mList.get(positionList.get(0)));
        });
            //-----------------------------------
//        //多选
//        mPopup.setOnEventLisenter(positionList -> {
//            StringBuffer buffer=new StringBuffer();
//            for (int i=0;i<positionList.size();i++){
//                buffer.append(mList.get(positionList.get(i))+",");
//            }
//            mTvContent.setText(buffer.toString());
//        });
    }

    /**
     * 数据
     *
     * @return
     */
    public ArrayList<String> getPopList() {
        ArrayList<String> popList = new ArrayList<>();
        popList.add(getString(R.string.reason_draw_back_buy_more));
        popList.add(getString(R.string.reason_draw_back_not_receive));
        popList.add(getString(R.string.reason_draw_back_its_fake));
        popList.add(getString(R.string.reason_draw_back_buy_error));
        popList.add(getString(R.string.reason_draw_back_type_error));
        popList.add(getString(R.string.reason_draw_back_quality_problem));
        popList.add(getString(R.string.reason_draw_back_deal));
        popList.add(getString(R.string.reason_draw_back_no_info_of_logistics));
        popList.add(getString(R.string.reason_draw_back_no_goods));
        popList.add(getString(R.string.reason_draw_back_another_reason));
        return popList;
    }
}


```

传送门：https://github.com/Urwateryi/PopupWindowCheckedList