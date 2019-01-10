package carbon.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.SubMenu;
import android.view.View;

import java.io.Serializable;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class MenuItem implements android.view.MenuItem, Serializable {
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

    public MenuItem(int groupId, int id, int order, CharSequence title) {
        this.id = id;
        this.groupId = groupId;
        this.order = order;
        this.title = title;
    }

    public void build() {


        //tooltip.setText(titleRes == 0 ? title : context.getResources().getString(titleRes));
        //fab.setImageDrawable(icon != null ? icon : context.getResources().getDrawable(iconRes));  // always null
        /*fab.setOnClickListener(v -> {
            if (enabled) {
            //    if (menu.listener == null || menu.listener.onMenuItemClick(this))
              //      menu.dismiss();
            }
        });*/
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
    public android.view.MenuItem setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    @Override
    public android.view.MenuItem setTitle(@StringRes int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    @Override
    public android.view.MenuItem setTitleCondensed(CharSequence title) {
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return null;
    }

    @Override
    public android.view.MenuItem setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public android.view.MenuItem setIcon(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    public Drawable getIcon(Context context) {
        return iconRes == 0 ? icon : context.getResources().getDrawable(iconRes);
    }

    @Override
    public android.view.MenuItem setIntent(Intent intent) {
        return this;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
        return this;
    }

    @Override
    public android.view.MenuItem setNumericShortcut(char numericChar) {
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public android.view.MenuItem setCheckable(boolean checkable) {
        return this;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public android.view.MenuItem setChecked(boolean checked) {
        return this;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public android.view.MenuItem setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public android.view.MenuItem setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
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
    public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
        return this;
    }

    @Override
    public android.view.MenuItem setActionView(View view) {
        return this;
    }

    @Override
    public android.view.MenuItem setActionView(@LayoutRes int resId) {
        return this;
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
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
    public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        return this;
    }

    public android.view.MenuItem setBackgroundDrawable(Drawable drawable) {
        background = drawable;
        return this;
    }

    public Drawable getBackgroundDrawable() {
        return background;
    }

    public void performAction() {
        //   view.performClick();
    }

    public void setIconTint(ColorStateList tint) {
        iconTint = tint;
    }

    public ColorStateList getIconTint() {
        return iconTint;
    }
}
