package jp.gr.java_conf.atsushitominaga


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertDialogFragment: DialogFragment() {


    var listener : AlertDialogListener? = null

    interface AlertDialogListener{
        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity()).apply {
            setTitle(getString(R.string.alert_go_test_title))
            setMessage(getString(R.string.alert_go_test_message))
            setPositiveButton("実行"){dialog, which ->
                listener?.onPositiveButtonClicked()

            }
            setNegativeButton("キャンセル"){dialog, which ->
                listener?.onNegativeButtonClicked()

            }
        }.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AlertDialogListener) {
            listener = context
        }

    }
}