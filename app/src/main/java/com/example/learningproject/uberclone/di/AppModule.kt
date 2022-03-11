package com.example.learningproject.uberclone.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton //create a single instance of the class and reuse it.
    @Provides//tell the dagger that it is a function.
    @Named("String1")
     fun provideTestingString1()="This is a String we will Inject."


    @Singleton //create a single instance of the class and reuse it.
    @Provides//tell the dagger that it is a function.
    @Named("String2")
    fun provideTestingString2()="This is a String we will Inject of Type2."

}