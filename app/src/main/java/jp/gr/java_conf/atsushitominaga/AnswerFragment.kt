package jp.gr.java_conf.atsushitominaga

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_answer.*
import java.lang.RuntimeException
import kotlin.math.exp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_PARAM1 = IntentKey.ANSWER.name
private val ARG_PARAM2 = IntentKey.EXPLANATION.name

/**
 * A simple [Fragment] subclass.
 * Use the [AnswerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnswerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var answer: String? = null
    private var explanation: String? = null

    private var mListener: OnFragmentInteracitonListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            answer = it.getString(ARG_PARAM1)
            explanation = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textAnswer.text = answer
        textExplanation.text = explanation

        btnGoNextQ.setOnClickListener {
            mListener?.onGoNextBtnOnAnswerFragmentClicked()
            requireFragmentManager().beginTransaction().remove(this).commit()

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteracitonListener){
            mListener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteracitonListener{
        fun onGoNextBtnOnAnswerFragmentClicked()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnswerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(strAnswer: String, strExplanation: String) =
            AnswerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, strAnswer)
                    putString(ARG_PARAM2, strExplanation)
                }
            }
    }
}
