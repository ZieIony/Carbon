package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.util.regex.Pattern;

import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;

/**
 * Created by Marcin on 2015-02-14.
 */
public class EditText extends android.widget.EditText {
    Roboto.Style style;
    private AnimUtils.Style inAnim, outAnim;
    int maxCharacters;
    int minCharacters;
    private Pattern pattern;
    float dividerPadding;
    int dividerColor;
    private Paint paint = new Paint();

    public EditText(Context context) {
        super(context);
        init(null, R.attr.carbon_editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditText, defStyle, 0);
        setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.EditText_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
        setInAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.EditText_carbon_inAnimation, 0)]);
        setOutAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.EditText_carbon_outAnimation, 0)]);
        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        setDividerPadding(a.getDimension(R.styleable.EditText_carbon_dividerPadding, 0));
        setDividerColor(a.getColor(R.styleable.EditText_carbon_dividerColor,0));
        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setColor(dividerColor);
        paint.setStrokeWidth(2);
        canvas.drawLine(0,getHeight()-dividerPadding,getWidth(),getHeight()-dividerPadding,paint);
    }

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    EditText.super.setVisibility(visibility);
                }
            });
        }
    }

    private void setTextStyle(Roboto.Style style) {
        this.style = style;
        if (!isInEditMode())
            super.setTypeface(Roboto.getTypeface(getContext(), style));
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public void setPattern(String pattern) {
        if(pattern==null) {
            this.pattern = null;
            return;
        }
        this.pattern = Pattern.compile(pattern);
    }

    public int getMinCharacters() {
        return minCharacters;
    }

    public void setMinCharacters(int minCharacters) {
        this.minCharacters = minCharacters;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public float getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(float dividerPadding) {
        this.dividerPadding = dividerPadding;
    }

    public AnimUtils.Style getOutAnim() {
        return outAnim;
    }

    public void setOutAnimation(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnim() {
        return inAnim;
    }

    public void setInAnimation(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }

    public Roboto.Style getStyle() {
        return style;
    }

    public void setStyle(Roboto.Style style) {
        this.style = style;
    }
}
