package com.gogreenyellow.pglab.urdashboard.main

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.data.PreferenceStorage
import com.gogreenyellow.pglab.urdashboard.scanner.ScannerActivity
import com.gogreenyellow.pglab.urdashboard.util.TokenUtil


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

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener { openScanner() }
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
            updateToken()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
                .setTitle(R.string.dit_update_token_title)
                .setView(R.layout.dialog_insert_token)
                .setPositiveButton(R.string.dit_update_button, null)
                .setNeutralButton(R.string.dit_scan_qr_code, null)

        if (arguments!!.getBoolean(ARG_CANCELABLE)) {
            builder.setNegativeButton(R.string.dit_cancel_button) { _, _ -> dismiss() }
        }

        return builder.create()
    }

    private fun updateToken() {
        val tokenEditText = dialog?.findViewById<EditText>(R.id.am_token_edit_text)
        if (TextUtils.isEmpty(tokenEditText?.text)) {
            listener.tokenUpdateFailed(arguments!!.getBoolean(ARG_CANCELABLE))
            return
        }

        if (!TokenUtil.isValid(tokenEditText!!.text.toString())) {
            listener.tokenUpdateFailed(arguments!!.getBoolean(ARG_CANCELABLE))
            return
        }

        PreferenceStorage.getInstance(activity!!)?.token = tokenEditText?.text.toString()
        listener.tokenUpdated()
        dismiss()
    }

    private fun openScanner() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.CAMERA),
                    1)
        } else {
            startActivityForResult(Intent(activity, ScannerActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            dialog?.findViewById<EditText>(R.id.am_token_edit_text)?.setText(
                    data?.extras?.getString(ScannerActivity.RESULT_KEY))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(activity, ScannerActivity::class.java), 0)
                openScanner()
            }
        }
    }

    interface TokenUpdatesListener {
        fun tokenUpdated()
        fun tokenUpdateFailed(cancelable: Boolean)
    }
}