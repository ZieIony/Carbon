package carbon.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.Carbon
import carbon.R
import carbon.component.LayoutComponent
import carbon.databinding.CarbonMenustripItemBinding
import carbon.drawable.ColorStateListFactory
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
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

    private inner class ItemComponent(parent: ViewGroup) : LayoutComponent<Item>(parent, R.layout.carbon_menustrip_item) {
        private val binding = CarbonMenustripItemBinding.bind(view)

        override fun bind(data: Item) {
            super.bind(data)
            binding.carbonIcon.setImageDrawable(data.icon)
            binding.carbonIcon.setTintList(data.iconTintList)
            binding.carbonText.text = data.title
            binding.carbonText.textColor = data.iconTintList
                    ?: ColorStateListFactory.makePrimaryText(context)

            if (selectionMode != SelectionMode.NONE)
                view.isSelected = selectedItems.contains(data)
        }
    }


    @Deprecated("Use itemFactory instead")
    var itemLayoutId: Int = 0

    private lateinit var _itemFactory: RowFactory<Item>
    var itemFactory: RowFactory<Item>
        get() = _itemFactory
        set(value) {
            adapter.putFactory(Item::class.java, value)
            _itemFactory = value
        }

    private lateinit var _orientation: carbon.view.Orientation
    var orientation: carbon.view.Orientation
        get() = _orientation
        set(value) {
            _orientation = value
            initItems()
        }

    var adapter: RowArrayAdapter<Serializable> = RowArrayAdapter()
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
        itemFactory = RowFactory<Item> { ItemComponent(this) }
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

        adapter.items = items
    }

    private fun initAdapter() {
        val vertical = orientation == carbon.view.Orientation.VERTICAL

        layoutManager = LinearLayoutManager(context, if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false)

        adapter.setOnItemClickedListener(Item::class.java, onItemClickedListener)

        setAdapter(adapter)
    }

    override fun setDivider(divider: Drawable?, height: Int) {
        /*val decoration = DividerItemDecoration(context, divider, height)
        decoration.setDrawAfter { i ->
            val items = this.items
            items != null && i < items.size - 1 && (items[i].groupId != items[i + 1].groupId)
        }
        addItemDecoration(decoration)*/
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
