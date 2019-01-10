package carbon.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import carbon.R
import java.io.Serializable

open class PaddingItem(val padding: Int) : Serializable

open class PaddingRow<Type : PaddingItem>(parent: ViewGroup) : Component<Type> {

    private val view: View

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_padding, parent, false)
    }

    override fun getView(): View {
        return view
    }

    override fun bind(data: Type) {
        view.minimumHeight = data.padding
    }
}
