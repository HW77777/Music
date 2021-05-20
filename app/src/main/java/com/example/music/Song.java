package com.example.music;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Time;
import java.util.Date;

public class Song {
    public String id=null;
    public String song;
    public String singer;
    public String album;
    public String duration;
    public String path;
    public String md5;
    public String md532;
    public PublicKey publicKey;
    public byte[] signature;
    public Song(){}

    public Song(String id, String songName, String singerName, String album, String duration, String md5, String md532,String path,PublicKey publicKey){
        this.id=id;
        this.song=songName;
        this.singer=singerName;
        this.album=album;
        this.duration=duration;
        this.md5=md5;
        this.md532=md532;
        this.path=path;
        this.publicKey=publicKey;
    }
    public Song(String song,String singer,String album,String duration,String path){
        this.song=song;
        this.singer=singer;
        this.album=album;
        this.duration=duration;
        this.path=path;
    }
    public String getSong(){
        return song;
    }
    public String getSinger(){
        return singer;
    }
    public String getAlbum(){return album;}
    public String getDuration(){
        return duration;
    }
    public String getPath(){return path;}
    public String getId(){return id;}
    public void setSong(String song){ this.song=song;}
    public void setSinger(String singer){this.singer=singer;}
    public void setAlbum(String album){this.album=album;}
    public void setDuration(String duration){this.duration=duration;}
    public void setPath(String path){this.path=path;}
    public  String calulateHash() {
        return StringUtil.applySha256(song+singer+album+duration+md5+md532+new Date().getTime());
    }
    public void generateSignature(PrivateKey privateKey)  {
        String data = StringUtil.getStringFromKey(publicKey)+id+
                song+singer+album+duration+md5+md532;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(publicKey);
        return StringUtil.verifyECDSASig(publicKey, data, signature);
    }
}
