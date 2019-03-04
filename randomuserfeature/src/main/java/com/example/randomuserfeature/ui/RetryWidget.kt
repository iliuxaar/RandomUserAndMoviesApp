package com.example.randomuserfeature.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.randomuserfeature.R
import kotlinx.android.synthetic.main.retry_widget.view.*

class RetryWidget: ConstraintLayout {

    constructor(context: Context?) : super(context) { initView(context)}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {initView(context)}

    private fun initView(context: Context?){
        inflate(context, R.layout.retry_widget, this)
    }

    public fun getButton(): Button = retryButton

}