package lucas.guardafilme.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import lucas.guardafilme.GuardaFilmeApplication
import lucas.guardafilme.data.UserRepository
import lucas.guardafilme.ui.welcome.WelcomeActivityModule
import javax.inject.Singleton

/**
 * Created by lucassantos on 19/10/17.
 */
@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class))
interface ApplicationComponent {

    fun inject(application: GuardaFilmeApplication)

//    fun getUserRepository(): UserRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent

    }

}