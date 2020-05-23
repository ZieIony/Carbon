package tk.zielony.carbonsamples.demo

import android.view.ViewGroup
import android.widget.TextView
import carbon.component.LayoutComponent
import tk.zielony.carbonsamples.R

class SongRow(parent: ViewGroup) : LayoutComponent<SongItem>(parent, R.layout.row_player_song) {
    override fun bind(data: SongItem) {
        view.findViewById<TextView>(R.id.id).text = data.id.toString()
        view.findViewById<TextView>(R.id.name).text = data.name
        view.findViewById<TextView>(R.id.duration).text = data.getDurationString()
    }
}
