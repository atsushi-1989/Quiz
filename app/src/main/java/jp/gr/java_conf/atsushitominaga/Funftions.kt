package jp.gr.java_conf.atsushitominaga

import android.content.Context
import android.media.SoundPool
import android.widget.Toast


fun SoundPool.play2 ( soundId: Int){
    this.play(soundId,1.0f,1.0f,0,0,1.0f)
}

fun makeToast(content: Context, message: String){
    Toast.makeText(content,message,Toast.LENGTH_SHORT).show()
}