package carbon.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import java.io.Serializable

@Deprecated("Use DividerItemDecoration instead to correctly make accessible lists")
open class DividerItem : Serializable

@Deprecated("Use DividerItemDecoration instead to correctly make accessible lists")
open class DividerRow<Type : DividerItem>(parent: ViewGroup) : Component<Type>() {

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_divider, parent, false)
        ViewCompat.setImportantForAccessibility(view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS)
    }
}

@Deprecated("Use DividerItemDecoration instead to correctly make accessible lists")
open class PaddedDividerRow<Type : DividerItem>(parent: ViewGroup) : Component<Type>() {

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.carbon_row_paddeddivider, parent, false)
        ViewCompat.setImportantForAccessibility(view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS)
    }
}
