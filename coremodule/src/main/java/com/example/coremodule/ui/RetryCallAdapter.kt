package com.example.coremodule.ui

import android.app.Application
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import java.net.UnknownHostException

class RetryCallAdapter <R>(
    private val originalAdapter : CallAdapter<R, *>,
    private val retrySubject: Subject<Unit>,
    private val app: Application,
    private val action: String
): CallAdapter<R, Any> {

    override fun adapt(call : Call<R>) : Any {
        val adaptedValue = originalAdapter.adapt(call)
        return when (adaptedValue) {
            is Completable -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retrySubject.toFlowable(BackpressureStrategy.LATEST)
                            .observeOn(Schedulers.io())
                    }
            }
            is Single<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retrySubject.toFlowable(BackpressureStrategy.LATEST)
                            .observeOn(Schedulers.io())
                    }
            }
            is Maybe<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retrySubject.toFlowable(BackpressureStrategy.LATEST)
                            .observeOn(Schedulers.io())
                    }
            }
            is Observable<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen { retrySubject.observeOn(Schedulers.io()) }
            }
            is Flowable<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retrySubject.toFlowable(BackpressureStrategy.LATEST)
                            .observeOn(Schedulers.io())
                    }
            }
            else -> {
                adaptedValue
            }
        }
    }

    override fun responseType() : Type = originalAdapter.responseType()

    private fun sendBroadcast(throwable : Throwable) {
        LocalBroadcastManager.getInstance(app).sendBroadcast(Intent(action))
    }

    companion object {
        @JvmStatic fun isUnknownHostException(exc: Throwable): Boolean{
            return UnknownHostException::class.java.isInstance(exc) || (isUnknownHostException(exc.cause!!))
        }
    }
}