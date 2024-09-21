package com.example.electricguitar.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electricguitar.MainActivity;
import com.example.electricguitar.MyDAO;
import com.example.electricguitar.R;
import com.example.electricguitar.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private Button play,save,empty;
    private EditText context,qpname;
    private MyDAO myDAO;
    private Handler handler = new Handler();
    private final int delay = 1000,right=200;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        myDAO = new MyDAO(getContext()); ;
        play=root.findViewById(R.id.play);
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        context=root.findViewById(R.id.edit_input);
        qpname=root.findViewById(R.id.edit_qpname);
        empty=root.findViewById(R.id.empty);
        save=root.findViewById(R.id.save);
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
        empty.setOnClickListener(new View.OnClickListener() {//清空输入框
            @Override
            public void onClick(View v) {
                context.setText(" ");
                qpname.setText(" ");

            }
        });
        save.setOnClickListener(new View.OnClickListener() {//将音谱保存
            @Override
            public void onClick(View v) {
                String n="liu";
                String t=qpname.getText().toString();
                String m=context.getText().toString();
                myDAO.insertInfomation(n,t,m);

            }
        });
        play.setOnClickListener(new View.OnClickListener() {//播放音谱
            @Override
            public void onClick(View v) {
                String s = context.getText().toString();
                List<Integer> audioList = new ArrayList<>();
                for (char c : s.toCharArray()) {

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





        return root;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
