package app.deep_linking_b

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException

class CommunicationService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private val binder: IMyAidlInterface.Stub = object : IMyAidlInterface.Stub() {
        @Throws(RemoteException::class)
        override fun sendMessage(message: String): String {
            return "Received message: $message"
        }
    }
}