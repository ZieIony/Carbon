package tk.zielony.carbonsamples.demo

import android.os.Bundle
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

class BackdropActivity:SamplesActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backdrop)
    }
}