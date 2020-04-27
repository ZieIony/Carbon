package carbon.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowIconsearchBinding
import carbon.widget.*
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

class IconSearchRow<Type : IconSearchItem>(
        parent: ViewGroup,
        val adapter: SearchAdapter<*>,
        val listener: SearchEditText.OnFilterListener<*>
) : LayoutComponent<Type>(parent, R.layout.carbon_row_iconsearch) {

    private val binding = CarbonRowIconsearchBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonIcon.setImageDrawable(data.icon)
        binding.carbonQuery.setDataProvider(adapter)
        binding.carbonQuery.setOnFilterListener(listener)
    }

}
