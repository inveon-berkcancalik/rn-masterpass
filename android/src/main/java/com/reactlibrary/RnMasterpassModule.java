package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.mastercard.mp.checkout.MasterpassError;
import com.mastercard.mp.checkout.MasterpassInitCallback;
import com.mastercard.mp.checkout.MasterpassMerchant;
import com.mastercard.mp.checkout.MasterpassMerchantConfiguration;

public class RnMasterpassModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RnMasterpassModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RnMasterpass";
    }

    @ReactMethod
    public void initialize(MasterpassMerchantConfiguration config, final Callback masterpassInitCallback) {
        MasterpassMerchant.initialize(config, new MasterpassInitCallback() {
            @Override
            public void onInitSuccess() {
                masterpassInitCallback.invoke();
            }

            @Override
            public void onInitError(MasterpassError masterpassError) {
                masterpassInitCallback.invoke(masterpassError);
            }
        });
        //callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }
}
