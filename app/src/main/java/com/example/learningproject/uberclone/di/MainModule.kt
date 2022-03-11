package com.example.learningproject.uberclone.di

import android.content.Context
import com.example.learningproject.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
object MainModule {
    //singleton is not is component of application
    @ActivityScoped //create a single instance of the class and reuse it,similar to singleton
    @Provides//tell the dagger that it is a function.
    @Named("String1")
    fun provideTestingString1(@ApplicationContext context: Context,testString:String)
    =context.getString(R.string.value)


    @Singleton //create a single instance of the class and reuse it.
    @Provides//tell the dagger that it is a function.
    @Named("String2")
    fun provideTestingString2()="This is a String we will Inject of Type2."
}