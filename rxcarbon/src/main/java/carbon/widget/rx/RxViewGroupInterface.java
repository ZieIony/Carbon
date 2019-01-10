package carbon.widget.rx;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxViewGroup;
import com.jakewharton.rxbinding2.view.ViewGroupHierarchyChangeEvent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import io.reactivex.Observable;

@SuppressLint("NewApi")
interface RxViewGroupInterface {
    @CheckResult
    @NonNull
    default Observable<ViewGroupHierarchyChangeEvent> changeEvents() {
        return RxViewGroup.changeEvents((ViewGroup) this);
    }

}