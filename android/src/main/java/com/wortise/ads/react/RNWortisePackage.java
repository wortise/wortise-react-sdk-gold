package com.wortise.ads.react;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class RNWortisePackage implements ReactPackage {
    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      return Arrays.<NativeModule>asList(
        new RNWortiseAdSettingsModule    (reactContext),
        new RNWortiseAppOpenModule       (reactContext),
        new RNWortiseConsentManagerModule(reactContext),
        new RNWortiseDataManagerModule   (reactContext),
        new RNWortiseInterstitialModule  (reactContext),
        new RNWortiseRewardedModule      (reactContext),
        new RNWortiseSdkModule           (reactContext)
      );
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Arrays.<ViewManager>asList(
        new WortiseBannerManager()
      );
    }
}
