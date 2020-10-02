package jp.gr.java_conf.atsushitominaga

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), AlertDialogFragment.AlertDialogListener {

    lateinit var interstitialAd: InterstitialAd
    var gradeOfTest: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_sound_main.setOnClickListener {
            changeSoundMode(it)
        }

        importQuestionsFromCSV()

        setBannerAd()
        setInterstitialAd()

        btnGrade1.setOnClickListener {
            if (isPaidGrade1){
                gradeOfTest = 1
                goTest(gradeOfTest)
                return@setOnClickListener
            }
            purchaseGrade1()
        }


        btnGrade2.setOnClickListener {
            gradeOfTest = 2
            goTest(gradeOfTest)
        }


    }

    private fun purchaseGrade1() {
        //Todo 1級購入処理(アプリ内課金)
    }

    private fun goTest(gradeOfTest: Int) {
        if(!isDataSetFinished) return makeToast(this@MainActivity,getString(R.string.import_fail_notice))
        if (!interstitialAd.isLoaded) return makeToast(this@MainActivity,getString(R.string.import_fail_notice))

        val dialog = AlertDialogFragment()
        dialog.show(supportFragmentManager, "GoTestAlert")

    }

    private fun setInterstitialAd() {
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)
        loadInterstitialAd()
        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
               loadInterstitialAd()
                // TestActivityに行く処理
                goTestActivity(gradeOfTest)

            }

        }
    }

    private fun goTestActivity(gradeOfTest: Int) {
        val intent = Intent(this@MainActivity,TestActivity::class.java).apply {
            putExtra(IntentKey.GRADE_OF_TEST.name, gradeOfTest)
        }
        startActivity(intent)

    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    private fun setBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()

        updateUi()
        adView?.resume()
        if(!interstitialAd.isLoaded) loadInterstitialAd()
    }

    override fun onPause() {
        super.onPause()

        adView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        adView?.destroy()
    }

    private fun updateUi() {

        if(isPassGrade2 && isPaidGrade1){
            btnGrade1.setBackgroundResource(R.drawable.button_grade1)
            btnGrade1.isEnabled = true
        }else if (isPassGrade2 && !isPaidGrade1){
            btnGrade1.setBackgroundResource(R.drawable.button_go_grade1)
            btnGrade1.isEnabled = true
        }else{
            btnGrade1.setBackgroundResource(R.drawable.button_vague_grade1)
            btnGrade1.isEnabled = false
        }
    }

    private fun importQuestionsFromCSV() {

        val reader = setCSVReader()
        var tempList: MutableList<Array<String>>? = null

        try {
            tempList = reader.readAll()
            writeCSVDataToRealm(tempList!!)
        }catch (e: IOException){
            makeToast(this@MainActivity,getString(R.string.import_fail))
            isDataSetFinished = false
        }finally {
            reader.close()
        }




    }

    private fun writeCSVDataToRealm(tempList: MutableList<Array<String>>) {

        val realm = Realm.getDefaultInstance()
        val iterator = tempList.iterator()

        realm.executeTransactionAsync({
            while (iterator.hasNext()){
                val record = iterator.next()
                val questionDB = it.createObject(QuestionModel::class.java)
                questionDB.apply {
                    id = record[0]
                    gradeId = record[1]
                    question = record[2]
                    answer = record[3]
                    choice1 = record[4]
                    choice2 = record[5]
                    choice3 = record[6]
                    explanation = record[7]
                }
            }

        },{
            isDataSetFinished = true
            makeToast(this@MainActivity, getString(R.string.import_success))

        }, {
            isDataSetFinished = false
            makeToast(this@MainActivity,getString(R.string.import_fail))
        })


    }

    private fun setCSVReader(): CSVReader {
        val assetManager: AssetManager = resources.assets
        val inputStream = assetManager.open("Questions.csv")
        val parser = CSVParserBuilder().withSeparator(',').build()
        return CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(parser).build()

    }


    //AlertDialogFragment.AlertDialogListener
    override fun onPositiveButtonClicked() {
        interstitialAd.show()
    }

    //AlertDialogFragment.AlertDialogListener
    override fun onNegativeButtonClicked() {
        makeToast(this@MainActivity,getString(R.string.press_cancel))
    }


}
