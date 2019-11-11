package carbon.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.Carbon
import carbon.R
import carbon.component.Component
import carbon.component.DataBindingComponent
import carbon.component.DividerItem
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import carbon.view.SelectionMode
import java.io.Serializable

open class MenuStrip : RecyclerView {

    open class Item : Serializable {
        var id: Int = 0
        var icon: Drawable? = null
        var title: CharSequence? = null
        var iconTintList: ColorStateList? = null
        var groupId: Int = 0

        constructor()

        constructor(id: Int, icon: Drawable, text: CharSequence) {
            this.id = id
            this.icon = icon
            this.title = text
        }

        constructor(menuItem: MenuItem) {
            id = menuItem.itemId
            try {   // breaks preview
                this.icon = menuItem.icon
            } catch (e: Exception) {
            }
            this.title = menuItem.title
            iconTintList = MenuItemCompat.getIconTintList(menuItem)
            groupId = menuItem.groupId
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Item

            if (id != other.id) return false
            if (title != other.title) return false
            if (groupId != other.groupId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + groupId
            return result
        }
    }

    private class SeparatorComponent : Component<DividerItem> {
        private var view: View

        constructor(parent: ViewGroup, layoutId: Int) {
            this.view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        }

        override fun getView(): View = view
    }

    private inner class ItemComponent(parent: ViewGroup) : DataBindingComponent<Item>(parent, itemLayoutId) {
        override fun bind(data: Item) {
            super.bind(data)
            if (selectionMode != SelectionMode.NONE)
                view.isSelected = selectedItems.contains(data)
        }
    }


    private var _itemLayoutId: Int = 0
    var itemLayoutId: Int
        get() = _itemLayoutId
        set(value) {
            _itemLayoutId = value
            initItems()
        }

    private var _separatorLayoutId: Int = 0
    var separatorLayoutId: Int
        get() = _separatorLayoutId
        set(value) {
            _separatorLayoutId = value
            initItems()
        }

    private lateinit var _orientation: carbon.view.Orientation
    var orientation: carbon.view.Orientation
        get() = _orientation
        set(value) {
            _orientation = value
            initItems()
        }

    var adapter: RowListAdapter<Serializable> = RowListAdapter(Item::class.java, RowFactory<Item> { ItemComponent(it) })
    var selectionMode: SelectionMode
        get() = adapter.selectionMode
        set(value) {
            adapter.selectionMode = value
        }

    private var _onItemClickedListener: OnItemClickedListener<Item>? = null
    var onItemClickedListener
        get() = _onItemClickedListener
        set(value) {
            _onItemClickedListener = value
            initItems()
        }

    private var items: Array<Item>? = null
    var menuItems: Array<Item>?
        get() = items
        set(value) {
            this.items = value
            initItems()
        }

    var selectedItems: List<Serializable>
        get() = adapter.selectedItems
        set(value) {
            adapter.selectedItems = value
            initItems()
        }

    var selectedIndices: List<Int>
        get() = adapter.selectedIndices
        set(value) {
            adapter.selectedIndices = value
            initItems()
        }

    constructor(context: Context) : super(context, null, R.attr.carbon_menuStripStyle) {
        initMenuStrip(null, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, R.attr.carbon_menuStripStyle) {
        initMenuStrip(attrs, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initMenuStrip(attrs, defStyleAttr)
    }

    private fun initMenuStrip(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuStrip, defStyleAttr, R.style.carbon_MenuStrip)

        orientation = carbon.view.Orientation.values()[a.getInt(R.styleable.MenuStrip_android_orientation, carbon.view.Orientation.VERTICAL.ordinal)]
        itemLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_itemLayout, R.layout.carbon_menustrip_item)
        separatorLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_separatorLayout, 0)
        selectionMode = SelectionMode.values()[a.getInt(R.styleable.MenuStrip_carbon_selectionMode, SelectionMode.NONE.ordinal)]
        val menuId = a.getResourceId(R.styleable.MenuStrip_carbon_menu, 0)
        if (menuId != 0)
            setMenu(menuId)

        a.recycle()
    }

    fun setMenu(resId: Int) = setMenu(Carbon.getMenu(context, resId))

    fun setMenu(menu: Menu) {
        items = (0 until menu.size()).map { Item(menu.getItem(it)) }.toTypedArray()
        initItems()
    }

    private fun initItems() {
        if (items == null)
            return

        initAdapter()

        val items: ArrayList<Serializable> = arrayListOf(*items!!)
        var i = 0
        while (i < items.size - 1) {
            if ((items[i] as Item).groupId != (items[i + 1] as Item).groupId)
                items.add(++i, DividerItem())
            i++
        }
        adapter.items = items
    }

    private fun initAdapter() {
        val vertical = orientation == carbon.view.Orientation.VERTICAL

        var separatorLayoutId = this.separatorLayoutId
        if (separatorLayoutId == 0)
            separatorLayoutId = if (vertical) R.layout.carbon_menustrip_hseparator_item else R.layout.carbon_menustrip_vseparator_item

        layoutManager = LinearLayoutManager(context, if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false)
        adapter.putFactory(DividerItem::class.java, { SeparatorComponent(it, separatorLayoutId) })

        adapter.setOnItemClickedListener(Item::class.java, onItemClickedListener)

        setAdapter(adapter)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        //begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()

        val ss = SavedState(superState)
        //end

        ss.selectedIndices = ArrayList(selectedIndices)

        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        //begin boilerplate code so parent classes can restore state
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        //end

        selectedIndices = state.selectedIndices
    }

    private open class SavedState : Parcelable {

        lateinit var selectedIndices: ArrayList<Int>

        var superState: Parcelable? = null

        constructor() {
            superState = null
        }

        constructor(superState: Parcelable?) {
            this.superState = if (superState !== EMPTY_STATE) superState else null
        }

        private constructor(parcel: Parcel) {
            val superState = parcel.readParcelable<Parcelable>(BottomNavigationView::class.java.classLoader)
            this.superState = superState ?: EMPTY_STATE
            this.selectedIndices = parcel.readSerializable() as ArrayList<Int>
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeParcelable(superState, flags)
            out.writeSerializable(this.selectedIndices)
        }

        companion object {
            val EMPTY_STATE: SavedState = object : MenuStrip.SavedState() {
            }

            //required field that makes Parcelables from a Parcel
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

}
