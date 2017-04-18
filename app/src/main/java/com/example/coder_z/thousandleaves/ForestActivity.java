package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/*import io.realm.Realm;
import io.realm.RealmResults;*/

/**
 * Created by coder-z on 17-3-19.
 */

public class ForestActivity extends Activity /*implements AdapterView.OnItemClickListener*/{
    public static final String Tag="ForestActivity";
    //private RealmResults<Leaf> least_leaf;
    private ListView mListView;
    private ListAdapter adapter;
    //private Realm realm=Realm.getDefaultInstance();
    public List<Leaf> leaves;
    /*   @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



    }*/

    public class ListAdapter extends BaseAdapter{


        public Context mContext;
        public LayoutInflater layoutInflater;

        ListAdapter(Context context,List<Leaf> leafs){
            mContext=context;
            leaves=leafs;
            layoutInflater=LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return leaves.size();
        }

        @Override
        public Object getItem(int i) {
            return leaves.get(i);
        }

        @Override
        public long getItemId(int i) {
            return leaves.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=layoutInflater.inflate(R.layout.info_list_item,null);
            ImageView img=(ImageView)v.findViewById(R.id.leaf_image);
            /**
             * 通过ImgUrl获取图片路径
             * 先用list存储，往后使用Realm
             */
            Leaf leaf=leaves.get(i);
           // Bitmap bitmap= getLocalBitMap(leaf.getImgUrl());
           // img.setImageBitmap(bitmap);
            TextView title=(TextView)v.findViewById(R.id.leaf_name);
            title.setText(leaf.getName());
            TextView description=(TextView)v.findViewById(R.id.leaf_describe);
            title.setText(leaf.getDesciption());
            return v;
        }

        //获取本地图片
        private Bitmap getLocalBitMap(String path){
            try {
                FileInputStream fs=new FileInputStream(path);
                return BitmapFactory.decodeStream(fs);
            } catch (FileNotFoundException e) {
                Log.e(Tag,"Not found file!");
                e.printStackTrace();
                return null;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.info_activity);
        /*least_leaf=Leaf.all(realm);*/
        mListView=(ListView)findViewById(R.id.list_view);
       // mListView.setOnItemClickListener(this);
        adapter=new ListAdapter(this,testE());
        mListView.setAdapter(adapter);
    }

    private List<Leaf> testE(){
        List<Leaf> list=new LinkedList<>();
        list.add(new Leaf(1,"wutong","xxxxxxx",""));
        list.add(new Leaf(2,"sanshen","yyyyyy",""));
        list.add(new Leaf(3,"xiangzhang","zzzz",""));
        list.add(new Leaf(4,"huaishu","kkkkkk",""));
        list.add(new Leaf(5,"willow","sssssss",""));
        return list;
    }






}
