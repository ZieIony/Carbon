package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowImagetextsubtextdateBinding
import java.io.Serializable

interface ImageTextSubtextDateItem : Serializable {
    val image: Drawable?
    val text: String?
    val subtext: String?
    val date: String?
}

open class DefaultImageTextSubtextDateItem : ImageTextSubtextDateItem {
    override var image: Drawable? = null
    override var text: String? = null
    override var subtext: String? = null
    override var date: String? = null

    constructor()

    constructor(image: Drawable?, text: String?, subtext: String?, date: String?) {
        this.image = image
        this.text = text
        this.subtext = subtext
        this.date = date
    }
}

open class ImageTextSubtextDateRow<Type : ImageTextSubtextDateItem>(parent: ViewGroup)
    : LayoutComponent<Type>(parent, R.layout.carbon_row_imagetextsubtextdate) {
    private val binding = CarbonRowImagetextsubtextdateBinding.bind(view)

    override fun bind(data: Type) {
        binding.carbonAvatar.setImageDrawable(data.image)
        binding.carbonText.text = data.text ?: ""
        binding.carbonSubtext.text = data.subtext ?: ""
        binding.carbonDate.text = data.date ?: ""
    }
}

