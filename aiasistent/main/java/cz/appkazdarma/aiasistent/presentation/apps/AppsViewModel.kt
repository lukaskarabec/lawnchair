package cz.appkazdarma.aiasistent.presentation.apps

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.domain.model.CompactAppInfo
import cz.appkazdarma.aiasistent.domain.use_case.get_apps.GetAppsUseCase
import cz.appkazdarma.aiasistent.domain.use_case.launch_app.LaunchAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getApps: GetAppsUseCase,
    private val launchAppIntent: LaunchAppUseCase
) : ViewModel() {

    private val _appsState = mutableStateOf<List<CompactAppInfo>>(emptyList())
    val appsState: State<List<CompactAppInfo>> = _appsState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _appsState.value = getApps() // načtení seznamu aplikací při inicializaci
        }
    }

    fun launchApp(packageName: String) {
        if (!launchAppIntent(packageName)) { // pokus o spuštění aplikace dle balíčku
            Log.e("AppsViewModel", "Couldn't launch app") // zalogování chyby při neúspěchu
        }
    }
}