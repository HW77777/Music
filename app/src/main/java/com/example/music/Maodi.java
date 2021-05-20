package com.example.music;

import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Maodi {
    ArrayList<Yitai> list=new ArrayList();

    public void maodi(ArrayList<String> h){
        Yitai yitai=new Yitai();
        //yitai.hash= StringUtil.applySha256(blockchain.get(blockchain.size() - 1).hash+blockchain.get(blockchain.size()-2).hash+blockchain.get(blockchain.size()-3).hash);
        String m = null;
        for (String s:h){
            m=m+s;
        }
        yitai.hash=StringUtil.applySha256(m);
        list.add(yitai);
        String s=new GsonBuilder().setPrettyPrinting().create().toJson(list);
        String n=Thread.currentThread().getName();
        String filename=String.format("锚定"+".txt");
        File mdfile=new File("D:\\1\\"+filename);
        if (!mdfile.exists()&&!mdfile.isDirectory()){
            System.out.println("文件夹不存在，创建路径D:\\1\\锚定"+".txt不存在");
            try {
                mdfile.createNewFile();
                System.out.println("锚定"+n + ".txt创建成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Writer writer=null;
        try {
            writer=new FileWriter(mdfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter=new BufferedWriter(writer);
        try {
            bufferedWriter.write(String.valueOf(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(".....锚定完成....." + java.lang.Thread.currentThread().getName());
    }
}
