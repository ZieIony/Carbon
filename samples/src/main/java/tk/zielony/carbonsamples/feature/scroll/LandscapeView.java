package tk.zielony.carbonsamples.feature.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import carbon.internal.ArgbEvaluator;
import carbon.internal.MathUtils;
import carbon.widget.FrameLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-03-18.
 */

public class LandscapeView extends FrameLayout implements ScrollChild {
    private static final float TREE_RANDOM = 0.6f;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random = new Random();
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    List<Landscape> landscapes = new ArrayList<>();
    private float starSize, starX, starY;

    List<Star> stars = new ArrayList<>();

    // params
    boolean drawStars = true, drawSun = true;
    int starColor = 0x3fffffff, sunColor, skyColor, landscapeColor, fogColor, planesCount;
    float landscapeHeight;
    private float padding;

    public LandscapeView(Context context) {
        super(context);
        init();
    }

    public LandscapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LandscapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LandscapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        sunColor = getResources().getColor(R.color.carbon_teal_50);
        skyColor = getResources().getColor(R.color.carbon_teal_100);
        landscapeColor = getResources().getColor(R.color.carbon_blue_900);
        fogColor = getResources().getColor(R.color.carbon_teal_50);
        planesCount = 5;
        starSize = getResources().getDimension(R.dimen.carbon_1dip) * (random.nextInt(16) + 8);
        landscapeHeight = getResources().getDimension(R.dimen.carbon_1dip) * 100;
        padding = getResources().getDimension(R.dimen.carbon_padding);
    }

    @Override
    public int onNestedScrollByY(int dy) {
        DependencyLayout.LayoutParams layoutParams = (DependencyLayout.LayoutParams) getLayoutParams();
        int newHeight = MathUtils.constrain(layoutParams.height - dy, getMinimumHeight(), getMaximumHeight());
        setElevation(MathUtils.map(getMaximumHeight(), getMinimumHeight(), 0, getResources().getDimension(carbon.R.dimen.carbon_elevationToolbar), newHeight));
        int usedDy = layoutParams.height - newHeight;
        layoutParams.height = newHeight;
        setLayoutParams(layoutParams);
        postInvalidate();
        return usedDy;
    }

    @Override
    public int getNestedScrollRange() {
        return 0;
    }

    @Override
    public int getNestedScrollY() {
        return 0;
    }

    public int getMinimumHeight() {
        return getResources().getDimensionPixelSize(carbon.R.dimen.carbon_toolbarHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed)
            return;
        if (getWidth() == 0 || getHeight() == 0)
            return;
        if (!landscapes.isEmpty())
            return;
        generate();
    }

    private void generate() {
        for (int i = 0; i < planesCount; i++) {
            float height = (float) i * landscapeHeight / (planesCount + 1);
            float fluctuation = (float) (i + 1) * landscapeHeight / (planesCount + 1);
            int color1 = (int) argbEvaluator.evaluate((float) i / planesCount, landscapeColor, fogColor);
            int color2 = (int) argbEvaluator.evaluate((float) (i + 1) / planesCount, landscapeColor, fogColor);
            landscapes.add(0, new Landscape(color1, color2, height, fluctuation, (float) (planesCount - i) / planesCount));
        }

        if (drawSun) {
            starX = random.nextInt((int) (getWidth() - padding * 2 - starSize)) + starSize * 0.5f + padding;
            starY = random.nextInt((int) ((getMaximumHeight() - landscapeHeight) / 2 - padding * 2 - starSize)) + starSize * 0.5f + padding;
        }

        if (drawStars) {
            stars.clear();
            int starCount = random.nextInt(50);
            for (int i = 0; i < starCount; i++)
                stars.add(new Star(random.nextInt(getWidth()), random.nextInt((int) (getMaximumHeight() - landscapeHeight)), (random.nextInt(2) + 1) * getResources().getDimension(R.dimen.carbon_1dip), starColor));
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);

        paint.setShader(new LinearGradient(0, getMaximumHeight() - landscapes.get(0).height - landscapes.get(0).fluctuation, 0, 0, fogColor, skyColor, Shader.TileMode.CLAMP));
        canvas.drawPaint(paint);
        paint.setShader(null);

        float translate = -(getMaximumHeight() - getHeight()) / (landscapes.size() - 1);
        canvas.save();
        canvas.translate(0, translate);
        if (drawSun) {
            paint.setColor(sunColor);
            canvas.drawCircle(starX, starY, starSize, paint);
        }
        if (drawStars) {
            for (Star s : stars)
                s.draw(canvas);
        }
        canvas.restore();
        paint.setAlpha(255);

        for (Landscape l : landscapes)
            l.draw(canvas);
    }

    private class Star {
        private final float x;
        private final float y;
        private final float size;
        private final int color;

        Star(float x, float y, float size, int color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
        }

        void draw(Canvas canvas) {
            paint.setColor(color);
            canvas.drawCircle(x, y, size, paint);
        }
    }

    private class Tree {
        Path path;

        Tree(float x, float y, float size) {
            size = getMaximumHeight() / (random.nextInt(8) + 12) * size;
            path = new Path();
            path.addRect(x - size / 10, y - size / 6, x + size / 10, y + size / 10, Path.Direction.CCW);
            path.moveTo(x - size / 3, y - size / 6);
            path.lineTo(x, y - size);
            path.lineTo(x + size / 3, y - size / 6);
            path.close();
        }

        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }

    private class Landscape {
        private final int color;
        private int color2;
        private float height;
        private float fluctuation;
        Path path;
        List<Tree> trees = new ArrayList<>();

        Landscape(int color, int color2, float height, float fluctuation, float scale) {
            this.color = color;
            this.color2 = color2;
            this.height = height;
            this.fluctuation = fluctuation;

            path = new Path();
            path.moveTo(0, getMaximumHeight());
            path.lineTo(getWidth(), getMaximumHeight());
            float prevX = getWidth();
            float prevY = (float) (getMaximumHeight() - height - Math.random() * fluctuation);
            path.lineTo(prevX, prevY);
            int widthDiv50 = (int) (getWidth() / (getResources().getDimension(R.dimen.carbon_1dip) * 100));
            int segments = random.nextInt(widthDiv50) + widthDiv50;
            for (int i = 0; i <= segments; i++) {
                float x = getWidth() * (segments - i) / segments;
                float y = (float) (getMaximumHeight() - height - Math.random() * fluctuation);
                float x33 = MathUtils.lerp(prevX, x, 0.33f);
                float x67 = MathUtils.lerp(prevX, x, 0.67f);
                path.cubicTo(x33, prevY, x67, y, x, y);
                if (random.nextFloat() > TREE_RANDOM)
                    trees.add(new Tree(prevX, prevY, scale));
                if (random.nextFloat() > TREE_RANDOM)
                    trees.add(new Tree(x33, MathUtils.lerp(prevY, y, 0.33f), scale));
                if (random.nextFloat() > TREE_RANDOM)
                    trees.add(new Tree(x67, MathUtils.lerp(prevY, y, 0.67f), scale));
                if (random.nextFloat() > TREE_RANDOM)
                    trees.add(new Tree(x, y, scale));
                prevX = x;
                prevY = y;
            }
            path.close();
        }

        void draw(Canvas canvas) {
            float translate = MathUtils.map(0, landscapes.size() - 1, (getMaximumHeight() - getHeight()) / 2.0f, getMaximumHeight() - getHeight(), landscapes.indexOf(this));
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.translate(0, -translate);
            paint.setColor(color2);
            for (Tree t : trees)
                t.draw(canvas);
            paint.setShader(new LinearGradient(0, getMaximumHeight() - height, 0, getMaximumHeight() - height - fluctuation, color, color2, Shader.TileMode.CLAMP));
            canvas.drawPath(path, paint);
            paint.setShader(null);
            canvas.restore();
        }
    }
}
