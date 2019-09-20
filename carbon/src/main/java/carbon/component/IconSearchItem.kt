package carbon.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowIconsearchBinding
import carbon.widget.SearchEditText
import java.io.Serializable

interface IconSearchItem : Serializable {
    val icon: Drawable
    val query: String?
    val hint: String?
}

class DefaultIconSearchItem : IconSearchItem {
    override var icon: Drawable
    override var query: String? = null
    override var hint: String? = null

    constructor(context: Context) {
        icon = context.resources.getDrawable(R.drawable.carbon_search)
    }

    constructor(context: Context, query: String, hint: String) : this(context) {
        this.query = query
        this.hint = hint
    }
}

class IconSearchRow<Type : IconSearchItem> : DataBindingComponent<Type> {

    constructor(parent: ViewGroup, dataProvider: SearchEditText.SearchDataProvider<*>, listener: SearchEditText.OnFilterListener) : super(parent, R.layout.carbon_row_iconsearch) {
        (binding as CarbonRowIconsearchBinding).carbonQuery.setDataProvider(dataProvider)
        (binding as CarbonRowIconsearchBinding).carbonQuery.setOnFilterListener(listener)
    }

}
