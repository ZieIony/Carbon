package carbon.component;

import java.io.Serializable;

public interface ItemTransformer<TypeFrom extends Serializable, TypeTo extends Serializable> {
    ItemTransformer EMPTY = item -> item;

    TypeTo transform(TypeFrom item);
}
