package com.example.music;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static com.example.music.MD5.md5HashCode;
import static com.example.music.MD5.md5HashCode32;

public class MainActivity2 extends ListActivity {
    private ArrayList<Song> list_data=new ArrayList<>();
    private ArrayList<Song> l=new ArrayList<>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main2);
        Intent intent=getIntent();
        try {
            list_data=getsong();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Myadapter listAdapter=new Myadapter(MainActivity2.this, R.layout.list,list_data);
        listView=findViewById(R.id.listview);
        this.setListAdapter(listAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = getIntent();
                Song s = list_data.get(position);
                String song=s.getSong();
                String singer=s.getSinger();
                String album=s.getAlbum();
                String duration=s.getDuration();
                String path=s.getPath();
               // String md5=s.getMd5();
               // String md532=s.getMd532();
              //  String songid=s.getId();
                data.putExtra("路径", path);
                data.putExtra("歌名", song);
                data.putExtra("歌手", singer);
                data.putExtra("专辑", album);
                data.putExtra("时间", duration);
                //data.putExtra("md5",md5);
                //data.putExtra("md532",md532);
                //data.putExtra("id",songid);
                setResult(2, data);
                finish();
            }
        });
    }

    public ArrayList<Song> getsong() throws FileNotFoundException {

        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<Song> lst=new ArrayList<>();
        for (int i=0;i<cursor.getCount();i++){
            Song s=new Song();
            cursor.moveToNext();
            String song = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String singer=cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            String album=cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            String duration =String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic=cursor.getInt((cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)));
            if(isMusic!=0){
                s.setSong(song);
                s.setSinger(singer);
                s.setAlbum(album);
               s.setDuration(duration);
                s.setPath(path);
               // String md5=md5HashCode(path);
               // String md532=md5HashCode32(path);
                //s.setMd5(md5);
                //s.setMd532(md532);
                //s.setID();
                lst.add(s);
            }
        }
        cursor.close();
        return lst;
    }



}