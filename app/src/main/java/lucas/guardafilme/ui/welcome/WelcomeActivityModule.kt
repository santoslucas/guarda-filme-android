package lucas.guardafilme.ui.welcome

import dagger.Binds
import dagger.Module
import dagger.Provides
import lucas.guardafilme.data.AuthProvider
import lucas.guardafilme.data.GFAuthProvider
import lucas.guardafilme.di.ActivityScoped

/**
 * Created by lucassantos on 21/10/17.
 */
@Module
class WelcomeActivityModule {

    @ActivityScoped
    @Provides
    fun provideAuthProvider(): AuthProvider {
        return GFAuthProvider()
    }
}