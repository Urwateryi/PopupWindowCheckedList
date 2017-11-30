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

传送门：https://github.com/Urwateryi/PopupWindowCheckedList