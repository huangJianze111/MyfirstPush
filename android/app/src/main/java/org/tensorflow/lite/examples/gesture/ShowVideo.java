package org.tensorflow.lite.examples.gesture;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.rokid.glass.instruct.InstructLifeManager;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;

    public class ShowVideo extends AppCompatActivity {
    VideoView video;
    boolean isPause=false;
    private  int miao=0;
    private InstructLifeManager mLifeManager;
    private  String TAG="jieduan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.show_video);
        super.onCreate(savedInstanceState);
        configInstruct();
        video=findViewById(R.id.vido);

        play(0);
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               video.resume();

            }
        });

       video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //设置 MediaPlayer 的 OnSeekComplete 监听
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        // seekTo 方法完成时的回调
                        if(isPause){
                            video.start();
                            isPause = false;
                        }
                    }
                });
            }
        });


    }
    private void play(int mes){
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/"+ R.raw.hemudu);
        video.setVideoURI(uri);
        video.seekTo(mes);
        video.start();

    }
    public void configInstruct() {
        mLifeManager = new InstructLifeManager(this, getLifecycle(), mInstructLifeListener);
        mLifeManager.addInstructEntity(
                new InstructEntity()
                        .addEntityKey(new EntityKey("播放", null))
                        .addEntityKey(new EntityKey(EntityKey.Language.en, "play"))
                        .setShowTips(true)
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                    video.start();
                            }
                        })
        )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("暂停", null))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "stop"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                       video.pause();
                                    }
                                })
                )
                .addInstructEntity(
                        new InstructEntity()
                                .addEntityKey(new EntityKey("快进", null))
                                .addEntityKey(new EntityKey(EntityKey.Language.en, "quick"))
                                .setShowTips(true)
                                .setCallback(new IInstructReceiver() {
                                    @Override
                                    public void onInstructReceive(Activity act, String key, InstructEntity instruct) {

                                            if(video.getCurrentPosition()+3000<video.getDuration()) {


                                                video.seekTo(video.getCurrentPosition()+3000);
                                                isPause=true;
                                                    };


                                    }
                                })
                );
        }

    private InstructLifeManager.IInstructLifeListener mInstructLifeListener = new InstructLifeManager.IInstructLifeListener() {
        @Override
        public boolean onInterceptCommand(String command) {
            if ("返回上一级".equals(command)) {
                Intent intent=new Intent(ShowVideo.this,CameraActivity.class);
                startActivity(intent);
                video.pause();

                return true;
            }

            return false;
        }

        @Override
        public void onTipsUiReady() {
            Log.d("AudioAi", "onTipsUiReady Call ");
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

