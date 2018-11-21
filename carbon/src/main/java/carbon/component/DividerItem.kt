package carbon.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import carbon.R
import java.io.Serializable

open class DividerItem : Serializable

open class DividerRow<Type : DividerItem>(parent: ViewGroup) : Component<Type> {

    private val view: View

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_divider, parent, false)
    }

    override fun getView(): View {
        return view
    }

}

open class PaddedDividerRow<Type : DividerItem>(parent: ViewGroup) : Component<Type> {

    private val view: View

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_paddeddivider, parent, false)
    }

    override fun getView(): View {
        return view
    }

}
