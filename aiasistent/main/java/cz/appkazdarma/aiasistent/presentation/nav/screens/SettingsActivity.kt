import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cz.appkazdarma.aiasistent.R
import cz.appkazdarma.aiasistent.presentation.nav.screens.ChangelogActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nastaveni) // nastavení layoutu aktivity

        val buttonChangelog: Button = findViewById(R.id.buttonChangelog) // získání reference na tlačítko
        buttonChangelog.setOnClickListener {
            val intent = Intent(this, ChangelogActivity::class.java) // vytvoření intentu na changelog
            startActivity(intent) // spuštění nové aktivity
        }
    }
}