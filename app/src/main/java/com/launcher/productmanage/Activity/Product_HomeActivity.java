package com.launcher.productmanage.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.launcher.productmanage.ApiManager.ProductApiManager;
import com.launcher.productmanage.Model.ProductData;
import com.launcher.productmanage.Utils.ConstansUtils;
import com.launcher.productmanage.Utils.DateUtils;
import com.launcher.productmanage.View.PullToRefreshView.PullToRefreshAttacher;
import com.launcher.productmanage.main.R;

import java.util.ArrayList;
import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-7-26.
 */
public class Product_HomeActivity extends MainActivity implements PullToRefreshAttacher.OnRefreshListener{
    private PullToRefreshAttacher pullToRefreshAttacher;
    private ListView listView;
    private MyAdapter adapter;
    private Handler handler;
    private List<ProductData> list = new ArrayList<ProductData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.product_home);


        listView = (ListView)findViewById(R.id.listview);
        adapter = new MyAdapter(this,list);
        listView.setAdapter(adapter);
        pullToRefreshAttacher = new PullToRefreshAttacher(this);
        pullToRefreshAttacher.setRefreshableView(listView,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("id",list.get(i).id);
                pushToNextActivity(bundle,Product_DetailsActivity.class);
            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    case ConstansUtils.MSG_SUCCESS:
                        pullToRefreshAttacher.setRefreshComplete();
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }

        };

        getAsyInformsData();
    }

    //获取新闻链表
    private void getAsyInformsData()
    {
        ProductApiManager.getProductsData().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ProductData>>() {
                    @Override
                    public void call(List<ProductData> informDatas) {
                        list.clear();
                        list.addAll(informDatas);

                        handler.obtainMessage(ConstansUtils.MSG_SUCCESS)
                                .sendToTarget();
                    }
                });




//        Observable.from(new informData()).mapMany(new Func1<informData, Observable<?>>() {
//            @Override
//            public Observable<?> call(informData informsData) {
//                return InformNetWorkModel.getInformsData();
//            }
//        }).subscribeOn(Schedulers.threadPoolForIO())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Object>() {
//                    @Override
//                    public void call(Object o) {
//
//                    }
//                });
    }

    @Override
    public void onRefreshStarted(View view) {
        getAsyInformsData();
    }

    //	内部类实现BaseAdapter  ，自定义适配器
    class MyAdapter extends BaseAdapter {

        private Context context;
        List<ProductData> list;

        public MyAdapter(Context context, List<ProductData> list)
        {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            // TODO Auto-generated method stub
            if(holder == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.product_home_item, null);
                holder = new ViewHolder();
                holder.informs_item_title = (TextView)convertView.findViewById(R.id.informs_item_title);
                holder.informs_item_time = (TextView)convertView.findViewById(R.id.informs_item_time);
                holder.informs_item_details = (TextView)convertView.findViewById(R.id.informs_item_details);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.informs_item_title.setText(list.get(position).product_name+"");
            holder.informs_item_details.setText(list.get(position).brief+"");
            holder.informs_item_time.setText(DateUtils.StimestampToDeatil(list.get(position).updated_at+""));


            return convertView;
        }
    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        TextView informs_item_title;
        TextView informs_item_time;
        TextView informs_item_details;
    }
}
