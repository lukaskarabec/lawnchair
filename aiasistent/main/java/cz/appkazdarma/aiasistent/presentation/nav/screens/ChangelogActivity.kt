package cz.appkazdarma.aiasistent.presentation.nav.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.appkazdarma.aiasistent.R

class ChangelogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changelog) // zobrazení seznamu změn aplikace
    }
}