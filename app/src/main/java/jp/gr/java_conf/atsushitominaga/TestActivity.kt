package jp.gr.java_conf.atsushitominaga

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_test.*
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity() {

    var gradeOfTest : Int = 0

    var numberOfQuestion = INITIAL_NUMBER_OF_QUESTION
    var numberOfLife = INITIAL_LIFE
    var cntQuestion = 0

    lateinit var realm: Realm
    lateinit var results: RealmResults<QuestionModel>
    lateinit var questionList: ArrayList<QuestionModel>



    lateinit var testStatus: TestStatus

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

}
