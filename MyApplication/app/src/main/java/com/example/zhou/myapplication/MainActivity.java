package com.example.zhou.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

        private ListView listView;
        private Myadapter adapter;
        private List<String> datas;
        private int screenWidth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            listView = (ListView) findViewById(R.id.listView);
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            screenWidth = outMetrics.widthPixels;
            initDatas();
            adapter = new Myadapter();
            listView.setAdapter(adapter);

        }

        private void initDatas() {
            // TODO Auto-generated method stub
            datas = new ArrayList<String>();
            for (int i = 0; i < 20; i++) {
                datas.add("删除我吧,删除我吧" + i);
            }
        }

        class Myadapter extends BaseAdapter {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return datas.get(position);
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                final ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(
                            R.layout.item_main, null);
                    viewHolder = new ViewHolder();
                    viewHolder.horizontalScrollView = (HorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView);
                    viewHolder.tvContexTextView = (TextView) convertView.findViewById(R.id.tv_context);
                    viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
                    viewHolder.toTop = (Button) convertView.findViewById(R.id.btn_toTop);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvContexTextView.setText(datas.get(position));
                viewHolder.tvContexTextView.setWidth(screenWidth);
                if (viewHolder.horizontalScrollView.getScrollX() > 0) {
                    viewHolder.horizontalScrollView.smoothScrollTo(0, 0);
                }
                convertView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                int actionWidth = viewHolder.btnDelete.getWidth()+viewHolder.toTop.getWidth();
                                int scrollX =viewHolder.horizontalScrollView.getScrollX();
                                if(scrollX>actionWidth/2){
                                    viewHolder.horizontalScrollView.smoothScrollTo(actionWidth, 0);
                                }else {
                                    viewHolder.horizontalScrollView.smoothScrollTo(0, 0);
                                }
                                return true;
                        }
                        return false;
                    }
                });
                viewHolder.tvContexTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        notifyDataSetChanged();
                    }
                });
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        datas.remove(position);
                        notifyDataSetChanged();
                    }
                });
                viewHolder.toTop.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String temp = datas.get(0);
                        datas.set(0, datas.get(position));
                        datas.set(position, temp);
                        notifyDataSetChanged();

                    }
                });
                return convertView;
            }

            class ViewHolder {
                HorizontalScrollView horizontalScrollView;
                TextView tvContexTextView;
                Button btnDelete;
                Button toTop;
            }
        }
}
