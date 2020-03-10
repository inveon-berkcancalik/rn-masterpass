package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import cardtek.masterpass.MasterPassServices;
import cardtek.masterpass.interfaces.CheckMasterPassListener;
import cardtek.masterpass.interfaces.GetCardsListener;
import cardtek.masterpass.response.InternalError;
import cardtek.masterpass.response.ServiceError;
import cardtek.masterpass.results.CheckMasterPassResult;
import cardtek.masterpass.results.GetCardsResult;
import cardtek.masterpass.util.MasterPassInfo;

public class RnMasterpassModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final MasterPassServices service;

    public RnMasterpassModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.service = new MasterPassServices(reactContext, "");
    }

    @Override
    public String getName() {
        return "RnMasterpass";
    }

    @ReactMethod
    public void initialize(ReadableMap config) {
        service.setMsisdn(config.getString("phone"));
        MasterPassInfo.setClientID(config.getString("clientId"));
        MasterPassInfo.setLanguage(config.getString("language"));
        MasterPassInfo.setMacroMerchantId(config.getString("macroMerchantId"));
    }

    @ReactMethod
    public void checkMasterpass(String token, String referenceNo, final Callback callback)
    {
        MasterPassServices s = new MasterPassServices(getReactApplicationContext(), "msisdn");
        s.checkMasterPass(token, referenceNo, new CheckMasterPassListener() {
            @Override
            public void onSuccess(CheckMasterPassResult checkMasterPassResult) {
                callback.invoke(checkMasterPassResult);
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                callback.invoke(serviceError);
            }

            @Override
            public void onInternalError(InternalError internalError) {
                callback.invoke(internalError);
            }
        });
    }

    @ReactMethod
    public void getCards(String token, String referanceNo, final Callback callback)
    {
        service.getCards(token, referanceNo, new GetCardsListener() {
            @Override
            public void onSuccess(GetCardsResult getCardsResult) {
                callback.invoke(getCardsResult);
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                callback.invoke(serviceError);
            }

            @Override
            public void onInternalError(InternalError internalError) {
                callback.invoke(internalError);
            }
        });
    }
}
