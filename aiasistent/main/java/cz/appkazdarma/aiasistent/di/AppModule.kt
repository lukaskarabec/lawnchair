package cz.appkazdarma.aiasistent.di

import android.app.Application
import androidx.room.Room
import cz.appkazdarma.aiasistent.data.local.apps.repository.AppRepositoryImpl
import cz.appkazdarma.aiasistent.data.local.notifications.NotificationDatabase
import cz.appkazdarma.aiasistent.data.local.notifications.repository.NotificationRepositoryImpl
import cz.appkazdarma.aiasistent.data.local.events.EventDatabase
import cz.appkazdarma.aiasistent.data.local.events.repository.EventRepositoryImpl
import cz.appkazdarma.aiasistent.data.local.notes.NoteDatabase
import cz.appkazdarma.aiasistent.data.local.notes.repository.NoteRepositoryImpl
import cz.appkazdarma.aiasistent.data.local.browser.BrowserDatabase
import cz.appkazdarma.aiasistent.data.local.browser.repository.BrowserRepositoryImpl
import cz.appkazdarma.aiasistent.data.local.tasks.TaskDatabase
import cz.appkazdarma.aiasistent.data.local.tasks.repository.TaskRepositoryImpl
import cz.appkazdarma.aiasistent.data.remote.OpenWeatherApi
import cz.appkazdarma.aiasistent.data.remote.repository.WeatherRepositoryImpl
import cz.appkazdarma.aiasistent.domain.repository.AppRepository
import cz.appkazdarma.aiasistent.domain.repository.NotificationRepository
import cz.appkazdarma.aiasistent.domain.repository.WeatherRepository
import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import cz.appkazdarma.aiasistent.domain.repository.NoteRepository
import cz.appkazdarma.aiasistent.domain.repository.BrowserRepository
import cz.appkazdarma.aiasistent.domain.repository.TaskRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NotificationDatabase {
        return Room.databaseBuilder(
            app,
            NotificationDatabase::class.java,
            "notifications_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(db: NotificationDatabase, appRepository: AppRepository): NotificationRepository {
        return NotificationRepositoryImpl(db.notificationDao, appRepository)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {

//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()

        return Retrofit.Builder()
            .baseUrl(OpenWeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: OpenWeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFusedLocationClient(app: Application): FusedLocationProviderClient {
        return LocationServices
            .getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideAppRepository(app: Application): AppRepository {
        return AppRepositoryImpl(app)
    }

    @Provides
    @Singleton
    fun provideEventDatabase(app: Application): EventDatabase {
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            "events_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventRepository(db: EventDatabase): EventRepository {
        return EventRepositoryImpl(db.eventDao)
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideBrowserDatabase(app: Application): BrowserDatabase {
        return Room.databaseBuilder(
            app,
            BrowserDatabase::class.java,
            "browser_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBrowserRepository(db: BrowserDatabase): BrowserRepository {
        return BrowserRepositoryImpl(db.browserDao)
    }

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "tasks_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.taskDao)
    }


}