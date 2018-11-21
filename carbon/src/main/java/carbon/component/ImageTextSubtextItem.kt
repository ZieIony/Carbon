package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R

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

open class ImageTextSubtextRow<Type : ImageTextSubtextItem>(parent: ViewGroup) : DataBindingComponent<Type>(parent, R.layout.carbon_row_imagetextsubtext)
