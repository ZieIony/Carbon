package carbon.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import carbon.CarbonContextWrapper;
import carbon.R;

public class FloatingMenuItem implements MenuItem {
    private final FloatingActionMenu menu;
    private LinearLayout view;
    private TextView tooltip;
    private FloatingActionButton fab;
    private Drawable background;
    private boolean enabled;
    private boolean visible;
    private int iconRes;
    private Drawable icon;
    private CharSequence title;
    private int titleRes;
    private int id;
    private int groupId;
    private int order;
    private ColorStateList iconTint;

    public FloatingMenuItem(int id, int groupId, int order, CharSequence title, FloatingActionMenu menu) {
        this.id = id;
        this.groupId = groupId;
        this.order = order;
        this.title = title;
        this.menu = menu;
    }

    public void build() {
        int[] location = new int[2];
        menu.anchor.getLocationOnScreen(location);

        WindowManager wm = (WindowManager) menu.anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        boolean left = location[0] < display.getWidth() + menu.anchor.getWidth() - location[0];

        ViewGroup content = (ViewGroup) menu.getContentView();
        Context context = new CarbonContextWrapper(content.getContext());
        LayoutInflater inflater = LayoutInflater.from(context);

        view = (LinearLayout) inflater.inflate(left ? R.layout.carbon_floatingactionmenu_left : R.layout.carbon_floatingactionmenu_right, content, false);
        tooltip = (TextView) view.findViewById(R.id.carbon_tooltip);
        fab = (FloatingActionButton) view.findViewById(R.id.carbon_fab);

        tooltip.setText(titleRes == 0 ? title : context.getResources().getString(titleRes));
        if (background != null)
            fab.setBackgroundDrawable(background);
        if (iconTint != null)
            fab.setTint(iconTint);
        fab.setImageDrawable(icon != null ? icon : context.getResources().getDrawable(iconRes));  // always null
        fab.setOnClickListener(v -> {
            if (menu.listener != null)
                menu.listener.onMenuItemClick(this);
            menu.dismiss();
        });
        content.addView(view);

        view.setEnabled(enabled);
        view.setVisibilityImmediate(visible ? View.INVISIBLE : View.GONE);
    }

    @Override
    public int getItemId() {
        return id;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        this.title = title;
        if (tooltip != null)
            tooltip.setText(title);
        return this;
    }

    @Override
    public MenuItem setTitle(@StringRes int titleRes) {
        this.titleRes = titleRes;
        if (tooltip != null)
            tooltip.setText(titleRes);
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return tooltip.getText();
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return null;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        this.icon = icon;
        if (fab != null)
            fab.setImageDrawable(icon);
        return this;
    }

    @Override
    public MenuItem setIcon(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        if (fab != null)
            fab.setImageResource(iconRes);
        return this;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        return this;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        return this;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        return this;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        this.visible = visible;
        if (view != null)
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (view != null)
            view.setEnabled(enabled);
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        return this;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        return this;
    }

    @Override
    public MenuItem setActionView(@LayoutRes int resId) {
        return this;
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return this;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        return this;
    }

    public MenuItem setBackgroundDrawable(Drawable drawable) {
        background = drawable;
        if (fab != null)
            fab.setBackgroundDrawable(drawable);
        return this;
    }

    public Drawable getBackgroundDrawable() {
        return background;
    }

    public void performAction() {
        view.performClick();
    }

    public void setIconTint(ColorStateList tint) {
        iconTint = tint;
        if (fab != null)
            fab.setTint(tint);
    }
}
