package carbon.widget

open class ListSearchAdapter<Type> : SearchAdapter<Type> {

    private val data: List<Type>

    constructor(data: List<Type>) : super() {
        this.data = data
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): Type {
        return data[i]
    }

    override fun getItemWords(item: Type): Array<String> {
        return arrayOf(item.toString())
    }
}
