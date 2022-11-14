package com.example.sec13a1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Random rand;
    Button btn1, btn2, btn3, btn4, btnStart, btnDifficulty;
    ArrayList<Integer> list;
    int [] sounds = new int[4];
    Button [] buttons;
    SoundPool sp;
    boolean playing=false, hard=false;
    int index, counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnStart = findViewById(R.id.btnStart);
        btnDifficulty = findViewById(R.id.btnDifficulty);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnDifficulty.setOnClickListener(this);

        buttons = new Button[]{btn1, btn2, btn3, btn4};

        rand = new Random();
        list = new ArrayList<Integer>();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
            sp=new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(aa).build();
        }
        else {
            sp=new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        }

        sounds[0] = sp.load(this,R.raw.sound1,1);
        sounds[1] = sp.load(this,R.raw.sound2,1);
        sounds[2] = sp.load(this,R.raw.sound3,1);
        sounds[3] = sp.load(this,R.raw.sound4,1);
    }

    @Override
    public void onClick(View view) {
        if(view == btnStart){
            list.clear();
            playing = true;
            nextNum();
            play();
        }
        else if(view == btnDifficulty){
            if(hard){
                hard = false;
                btnDifficulty.setText("easy");
                btnDifficulty.setBackgroundColor(Color.BLUE);
            }
            else{
                hard = true;
                btnDifficulty.setText("hard");
                btnDifficulty.setBackgroundColor(Color.RED);
            }
        }
        else{
            index = java.util.Arrays.asList(buttons).indexOf(view);
            if(playing){
                if(counter == list.size()-1 && index == list.get(counter)){
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    counter=0;
                    nextNum();
                    play();
                }
                else if(index == list.get(counter)){
                    sp.play(sounds[list.get(index)],1, 1, 0, 0, 1);
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    counter++;
                }
                else{
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                    counter=0;
                    playing = false;
                }
            }
        }
    }

    public void nextNum(){
        list.add(rand.nextInt(4));
    }

    public void play(){
        if(hard){
            btnPlay(buttons[list.get(list.size()-1)] , sounds[list.get(list.size()-1)]);
        }
        else{
            Handler handler = new Handler();
            for(int i=0; i<list.size(); i++){
                index = 0;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnPlay(buttons[list.get(index)] , sounds[list.get(index)]);
                        index++;
                    }
                }, 1000*i);
            }
        }
    }

    public void btnPlay(Button btn, int sound){
        btn.setAlpha((float) 0.7);
        sp.play(sound,1, 1, 0, 0, 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                btn.setAlpha(1);
            }
        }, 500);
    }


}