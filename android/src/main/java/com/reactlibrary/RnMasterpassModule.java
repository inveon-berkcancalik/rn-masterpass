package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private MasterPassServices service;

    public RnMasterpassModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        //this.service = new MasterPassServices(reactContext, "");
    }

    @Override
    public String getName() {
        return "RnMasterpass";
    }

    @ReactMethod
    public void initialize(ReadableMap config) {
        service = new MasterPassServices(getReactApplicationContext(), config.getString("phone"));

        if (config.getBoolean("sandbox")) {
            MasterPassInfo.setUrl("https://test.masterpassturkiye.com/MasterpassJsonServerHandler/v2/");
        }

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
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(checkMasterPassResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#1 JSONObject parsing error.");
                }
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(serviceError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#2 JSONObject parsing error.");
                }
            }

            @Override
            public void onInternalError(InternalError internalError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(internalError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#3 JSONObject parsing error.");
                }
            }
        });
    }

    @ReactMethod
    public void getCards(String token, String referanceNo, final Callback callback)
    {
        service.getCards(token, referanceNo, new GetCardsListener() {
            @Override
            public void onSuccess(GetCardsResult getCardsResult) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(getCardsResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#4 JSONObject parsing error.");
                }
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(serviceError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#5 JSONObject parsing error.");
                }
            }

            @Override
            public void onInternalError(InternalError internalError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(internalError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#6 JSONObject parsing error.");
                }
            }
        });
    }
}
