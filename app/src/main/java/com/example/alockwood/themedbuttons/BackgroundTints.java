package com.example.alockwood.themedbuttons;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;

/**
 * Utility class for creating background tint {@link ColorStateList}s.
 */
public final class BackgroundTints {
  private static final int[] DISABLED_STATE_SET = new int[]{-android.R.attr.state_enabled};
  private static final int[] PRESSED_STATE_SET = new int[]{android.R.attr.state_pressed};
  private static final int[] FOCUSED_STATE_SET = new int[]{android.R.attr.state_focused};
  private static final int[] EMPTY_STATE_SET = new int[0];

  /**
   * Returns a {@link ColorStateList} that can be used as a colored button's background tint.
   * Note that this code makes use of the {@code android.support.v4.graphics.ColorUtils}
   * utility class.
   */
  public static ColorStateList forColoredButton(Context context, @ColorInt int backgroundColor) {
    // On pre-Lollipop devices, we need 4 states total (disabled, pressed, focused, and default).
    // On post-Lollipop devices, we need 2 states total (disabled and default); the button's
    // RippleDrawable will animate the pressed and focused state changes for us automatically.
    final int numStates = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 2 : 4;

    final int[][] states = new int[numStates][];
    final int[] colors = new int[numStates];

    int i = 0;

    states[i] = DISABLED_STATE_SET;
    colors[i] = getDisabledButtonBackgroundColor(context);
    i++;

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      final int highlightedBackgroundColor = getHighlightedBackgroundColor(context, backgroundColor);

      states[i] = PRESSED_STATE_SET;
      colors[i] = highlightedBackgroundColor;
      i++;

      states[i] = FOCUSED_STATE_SET;
      colors[i] = highlightedBackgroundColor;
      i++;
    }

    states[i] = EMPTY_STATE_SET;
    colors[i] = backgroundColor;

    return new ColorStateList(states, colors);
  }

  /**
   * Returns the theme-dependent ARGB background color to use for disabled buttons.
   */
  @ColorInt
  private static int getDisabledButtonBackgroundColor(Context context) {
    // Extract the disabled alpha to apply to the button using the context's theme.
    // (0.26f for light themes and 0.30f for dark themes).
    final TypedValue tv = new TypedValue();
    context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, tv, true);
    final float disabledAlpha = tv.getFloat();

    // Use the disabled alpha factor and the button's default normal color
    // to generate the button's disabled background color.
    final int colorButtonNormal = getThemeAttrColor(context, R.attr.colorButtonNormal);
    final int originalAlpha = Color.alpha(colorButtonNormal);
    return ColorUtils.setAlphaComponent(
        colorButtonNormal, Math.round(originalAlpha * disabledAlpha));
  }

  /**
   * Returns the theme-dependent ARGB color that results when colorControlHighlight is drawn
   * on top of the provided background color.
   */
  @ColorInt
  private static int getHighlightedBackgroundColor(Context context, @ColorInt int backgroundColor) {
    final int colorControlHighlight = getThemeAttrColor(context, R.attr.colorControlHighlight);
    return ColorUtils.compositeColors(colorControlHighlight, backgroundColor);
  }

  /** Returns the theme-dependent ARGB color associated with the provided theme attribute. */
  @ColorInt
  private static int getThemeAttrColor(Context context, @AttrRes int attr) {
    final TypedArray array = context.obtainStyledAttributes(null, new int[]{attr});
    try {
      return array.getColor(0, 0);
    } finally {
      array.recycle();
    }
  }

  private BackgroundTints() {}
}
