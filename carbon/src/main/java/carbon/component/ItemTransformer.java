package carbon.component;

public interface ItemTransformer<TypeFrom, TypeTo> {
    ItemTransformer EMPTY = item -> item;

    TypeTo transform(TypeFrom item);
}
