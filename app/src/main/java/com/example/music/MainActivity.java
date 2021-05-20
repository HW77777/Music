package com.example.music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.music.MD5.md5HashCode;
import static com.example.music.MD5.md5HashCode32;


public class MainActivity extends AppCompatActivity implements Runnable {
    private Button button;
    private TextView songname;
    private TextView singername;
    private TextView album;
    private TextView duration;
    private TextView id;
    private TextView height;
    private TextView textView;
    private TextView time;
    private LinearLayout linearLayout;
    private TextView choosetext;
    private TextView t;
    static Handler handler;


    public static CopyOnWriteArrayList<Block> blockchain = new CopyOnWriteArrayList<Block>();
    public static int difficulty = 1;
    public static Song genesisMp3Info;
    Wallet walletA=new Wallet();
    Wallet coinbase = new Wallet();
    Song s=new Song();
    public static CopyOnWriteArrayList<Song> mp3Infospool=new CopyOnWriteArrayList<>();
    static Node[] votepool=new Node[10];//投票节点池
    static Node[] superNode=new Node[5];//竞选节点
    static Node[] mineNode=new Node[3];//挖矿节点
    static Block genesis = new Block("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        createvoteNode();
        createsuperNode();
        votingandtrust();
        sortMineNode();
        genesisMp3Info=new Song("002f8ee7700cad580171a0e0dbe3f85ffa5a83355b2e75db3f5a04483d82367b",
                "阿珍爱上了阿强",
                "五条人","梦幻丽莎发廊",
                "409545","b3990b6e98115b94a134f627b48b434d",
                "b3990b6e98115b94a134f627b48b434d",
                "/storage/emulated/0/qqmusic/song/五条人 - 阿珍爱上了阿强 [mqms2].mp3",
                walletA.publicKey);
        genesisMp3Info.generateSignature(walletA.privateKey);
        mp3Infospool.add(genesisMp3Info);

        genesis.addmp3Infos(mp3Infospool);
        //genesis.name=Thread.currentThread().getName();
        genesis.mineBlock(difficulty);
        blockchain.add(genesis);
        genesis.height=1;
        isChainValid();
        mp3Infospool.clear();


        MainActivity n1=new MainActivity();
        MainActivity n2=new MainActivity();
        MainActivity n3=new MainActivity();
        new java.lang.Thread(n1,mineNode[0].name).start();
        new java.lang.Thread(n2,mineNode[1].name).start();
        new java.lang.Thread(n3,mineNode[2].name).start();



        handler=new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what==5){
                    Bundle bundle= (Bundle) msg.obj;
                    String song=bundle.getString("歌名");
                    String singer=bundle.getString("歌手");
                    String albu=bundle.getString("专辑");
                    String diration=bundle.getString("时间");
                    String ida=bundle.getString("id");
                    String heighta=bundle.getString("高度");
                    String timea=bundle.getString("存证时间");
                    songname.setText(song);
                    singername.setText(singer);
                    album.setText(albu);
                    duration.setText(diration);
                    id.setText(ida);
                    time.setText(timea);
                    height.setText(heighta);
                    textView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void initView(){
        button=findViewById(R.id.button);
        songname=findViewById(R.id.songname);
        singername=findViewById(R.id.singername);
        album=findViewById(R.id.album);
        duration=findViewById(R.id.duration);
        id=findViewById(R.id.id);
        height=findViewById(R.id.height);
        textView=findViewById(R.id.czxx);
        linearLayout=findViewById(R.id.linear);
        choosetext=findViewById(R.id.choosesong);
        time=findViewById(R.id.time);
        t=findViewById(R.id.s);
    }
    public void Button(View view){
        s.generateSignature(walletA.privateKey);
        mp3Infospool.add(s);
        makeText(this,"存证成功", LENGTH_SHORT).show();
    }


    public void Button2(View view){
        Intent ac2=new Intent(this,MainActivity2.class);
        startActivityForResult(ac2,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            choosetext.setText(data.getStringExtra("歌名"));
            choosetext.setVisibility(View.VISIBLE);
            t.setText(data.getStringExtra("路径"));
            t.setVisibility(View.VISIBLE);
            String song=data.getStringExtra("歌名");
            String singer=data.getStringExtra("歌手");
            String album=data.getStringExtra("专辑");
            String duration=data.getStringExtra("时间");
            String path=data.getStringExtra("路径");
            try {
                String md5=md5HashCode(path);
                String md532=md5HashCode32(path);
                s=new Song(s.calulateHash(),song,singer,album,duration,md5,md532,path,walletA.publicKey);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }





    //普通节点  仅同步
    public static void createvoteNode(){
        for(int i=0;i<10;i++){
            votepool[i]=new Node();
            votepool[i].address= "node" + i;
            votepool[i].votes=new Random().nextInt(100);
            System.out.println(votepool[i].address + "   " + votepool[i].votes);
        }
    }
    //竞选节点  去承诺时间
    public static void createsuperNode(){
        for (int i=0;i<5;i++){
            superNode[i]=new Node();
            superNode[i].promisetime= new Random().nextInt(9)+1;//承诺时间
            superNode[i].address= "superNode" + i;
            System.out.println(superNode[i].address+"   承诺时间："+superNode[i].promisetime);
        }
    }
    public static void votingandtrust(){
        HashMap map=new HashMap();
        List lst;
        int vn[]=new int[5];
        for (int i=0;i<10;i++){//普通
            for (int t=0;t<5;t++){//竞选
                int v=new Random().nextInt(20);
                if (v>votepool[i].votes)continue;
                votepool[i].votes-=v;
                superNode[t].votes+=v;
                vn[t]=v;
                map.put(vn[t],t);
            }
            lst=new ArrayList();
            Arrays.sort(vn);
            for (int n = 0; n < 5; n++) {
                lst.add(vn[n]);
            }
            Collections.reverse(lst);
            for (int m = 0; m < 5; m++) {
                vn[m]= (Integer) lst.get(m);
            }
            System.out.println("信赖的竞选节点："+map.get(vn[0])+"    "+votepool[i].address);
        }
        for (int i = 0; i<5; i++) {
            System.out.println(superNode[i].votes + "   " + superNode[i].address);
            System.out.println("..............");
        }
    }
    //承诺时间
    public static void sortMineNode(){
        for (int i=0;i<4;i++){
            for (int t=0;t<4;t++){
                if(1.0/superNode[t].promisetime>1.0/superNode[t+1].promisetime){
                    Node temp;
                    temp=superNode[t];
                    superNode[t]=superNode[t+1];
                    superNode[t+1]=temp;
                }
            }
        }
        for (int i=0;i<3;i++){
            mineNode[i]=new Node();
            mineNode[i].address=superNode[i].address;
            mineNode[i].promisetime=superNode[i].promisetime;
            mineNode[i].name= "mineNode"+i;
            System.out.println("承诺时间："+1.0 / mineNode[i].promisetime + "   " + mineNode[i].address);
        }
    }
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        //tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //循环遍历区块链进行hash检查
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //比对前一个区块的hash值和previousHash值
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //增加hash值是否已经计算过
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }
            for (int t=0;t<currentBlock.mp3Infos.size();t++){
                Song currentMp3Info=currentBlock.mp3Infos.get(t);
                if (!currentMp3Info.verifySignature()){
                    System.out.println("#Signature on Mp3Info(" + t + ") is Invalid");
                    return false;
                }
            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public synchronized static void addBlock() throws InterruptedException {
        ArrayList h=new ArrayList();
        int n=0;
        while (n<3){
            Block previousblock =blockchain.get(blockchain.size()-1);
            Block block=new Block(previousblock.hash);
            //block.addTransaction(walletA.sendFunds(walletB.publicKey,0.1f));
            block.addmp3Infos(mp3Infospool);//walletA.setMp3Info("C:\\Users\\HW\\Music\\HIT-5 - 懂爱的孤独 (慢版).mp3")
            block.name=Thread.currentThread().getName();
            block.mineBlock(difficulty);
            blockchain.add(block);
            block.height=blockchain.size();
            isChainValid();
            if(mp3Infospool!=null&&mp3Infospool.size()!=0){
                Song s=mp3Infospool.get(0);
                String song=s.getSong();
                String singer=s.getSinger();
                String album=s.getAlbum();
                String duration=s.getDuration();
                String id=s.getId();
                String height= String.valueOf(block.height);//String.valueOf(blockchain.get(blockchain.size()-1).height);
                String time=String.valueOf(block.timeStamp);
                Bundle bundle=new Bundle();
                bundle.putString("歌名",song);
                bundle.putString("歌手",singer);
                bundle.putString("专辑",album);
                bundle.putString("时间",duration);
                bundle.putString("id",id);
                bundle.putString("高度",height);
                bundle.putString("存证时间",time);
                Message msg=handler.obtainMessage(5);
                msg.obj=bundle;
                handler.sendMessage(msg);
            }
            mp3Infospool.clear();
            Thread.sleep(1000);
            h.add(blockchain.get(blockchain.size()-1).hash);
            n++;
        }
       // m.maodi(h);
    }

    @Override
    public void run() {


        if(Thread.currentThread().getName().equals(mineNode[0].name)){
            Block block1 = new Block(genesis.hash);
            block1.addmp3Infos(mp3Infospool);
            block1.name=Thread.currentThread().getName();
            block1.mineBlock(difficulty);
            blockchain.add(block1);
            block1.height=blockchain.size();
            isChainValid();

            Block block2 = new Block(block1.hash);
            block2.addmp3Infos(mp3Infospool);
            block2.name=Thread.currentThread().getName();
            block2.mineBlock(difficulty);
            blockchain.add(block2);
            block2.height=blockchain.size();
            isChainValid();

            Block block3 = new Block(block2.hash);
            block3.addmp3Infos(mp3Infospool);
            block3.name=Thread.currentThread().getName();
            block3.mineBlock(difficulty);
            blockchain.add(block3);
            block3.height=blockchain.size();
            isChainValid();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                addBlock();
                System.out.println( "循环开始 检验" + Thread.currentThread().getName());

        } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}
}