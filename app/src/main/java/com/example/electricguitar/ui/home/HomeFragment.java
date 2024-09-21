package com.example.electricguitar.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electricguitar.MyDAO;
import com.example.electricguitar.R;
import com.example.electricguitar.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    // 声明变量
    private FragmentHomeBinding binding;
    private ListView listView;
    private List<Map<String,Object>> listData;
    private Map<String,Object> listItem;
    private SimpleAdapter listAdapter;
    private Context mContext;
    private MyDAO myDAO;
    private Button button;
    private Handler handler = new Handler();
    private final int delay = 300,right=200;
    private EditText editText;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button=root.findViewById(R.id.search_b);

        editText=root.findViewById(R.id.search);


        listView = root.findViewById(R.id.list_view);  // 获取列表视图控件
        myDAO = new MyDAO(getContext());  // 初始化 MyDAO 对象
        displayRecords();  // 显示数据库记录

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.clear();
                listAdapter.notifyDataSetChanged();
                String s=editText.getText().toString();

                Cursor cursor = myDAO.query2(s);
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

                listAdapter.notifyDataSetChanged(); // 更新列表视图的显示

            }
        });



        return root;
    }

    public void displayRecords() {//显示信息的方法
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
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);

        listData = new ArrayList<>();
        mContext = getContext();
        Cursor cursor = myDAO.allQuery();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}