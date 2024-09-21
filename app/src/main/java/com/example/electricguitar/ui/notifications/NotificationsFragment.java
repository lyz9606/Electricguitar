package com.example.electricguitar.ui.notifications;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electricguitar.MyDAO;
import com.example.electricguitar.R;
import com.example.electricguitar.databinding.FragmentNotificationsBinding;
import com.example.electricguitar.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationsFragment extends Fragment {
    private ListView listView;
    private List<Map<String,Object>> listData;
    private Map<String,Object> listItem;
    private SimpleAdapter listAdapter;
    private Context mContext;
    private MyDAO myDAO;
    //private MyBord myReceiver;
    //private String a;
    private Handler handler = new Handler();
    private final int delay = 1000,right=200;


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.list_view);  // 获取列表视图控件
        myDAO = new MyDAO(getContext());  // 初始化 MyDAO 对象
        displayRecords("liu");// 显示数据库记录

        return root;
    }




    public void displayRecords(String s) {
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        Map<Character, int[]> toneMap = new HashMap<>();
        toneMap.put('1', new int[]{R.raw.d});
        toneMap.put('2', new int[]{R.raw.re});
        toneMap.put('3', new int[]{R.raw.me});
        toneMap.put('4', new int[]{R.raw.fa});
        toneMap.put('5', new int[]{R.raw.s});
        toneMap.put('6', new int[]{R.raw.la});
        toneMap.put('7', new int[]{R.raw.x});
        toneMap.put('8', new int[]{R.raw.d});
        toneMap.put('9', new int[]{R.raw.re});
        toneMap.put('0', new int[]{R.raw.me});
        int[] defaultAudio = new int[]{R.raw.df}; // 默认音频资源ID

        listData = new ArrayList<>();
        mContext = getContext();
        Cursor cursor = myDAO.query3(s);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String inform = cursor.getString(cursor.getColumnIndex("inform"));
            listItem = new HashMap<>();
            listItem.put("title", title);
            listItem.put("id", id);
            listItem.put("inform", inform);

            listData.add(listItem);
        }
        listAdapter = new SimpleAdapter(mContext,
                listData,
                R.layout.list_view,
                new String[]{"title", "inform"},
                new int[]{R.id.text_title, R.id.text_inform});
        listView.setAdapter(listAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> rec= (Map<String, Object>) listAdapter.getItem(position);  //从适配器取记录
                String seid=rec.get("id").toString();
                String title=rec.get("title").toString();
                String inf=rec.get("inform").toString();
                Intent intent=new Intent(getActivity(), operation.class);
                intent.putExtra("id",seid);
                intent.putExtra("title",title);
                intent.putExtra("inf",inf);
                startActivity(intent);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //列表项监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> rec= (Map<String, Object>) listAdapter.getItem(position);  //从适配器取记录
                String inf=rec.get("inform").toString();
                List<Integer> audioList = new ArrayList<>();
                for (char c : inf.toCharArray()) {

                    int[] audioFiles = toneMap.getOrDefault(c,defaultAudio); // 获取键盘字符对应的音频资源ID数组
                    if (audioFiles != null) {
                        for (int audio : audioFiles) {
                            audioList.add(audio);
                        }
                    }

                }
                playAudioSequentially(audioList, 0);



            }
            private void playAudioSequentially(List<Integer> audioList, int i) {//音谱播放的方法
                if (i < audioList.size()) {
                    int audioId = audioList.get(i);
                    if (audioId == ' ' || audioId == ',' || audioId == ';' || audioId == '/') {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playAudioSequentially(audioList, i + 1);
                            }
                        }, delay);
                    }else {
                        soundPool.load(getContext(), audioId, 1);
                        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                            @Override
                            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                                Log.d("test", "加载完成");
                                //开始播放
                                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1);


                                // 播放完当前音频后继续下一个音频
                                //playAudioSequentially(audioList, i + 1);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        playAudioSequentially(audioList, i + 1);
                                    }
                                }, right);


                            }

                        });
                    }
                }
            }
        });
    }
}