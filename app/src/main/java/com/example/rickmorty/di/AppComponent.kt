
import com.example.rickmorty.di.AppModule
import com.example.rickmorty.di.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelFactoryModule::class])
interface AppComponent
