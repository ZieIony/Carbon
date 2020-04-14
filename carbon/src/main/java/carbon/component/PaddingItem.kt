package carbon.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import carbon.R
import java.io.Serializable

@Deprecated("Use PaddingItemDecoration instead to correctly make accessible lists")
open class PaddingItem(val padding: Int) : Serializable

@Deprecated("Use PaddingItemDecoration instead to correctly make accessible lists")
open class PaddingRow<Type : PaddingItem>(parent: ViewGroup) : Component<Type> {

    private val view: View

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_padding, parent, false)
        ViewCompat.setImportantForAccessibility(view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS)
    }

    override fun getView(): View {
        return view
    }

    override fun bind(data: Type) {
        view.minimumHeight = data.padding
    }
}
