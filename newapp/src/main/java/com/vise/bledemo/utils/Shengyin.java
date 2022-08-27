package com.vise.bledemo.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.vise.bledemo.R;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Shengyin {
    static MediaPlayer mediaPlayer01;
    static MediaPlayer mediaPlayer02;
    static MediaPlayer mediaPlayer03;
    static MediaPlayer mMediaPlayer;

    public static void register(Context context){
        mediaPlayer01 = MediaPlayer.create(context, R.raw.sheng1);
        mediaPlayer02 = MediaPlayer.create(context, R.raw.sheng1);
        mediaPlayer03 = MediaPlayer.create(context, R.raw.sheng1);
    }

    public static void play1(){
       // mediaPlayer01.start();
    }

    public static void play2(){
      //  mediaPlayer02.start();
    }

    public static void play3(){
      //  mediaPlayer03.start();
    }

    public static void registerPlayer(Context context){
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sheng1);
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        mediaPlayer.start();

      //  private MediaPlayer mMediaPlayer;
//
//        if(mMediaPlayer==null){
//            mMediaPlayer=new MediaPlayer();
//        }else{
//            mMediaPlayer.stop();
//            mMediaPlayer.release();
//            mMediaPlayer=new MediaPlayer();
//        }
//        try {
//            mMediaPlayer.setDataSource(String.valueOf(R.raw.sheng1));
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }
}
