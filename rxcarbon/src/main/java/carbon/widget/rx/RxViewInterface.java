package carbon.widget.rx;

import android.annotation.SuppressLint;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.view.ViewAttachEvent;
import com.jakewharton.rxbinding2.view.ViewLayoutChangeEvent;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;

import java.util.concurrent.Callable;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import carbon.animation.AnimatedView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.M;
import static com.jakewharton.rxbinding2.internal.Functions.CALLABLE_ALWAYS_TRUE;
import static com.jakewharton.rxbinding2.internal.Functions.PREDICATE_ALWAYS_TRUE;

@SuppressLint("NewApi")
interface RxViewInterface {
    @CheckResult
    @NonNull
    default Observable<Object> attaches() {
        return RxView.attaches((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<ViewAttachEvent> attachEvents() {
        return RxView.attachEvents((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<Object> detaches() {
        return RxView.detaches((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<Object> clicks() {
        return RxView.clicks((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<DragEvent> drags() {
        return RxView.drags((View) this, PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<DragEvent> drags(@NonNull Predicate<? super DragEvent> handled) {
        return RxView.drags((View) this, handled);
    }

    @RequiresApi(JELLY_BEAN)
    @CheckResult
    @NonNull
    default Observable<Object> draws() {
        return RxView.draws((View) this);
    }

    @CheckResult
    @NonNull
    default InitialValueObservable<Boolean> focusChanges() {
        return RxView.focusChanges((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<Object> globalLayouts() {
        return RxView.globalLayouts((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<MotionEvent> hovers() {
        return RxView.hovers((View) this, PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<MotionEvent> hovers(@NonNull Predicate<? super MotionEvent> handled) {
        return RxView.hovers((View) this, handled);
    }

    @CheckResult
    @NonNull
    default Observable<Object> layoutChanges() {
        return RxView.layoutChanges((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<ViewLayoutChangeEvent> layoutChangeEvents() {
        return RxView.layoutChangeEvents((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<Object> longClicks() {
        return RxView.longClicks((View) this, CALLABLE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<Object> longClicks(@NonNull Callable<Boolean> handled) {
        return RxView.longClicks((View) this, handled);
    }

    @CheckResult
    @NonNull
    default Observable<Object> preDraws(@NonNull Callable<Boolean> proceedDrawingPass) {
        return RxView.preDraws((View) this, proceedDrawingPass);
    }

    @RequiresApi(M)
    @CheckResult
    @NonNull
    default Observable<ViewScrollChangeEvent> scrollChangeEvents() {
        return RxView.scrollChangeEvents((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<Integer> systemUiVisibilityChanges() {
        return RxView.systemUiVisibilityChanges((View) this);
    }

    @CheckResult
    @NonNull
    default Observable<MotionEvent> touches() {
        return RxView.touches((View) this, PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<MotionEvent> touches(@NonNull Predicate<? super MotionEvent> handled) {
        return RxView.touches((View) this, handled);
    }

    @CheckResult
    @NonNull
    default Observable<KeyEvent> keys() {
        return RxView.keys((View) this, PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<KeyEvent> keys(@NonNull Predicate<? super KeyEvent> handled) {
        return RxView.keys((View) this, handled);
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> activated() {
        return (Consumer<Boolean>) ((View) this)::setActivated;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> clickable() {
        return (Consumer<Boolean>) ((View) this)::setClickable;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> enabled() {
        return (Consumer<Boolean>) ((View) this)::setEnabled;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> pressed() {
        return (Consumer<Boolean>) ((View) this)::setPressed;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Boolean> selected() {
        return (Consumer<Boolean>) ((View) this)::setSelected;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> visibility() {
        return (Consumer<Integer>) ((View) this)::setVisibility;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> animatedVisibility() {
        return (Consumer<Integer>) ((AnimatedView) this)::animateVisibility;
    }

}
