package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowImagetextsubtextBinding
import java.io.Serializable

interface ImageTextSubtextItem : Serializable {
    val image: Drawable?
    val text: String?
    val subtext: String?
}

open class DefaultImageTextSubtextItem : ImageTextSubtextItem {
    override var image: Drawable? = null
    override var text: String? = null
    override var subtext: String? = null

    constructor()

    constructor(image: Drawable?, text: String?, subtext: String?) {
        this.image = image
        this.text = text
        this.subtext = subtext
    }
}

open class ImageTextSubtextRow<Type : ImageTextSubtextItem>(parent: ViewGroup)
    : LayoutComponent<Type>(parent, R.layout.carbon_row_imagetextsubtext) {
    private val binding = CarbonRowImagetextsubtextBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonAvatar.setImageDrawable(data.image)
        binding.carbonText.text = data.text ?: ""
        binding.carbonSubtext.text = data.subtext ?: ""
    }
}
