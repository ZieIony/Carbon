package carbon.component

import android.view.ViewGroup
import carbon.R
import java.io.Serializable

interface HeaderItem : Serializable {
    val text: String?
}

open class DefaultHeaderItem : HeaderItem {
    override var text: String? = null

    constructor()

    constructor(text: String?) {
        this.text = text
    }
}

open class HeaderRow<Type : DefaultHeaderItem>(parent: ViewGroup) : DataBindingComponent<Type>(parent, R.layout.carbon_row_header)
open class PaddedHeaderRow<Type : DefaultHeaderItem>(parent: ViewGroup) : DataBindingComponent<Type>(parent, R.layout.carbon_row_paddedheader)
