package carbon.recycler;

import java.io.Serializable;

import carbon.component.ItemTransformer;

class RowDescriptor<TypeFrom extends Serializable, TypeTo extends Serializable> {
    ItemTransformer<TypeFrom, TypeTo> transformer;
    RowFactory<TypeTo> factory;

    public RowDescriptor(ItemTransformer<TypeFrom, TypeTo> transformer, RowFactory<TypeTo> factory) {
        this.transformer = transformer;
        this.factory = factory;
    }
}
