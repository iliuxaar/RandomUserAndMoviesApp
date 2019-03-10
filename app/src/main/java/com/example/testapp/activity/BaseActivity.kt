package com.example.testapp.activity

import android.app.Dialog
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.coremodule.pm.BackHandler
import com.example.testapp.App
import com.example.testapp.R


abstract class BaseActivity : AppCompatActivity(), BackHandler {

    protected abstract val layoutId: Int

    private var dialog: ErrorDialog? = null

    private var isWasErrorInBackGround = false

    private val errorReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == App.ERROR_ACTION) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) createDialog()
                else isWasErrorInBackGround = true
            }
        }
    }

    private fun createDialog() {
        if (dialog == null) dialog = ErrorDialog()
        if (dialog?.isAdded!!) return
        dialog?.showNow(supportFragmentManager, getString(R.string.error_dialog_tag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(errorReceiver, IntentFilter(App.ERROR_ACTION))
        if(isWasErrorInBackGround) {
            createDialog()
            isWasErrorInBackGround = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceiver)
        dialog = null
    }

    override fun onBackPressed() {
        if (!handleBack()) super.onBackPressed()
    }

    abstract override fun handleBack(): Boolean
}

class ErrorDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
            .setMessage(getString(R.string.error_message_hint))
            .setPositiveButton(getString(R.string.retry_hint)) { _, _ -> dismiss() }
            .setNeutralButton(getString(R.string.quit_hint)) { _, _ -> activity?.finish() }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        App.component.retrySubject().onNext(Unit)
    }
}