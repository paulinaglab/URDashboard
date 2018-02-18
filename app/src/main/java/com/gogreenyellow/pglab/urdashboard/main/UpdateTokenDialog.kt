package com.gogreenyellow.pglab.urdashboard.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.EditText
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage

import kotlinx.android.synthetic.main.dialog_insert_token.*


/**
 * Created by Paulina on 13.02.2018.
 */
class UpdateTokenDialog : DialogFragment() {

    companion object {
        const val ARG_CANCELABLE = "cancelable"

        fun newInstance(cancelable: Boolean): UpdateTokenDialog {
            val fragment = UpdateTokenDialog()
            val args = Bundle()
            args.putBoolean(ARG_CANCELABLE, cancelable)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var listener: TokenUpdatesListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as TokenUpdatesListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
                .setTitle(R.string.dit_update_token_title)
                .setView(R.layout.dialog_insert_token)
                .setPositiveButton(R.string.dit_update_button) { dialog, which -> updateToken() }

        if (arguments.getBoolean(ARG_CANCELABLE)) {
            builder.setNegativeButton(R.string.dit_cancel_button) { dialog, which -> dismiss() }
        }

        return builder.create()
    }

    private fun updateToken() {
        val tokenEditText = dialog?.findViewById<EditText>(R.id.am_token_edit_text)
        if (!TextUtils.isEmpty(tokenEditText?.text)) {
            PreferenceStorage.getInstance(activity)?.token = tokenEditText?.text.toString()
            listener.tokenUpdated()
        } else {
            listener.tokenUpdateFailed(arguments.getBoolean(ARG_CANCELABLE))
        }
    }

    interface TokenUpdatesListener {
        fun tokenUpdated()
        fun tokenUpdateFailed(cancelable: Boolean)
    }
}