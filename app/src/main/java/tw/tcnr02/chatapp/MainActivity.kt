package tw.tcnr02.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.getstream.sdk.chat.utils.extensions.inflater
import dagger.hilt.android.AndroidEntryPoint
import tw.tcnr02.chatapp.databinding.ActivityMainBinding
import java.util.zip.Inflater


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}