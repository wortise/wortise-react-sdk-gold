package com.wortise.ads.react;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.wortise.ads.AdError;
import com.wortise.ads.interstitial.InterstitialAd;

public class RNWortiseInterstitialModule extends ReactContextBaseJavaModule implements InterstitialAd.Listener, LifecycleEventListener {

  public static final String EVENT_CLICKED    = "onInterstitialClicked";
  public static final String EVENT_DISMISSED  = "onInterstitialDismissed";
  public static final String EVENT_FAILED     = "onInterstitialFailed";
  public static final String EVENT_IMPRESSION = "onInterstitialImpression";
  public static final String EVENT_LOADED     = "onInterstitialLoaded";
  public static final String EVENT_SHOWN      = "onInterstitialShown";


  private Handler mHandler = new Handler(Looper.getMainLooper());

  private InterstitialAd mInterstitialAd;


  public RNWortiseInterstitialModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "RNWortiseInterstitial";
  }

  @ReactMethod
  public void addListener(String eventName) {
  }

  @ReactMethod
  public void destroy() {
    if (mInterstitialAd == null) {
      return;
    }

    mInterstitialAd.destroy();
    mInterstitialAd = null;
  }

  @ReactMethod
  public void isAvailable(Promise promise) {
    promise.resolve((mInterstitialAd != null) && mInterstitialAd.isAvailable());
  }

  @ReactMethod
  public void isShowing(Promise promise) {
    promise.resolve((mInterstitialAd != null) && mInterstitialAd.isShowing());
  }

  @ReactMethod
  public void loadAd() {
    if (mInterstitialAd == null) {
      return;
    }

    mHandler.post(() -> mInterstitialAd.loadAd());
  }

  @ReactMethod
  public void removeListeners(Integer count) {
  }

  @ReactMethod
  public void setAdUnitId(String adUnitId) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
        return;
    }

    destroy();

    mInterstitialAd = new InterstitialAd(currentActivity, adUnitId);
    mInterstitialAd.setListener(this);
  }

  @ReactMethod
  public void showAd(Promise promise) {
    Activity currentActivity = getCurrentActivity();

    if (mInterstitialAd == null) {
        promise.resolve(false);
        return;
    }

    mHandler.post(() -> {

      boolean result = (currentActivity != null)
        ? mInterstitialAd.showAd(currentActivity)
        : mInterstitialAd.showAd();

      promise.resolve(result);
    });
  }


  private void sendEvent(String eventName, WritableMap params) {
    getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }


  @Override
  public void onHostDestroy() {
    destroy();
  }

  @Override
  public void onHostPause() {
  }

  @Override
  public void onHostResume() {
  }

  @Override
  public void onInterstitialClicked(InterstitialAd ad) {
    sendEvent(EVENT_CLICKED, null);
  }

  @Override
  public void onInterstitialDismissed(InterstitialAd ad) {
    sendEvent(EVENT_DISMISSED, null);
  }

  @Override
  public void onInterstitialFailed(InterstitialAd ad, AdError error) {
    WritableMap event = Arguments.createMap();

    event.putString("message", error.toString());
    event.putString("name",    error.name());

    sendEvent(EVENT_FAILED, event);
  }

  @Override
  public void onInterstitialImpression(InterstitialAd ad) {
    sendEvent(EVENT_IMPRESSION, null);
  }

  @Override
  public void onInterstitialLoaded(InterstitialAd ad) {
    sendEvent(EVENT_LOADED, null);
  }

  @Override
  public void onInterstitialShown(InterstitialAd ad) {
    sendEvent(EVENT_SHOWN, null);
  }
}
