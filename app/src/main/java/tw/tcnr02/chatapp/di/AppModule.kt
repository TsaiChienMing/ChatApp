package tw.tcnr02.chatapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import tw.tcnr02.chatapp.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context) =
        ChatClient.Builder(context.getString(R.string.api_key),context).build()
}