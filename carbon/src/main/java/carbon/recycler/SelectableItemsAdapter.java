package carbon.recycler;

import androidx.annotation.NonNull;

import java.util.List;

import carbon.view.SelectionMode;

public interface SelectableItemsAdapter<I> {
    void setSelectionMode(@NonNull SelectionMode selectionMode);

    SelectionMode getSelectionMode();

    void setSelectedIndices(List<Integer> selectedIndices);

    List<Integer> getSelectedIndices();

    void setSelectedItems(List<I> selectedItems);

    List<I> getSelectedItems();

    void selectItem(I item);
}
