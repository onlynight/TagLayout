package com.github.onlynight.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.onlynight.taglayout.library.TagLayout;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        fakeData();
    }

    private void fakeData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("add");
        }

        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> mData = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recycler, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setData(List<String> mData) {
            this.mData = mData;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TagLayout tagLayout;
            private TagAdapter adapter;

            public ViewHolder(View itemView) {
                super(itemView);
                tagLayout = (TagLayout) itemView.findViewById(R.id.tagLayout);
                adapter = new TagAdapter();
                tagLayout.setAdapter(adapter);
            }

            private void fakeData() {
            }

            private class TagAdapter extends BaseAdapter {

                @Override
                public int getCount() {
                    return 6;
                }

                @Override
                public Object getItem(int position) {
                    return "tag";
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View contentView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_tag, parent, false);
                    return contentView;
                }

            }

        }

    }
}
