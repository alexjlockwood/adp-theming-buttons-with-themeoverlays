package com.example.alockwood.themedbuttons;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
  private static final String STATE_ARE_ALL_BUTTONS_ENABLED = "state_are_all_buttons_enabled";

  private boolean areAllButtonsEnabled;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState != null) {
      areAllButtonsEnabled = savedInstanceState.getBoolean(STATE_ARE_ALL_BUTTONS_ENABLED);
    }

    updateButtonState();
  }

  private void updateButtonState() {
    final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearlayout);
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      viewGroup.getChildAt(i).setEnabled(areAllButtonsEnabled);
    }
    invalidateOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.enable_disable_all)
        .setTitle(areAllButtonsEnabled ? R.string.disable_all : R.string.enable_all);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.enable_disable_all) {
      areAllButtonsEnabled = !areAllButtonsEnabled;
      updateButtonState();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(STATE_ARE_ALL_BUTTONS_ENABLED, areAllButtonsEnabled);
  }
}
