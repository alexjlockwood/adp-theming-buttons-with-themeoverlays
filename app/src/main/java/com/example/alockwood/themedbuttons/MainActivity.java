package com.example.alockwood.themedbuttons;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
  private static final boolean SHOULD_ENABLE_BUTTONS = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearlayout);
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      viewGroup.getChildAt(i).setEnabled(SHOULD_ENABLE_BUTTONS);
    }
  }
}
