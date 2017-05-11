package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Imageable{

    public static String TAG="MainActivity";
    private static String IMG_TAG="image";
    public static final int REQUEST_PHOTO=1;
    private static final int REQUST_ALUBM=2;
    private static final String POST_SERVER_URL="http://thousandleaves.chinacloudapp.cn/";
    private static final String PARAMS_IMG_NAME="name";
    private static final String PARAMS_IMG_CONTENT="image";

    private boolean IsShowingResult=false;


    private Button mPhotoButton;
    private Button mPhotoManage;
    private Button mNetTest;
    private Button mUpLoad;
    private Button mUpLoads;
    private ImageView mImageView;
    private TextView mTestText;

    private Uri imageUri;

    private String temp_filename="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoButton=(Button)findViewById(R.id.photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PhotoFragmentActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
            }
        });
        mPhotoManage=(Button)findViewById(R.id.photo_manage);
        mPhotoManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("从相册中选取").setMessage("要从相册选取照片吗？").setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(Intent.ACTION_PICK,null);
                                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(intent,REQUST_ALUBM);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                            }
                        }).create();
                dialog.show();
            }
        });
        Log.d(TAG,TAG+"start");
        mTestText=(TextView)findViewById(R.id.text_net_test);
        //Toast.makeText(this,"THis is Main activity",Toast.LENGTH_LONG).show();
        mImageView=(ImageView) findViewById(R.id.image_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path=getFileStreamPath(temp_filename).getAbsolutePath();
                ImageFragment.newInstance(path).show(getFragmentManager(),IMG_TAG);
            }
        });
        mNetTest=(Button)findViewById(R.id.net_test);
        mNetTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://thousandleaves.chinacloudapp.cn/";
                StringRequest request=new StrRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(MainActivity.this,"访问成功",Toast.LENGTH_LONG).show();
                        Log.d(TAG,s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG,volleyError.toString());
                    }
                });
                LocationApplication.getRequestQueue().add(request);
            }
        });
        mUpLoad=(Button)findViewById(R.id.image_upload);
        mUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog=ProgressDialog.show(MainActivity.this,"正在上传图片","请稍候",false,false);
                StrRequest request=new StrRequest(Request.Method.POST, POST_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        mTestText.setText(s);
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this,volleyError.getLocalizedMessage()+"",Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Bitmap bitmap=BitmapFactory.decodeFile(getFileStreamPath(temp_filename).getAbsolutePath());
                        String img=getStringImg(bitmap);
                        Map<String,String> params=new HashMap<String, String>();
                        params.put(PARAMS_IMG_NAME,temp_filename);
                        params.put(PARAMS_IMG_CONTENT,img);
                        //params.put("name","coder_z");
                        return params;
                    }
                };
                RequestQueue queue=LocationApplication.getRequestQueue();
                queue.add(request);
            }
        });
        mUpLoads=(Button)findViewById(R.id.image_uploads);
        mUpLoads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerTask task=new ServerTask();
                task.execute(getAbsolutePath(temp_filename));
            }
        });

    }


    @Override
    public void onActivityResult(int RequestCode,int ResultCode,Intent data){
        if(ResultCode!= Activity.RESULT_OK) return ;
        else {
            switch (RequestCode){
                case REQUEST_PHOTO:
                        String path=data.getStringExtra(PhotoFragment.FILENAME);
                        if(path!=null&&!path.equals("")) {
                            showPhoto(path);
                            temp_filename=path;
                        }
                        break;
                case REQUST_ALUBM:
/*                        Uri image=data.getData();
                        String[] column={MediaStore.Images.Media.DATA};                             //打开媒体库
                        Cursor cursor=getContentResolver().query(image,column,null,null,null);      //通过Uri搜寻指定照片
                        cursor.moveToFirst();
                        String image_path=cursor.getString(cursor.getColumnIndex(column[0]));                               //取得字段
                        showPhoto(image_path);*/
                    try {
                        BitmapDrawable b=PictureUtil.getScaledDrawable(this,imageUri);
                        mImageView.setImageDrawable(b);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG,"can`t find the file");
                        e.printStackTrace();
                    }
                    break;

            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,TAG+"start");
    }

    public void showPhoto(String filename) {
        String path=this.getFileStreamPath(filename).getAbsolutePath();
        showPhoto(path,false);
    }


    public void showPhoto(String path,boolean nonesense) {
        BitmapDrawable b= PictureUtil.getScaledDrawable(this,path);
        mImageView.setImageDrawable(b);
    }

    private String getAbsolutePath(String filename){
        return this.getFileStreamPath(filename).getAbsolutePath();
    }

    //把图片压缩成字符串格式
    private String getStringImg(Bitmap bitmap){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bytes=bos.toByteArray();
        String strImg= Base64.encodeToString(bytes,Base64.DEFAULT);
        return strImg;
    }



    public class ServerTask extends AsyncTask<String, Integer , Void> {
        public byte[] dataToServer;
        //Task state
        private final int UPLOADING_PHOTO_STATE  = 0;
        private final int SERVER_PROC_STATE  = 1;
        private boolean mCameraReadyFlag=false;
        private final static String TAG="ServerTask";

        private ProgressDialog dialog;

        HttpURLConnection uploadTest() {
            final String SERVER_URL="http://thousandleaves.chinacloudapp.cn/";
            try {
                URL url=new URL(SERVER_URL);
                final HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+"*****");


                return connection;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }



        }

        //upload photo to server
        HttpURLConnection uploadPhoto(FileInputStream fileInputStream)
        {

            //final String serverFileName = "test"+ (int) Math.round(Math.random()*1000) + ".jpg";
            final String serverFileName = "test.jpg";
            final String lineEnd = "\r\n";
            final String twoHyphens = "--";
            final String boundary = "*****";
            final String SERVERURL="http://thousandleaves.chinacloudapp.cn/";
            Log.e("msg","begin HttpURLConnection......");
            try
            {
                URL url = new URL(SERVERURL);
                // Open a HTTP connection to the URL
                final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);

                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + serverFileName +"\"" + lineEnd);
                //dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + serverFileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                // read file and write it into form...
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                publishProgress(SERVER_PROC_STATE);
                // close streams
                fileInputStream.close();
                dos.flush();
                Log.e("msg","upload finished!");
                return conn;
            }
            catch (MalformedURLException ex){
                Log.e(TAG, "error: " + ex.getMessage(), ex);
                return null;
            }
            catch (IOException ioe){
                Log.e(TAG, "error: " + ioe.getMessage(), ioe);
                return null;
            }
        }

        //get image result from server and display it in result view
        void getResultImage(HttpURLConnection conn){
            // retrieve the response from server
            InputStream is;
            try {
                is = conn.getInputStream();
                //get result image from server
                //resultForWebImage = BitmapFactory.decodeStream(is);
                is.close();
                IsShowingResult = true;
                Log.d("msg","download finished!");
            } catch (IOException e) {
                Log.e(TAG,"getResultImage:"+e.toString());
                e.printStackTrace();
            }
        }
        //Main code for processing image algorithm on the server
        void processImage(String inputImageFilePath){
            publishProgress(UPLOADING_PHOTO_STATE);
            File inputFile = new File(inputImageFilePath);
            try {

                //create file stream for captured image file
                FileInputStream fileInputStream  = new FileInputStream(inputFile);


                //upload photo
                final HttpURLConnection  conn = uploadPhoto(fileInputStream);

                //get processed photo from server
                if (conn != null){
                    getResultImage(conn);
                    //String download="http://10.10.145.154/EE368_Android_Tutorial3_Server/computeSIFTOnSCIEN.php";

                }
                fileInputStream.close();
            }
            catch (FileNotFoundException ex){
                Log.e(TAG, "processImage"+ex.toString());
            }
            catch (IOException ex){
                Log.e(TAG, "processImage"+ex.toString());
            }
        }

        public ServerTask() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Photo captured");
            this.dialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {           //background operation
            String uploadFilePath = params[0];
            //processImage(uploadFilePath);                         //For testing and avoid funtioning it




            //release camera when previous image is processed
            mCameraReadyFlag = true;
            return null;
        }
        //progress update, display dialogs
        @Override
        protected void onProgressUpdate(Integer... progress) {
            if(progress[0] == UPLOADING_PHOTO_STATE){
                dialog.setMessage("Uploading");
                dialog.show();
            }
            else if (progress[0] == SERVER_PROC_STATE){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog.setMessage("Processing");
                dialog.show();
            }
        }
        @Override
        protected void onPostExecute(Void param) {
            if (dialog.isShowing()) {
                dialog.dismiss();

                if(IsShowingResult){
                    //ShowImage(resultForWebImage, 2);
                    IsShowingResult=false;
                }
            }
        }
    }



}
