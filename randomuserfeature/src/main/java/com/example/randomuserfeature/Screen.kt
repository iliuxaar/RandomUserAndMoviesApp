package com.example.randomuserfeature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import me.dmdev.rxpm.base.PmSupportFragment


abstract class Screen<PM : ScreenPresentationModel> :
    PmSupportFragment<PM>(),
    BackHandler {

    abstract val screenLayout: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(screenLayout, container, false)
    }

    override fun onBindPresentationModel(pm: PM) {
        pm.errorDialog bindTo { message, _ ->
            AlertDialog.Builder(context!!)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
        }
    }

    override fun handleBack(): Boolean {
        presentationModel.backAction.consumer.accept(Unit)
        return true
    }

//    val progressConsumer = Consumer<Boolean> {
//        if (it) {
//            childFragmentManager.showDialog(ProgressDialog())
//        } else {
//            childFragmentManager
//                .findScreen<ProgressDialog>()
//                ?.dismiss()
//        }
//    }
}