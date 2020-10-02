package jp.gr.java_conf.atsushitominaga

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_test.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class TestActivity : AppCompatActivity(), AnswerFragment.OnFragmentInteracitonListener,
    FinishFragment.OnFragmentInteractionListener {

    var gradeOfTest : Int = 0

    var numberOfQuestion = INITIAL_NUMBER_OF_QUESTION //初期問題数（のこり問題数)
    var numberOfLife = INITIAL_LIFE
    var cntQuestion = 0 //何問出したか

    lateinit var realm: Realm
    lateinit var results: RealmResults<QuestionModel>
    lateinit var questionList: ArrayList<QuestionModel>


    lateinit var testStatus: TestStatus

    var strAnswer = ""      //こたえ
    var strExplanation = "" //解説

    lateinit var timer: Timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        fab_sound_test.setOnClickListener {
            changeSoundMode(it)
        }

        fab_point_get.setOnClickListener {
            purchasePoint()
        }

        gradeOfTest = intent.extras!!.getInt(IntentKey.GRADE_OF_TEST.name)

        setBannerAd()

        btnBack.setOnClickListener {
            Snackbar.make(it,getString(R.string.finish_quiz),Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.finish), View.OnClickListener {
                    finish()
                }).show()
        }

        testStatus = TestStatus.RUNNING

        realm = Realm.getDefaultInstance()
        setQuestionList(gradeOfTest)

        btnAnswer1.setOnClickListener { answerCheck(it) }
        btnAnswer2.setOnClickListener { answerCheck(it) }
        btnAnswer3.setOnClickListener { answerCheck(it) }
        btnAnswer4.setOnClickListener { answerCheck(it) }

        timer = Timer()

        setQuestion() //問題を出す

    }

    private fun answerCheck(view: View?) {
        setButtonEnabled(false)
        imageJudge.visibility = View.VISIBLE
        numberOfQuestion -= 1
        textNumberOfQuestions.text = numberOfQuestion.toString()

        val button : Button = view as Button

        if (button.text == strAnswer){
            imageJudge.setImageResource(R.drawable.image_correct)
            if (isSoundOn) soundPool?.play2(soundIdCorrect)
        }else{
            numberOfLife -= 1
            textLife.text = numberOfLife.toString()
            imageJudge.setImageResource(R.drawable.image_incorrect)
            if (isSoundOn) soundPool?.play2(soundIdInCorrect)
        }

        timer.schedule(1000,{runOnUiThread{
            supportFragmentManager.beginTransaction().add(R.id.container_answer_finish,
                AnswerFragment.newInstance(strAnswer,strExplanation)).commit()

        }})

    }

    private fun setQuestion() {

        setButtonEnabled(true)
        imageJudge.visibility = View.INVISIBLE
        textQuestion.text = questionList[cntQuestion].question
        strAnswer = questionList[cntQuestion].answer
        strExplanation = questionList[cntQuestion].explanation

        val choices = ArrayList<String>()
        choices.add(questionList[cntQuestion].answer)
        choices.add(questionList[cntQuestion].choice1)
        choices.add(questionList[cntQuestion].choice2)
        choices.add(questionList[cntQuestion].choice3)
        Collections.shuffle(choices)
        btnAnswer1.text = choices[0]
        btnAnswer2.text = choices[1]
        btnAnswer3.text = choices[2]
        btnAnswer4.text = choices[3]

        cntQuestion++
    }

    private fun setButtonEnabled(isEnabled: Boolean) {
        if(!isEnabled){
            btnAnswer1.isEnabled = false
            btnAnswer2.isEnabled = false
            btnAnswer3.isEnabled = false
            btnAnswer4.isEnabled = false
            btnBack.isEnabled = false
            fab_sound_test.isEnabled = false
            fab_point_get.isEnabled = false
            return
        }
        btnAnswer1.isEnabled = true
        btnAnswer2.isEnabled = true
        btnAnswer3.isEnabled = true
        btnAnswer4.isEnabled = true
        btnBack.isEnabled = true
        fab_sound_test.isEnabled = true
        fab_point_get.isEnabled = true

    }

    private fun setQuestionList(gradeOfTest: Int) {
        results = realm.where(QuestionModel::class.java).equalTo(QuestionModel::gradeId.name,gradeOfTest.toString()).findAll()
        questionList = ArrayList(results)
        Collections.shuffle(questionList)

    }

    override fun onResume() {
        super.onResume()
        updateUi()

    }

    private fun updateUi() {
        when(gradeOfTest){
            1 -> imageHeaderTest.setImageResource(R.drawable.image_header_grade1)
            2 -> imageHeaderTest.setImageResource(R.drawable.image_header_grade2)
        }

        if (isSoundOn){
            fab_sound_test.setImageResource(R.drawable.ic_volume_up_black_24dp)
        }else{
            fab_sound_test.setImageResource(R.drawable.ic_volume_off_black_24dp)
        }

        textNumberOfQuestions.setText(numberOfQuestion.toString())
        textLife.setText(numberOfLife.toString())


    }

    private fun setBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adView2.loadAd(adRequest)
    }

    private fun purchasePoint() {
        //Todo ３問減らし購入処理(アプリ内課金)

    }

    // AnswerFragment.OnFragmentInteracitonListener
    override fun onGoNextBtnOnAnswerFragmentClicked() {
        goNextStepAfterAnswerCheck()
    }

    private fun goNextStepAfterAnswerCheck() {
        if (numberOfLife <= 0){
            supportFragmentManager.beginTransaction()
                .add(R.id.container_answer_finish,FinishFragment.newInstance(gradeOfTest,TestStatus.FINISH_LOSE)).commit()
            return
        }

        if (numberOfQuestion <= 0){
            recordPassStatus(gradeOfTest)
            supportFragmentManager.beginTransaction()
                .add(R.id.container_answer_finish,FinishFragment.newInstance(gradeOfTest,TestStatus.FINISH_WIN)).commit()
            return
        }

        setQuestion()
    }

    private fun recordPassStatus(gradeOfTest: Int) {
        val prefs = this.getSharedPreferences(PREF_FILE_NAME,android.content.Context.MODE_PRIVATE)
        val editor = prefs.edit()

        when(gradeOfTest){
            1 -> {
                isPassGrade1 = true
                editor.putBoolean(PrefKey.PASS_GRADE1.name, isPaidGrade1).commit()
            }

            2 ->{
                isPassGrade2 = true
                editor.putBoolean(PrefKey.PASS_GRADE2.name, isPassGrade2).commit()
            }
        }

    }


    // FinishFragment.OnFragmentInteractionListener
    override fun onGoNextBtnOnFinishFragmentClicked() {
        backToMainActivity()
    }

    private fun backToMainActivity() {
        finish()
    }

}
