package cz.appkazdarma.aiasistent.presentation.home

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.domain.model.CompactAppInfo
import cz.appkazdarma.aiasistent.domain.model.GeminiItem
import cz.appkazdarma.aiasistent.domain.model.NetworkUsageItem
import cz.appkazdarma.aiasistent.domain.model.WeatherWidgetItem
import cz.appkazdarma.aiasistent.domain.use_case.get_app.GetAppUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_app.StringType
import cz.appkazdarma.aiasistent.domain.use_case.get_formatted_date.GetFormattedDateUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_prompt.GetGeminiPromptUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_gemini_response.GetGeminiResponseUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_weather_widget.GetWeatherWidgetUseCase
import cz.appkazdarma.aiasistent.domain.use_case.launch_app.LaunchAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherWidget: GetWeatherWidgetUseCase,
    private val getGeminiPrompt: GetGeminiPromptUseCase,
    private val getGeminiResponse: GetGeminiResponseUseCase,
    private val getFormattedDate: GetFormattedDateUseCase,
    private val launchAppIntent: LaunchAppUseCase,
    private val getApp: GetAppUseCase
) : ViewModel() {

    private var lastUpdate: Long = 0
    private val updateThreshold = 10 * 60 * 1000

    private var weatherJob: Job? = null
    private var geminiJob: Job? = null

    private val _dateState = mutableStateOf("")
    val dateState: State<String> = _dateState

    private val _weatherState = mutableStateOf<Resource<WeatherWidgetItem>>(Resource.Loading())
    val weatherState: State<Resource<WeatherWidgetItem>> = _weatherState

    private val _geminiState = mutableStateOf<Resource<GeminiItem>>(Resource.Loading())
    val geminiState: State<Resource<GeminiItem>> = _geminiState

    private val _backgroundImagePath = mutableStateOf<String?>(null)
    val backgroundImagePath: State<String?> = _backgroundImagePath

    val showNetworkSpeed = mutableStateOf(false)
    val networkUsageState = mutableStateOf<NetworkUsageItem?>(null)

    val showScreenTime = mutableStateOf(false)
    val screenTimeDetailed = mutableStateOf(false)

    //maybe move this up to navscreen
    private val _eventFlow = MutableSharedFlow<HomeUIEvent>()
    val eventFlow = _eventFlow

    fun updateContents() {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastUpdate = currentTime - lastUpdate

        _dateState.value = getFormattedDate()

        if (timeSinceLastUpdate > updateThreshold) {
            lastUpdate = currentTime

            viewModelScope.launch(Dispatchers.IO) {
                updateWeatherWidget()
                updateGeminiResponse()
            }
        }
    }

    fun resetLastUpdate() {
        lastUpdate = 0
    }

    fun completeGeminiAnimationState() {
        when (_geminiState.value) {
            is Resource.Success -> {
                _geminiState.value.data?.let {
                    _geminiState.value = Resource.Success(it.copy(hasAnimated = true))
                }
            }

            else -> Unit
        }
    }

    fun launchApp(packageName: String) {
        if (!launchAppIntent(packageName)) {
            viewModelScope.launch {
                _eventFlow.emit(HomeUIEvent.ShowSnackbar("Couldn't launch app"))
            }
        }
    }

    suspend fun getAppByPackageName(packageName: String) : CompactAppInfo? {
        return getApp(packageName, StringType.APP_PACKAGE_NAME)
    }

    private suspend fun updateGeminiResponse() {
        geminiJob?.cancel()
        _geminiState.value = Resource.Loading()

        val (prompt, history) = getGeminiPrompt()

        // can be improved
        geminiJob = getGeminiResponse(prompt, history)
            .onEach { result ->
                _geminiState.value = when (result) {
                    is Resource.Success -> Resource.Success(result.data as GeminiItem)
                    is Resource.Loading -> Resource.Loading(result.data)
                    is Resource.Error -> {
                        result.message?.let { message ->
                            _eventFlow.emit(HomeUIEvent.ShowSnackbar(message))
                        }

                        Resource.Error("Couldn't get Gemini response")
                    }
                }
            }.launchIn(viewModelScope)
    }

    private suspend fun updateWeatherWidget() {
        weatherJob?.cancel()
        _weatherState.value = Resource.Loading()

        // can be improved
        weatherJob = getWeatherWidget()
            .onEach { result ->
                _weatherState.value = when (result) {
                    is Resource.Success -> Resource.Success(result.data as WeatherWidgetItem)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> {
                        result.message?.let { message ->
                            _eventFlow.emit(HomeUIEvent.ShowSnackbar(message))
                        }

                       Resource.Error("No WX")
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun setBackgroundImagePath(path: String) {
        _backgroundImagePath.value = path
    }

    fun saveBackgroundImagePath(context: Context) {
        context.getSharedPreferences("home", Context.MODE_PRIVATE)
            .edit()
            .putString("backgroundImagePath", _backgroundImagePath.value)
            .apply()
    }

    fun setShowNetworkSpeed(context: Context, show: Boolean) {
        showNetworkSpeed.value = show
    }

    fun loadNetworkUsage(context: Context) {
        // TODO: implement network usage retrieval
    }

    fun setShowScreenTime(context: Context, show: Boolean) {
        showScreenTime.value = show
    }

    fun loadScreenTime() {
        // TODO: implement screen time retrieval
    }

    fun setScreenTimeDetailed(context: Context, detailed: Boolean) {
        screenTimeDetailed.value = detailed
    }

}