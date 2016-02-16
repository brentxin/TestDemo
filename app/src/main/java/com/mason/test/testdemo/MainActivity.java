package com.mason.test.testdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<Person> data;
    PersonAdapter adapter;
    SwipeRefreshLayout.OnRefreshListener listener;
    int old = 0;
    int young = 0;
    LinearLayoutManager mLayoutManager;
    TextView tv;
    volatile boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv = (TextView)findViewById(R.id.tv_loadmore);


        data = new ArrayList<Person>();
        data.add(new Person("老大", 25));
        data.add(new Person("老二", 21));
        data.add(new Person("老三", 21));
        data.add(new Person("老四", 21));
        data.add(new Person("老五", 21));
        data.add(new Person("老六", 21));
        data.add(new Person("老七", 21));
        data.add(new Person("老八", 21));
        data.add(new Person("老九", 21));
        adapter = new PersonAdapter(data);
        adapter.setOnRecyclerViewListener(new PersonAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你选择了："+data.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(int position) {
                Toast.makeText(MainActivity.this, "你删除了："+data.get(position).getName(), Toast.LENGTH_SHORT).show();
                data.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        data.add(0, new Person("young dog" + ++young, 21));
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1700);
            }
        };
        //swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0x00225566);
        swipeRefreshLayout.setColorSchemeColors(0xff22cc22);
//        swipeRefreshLayout.setColorSchemeColors(android.R.color.black,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(listener);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        listener.onRefresh();
//        swipeRefreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//            }
//        },200);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem =  mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if(!isLoading){
                        isLoading = true;
                        tv.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                data.add(new Person("old dog" + ++old, 31));
                                adapter.notifyDataSetChanged();
                                tv.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                                isLoading = false;
                            }
                        }, 1700);

                    }



//                    }
                }
            }
        });
    }

}
