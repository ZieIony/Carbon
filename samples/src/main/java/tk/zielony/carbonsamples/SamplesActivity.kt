package tk.zielony.carbonsamples

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class SamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                title = getString(it.title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.menu != 0)
                menuInflater.inflate(it.menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

}
