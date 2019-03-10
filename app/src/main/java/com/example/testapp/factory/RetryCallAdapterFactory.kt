package com.example.testapp.factory

import com.example.coremodule.ui.RetryCallAdapter
import com.example.testapp.App
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RetryCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        fun create() : RetryCallAdapterFactory = RetryCallAdapterFactory()
    }

    private val originalFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType : Type, annotations : Array<Annotation>, retrofit : Retrofit) : CallAdapter<*, *>? {
        val adapter = originalFactory.get(returnType, annotations, retrofit) ?: return null
        return RetryCallAdapter(adapter, App.component.retrySubject(), App.instance, App.ERROR_ACTION)
    }
}