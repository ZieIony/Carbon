package carbon.widget

open class ArraySearchAdapter<Type> : SearchAdapter<Type> {

    private val data: Array<Type>

    constructor(data: Array<Type>) : super() {
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
