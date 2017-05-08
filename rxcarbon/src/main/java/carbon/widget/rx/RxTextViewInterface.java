package carbon.widget.rx;

import android.annotation.SuppressLint;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Functions;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewBeforeTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

@SuppressLint("NewApi")
interface RxTextViewInterface {

    @CheckResult
    @NonNull
    default Observable<Integer> editorActions() {
        return editorActions(Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<Integer> editorActions(@NonNull Predicate<? super Integer> handled) {
        return RxTextView.editorActions((android.widget.TextView) this, handled);
    }

    @CheckResult
    @NonNull
    default Observable<TextViewEditorActionEvent> editorActionEvents() {
        return editorActionEvents(Functions.PREDICATE_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    default Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull Predicate<? super TextViewEditorActionEvent> handled) {
        return RxTextView.editorActionEvents((android.widget.TextView) this, handled);
    }

    @CheckResult
    @NonNull
    default InitialValueObservable<CharSequence> textChanges() {
        return RxTextView.textChanges((android.widget.TextView) this);
    }

    @CheckResult
    @NonNull
    default InitialValueObservable<TextViewTextChangeEvent> textChangeEvents() {
        return RxTextView.textChangeEvents((android.widget.TextView) this);
    }

    @CheckResult
    @NonNull
    default InitialValueObservable<TextViewBeforeTextChangeEvent> beforeTextChangeEvents() {
        return RxTextView.beforeTextChangeEvents((android.widget.TextView) this);
    }

    @CheckResult
    @NonNull
    default InitialValueObservable<TextViewAfterTextChangeEvent> afterTextChangeEvents() {
        return RxTextView.afterTextChangeEvents((android.widget.TextView) this);
    }

    @CheckResult
    @NonNull
    default Consumer<? super CharSequence> text() {
        return (Consumer<CharSequence>) ((android.widget.TextView) this)::setText;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> textRes() {
        return (Consumer<Integer>) ((android.widget.TextView) this)::setText;
    }

    @CheckResult
    @NonNull
    default Consumer<? super CharSequence> error() {
        return (Consumer<CharSequence>) ((android.widget.TextView) this)::setError;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> errorRes() {
        return (Consumer<Integer>) textRes -> ((android.widget.TextView) this).setError(((android.widget.TextView) this).getContext().getResources().getText(textRes));
    }

    @CheckResult
    @NonNull
    default Consumer<? super CharSequence> hint() {
        return (Consumer<CharSequence>) ((android.widget.TextView) this)::setHint;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> hintRes() {
        return (Consumer<Integer>) ((android.widget.TextView) this)::setHint;
    }

    @CheckResult
    @NonNull
    default Consumer<? super Integer> textColor() {
        return (Consumer<Integer>) ((android.widget.TextView) this)::setTextColor;
    }

}
