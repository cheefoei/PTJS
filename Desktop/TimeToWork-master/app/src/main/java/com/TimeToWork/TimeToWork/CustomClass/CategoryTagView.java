package com.TimeToWork.TimeToWork.CustomClass;

import android.content.Context;
import android.util.AttributeSet;

import com.TimeToWork.TimeToWork.R;

public class CategoryTagView extends android.support.v7.widget.AppCompatTextView {

    public CategoryTagView(Context context) {
        super(context);
    }

    public CategoryTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        super.setText(text, type);

        if (text.equals(getContext().getString(R.string.category_ambassador))) {
            setBackgroundResource(R.drawable.bg_label_tag_pink);
        } else if (text.equals(getContext().getString(R.string.category_driver))) {
            setBackgroundResource(R.drawable.bg_label_tag_orange);
        } else if (text.equals(getContext().getString(R.string.category_event_crew))) {
            setBackgroundResource(R.drawable.bg_label_tag_teal);
        } else if (text.equals(getContext().getString(R.string.category_promoter))) {
            setBackgroundResource(R.drawable.bg_label_tag_blue);
        } else if (text.equals(getContext().getString(R.string.category_tutor))) {
            setBackgroundResource(R.drawable.bg_label_tag_green);
        } else if (text.equals(getContext().getString(R.string.category_waiter))) {
            setBackgroundResource(R.drawable.bg_label_tag_teal);
        } else if (text.equals(getContext().getString(R.string.category_other))) {
            setBackgroundResource(R.drawable.bg_label_tag_grey);
        }
    }
}
