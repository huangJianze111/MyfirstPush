package org.tensorflow.lite.examples.gesture;

import android.app.Activity;
import android.app.Application;

import com.rokid.glass.instruct.VoiceInstruction;
import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructEntity;

public class InstructApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化语音指令SDK，App运行时默认关闭百灵鸟
        VoiceInstruction.init(this);
        // 或者使用下面的，插件与百灵鸟混合模式
        // VoiceInstruction.init(this, false);


        // 设置全局指令，无全局指令可以删掉下面的代码
        // eg：”返回“指令
        VoiceInstruction.getInstance().addGlobalInstruct(
                new InstructEntity()
                        .setGlobal(true)
                        .addEntityKey(new EntityKey("", ""))
                        .addEntityKey(new EntityKey(EntityKey.Language.en, "back last page"))
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                try {
                                    if (act != null) {
                                        act.finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
    }
}