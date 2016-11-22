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

    private TagAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagLayout = (TagLayout) findViewById(R.id.tagLayout);
        initAdapter();
        setSelectMode();
        setSelect();
        setListener();
    }

    private void initAdapter() {
        List<Tag> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new Tag(getResources().getString(R.string.tag) + (i + 1)));
        }
        adapter = new TagAdapter(this);
        adapter.addTags(data);
        tagLayout.setAdapter(adapter);
    }

    private void setSelectMode() {
        tagLayout.setSelectMode(TagLayout.SELECT_MODE_SINGLE);
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
            public void onCanNotSelectMore(boolean selected, int currentSelected, List<Integer> allSelected) {
                Toast.makeText(MainActivity.this,
                        "the MAX select item is " + tagLayout.getMaxSelectCount(),
                        Toast.LENGTH_SHORT).show();
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
//        tagLayout.setAdapter(adapter);
    }
}
