package com.github.onlynight.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.onlynight.taglayout.library.TagLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TagLayout tagLayout;
    private TagLayout tagLayout1;
    private TagLayout tagLayout2;

    private TagAdapter adapter;
    private TagAdapter adapter1;
    private TagAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagLayout = (TagLayout) findViewById(R.id.tagLayout);
        tagLayout1 = (TagLayout) findViewById(R.id.tagLayout1);
        tagLayout2 = (TagLayout) findViewById(R.id.tagLayout2);

        initAdapter();
        setSelectMode();
        setSelect();
        setListener();
    }

    private void initAdapter() {
        List<Tag> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add(new Tag(getResources().getString(R.string.tag) + (i + 1)));
        }
        adapter = new TagAdapter(this);
        adapter.addTags(data);
        tagLayout.setAdapter(adapter);

        List<Tag> data1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data1.add(new Tag(getResources().getString(R.string.tag) + (i + 1)));
        }
        adapter1 = new TagAdapter(this);
        adapter1.addTags(data1);
        tagLayout1.setAdapter(adapter1);

        List<Tag> data2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data2.add(new Tag(getResources().getString(R.string.tag) + (i + 1)));
        }
        adapter2 = new TagAdapter(this);
        adapter2.addTags(data2);
        tagLayout2.setAdapter(adapter2);
    }

    private void setSelectMode() {
        tagLayout.setSelectMode(TagLayout.SELECT_MODE_SINGLE);
        tagLayout1.setSelectMode(TagLayout.SELECT_MODE_ALL);
        // this method is the same as setSelectMode();
        // tagLayout.setMaxSelectCount(1);
    }

    private void setSelect() {
        List<Integer> selected = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            selected.add(14 + i);
        }
        tagLayout.setSelect(selected);
    }

    private void setListener() {
        tagLayout.setOnTagItemSelectedListener(new TagLayout.OnTagItemSelectedListener() {
            @Override
            public void onSelected(boolean selected, int currentSelected, List<Integer> allSelected) {
                String showStr = "";
                if (selected) {
                    showStr = "select item " + currentSelected;
                } else {
                    showStr = "cancel select item " + currentSelected;
                }
                Toast.makeText(MainActivity.this, showStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onCanNotSelectMore(boolean selected, int currentSelected, List<Integer> allSelected) {
                Toast.makeText(MainActivity.this,
                        "the MAX select item is " + tagLayout.getMaxSelectCount(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        tagLayout2.setOnTagItemSelectedListener(new TagLayout.OnTagItemSelectedListener() {
            @Override
            public void onSelected(boolean selected, int currentSelected, List<Integer> allSelected) {
            }

            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "click position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanNotSelectMore(boolean selected, int currentSelected, List<Integer> allSelected) {

            }
        });
    }

    public void onClickUpdateButton(View view) {
        List<Tag> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new Tag(getResources().getString(R.string.tag) + (i + 1)));
        }
        adapter.clear();
        adapter.addTags(data);
        adapter.notifyDataSetChanged();
        tagLayout.setMaxSelectCount(1);
        List<Integer> select = new LinkedList<>();
        select.add(1);
        tagLayout.setSelect(select);
//        tagLayout.setAdapter(adapter);
    }
}
