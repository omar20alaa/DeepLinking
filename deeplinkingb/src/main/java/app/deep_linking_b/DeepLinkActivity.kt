package app.deep_linking_b

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.deep_linking_b.databinding.ActivityDeepLinkBinding


class DeepLinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeepLinkBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeepLinkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val data = intent.data
        if (data != null) {
            val message = data.getQueryParameter("message")
            binding.tvRecievedText.text = "Received deep link message: $message"
            Toast.makeText(this, "Received deep link message: $message", Toast.LENGTH_LONG).show()
        }
    }


}