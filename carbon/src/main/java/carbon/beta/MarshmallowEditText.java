package carbon.beta;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.view.ContextThemeWrapper;

import java.lang.reflect.Field;

import carbon.R;
import carbon.internal.EditTextMenu;
import carbon.widget.EditText;

public class MarshmallowEditText extends EditText {
    private static final int ID_CUT = android.R.id.cut;
    private static final int ID_COPY = android.R.id.copy;
    private static final int ID_PASTE = android.R.id.paste;
    private static final int ID_COPY_URL = android.R.id.copyUrl;
    private static final int ID_SELECT_ALL = android.R.id.selectAll;

    public MarshmallowEditText(Context context) {
        super(context);
        initActionModeCallback();
    }

    public MarshmallowEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initActionModeCallback();
    }

    public MarshmallowEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initActionModeCallback();
    }

    public MarshmallowEditText(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initActionModeCallback();
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean result = super.onTextContextMenuItem(id);
        if (id == ID_SELECT_ALL)
            showContextMenu();
        return result;
    }


    // -------------------------------
    // popup
    // -------------------------------


    EditTextMenu popupMenu;
    private boolean isShowingPopup = false;
    WindowManager brokenWindowManager = new WindowManager() {
        @Override
        public Display getDefaultDisplay() {
            return null;
        }

        @Override
        public void removeViewImmediate(View view) {

        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {
            final WindowManager.LayoutParams wparams
                    = (WindowManager.LayoutParams) params;
            view.setLayoutParams(wparams);
        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
            final WindowManager.LayoutParams wparams
                    = (WindowManager.LayoutParams) params;
            view.setLayoutParams(wparams);
        }

        @Override
        public void removeView(View view) {

        }
    };

    @Override
    public int getSelectionStart() {
        try {
            if (brokenWindowManager != null) {
                Field ccmf = android.widget.TextView.class.getDeclaredField("mCursorControllerMenu");
                ccmf.setAccessible(true);
                Object ccm = ccmf.get(this);
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindow");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindowArrowDown");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindowArrowUp");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                brokenWindowManager = null;
            }
        } catch (Exception e) {

        }
        return super.getSelectionStart();
    }

    @Override
    public ActionMode startActionMode(final ActionMode.Callback callback) {
        ActionMode.Callback c = new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return callback.onPrepareActionMode(mode, menu);
                //createMenu(menu);
            }

            public void onDestroyActionMode(ActionMode mode) {
                popupMenu.dismiss();
                callback.onDestroyActionMode(mode);
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return callback.onCreateActionMode(mode, menu);
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return callback.onActionItemClicked(mode, item);
            }
        };
        return super.startActionMode(c);
    }

    @Override
    public ActionMode startActionMode(final ActionMode.Callback callback, int type) {
        ActionMode.Callback c = new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                prepareMenu();
                return true;
            }

            public void onDestroyActionMode(ActionMode mode) {
                callback.onDestroyActionMode(mode);
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //              mode.setCustomView(new View(getContext()));
//                callback.onCreateActionMode(mode, menu);
                callback.onCreateActionMode(mode, menu);
                createMenu(menu);
                menu.clear();
                return true;//
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return callback.onActionItemClicked(mode, item);
            }
        };
        return super.startActionMode(c, type);
    }

    private void initActionModeCallback() {
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                createMenu(menu);
                return true;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            setCustomInsertionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    createMenu(menu);

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
        //createMenu(menu);
    }

    private void prepareMenu() {
        if (popupMenu.hasVisibleItems()) {
            popupMenu.show(this);
            isShowingPopup = true;
        }
    }

    private void createMenu(Menu menu) {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.carbon_editMenuTheme, outValue, true);
        int theme = outValue.resourceId;
        Context themedContext = new ContextThemeWrapper(getContext(), theme);

        popupMenu = new EditTextMenu(themedContext);
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowingPopup = false;
            }
        });

        popupMenu.initCopy(menu.findItem(ID_COPY));
        popupMenu.initCut(menu.findItem(ID_CUT));
        popupMenu.initPaste(menu.findItem(ID_PASTE));
        popupMenu.initSelectAll(menu.findItem(ID_SELECT_ALL));
        //menu.clear();
        /*try {
            mIgnoreActionUpEventField.set(editor, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);

        if (popupMenu != null)
            popupMenu.update();

        return result;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isShowingPopup)
            popupMenu.showImmediate(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isShowingPopup)
            popupMenu.dismissImmediate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.isShowingPopup = this.isShowingPopup ? 1 : 0;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.isShowingPopup = ss.isShowingPopup > 0;
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        int isShowingPopup;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(EditText.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.isShowingPopup = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeInt(this.isShowingPopup);
        }

        public Parcelable getSuperState() {
            return superState;
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
