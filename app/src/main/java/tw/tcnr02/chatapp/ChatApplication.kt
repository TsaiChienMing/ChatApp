package tw.tcnr02.chatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication:Application() {

    //  it will take object AppModule provides fun ChatClient.
    @Inject
    lateinit var client:ChatClient

    override fun onCreate() {
        super.onCreate()
        //initialize this stream sbk
        ChatDomain.Builder(client,applicationContext).build()
    }
}