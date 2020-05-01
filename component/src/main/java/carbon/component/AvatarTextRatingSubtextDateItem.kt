package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowAvatartextratingsubtextdateBinding
import java.io.Serializable

interface AvatarTextRatingSubtextDateItem : Serializable {
    val avatar: Drawable?
    val text: String?
    val rating: Int
    val subtext: String?
    val date: String?
}

open class DefaultAvatarTextRatingSubtextDateItem : AvatarTextRatingSubtextDateItem {
    override var avatar: Drawable? = null
    override var text: String? = null
    override var rating: Int = 0
    override var subtext: String? = null
    override var date: String? = null

    constructor()

    constructor(avatar: Drawable, text: String, rating: Int, subtext: String, date: String) {
        this.avatar = avatar
        this.text = text
        this.rating = rating
        this.subtext = subtext
        this.date = date
    }
}

open class AvatarTextRatingSubtextDateRow<Type : AvatarTextRatingSubtextDateItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_avatartextratingsubtextdate) {
    private val binding = CarbonRowAvatartextratingsubtextdateBinding.bind(view)

    override fun bind(data: Type) {
        binding.carbonAvatar.setImageDrawable(data.avatar)
        binding.carbonDate.text = data.date
        binding.carbonRating.rating = data.rating.toFloat()
        binding.carbonText.text = data.text
        binding.carbonSubtext.text = data.subtext
        binding.carbonMarker2.setText(data.subtext)
    }
}
