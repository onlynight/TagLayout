package com.github.onlynight.taglayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lion on 2016/11/22.
 */

public class TagAdapter extends BaseAdapter {

    private List<Tag> tags = new ArrayList<>();
    private Context context;

    public void addTags(List<Tag> tags) {
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public void clear() {
        tags.clear();
    }

    public TagAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Tag tag = tags.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_tag, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textTag = (TextView) convertView.findViewById(R.id.textTag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textTag.setText(tag.getContent());

        return convertView;
    }

    public class ViewHolder {
        public TextView textTag;
    }
}
