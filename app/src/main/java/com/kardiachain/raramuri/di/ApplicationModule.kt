package com.kardiachain.raramuri.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kardiachain.raramuri.R
import com.kardiachain.raramuri.WearApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun providesMainApplicationInstance(@ApplicationContext context: Context): WearApplication {
        return context as WearApplication
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharePreference(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            application.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }
}
