package jp.gr.java_conf.atsushitominaga

import android.media.SoundPool

var soundPool : SoundPool? = null

var soundIdCorrect = 0
var soundIdInCorrect = 0
var soundIdApplause = 0
var soundIdTin = 0

val PREF_FILE_NAME = "jp.gr.java_conf.atsushitominaga.quiz.status"

var isPassGrade1 = false    //一級合格
var isPassGrade2 = false    //二級合格
var isPaidGrade1 = false    //一級を買ったかどうか

enum class PrefKey{
    PASS_GRADE1,
    PASS_GRADE2
}

var isSoundOn = false

var isDataSetFinished = false   //CSVデータが取れたかどうか


