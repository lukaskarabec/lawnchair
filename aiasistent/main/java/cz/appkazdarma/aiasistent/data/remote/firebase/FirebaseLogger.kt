package cz.appkazdarma.aiasistent.data.remote.firebase

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility object for logging arbitrary data to Firebase Realtime Database.
 *
 * Data are organised in the following hierarchy:
 * `logs/{date}/{time}/{device}/{part}`
 * where date is formatted as yyyyMMdd and time as HHmmssSSS.
 *
 * @param part      Identifier of the part of the application sending the data.
 *                  Slashes in [part] create additional path segments.
 * @param data      Arbitrary object to be stored under the generated path.
 */
object FirebaseLogger {
    private val rootRef = FirebaseDatabase.getInstance().reference.child("logs")

    fun log(part: String, data: Any) {
        val now = Date()
        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(now)
        val time = SimpleDateFormat("HHmmssSSS", Locale.getDefault()).format(now)
        val device = Build.MODEL ?: "unknown"

        rootRef
            .child(date)
            .child(time)
            .child(device)
            .child(part)
            .push()
            .setValue(data)
    }
}

