package jp.gr.java_conf.atsushitominaga

import io.realm.RealmObject

open class QuestionModel:RealmObject() {

    //id,gradeId,question,answer,choice1,choice2,choice3,explanation

    //id:0
    var id : String = ""
    //級:1
    var grandeId: String = ""
    //問題:2
    var question: String = ""
    //こたえ:3
    var answer: String = ""
    //選択肢1:4
    var choice1 : String = ""
    //選択肢2:5
    var choice2 : String = ""
    //選択肢3:6
    var choice3 : String = ""
    //解説: 7
    var explanation: String = ""

}