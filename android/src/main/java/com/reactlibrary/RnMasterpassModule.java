package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.mastercard.mp.checkout.MasterpassError;
import com.mastercard.mp.checkout.MasterpassInitCallback;
import com.mastercard.mp.checkout.MasterpassMerchant;
import com.mastercard.mp.checkout.MasterpassMerchantConfiguration;

import java.util.Locale;

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
    public void initialize(ReadableMap _config, final Callback masterpassInitCallback) {

        MasterpassMerchantConfiguration config = new MasterpassMerchantConfiguration.Builder()
                .setContext(getReactApplicationContext()) //context
                .setEnvironment(_config.getString("environment")) //environment
                .setLocale(new Locale("tr","TR")) //locale
                .setMerchantName(_config.getString("merchantName"))
                .setExpressCheckoutEnabled(_config.getBoolean("expressCheckoutEnabled"))//if merchant is express enabled
                .setCheckoutId(_config.getString("checkoutId"))
                .build();

        MasterpassMerchant.initialize(config, new MasterpassInitCallback() {
            @Override
            public void onInitSuccess() {
                masterpassInitCallback.invoke(true);
            }

            @Override
            public void onInitError(MasterpassError masterpassError) {
                masterpassInitCallback.invoke(masterpassError);
            }
        });
        //callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }
}
