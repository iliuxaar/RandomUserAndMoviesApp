package com.example.testapp.di

import android.content.Context
import com.example.coremodule.navigation.Router
import com.example.testapp.App
import com.example.testapp.activity.MainActivity
import com.example.testapp.di.module.ComponentsModule
import com.example.testapp.di.module.FragmentsModule
import com.example.testapp.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, FragmentsModule::class, ComponentsModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): AppComponent.Builder
        fun build(): AppComponent
    }

    fun retrofit(): Retrofit
    fun router(): Router
    fun retrySubject(): PublishSubject<Unit>
}