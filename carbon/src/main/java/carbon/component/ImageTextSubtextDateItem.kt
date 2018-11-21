package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R

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

open class ImageTextSubtextDateRow<Type : ImageTextSubtextDateItem>(parent: ViewGroup) : DataBindingComponent<Type>(parent, R.layout.carbon_row_imagetextsubtextdate)
