package jp.gr.java_conf.atsushitominaga

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_finish.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_PARAM1 = IntentKey.GRADE_FINISH.name
private val ARG_PARAM2 = IntentKey.TEST_STATUS.name

/**
 * A simple [Fragment] subclass.
 * Use the [FinishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var gradeOfTest: Int = 0
    private var testStatus: Enum<TestStatus>? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gradeOfTest = it.getInt(ARG_PARAM1)
            testStatus = it.getSerializable(ARG_PARAM2) as TestStatus
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when(testStatus){
            TestStatus.FINISH_WIN ->{
                imageFinish.setImageResource(R.drawable.image_win)
                if(isSoundOn) soundPool?.play2(soundIdApplause)
            }
            TestStatus.FINISH_LOSE ->{
                imageFinish.setImageResource(R.drawable.image_lose)
                if(isSoundOn) soundPool?.play2(soundIdTin)
            }
        }

        btnGoNext.setOnClickListener {
            mListener?.onGoNextBtnOnFinishFragmentClicked()
            requireFragmentManager().beginTransaction().remove(this).commit()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener){
            mListener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener{
        fun onGoNextBtnOnFinishFragmentClicked()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinishFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(gradeOfTest: Int, testStatus: TestStatus) =
            FinishFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, gradeOfTest)
                    putSerializable(ARG_PARAM2, testStatus)
                }
            }
    }
}
