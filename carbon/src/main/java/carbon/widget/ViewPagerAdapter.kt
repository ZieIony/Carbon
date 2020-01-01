package carbon.widget

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter


class ViewPagerAdapter : PagerAdapter {

    class Item(val view: View, val title: String?)

    val items: Array<Item>

    constructor(views: Array<View>) : super() {
        this.items = (views.map { Item(it, null) }).toTypedArray()
    }

    constructor(items: Array<Item>) : super() {
        this.items = items
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return items[position].title
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount() = items.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pager = container as ViewPager
        val view = items[position].view
        pager.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}