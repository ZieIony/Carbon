package carbon.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.lang.reflect.Field;

import carbon.R;

/**
 * Created by Marcin on 2015-09-29.
 */
public class EditorMenu extends PopupWindow implements ContextMenu {
    private View mAnchorView;

    public EditorMenu(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.carbon_editormenu, null, false));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
        setClippingEnabled(false);
    }

    public boolean show(View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, 0);

        update();

        View content = getContentView().findViewById(R.id.carbon_menuContent);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, 0);

        update();

        LinearLayout content = (LinearLayout) getContentView().findViewById(R.id.carbon_menuContent);
        content.setVisibilityImmediate(View.VISIBLE);

        return true;
    }

    public void update() {
        if (mAnchorView == null)
            return;

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_padding);
        int itemHeight = (int) res.getDimension(R.dimen.carbon_toolbarHeight);

        Rect windowRect = new Rect();
        mAnchorView.getWindowVisibleDisplayFrame(windowRect);

        int[] location = new int[2];
        mAnchorView.getLocationInWindow(location);

        int popupX = location[0] - margin;
        int popupY = location[1] - margin;

        LinearLayout content = (LinearLayout) getContentView().findViewById(R.id.carbon_menuContent);
        content.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec((int) content.getContext().getResources().getDimension(R.dimen.carbon_toolbarHeight), View.MeasureSpec.EXACTLY));
        update(popupX, popupY, content.getMeasuredWidth() + margin * 2, content.getMeasuredHeight() + margin * 2);

        super.update();
    }

    @Override
    public void dismiss() {
        LinearLayout content = (LinearLayout) getContentView().findViewById(R.id.carbon_menuContent);
        content.setVisibility(View.INVISIBLE);
        content.getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                EditorMenu.super.dismiss();
            }
        });
    }

    public void dismissImmediate() {
        super.dismiss();
    }


    @Override
    public MenuItem add(CharSequence charSequence) {
        return null;
    }

    @Override
    public MenuItem add(int i) {
        return null;
    }

    @Override
    public MenuItem add(int i, int i1, int i2, CharSequence charSequence) {
        return null;
    }

    @Override
    public MenuItem add(int i, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(CharSequence charSequence) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i, int i1, int i2, CharSequence charSequence) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public int addIntentOptions(int i, int i1, int i2, ComponentName componentName, Intent[] intents, Intent intent, int i3, MenuItem[] menuItems) {
        return 0;
    }

    @Override
    public void removeItem(int i) {

    }

    @Override
    public void removeGroup(int i) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void setGroupCheckable(int i, boolean b, boolean b1) {

    }

    @Override
    public void setGroupVisible(int i, boolean b) {

    }

    @Override
    public void setGroupEnabled(int i, boolean b) {

    }

    @Override
    public boolean hasVisibleItems() {
        View cut = getContentView().findViewById(R.id.carbon_cut);
        View copy = getContentView().findViewById(R.id.carbon_copy);
        View paste = getContentView().findViewById(R.id.carbon_paste);
        View selectAll = getContentView().findViewById(R.id.carbon_selectAll);

        return cut.getVisibility() == View.VISIBLE || copy.getVisibility() == View.VISIBLE || paste.getVisibility() == View.VISIBLE || selectAll.getVisibility() == View.VISIBLE;
    }

    @Override
    public MenuItem findItem(int i) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public MenuItem getItem(int i) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean performShortcut(int i, KeyEvent keyEvent, int i1) {
        return false;
    }

    @Override
    public boolean isShortcutKey(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean performIdentifierAction(int i, int i1) {
        return false;
    }

    @Override
    public void setQwertyMode(boolean b) {

    }

    @Override
    public ContextMenu setHeaderTitle(int i) {
        return null;
    }

    @Override
    public ContextMenu setHeaderTitle(CharSequence charSequence) {
        return null;
    }

    @Override
    public ContextMenu setHeaderIcon(int i) {
        return null;
    }

    @Override
    public ContextMenu setHeaderIcon(Drawable drawable) {
        return null;
    }

    @Override
    public ContextMenu setHeaderView(View view) {
        return null;
    }

    @Override
    public void clearHeader() {

    }

    public void initCopyVisible(final MenuItem item) {
        View button = getContentView().findViewById(R.id.carbon_copy);
        if (item != null) {
            final MenuItem.OnMenuItemClickListener mClickListener;
            try {
                Field mClickListenerField = item.getClass().getDeclaredField("mClickListener");
                mClickListenerField.setAccessible(true);
                mClickListener = (MenuItem.OnMenuItemClickListener) mClickListenerField.get(item);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onMenuItemClick(item);
                        dismiss();
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    public void initCutVisible(final MenuItem item) {
        View button = getContentView().findViewById(R.id.carbon_cut);
        if (item != null) {
            final MenuItem.OnMenuItemClickListener mClickListener;
            try {
                Field mClickListenerField = item.getClass().getDeclaredField("mClickListener");
                mClickListenerField.setAccessible(true);
                mClickListener = (MenuItem.OnMenuItemClickListener) mClickListenerField.get(item);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onMenuItemClick(item);
                        dismiss();
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    public void initPasteVisible(final MenuItem item) {
        View button = getContentView().findViewById(R.id.carbon_paste);
        if (item != null) {
            final MenuItem.OnMenuItemClickListener mClickListener;
            try {
                Field mClickListenerField = item.getClass().getDeclaredField("mClickListener");
                mClickListenerField.setAccessible(true);
                mClickListener = (MenuItem.OnMenuItemClickListener) mClickListenerField.get(item);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onMenuItemClick(item);
                        dismiss();
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    public void initSelectAllVisible(final MenuItem item) {
        View button = getContentView().findViewById(R.id.carbon_selectAll);
        if (item != null) {
            final MenuItem.OnMenuItemClickListener mClickListener;
            try {
                Field mClickListenerField = item.getClass().getDeclaredField("mClickListener");
                mClickListenerField.setAccessible(true);
                mClickListener = (MenuItem.OnMenuItemClickListener) mClickListenerField.get(item);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onMenuItemClick(item);
                        dismiss();
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }
}
