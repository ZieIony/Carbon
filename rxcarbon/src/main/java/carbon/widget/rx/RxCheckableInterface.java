package carbon.widget.rx;

import android.annotation.SuppressLint;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import io.reactivex.functions.Consumer;

@SuppressLint("NewApi")
interface RxCheckableInterface {

    @CheckResult
    @NonNull
    default InitialValueObservable<Boolean> checkedChanges() {
        return RxCompoundButton.checkedChanges((CompoundButton) this);
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> checked() {
        return ((CompoundButton) this)::setChecked;
    }

    /*@CheckResult
    @NonNull
    default Consumer<? super Object> toggle() {
        return RxCompoundButton.toggle((CompoundButton) this);
    }*/
}
