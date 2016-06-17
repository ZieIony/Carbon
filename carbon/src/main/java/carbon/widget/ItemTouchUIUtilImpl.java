/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package carbon.widget;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import carbon.shadow.ShadowView;


/**
 * Package private class to keep implementations. Putting them inside ItemTouchUIUtil makes them
 * public API, which is not desired in this case.
 */
class ItemTouchUIUtilImpl {
    static class Carbon implements ItemTouchUIUtil {
        @Override
        public void onDraw(Canvas c, RecyclerView recyclerView, View view,
                           float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (isCurrentlyActive) {
                Object originalElevation = view.getTag(R.id.item_touch_helper_previous_elevation);
                if (originalElevation == null) {
                    originalElevation = view instanceof ShadowView ? ((ShadowView) view).getElevation() : ViewCompat.getElevation(view);
                    float newElevation = 1f + findMaxElevation(recyclerView, view);
                    if (view instanceof ShadowView) {
                        ((ShadowView) view).setElevation(newElevation);
                    } else {
                        ViewCompat.setElevation(view, newElevation);
                    }
                    view.setTag(R.id.item_touch_helper_previous_elevation, originalElevation);
                }
            }
            ViewHelper.setTranslationX(view, dX);
            ViewHelper.setTranslationY(view, dY);
        }

        private float findMaxElevation(RecyclerView recyclerView, View itemView) {
            final int childCount = recyclerView.getChildCount();
            float max = 0;
            for (int i = 0; i < childCount; i++) {
                final View child = recyclerView.getChildAt(i);
                if (child == itemView) {
                    continue;
                }
                final float elevation = ViewCompat.getElevation(child);
                if (elevation > max) {
                    max = elevation;
                }
            }
            return max;
        }

        @Override
        public void clearView(View view) {
            final Object tag = view.getTag(R.id.item_touch_helper_previous_elevation);
            if (tag != null && tag instanceof Float) {
                if (view instanceof ShadowView) {
                    ((ShadowView) view).setElevation((Float) tag);
                } else {
                    ViewCompat.setElevation(view, (Float) tag);
                }
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null);
            ViewHelper.setTranslationX(view, 0f);
            ViewHelper.setTranslationY(view, 0f);
        }

        @Override
        public void onSelected(View view) {

        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView recyclerView,
                               View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        }
    }

}
