package app.deep_linking_a

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.deep_linking_a.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mService: IMyAidlInterface? = null
    private var mBound = false

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            mService = IMyAidlInterface.Stub.asInterface(service)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent()
        intent.setComponent(
            ComponentName(
                "app.appb",
                "app.appb.CommunicationService"
            )
        )
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
    }

    private fun sendMessage(message: String) {
        if (mBound) {
            try {
                val response: String = mService!!.sendMessage(message)
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.sendToAppBButton.setOnClickListener {
            val packageName = "app.deep_linking_b"
            val isInstalled = isPackageInstalled(packageName, packageManager)
            if (isInstalled) {
                val message = "Hello from App A Using AIDL"
                sendMessage(message)
                val uri =
                    Uri.parse("appb://deeplink?message=${URLEncoder.encode(message, "UTF-8")}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please install App B first", Toast.LENGTH_SHORT).show()
            }
    }
}

fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
}