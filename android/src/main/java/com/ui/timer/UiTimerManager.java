package com.ui.timer;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;

@ReactModule(name = UiTimerManager.REACT_CLASS)
public class UiTimerManager extends ViewGroupManager<UiTimerView> {

  protected static final String REACT_CLASS = "RNTUiTimer";
  private final @Nullable Object mCallerContext;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  public UiTimerManager(Object callerContext) {
    mCallerContext = callerContext;
  }

  public UiTimerManager() {
    mCallerContext = null;
  }

  @Override
  public UiTimerView createViewInstance(ThemedReactContext context) {
    return new UiTimerView(context);
  }

  @ReactProp(name = "timeout")
  public void setTimeout(UiTimerView view, @Nullable Integer timeout) {
    view.setTimeout( timeout );
  }

}