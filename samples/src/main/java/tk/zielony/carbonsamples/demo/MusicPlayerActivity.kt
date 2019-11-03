package tk.zielony.carbonsamples.demo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DataBindingComponent
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import kotlinx.android.synthetic.main.activity_musicplayer.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import java.io.Serializable

@ActivityAnnotation(layout = R.layout.activity_musicplayer, title = R.string.musicPlayerActivity_title)
class MusicPlayerActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val songs = arrayListOf(
                SongItem(1, "Code Around the Clock", 213),
                SongItem(2, "Compile Yourself", 225),
                SongItem(3, "Androidic Dreaming", 167),
                SongItem(4, "Carbonite Tonight", 192),
                SongItem(5, "Getting Away With Errors", 214),
                SongItem(6, "Nine Million XMLs", 202),
                SongItem(7, "Last Commit in the Repo", 276),
                SongItem(8, "Don't Fix Me Down", 234)
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = RowListAdapter(songs, RowFactory { parent -> DataBindingComponent<SongItem>(parent, R.layout.row_player_song) })
    }

    override fun applyTheme() {
        super.applyTheme()
        theme.applyStyle(R.style.Translucent, true)
    }
}

class SongItem(val id: Int, val name: String, val duration: Int) : Serializable {
    fun getDurationString(): String {
        return String.format("%d:%02d", duration / 60, duration % 60)
    }
}