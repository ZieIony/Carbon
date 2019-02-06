/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package carbon.shadow;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;

import com.google.android.material.internal.Experimental;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.TintAwareDrawable;
import carbon.shadow.ShapeAppearancePathProvider.PathListener;
import carbon.shadow.ShapePath.ShadowCompatOperation;

/**
 * Base drawable class for Material Shapes that handles shadows, elevation, scale and color for a
 * generated path.
 */
@Experimental("The shapes API is currently experimental and subject to change")
public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable {

    private MaterialShapeDrawableState drawableState;

    // Inter-method state.
    private final ShadowCompatOperation[] cornerShadowOperation = new ShadowCompatOperation[4];
    private final ShadowCompatOperation[] edgeShadowOperation = new ShadowCompatOperation[4];
    private boolean pathDirty;

    // Pre-allocated objects that are re-used several times during path computation and rendering.
    private final Path path = new Path();
    private final RectF rectF = new RectF();
    private final Region transparentRegion = new Region();
    private final Region scratchRegion = new Region();

    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final ShadowRenderer shadowRenderer = new ShadowRenderer();
    private final PathListener pathShadowListener;
    private final ShapeAppearancePathProvider pathProvider = new ShapeAppearancePathProvider();

    @Nullable
    private PorterDuffColorFilter tintFilter;

    /**
     * @param shapeAppearanceModel the {@link ShapeAppearanceModel} containing the path that will be
     *                             rendered in this drawable.
     */
    public MaterialShapeDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this(new MaterialShapeDrawableState(shapeAppearanceModel));
    }

    private MaterialShapeDrawable(MaterialShapeDrawableState drawableState) {
        this.drawableState = drawableState;
        fillPaint.setStyle(Style.FILL);
        updateTintFilter();
        // Listens to additions of corners and edges, to create the shadow operations.
        pathShadowListener =
                new PathListener() {
                    @Override
                    public void onCornerPathCreated(ShapePath cornerPath, Matrix transform, int count) {
                        cornerShadowOperation[count] = cornerPath.createShadowCompatOperation(transform);
                    }

                    @Override
                    public void onEdgePathCreated(ShapePath edgePath, Matrix transform, int count) {
                        edgeShadowOperation[count] = edgePath.createShadowCompatOperation(transform);
                    }
                };
    }

    @Nullable
    @Override
    public ConstantState getConstantState() {
        return drawableState;
    }

    @NonNull
    @Override
    public Drawable mutate() {
        drawableState = new MaterialShapeDrawableState(drawableState);
        return this;
    }

    private static int modulateAlpha(int paintAlpha, int alpha) {
        int scale = alpha + (alpha >>> 7); // convert to 0..256
        return (paintAlpha * scale) >>> 8;
    }

    @Override
    public void setTintMode(@NonNull Mode tintMode) {
        if (drawableState.tintMode != tintMode) {
            drawableState.tintMode = tintMode;
            updateTintFilter();
            invalidateSelfIgnoreShape();
        }
    }

    @Override
    public void setTintList(ColorStateList tintList) {
        drawableState.tintList = tintList;
        updateTintFilter();
        invalidateSelfIgnoreShape();
    }

    /**
     * Get the tint list used by the shape's paint.
     */
    public ColorStateList getTintList() {
        return drawableState.tintList;
    }

    @Override
    public void setTint(@ColorInt int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    @Override
    public int getOpacity() {
        // OPAQUE or TRANSPARENT are possible, but the complexity of determining this based on the
        // shape model outweighs the optimizations gained.
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (drawableState.alpha != alpha) {
            drawableState.alpha = alpha;
            invalidateSelfIgnoreShape();
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        drawableState.colorFilter = colorFilter;
        invalidateSelfIgnoreShape();
    }

    @Override
    public Region getTransparentRegion() {
        Rect bounds = getBounds();
        transparentRegion.set(bounds);
        calculatePath(getBoundsAsRectF(), path);
        scratchRegion.setPath(path, transparentRegion);
        transparentRegion.op(scratchRegion, Op.DIFFERENCE);
        return transparentRegion;
    }

    protected RectF getBoundsAsRectF() {
        Rect bounds = getBounds();
        rectF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        return rectF;
    }

    public void setCornerRadius(float cornerRadius) {
        drawableState.shapeAppearanceModel.setCornerRadius(cornerRadius);
        invalidateSelf();
    }

    /**
     * Get the interpolation of the path, between 0 and 1. Ranges between 0 (none) and 1 (fully)
     * interpolated.
     *
     * @return the interpolation of the path.
     */
    public float getInterpolation() {
        return drawableState.interpolation;
    }

    /**
     * Set the interpolation of the path, between 0 and 1. Ranges between 0 (none) and 1 (fully)
     * interpolated. An interpolation of 1 generally indicates a fully rendered path, while an
     * interpolation of 0 generally indicates a fully healed path, which is usually a rectangle.
     *
     * @param interpolation the desired interpolation.
     */
    public void setInterpolation(float interpolation) {
        if (drawableState.interpolation != interpolation) {
            drawableState.interpolation = interpolation;
            invalidateSelf();
        }
    }

    /**
     * Returns the elevation used to render fake shadows when {@link #requiresCompatShadow()} is
     * true. This value is the same as the native elevation that would be used to render shadows on
     * API 21 and up.
     */
    public float getElevation() {
        return drawableState.elevation;
    }

    /**
     * Sets the elevation used to render shadows when {@link #requiresCompatShadow()} is true. This
     * value is the same as the native elevation that would be used to render shadows on API 21 and
     * up.
     *
     * <p>TODO: The shadow radius should be the actual radius drawn, elevation should be
     * the height of the closest equivalent native elevation which produces a similar shadow.
     */
    public void setElevation(float elevation) {
        if (drawableState.elevation != elevation) {
            drawableState.shadowCompatRadius = Math.round(elevation);
            drawableState.elevation = elevation;
            invalidateSelfIgnoreShape();
        }
    }

    /**
     * Get the shadow elevation rendered by the path.
     *
     * @deprecated use {@link #getElevation()} instead.
     */
    @Deprecated
    public int getShadowElevation() {
        return (int) getElevation();
    }

    /**
     * Set the shadow elevation rendered by the path.
     *
     * @param shadowElevation the desired elevation.
     * @deprecated use {@link #setElevation(float)} instead.
     */
    @Deprecated
    public void setShadowElevation(int shadowElevation) {
        setElevation(shadowElevation);
    }

    /**
     * Get the shadow radius rendered by the path.
     *
     * @return the shadow radius rendered by the path.
     * @deprecated use {@link #getElevation()} instead.
     */
    @Deprecated
    public int getShadowRadius() {
        return drawableState.shadowCompatRadius;
    }

    /**
     * Set the shadow radius rendered by the path.
     *
     * @param shadowRadius the desired shadow radius.
     * @deprecated use {@link #setElevation(float)} instead.
     */
    @Deprecated
    public void setShadowRadius(int shadowRadius) {
        drawableState.shadowCompatRadius = shadowRadius;
    }

    @Override
    public void invalidateSelf() {
        pathDirty = true;
        invalidateSelfIgnoreShape();
    }

    /**
     * Invalidate without recalculating the path associated with this shape. This is useful if the
     * shape has stayed the same but we still need to be redrawn, such as when the color has
     * changed.
     */
    private void invalidateSelfIgnoreShape() {
        super.invalidateSelf();
    }

    /**
     * Get the current style used by the shape's paint.
     *
     * @return current used paint style.
     */
    public Style getPaintStyle() {
        return drawableState.paintStyle;
    }

    /**
     * Set the style used by the shape's paint.
     *
     * @param paintStyle the desired style.
     */
    public void setPaintStyle(Style paintStyle) {
        drawableState.paintStyle = paintStyle;
        invalidateSelfIgnoreShape();
    }

    /**
     * Returns whether the shape has a fill.
     */
    private boolean hasFill() {
        return drawableState.paintStyle == Style.FILL_AND_STROKE
                || drawableState.paintStyle == Style.FILL;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        pathDirty = true;
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        fillPaint.setColorFilter(tintFilter);
        final int prevAlpha = fillPaint.getAlpha();
        fillPaint.setAlpha(modulateAlpha(prevAlpha, drawableState.alpha));

        if (pathDirty) {
            calculatePath(getBoundsAsRectF(), path);
            pathDirty = false;
        }

        drawCompatShadow(canvas);
        drawFillShape(canvas);

        fillPaint.setAlpha(prevAlpha);
    }

    /**
     * Draw the path or try to draw a round rect if possible.
     */
    private void drawShape(
            Canvas canvas,
            Paint paint,
            Path path,
            ShapeAppearanceModel shapeAppearanceModel,
            RectF bounds) {
        if (shapeAppearanceModel.isRoundRect()) {
            float cornerSize = shapeAppearanceModel.getTopRightCorner().getCornerSize();
            canvas.drawRoundRect(bounds, cornerSize, cornerSize, paint);
        } else {
            canvas.drawPath(path, paint);
        }
    }

    private void drawFillShape(Canvas canvas) {
        drawShape(canvas, fillPaint, path, drawableState.shapeAppearanceModel, getBoundsAsRectF());
    }

    /**
     * Draws a shadow using gradients which can be used in the cases where native elevation can't.
     * This draws the shadow in multiple parts. It draws the shadow for each corner and edge
     * separately. Then it fills in the center space with the main shadow colored paint. If there is
     * no shadow offset, this will skip the drawing of the center filled shadow since that will be
     * completely covered by the shape.
     */
    private void drawCompatShadow(Canvas canvas) {

        // Draw the fake shadow for each of the corners and edges.
        for (int index = 0; index < 4; index++) {
            cornerShadowOperation[index].draw(shadowRenderer, drawableState.shadowCompatRadius, canvas);
            edgeShadowOperation[index].draw(shadowRenderer, drawableState.shadowCompatRadius, canvas);
        }
    }

    /**
     * @deprecated see {@link ShapeAppearancePathProvider}
     */
    @Deprecated
    public void getPathForSize(int width, int height, Path path) {
        calculatePathForSize(new RectF(0, 0, width, height), path);
    }

    /**
     * @deprecated see {@link ShapeAppearancePathProvider}
     */
    public void getPathForSize(Rect bounds, Path path) {
        calculatePathForSize(new RectF(bounds), path);
    }

    private void calculatePathForSize(RectF bounds, Path path) {
        pathProvider.calculatePath(
                drawableState.shapeAppearanceModel,
                drawableState.interpolation,
                bounds,
                pathShadowListener,
                path);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(Outline outline) {
        boolean isRoundRect = drawableState.shapeAppearanceModel.isRoundRect();

        if (isRoundRect) {
            float radius = drawableState.shapeAppearanceModel.getTopLeftCorner().getCornerSize();
            outline.setRoundRect(getBounds(), radius);
            return;
        }

        calculatePath(getBoundsAsRectF(), path);
        if (path.isConvex()) {
            outline.setConvexPath(path);
        }
    }

    private void calculatePath(RectF bounds, Path path) {
        calculatePathForSize(bounds, path);
    }

    private void updateTintFilter() {
        tintFilter = calculateTintFilter(drawableState.tintList, drawableState.tintMode);
        if (tintFilter != null)
            shadowRenderer.setShadowColor(drawableState.tintList.getColorForState(getState(), Color.TRANSPARENT));
    }

    @Nullable
    private PorterDuffColorFilter calculateTintFilter(
            ColorStateList tintList, Mode tintMode) {
        if (tintList == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(
                tintList.getColorForState(getState(), Color.TRANSPARENT), tintMode);
    }

    @Override
    public boolean isStateful() {
        return super.isStateful()
                || (drawableState.tintList != null && drawableState.tintList.isStateful());
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean invalidateSelf = super.onStateChange(state);

        updateTintFilter();

        return invalidateSelf;
    }

    static final class MaterialShapeDrawableState extends ConstantState {

        @NonNull
        public ShapeAppearanceModel shapeAppearanceModel;

        public ColorFilter colorFilter;
        public ColorStateList tintList = null;
        public Mode tintMode = Mode.SRC_IN;

        public float interpolation = 1f;

        public int alpha = 255;
        public float elevation = 0;
        public int shadowCompatRadius = 0;

        public Style paintStyle = Style.FILL_AND_STROKE;

        public MaterialShapeDrawableState(@NonNull ShapeAppearanceModel shapeAppearanceModel) {
            this.shapeAppearanceModel = shapeAppearanceModel;
        }

        public MaterialShapeDrawableState(MaterialShapeDrawableState orig) {
            shapeAppearanceModel = new ShapeAppearanceModel(orig.shapeAppearanceModel);
            colorFilter = orig.colorFilter;
            tintMode = orig.tintMode;
            tintList = orig.tintList;
            alpha = orig.alpha;
            interpolation = orig.interpolation;
            elevation = orig.elevation;
            shadowCompatRadius = orig.shadowCompatRadius;
            paintStyle = orig.paintStyle;
        }

        @Override
        public Drawable newDrawable() {
            return new MaterialShapeDrawable(this);
        }

        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }
}
