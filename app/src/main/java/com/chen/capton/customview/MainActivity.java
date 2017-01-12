package com.chen.capton.customview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import chen.capton.optionlayout.OptionLayout;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    Adapter adapter;
    List<String> dataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for (int i = 0; i <6; i++) {
            dataList.add("这是item布局"+i);
        }

        listView= (ListView) findViewById(R.id.listview);
        adapter=new Adapter(this,dataList);
        listView.setAdapter(adapter);
    }



    class Adapter extends BaseAdapter implements OptionLayout.OptionClickListener{
         Context context;
         List<String> dataList;
        LayoutInflater inflater;
        public Adapter(Context context, List<String> dataList) {
            this.context=context;
            this.dataList=dataList;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                convertView= inflater.inflate(R.layout.option_layout,parent,false);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            ((OptionLayout)convertView).setOptionClickListener(this);
            ((OptionLayout)convertView).setPosition(position);

            viewHolder.textView.setText(dataList.get(position));

            return convertView;
        }

        @Override
        public void leftOptionClick(View view,int postition) {
           String toTop=dataList.remove(postition);
            dataList.add(0,toTop);
            notifyDataSetChanged();
        }

        @Override
        public void rightOptionClick(View view,int postition) {
             dataList.remove(postition);
             notifyDataSetChanged();
        }

        @Override
        public void itemClick(View view,int postition) {
            Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();

        }

        class ViewHolder {
            public TextView textView;

            public ViewHolder(View view) {
                this.textView = textView;
                textView= (TextView) view.findViewById(R.id.textView);
            }
        }

    }

}
