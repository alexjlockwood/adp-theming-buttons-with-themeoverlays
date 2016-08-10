package com.example.alockwood.themedbuttons;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;

/**
 * Utility class for generating background tint {@link ColorStateList}s.
 */
final class BackgroundTints {
  private static final int[] DISABLED_STATE_SET = new int[]{-android.R.attr.state_enabled};
  private static final int[] PRESSED_STATE_SET = new int[]{android.R.attr.state_pressed};
  private static final int[] FOCUSED_STATE_SET = new int[]{android.R.attr.state_focused};
  private static final int[] EMPTY_STATE_SET = new int[0];

  /**
   * Returns a {@link ColorStateList} that can be used as a colored button's background tint.
   * Note that this code makes use of the {@code android.support.v4.graphics.ColorUtils}
   * and {@code android.support.v7.content.res.AppCompatResources} utility classes.
   */
  public static ColorStateList forColoredButton(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // On API 21+ we can extract the ColorStateList from XML directly.
      // We could also construct the ColorStateList programatically similar to below
      // (as long as we remember not to include the pressed and focused states
      // on API 21+, since the button's foreground RippleDrawable will animate these
      // state changes automatically).
      return AppCompatResources.getColorStateList(context, R.color.btn_colored_background_tint);
    }

    // On older platform versions, there's no easy way to generate the pressed/focused
    // state colors in XML, so we have to create the ColorStateList programatically.

    final int[][] states = new int[4][];
    final int[] colors = new int[4];

    final int accentColor = getThemeAttrColor(context, R.attr.colorAccent);
    final int colorControlHighlight = getThemeAttrColor(context, R.attr.colorControlHighlight);

    states[0] = DISABLED_STATE_SET;
    colors[0] = getDisabledButtonBackgroundColor(context);

    states[1] = PRESSED_STATE_SET;
    colors[1] = ColorUtils.compositeColors(colorControlHighlight, accentColor);

    states[2] = FOCUSED_STATE_SET;
    colors[2] = ColorUtils.compositeColors(colorControlHighlight, accentColor);

    states[3] = EMPTY_STATE_SET;
    colors[3] = accentColor;

    return new ColorStateList(states, colors);
  }

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
    return ColorUtils.setAlphaComponent(colorButtonNormal, Math.round(originalAlpha * disabledAlpha));
  }

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
