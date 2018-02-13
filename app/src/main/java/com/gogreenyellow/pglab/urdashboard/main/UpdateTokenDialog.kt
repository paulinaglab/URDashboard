package com.gogreenyellow.pglab.urdashboard.main

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.gogreenyellow.pglab.urdashboard.R

/**
 * Created by Paulina on 13.02.2018.
 */
class UpdateTokenDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.dit_update_token_title)
                .setView(R.layout.dialog_insert_token)
                .setNegativeButton(R.string.dit_cancel_button) { dialog, which -> dismiss() }
                .setPositiveButton(R.string.dit_update_button) { dialog, which -> updateToken() }
                .create()
    }

    fun updateToken() {
        // TODO
    }
}