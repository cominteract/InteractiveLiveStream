package com.ainsigne.interactivelivestreaming.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
    }

    private fun showAlert(message: String?): AlertDialog? {
        val context: Context = getContext() ?: return AlertDialog.Builder(context).setTitle("Tips").setMessage(message)
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
        return null
    }

    protected fun showLongToast(msg: String?) {
        handler?.post(Runnable {
            if (this@BaseFragment == null || getContext() == null) {
                return@Runnable
            }
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        })
    }
}