package tk.zielony.carbonsamples.guidelines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tk.zielony.carbonsamples.R

class BasilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basil)
    }
}