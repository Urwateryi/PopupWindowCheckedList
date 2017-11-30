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
        mPopup.setTagTxt(getString(R.string.choose_drawback_reason_of_refuse))
                .setButtomTxt(getString(R.string.cancel))
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
