import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.Track


class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    private val artImageView: ImageView = itemView.findViewById(R.id.imageTrack)

    private fun isNetworkAvailable(): Boolean {
        val conManager = itemView.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.trackTime

        if (isNetworkAvailable()) {
        Glide.with(itemView.context)
            .load(track.artUrl)
            .transform(RoundedCorners(2))
            .error(R.drawable.placeholder)
            .into(artImageView)
        } else {

            Glide.with(itemView.context)
                .load(R.drawable.placeholder)
                .transform(RoundedCorners(2))
                .into(artImageView)
        }
    }


}