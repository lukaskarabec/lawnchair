package cz.appkazdarma.aiasistent.presentation.apps.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.appkazdarma.aiasistent.presentation.apps.AppsViewModel

@Composable
fun AppsScreen(
    modifier: Modifier = Modifier,
    viewModel: AppsViewModel = hiltViewModel()
) {
    val appsState by viewModel.appsState // pozorování seznamu aplikací
    // dočasné řešení pro práci s oblíbenými aplikacemi
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    Box(modifier = modifier
        .padding(horizontal = 16.dp)
        .fillMaxSize()) {

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2), // dvě sloupce v mřížce
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                count = appsState.size,
                key = { index -> appsState[index].packageName }) // unikátní klíč pro každý prvek
            { index ->
                AppCard(
                    appInfo = appsState[index],
                    animate = true,
                    onClick = {
                        viewModel.launchApp(appsState[index].packageName) // spuštění vybrané aplikace
                    },
                    onLongClick = {
                        // dočasná logika pro přidání či odebrání z oblíbených
                        val editor = sharedPreferences.edit()
                        val currentFavorites = sharedPreferences.getStringSet("favorites", emptySet()) ?: emptySet()
                        val newFavorites = if (currentFavorites.contains(appsState[index].packageName)) {
                            Toast.makeText(context, "Removed ${appsState[index].label} from favorites", Toast.LENGTH_SHORT).show()
                            currentFavorites - appsState[index].packageName
                        } else {
                            Toast.makeText(context, "Added ${appsState[index].label} to favorites", Toast.LENGTH_SHORT).show()
                            currentFavorites + appsState[index].packageName
                        }

                        editor.putStringSet("favorites", newFavorites)
                        editor.apply() // uložení změn
                    }
                )
            }
        }
    }
}