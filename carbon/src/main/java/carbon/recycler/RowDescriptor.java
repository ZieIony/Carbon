package carbon.recycler;

import carbon.component.ItemTransformer;

class RowDescriptor<TypeFrom, TypeTo> {
    ItemTransformer<TypeFrom, TypeTo> transformer;
    RowFactory<TypeTo> factory;

    public RowDescriptor(ItemTransformer<TypeFrom, TypeTo> transformer, RowFactory<TypeTo> factory) {
        this.transformer = transformer;
        this.factory = factory;
    }
}
