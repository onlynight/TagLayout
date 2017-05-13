package com.github.onlynight.taglayout.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lion on 2016/11/22.
 * Tag item layout.
 */

public class TagLayout extends ViewGroup {

    /**
     * the select mode all, you can select all tag.
     */
    public static final int SELECT_MODE_ALL = -1;

    /**
     * the select mode none, you can't select any item.
     */
    public static final int SELECT_MODE_NONE = 0;

    /**
     * the select mode single, you can only select one item.
     */
    public static final int SELECT_MODE_SINGLE = 1;

    /**
     * save the {@link TagLayout} max width.
     */
    private int mMaxWidth;

    /**
     * save the {@link TagLayout} max width.
     */
    private int mMaxHeight;

    /**
     * save the max select count.</p>
     * it is also select mode.
     * {@link this#SELECT_MODE_ALL},{@link this#SELECT_MODE_NONE},{@link this#SELECT_MODE_SINGLE}
     */
    private int mMaxSelectCount = 1;

    /**
     * Is the single line.
     */
    private boolean mIsSingleLine = false;

    private BaseAdapter mAdapter;
    private OnTagItemSelectedListener onTagItemSelectedListener;
    private boolean registeredDataObserver = false;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            setAdapter(mAdapter);
        }
    };

    /**
     * To set this listener you can listen item select action and
     * listen there're no more tag can be selected
     *
     * @param onTagItemSelectedListener
     */
    public void setOnTagItemSelectedListener(OnTagItemSelectedListener onTagItemSelectedListener) {
        this.onTagItemSelectedListener = onTagItemSelectedListener;
    }

    public TagLayout(Context context) {
        super(context);
        init(null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        init(array);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.TagLayout, defStyleAttr, 0);
        init(array);
    }

    @RequiresApi(21)
    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.TagLayout, defStyleAttr, defStyleRes);
        init(array);
    }

    private void init(TypedArray array) {
        if (array != null) {
            mMaxSelectCount = array.getInteger(
                    R.styleable.TagLayout_maxSelectCount, 1);
            mIsSingleLine = array.getBoolean(R.styleable.TagLayout_singleLine, false);
        }
    }

    /**
     * get the max select count
     *
     * @return
     */
    public int getMaxSelectCount() {
        return mMaxSelectCount;
    }

    /**
     * set the base adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        removeAllViews();
        if (mAdapter == null) {
            return;
        }

        if (!registeredDataObserver) {
            registeredDataObserver = true;
            mAdapter.registerDataSetObserver(dataSetObserver);
        }

        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            View child = this.mAdapter.getView(i, null, this);
            addView(child);
        }
        setSelectMode(mMaxSelectCount);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int lines = 0;
        int lineWidth = 0;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams =
                    (MarginLayoutParams) child.getLayoutParams();

            lineWidth += child.getMeasuredWidth() + layoutParams.leftMargin
                    + layoutParams.rightMargin;
            if (lineWidth > width) {
                lines++;
                lineWidth = child.getMeasuredWidth() + layoutParams.leftMargin
                        + layoutParams.rightMargin;
                lineHeight = lines * (child.getMeasuredHeight() +
                        layoutParams.topMargin + layoutParams.bottomMargin);
            }

            child.layout(lineWidth - child.getMeasuredWidth() - layoutParams.rightMargin,
                    lineHeight + layoutParams.topMargin,
                    lineWidth - layoutParams.rightMargin,
                    lineHeight + layoutParams.topMargin + child.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int lines = 1;
        int lineWidth = 0;
        mMaxWidth = 0;
        mMaxHeight = 0;
        int eachLineHeight = 0;

        if (mIsSingleLine) {
            for (int i = 0; i < getChildCount(); i++) {
                MarginLayoutParams params = (MarginLayoutParams)
                        getChildAt(i).getLayoutParams();

                // calculate child total size include margin.
                childWidth = getChildAt(i).getMeasuredWidth() +
                        params.leftMargin + params.rightMargin;
                childHeight = getChildAt(i).getMeasuredHeight() +
                        params.topMargin + params.bottomMargin;
                eachLineHeight = childHeight;

//                lineWidth += childWidth;
//                if (lineWidth >= widthSpecSize) {
//                    lines++;
//                    lineWidth = childWidth;
//                    mMaxWidth = widthSpecSize;
//                }
                mMaxWidth += childWidth;
            }

            mMaxHeight = eachLineHeight;
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                MarginLayoutParams params = (MarginLayoutParams)
                        getChildAt(i).getLayoutParams();

                // calculate child total size include margin.
                childWidth = getChildAt(i).getMeasuredWidth() +
                        params.leftMargin + params.rightMargin;
                childHeight = getChildAt(i).getMeasuredHeight() +
                        params.topMargin + params.bottomMargin;
                eachLineHeight = childHeight;

                lineWidth += childWidth;
                if (lineWidth >= widthSpecSize) {
                    lines++;
                    lineWidth = childWidth;
                    mMaxWidth = widthSpecSize;
                }
            }

            mMaxHeight = eachLineHeight * lines;
        }

        setSelfSize(widthSpecMode, widthSpecSize, heightSpecMode, heightSpecSize);
    }

    /**
     * set self size.
     *
     * @param widthSpecMode  width measure spec mode
     * @param widthSpecSize  width measure spec size
     * @param heightSpecMode height measure spec mode
     * @param heightSpecSize height measure spec size
     */
    private void setSelfSize(int widthSpecMode, int widthSpecSize,
                             int heightSpecMode, int heightSpecSize) {
        int finalWidth = 0;
        int finalHeight = 0;

        if (mIsSingleLine) {
            switch (widthSpecMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.UNSPECIFIED:
                    finalWidth = mMaxWidth > widthSpecSize ? mMaxWidth : widthSpecSize;
                    break;
                case MeasureSpec.AT_MOST:
                    finalWidth = mMaxWidth;
                    break;
            }

            switch (heightSpecMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.UNSPECIFIED:
                    finalHeight = mMaxHeight < heightSpecSize ? mMaxHeight : heightSpecSize;
                    break;
                case MeasureSpec.AT_MOST:
                    finalHeight = mMaxHeight;
                    break;
            }
        } else {
            switch (widthSpecMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.UNSPECIFIED:
                    finalWidth = widthSpecSize;
                    break;
                case MeasureSpec.AT_MOST:
                    finalWidth = mMaxWidth;
                    break;
            }

            switch (heightSpecMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.UNSPECIFIED:
                    finalHeight = heightSpecSize;
                    break;
                case MeasureSpec.AT_MOST:
                    finalHeight = mMaxHeight;
                    break;
            }
        }

        setMeasuredDimension(finalWidth, finalHeight);
    }

    /**
     * set the item layout parmas to {@link android.view.ViewGroup.MarginLayoutParams}.
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * clear all the selected item.
     */
    public void clearSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setSelected(false);
        }
    }

    /**
     * get selected item id.
     *
     * @return a list of selected item's id.
     */
    public List<Integer> getSelected() {
        List<Integer> selected = new LinkedList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.isSelected()) {
                selected.add(i);
            }
        }
        return selected;
    }

    /**
     * set item state to selected.
     *
     * @param items the items you want to be set select.
     */
    public void setSelect(List<Integer> items) {
        if (items == null) {
            return;
        }

        clearSelect();

        if (mMaxSelectCount == 0) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            if (mMaxSelectCount == 1) {
                if (items.get(i) < getChildCount()) {
                    View child = getChildAt(items.get(i));
                    child.setSelected(true);
                }
            } else {
                if (i + 1 <= mMaxSelectCount) {
                    if (items.get(i) < getChildCount()) {
                        View child = getChildAt(items.get(i));
                        child.setSelected(true);
                    }
                }
            }
        }
    }

    /**
     * set the select mode.
     *
     * @param selectModeOrMaxSelectCount {@link TagLayout#SELECT_MODE_ALL} can select all.</p>
     *                                   {@link TagLayout#SELECT_MODE_NONE} can select none.</p>
     *                                   {@link TagLayout#SELECT_MODE_SINGLE} can select one.</p>
     *                                   or you can set the max select count you want.</p>
     */
    public void setSelectMode(int selectModeOrMaxSelectCount) {
        this.mMaxSelectCount = selectModeOrMaxSelectCount;

        if (mMaxSelectCount == 0) {
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                getChildAt(i).setOnClickListener(null);
            }
            clearSelect();
        } else {
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                final int index = i;
                final View child = getChildAt(i);
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mMaxSelectCount == 1) {
                            clearSelect();
                            child.setSelected(!child.isSelected());
                            if (onTagItemSelectedListener != null) {
                                List<Integer> selected = getSelected();
                                onTagItemSelectedListener.onSelected(child.isSelected(),
                                        index, selected);
                            }
                        } else if (mMaxSelectCount > 1) {
                            List<Integer> selected = getSelected();
                            if (selected.size() >= mMaxSelectCount) {
                                if (child.isSelected()) {
                                    child.setSelected(false);
                                    if (onTagItemSelectedListener != null) {
                                        onTagItemSelectedListener.onSelected(
                                                false, index, selected);
                                    }
                                } else {
                                    if (onTagItemSelectedListener != null) {
                                        onTagItemSelectedListener.onCanNotSelectMore(
                                                false, index, selected);
                                    }
                                }
                            } else {
                                child.setSelected(!child.isSelected());
                                if (onTagItemSelectedListener != null) {
                                    onTagItemSelectedListener.onSelected(
                                            child.isSelected(), index, selected);
                                }
                            }
                        } else if (mMaxSelectCount == -1) {
                            child.setSelected(!child.isSelected());
                            if (onTagItemSelectedListener != null) {
                                List<Integer> selected = getSelected();
                                onTagItemSelectedListener.onSelected(
                                        child.isSelected(), index, selected);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * set the tag layout max select count.</p>
     * this method is the same as {@link TagLayout#setSelectMode(int)}
     *
     * @param maxSelectCount it is bigger than 0,</p>
     *                       the -1 is means you can select all tag item.</p>
     *                       {@link TagLayout#SELECT_MODE_ALL} can select all.</p>
     *                       {@link TagLayout#SELECT_MODE_NONE} can select none.</p>
     *                       {@link TagLayout#SELECT_MODE_SINGLE} can select one.</p>
     */
    public void setMaxSelectCount(int maxSelectCount) {
        setSelectMode(maxSelectCount);
    }

    public interface OnTagItemSelectedListener {
        void onSelected(boolean selected, int currentSelected, List<Integer> allSelected);

        void onCanNotSelectMore(boolean selected, int currentSelected, List<Integer> allSelected);
    }

}
