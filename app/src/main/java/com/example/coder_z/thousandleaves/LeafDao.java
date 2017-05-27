package com.example.coder_z.thousandleaves;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coder-z on 17-4-16.
 */

public class LeafDao {
    public static final String DB_NAME="db_leaf";
    public static final String TABLE_NAME="leaf";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_DESCRIBE="description";
    public static final String COLUMN_IMGURL="ImgUrl";
    private Context context;
    private SQLiteDatabase m_db;
    private DBOpenHelper mDBOpenHelper;
    private int version=1;

    LeafDao(Context context) {
        this.context=context;
    }

    public Leaf[] queryAll(){
        Cursor cursor= m_db.query(TABLE_NAME,
                        new String[]{COLUMN_ID,COLUMN_NAME,COLUMN_DESCRIBE,COLUMN_IMGURL},null,null,null,null,null);
        return ConvertToLeaf(cursor);
    }

    public Leaf queryById(String id){
        Cursor cursor= m_db.query(TABLE_NAME,
                new String[]{COLUMN_ID,COLUMN_NAME,COLUMN_DESCRIBE,COLUMN_IMGURL},COLUMN_ID+"="+id,null,null,null,null);
        return ConvertToLeaf(cursor)[0];
    }

    public Leaf queryByName(String name){
        Cursor cursor= m_db.query(TABLE_NAME,
                new String[]{COLUMN_ID,COLUMN_NAME,COLUMN_DESCRIBE,COLUMN_IMGURL},COLUMN_NAME+"="+name,null,null,null,null);
        return ConvertToLeaf(cursor)[0];
    }

    public void insertLeaf(Leaf leaf){
        m_db.execSQL("insert into "+TABLE_NAME+"("+COLUMN_ID+","+COLUMN_NAME+","+COLUMN_DESCRIBE+","+COLUMN_IMGURL
                                                +") values"+"("+leaf.getId()+","+leaf.getName()+","+leaf.getDescription()+","+leaf.getImgUrl()+")");
    }

    public long insert(Leaf leaf) {
        ContentValues contentValues=new ContentValues();
        //contentValues.put(COLUMN_ID,leaf.getId());
        contentValues.put(COLUMN_NAME,leaf.getName());
        contentValues.put(COLUMN_DESCRIBE,leaf.getDescription());
        contentValues.put(COLUMN_IMGURL,leaf.getImgUrl());
        return m_db.insert(TABLE_NAME,null,contentValues);
    }

    public void deleteLeaf(){

    }

    public void open(){
        mDBOpenHelper=new DBOpenHelper(context,DB_NAME,null,version);
        m_db=mDBOpenHelper.getWritableDatabase();
    }

    public void close(){
        if(m_db!=null){
            m_db.close();
            m_db=null;
        }
    }

    private Leaf[] ConvertToLeaf(Cursor cursor){
        int count=cursor.getCount();
        if(count==0||!cursor.moveToFirst())
            return null;
        Leaf[] leaves=new Leaf[count];
        for(int i=0;i<count;i++){
            leaves[i]=new Leaf(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                                ,cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                                ,cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIBE))
                                ,cursor.getString(cursor.getColumnIndex(COLUMN_IMGURL)));
            cursor.moveToNext();

        }
        return leaves;
    }



    class DBOpenHelper extends SQLiteOpenHelper{
        private String CREATE_COMMAND="create table "+TABLE_NAME+" ("+COLUMN_ID+" integer primary key autoincrement, "
                                                                     +COLUMN_NAME+" text not null,"
                                                                     +COLUMN_DESCRIBE+" text not null,"
                                                                     +COLUMN_IMGURL+" text not null"+");";

        private static final String DROP_COMMAND="DROP TABLE IF EXISTS ";

        public DBOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
            super(context,name,factory,version);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_COMMAND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COMMAND);
            onCreate(m_db);
        }
    }

}
