package com.gogreenyellow.pglab.urdashboard.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage

import kotlinx.android.synthetic.main.dialog_insert_token.*


/**
 * Created by Paulina on 13.02.2018.
 */
class UpdateTokenDialog : DialogFragment() {

    private lateinit var listener: TokenUpdatesListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as TokenUpdatesListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.dit_update_token_title)
                .setView(R.layout.dialog_insert_token)
                .setNegativeButton(R.string.dit_cancel_button) { dialog, which -> dismiss() }
                .setPositiveButton(R.string.dit_update_button) { dialog, which -> updateToken() }
                .create()
    }

    private fun updateToken() {
        PreferenceStorage.getInstance(activity)?.token = am_token_edit_text.text.toString()
    }

    interface TokenUpdatesListener {
        fun tokenUpdated()
    }
}