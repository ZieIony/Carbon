package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowAvatartextBinding

import java.io.Serializable

interface AvatarTextItem : Serializable {
    val avatar: Drawable?
    val text: String?
}

open class DefaultAvatarTextItem : AvatarTextItem {
    override var avatar: Drawable? = null
    override var text: String? = null

    constructor()

    constructor(avatar: Drawable, text: String) {
        this.avatar = avatar
        this.text = text
    }
}

open class AvatarTextRow<Type : AvatarTextItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_avatartext) {
    private val binding = CarbonRowAvatartextBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonText.text = data.text.toString()
        binding.carbonAvatar.setImageDrawable(data.avatar)
    }
}
