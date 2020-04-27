package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowAvatartextsubtext2dateBinding
import carbon.component.databinding.CarbonRowAvatartextsubtextdateBinding
import java.io.Serializable

interface AvatarTextSubtextDateItem : Serializable {
    val avatar: Drawable?
    val text: String?
    val subtext: String?
    val date: String?
}

open class DefaultAvatarTextSubtextDateItem : AvatarTextSubtextDateItem {
    override var avatar: Drawable? = null
    override var text: String? = null
    override var subtext: String? = null
    override var date: String? = null

    constructor()

    constructor(avatar: Drawable, text: String, subtext: String, date: String) {
        this.avatar = avatar
        this.text = text
        this.subtext = subtext
        this.date = date
    }
}

open class AvatarTextSubtextDateRow<Type : AvatarTextSubtextDateItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_avatartextsubtextdate) {
    private val binding = CarbonRowAvatartextsubtextdateBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonAvatar.setImageDrawable(data.avatar)
        binding.carbonDate.text = data.date ?: ""
        binding.carbonText.text = data.text ?: ""
        binding.carbonSubtext.text = data.subtext ?: ""
        binding.carbonMarker2.text = data.subtext
    }
}

open class AvatarTextSubtext2DateRow<Type : AvatarTextSubtextDateItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_avatartextsubtext2date) {
    private val binding = CarbonRowAvatartextsubtext2dateBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonAvatar.setImageDrawable(data.avatar)
        binding.carbonDate.text = data.date ?: ""
        binding.carbonText.text = data.text ?: ""
        binding.carbonSubtext.text = data.subtext ?: ""
        binding.carbonMarker2.text = data.subtext
    }
}
