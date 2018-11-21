package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R

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

open class AvatarTextSubtextDateRow(parent: ViewGroup) : DataBindingComponent<AvatarTextItem>(parent, R.layout.carbon_row_avatartextsubtextdate)
open class AvatarTextSubtext2DateRow<Type : AvatarTextSubtextDateItem>(parent: ViewGroup) : DataBindingComponent<Type>(parent, R.layout.carbon_row_avatartextsubtext2date)
