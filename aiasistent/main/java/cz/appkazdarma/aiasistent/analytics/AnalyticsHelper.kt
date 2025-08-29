package cz.appkazdarma.aiasistent.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

/**
 * Pomocný objekt pro logování analytických událostí do Firebase.
 */
object AnalyticsHelper {
    private var analytics: FirebaseAnalytics? = null // instance Firebase Analytics

    /** Inicializuje Firebase Analytics pro zadaný kontext. */
    fun initialize(context: Context) {
        analytics = Firebase.analytics // získání instance svázané s aplikací
    }

    /** Zaloguje zobrazení obrazovky se zadaným názvem. */
    fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName) // název obrazovky
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName) // třída obrazovky
        }
        analytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle) // odeslání události do Firebase
    }

    /** Zaloguje vlastní událost s volitelnými parametry. */
    fun logEvent(name: String, params: Bundle? = null) {
        analytics?.logEvent(name, params) // odeslání události s parametry, pokud existují
    }
}
