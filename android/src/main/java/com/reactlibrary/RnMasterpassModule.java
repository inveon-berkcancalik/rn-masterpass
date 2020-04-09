package com.reactlibrary;

import android.util.Log;
import android.widget.TextView;

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
import cardtek.masterpass.attributes.MasterPassEditText;
import cardtek.masterpass.interfaces.CheckMasterPassListener;
import cardtek.masterpass.interfaces.DeleteCardListener;
import cardtek.masterpass.interfaces.GetCardsListener;
import cardtek.masterpass.interfaces.PurchaseListener;
import cardtek.masterpass.interfaces.RegisterCardListener;
import cardtek.masterpass.nfc.MasterPassNfcReaderListener;
import cardtek.masterpass.response.InternalError;
import cardtek.masterpass.response.NfcReaderResult;
import cardtek.masterpass.response.ServiceError;
import cardtek.masterpass.response.ServiceResult;
import cardtek.masterpass.results.CheckMasterPassResult;
import cardtek.masterpass.results.DeleteCardResult;
import cardtek.masterpass.results.GetCardsResult;
import cardtek.masterpass.results.PurchaseResult;
import cardtek.masterpass.results.RegisterCardResult;
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
    public void initialize(ReadableMap config, final Callback callback) {
        service = new MasterPassServices(getReactApplicationContext(), config.getString("phone"));

        if (config.getString("env").toLowerCase().equals("uatui")){
            MasterPassInfo.setUrl("https://uatui.masterpassturkiye.com/v2");
        } else if(config.getString("env").toLowerCase().equals("test")) {
            MasterPassInfo.setUrl("https://test.masterpassturkiye.com/MasterpassJsonServerHandler/v2");
        } else if(config.getString("env").toLowerCase().equals("prod")) {
            MasterPassInfo.setUrl("https://masterpassturkiye.com/v2");
        }

        MasterPassInfo.setClientID(config.getString("clientId"));
        MasterPassInfo.setLanguage(config.getString("language"));
        MasterPassInfo.setMacroMerchantId(config.getString("macroMerchantId"));

        service.startNfcReader(getCurrentActivity(), new MasterPassNfcReaderListener() {
            @Override
            public void onCardReadFail() {
                callback.invoke();
            }

            @Override
            public void onCardReadSuccess(NfcReaderResult nfcReaderResult) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(nfcReaderResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#NFC1 JSONObject parsing error.");
                }
            }

            @Override
            public void onInternalError(InternalError internalError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(internalError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#NFC2 JSONObject parsing error.");
                }
            }
        });
    }

    @ReactMethod
    public void checkMasterpass(String token, String referenceNo, final Callback callback)
    {
        service.checkMasterPass(token, referenceNo, new CheckMasterPassListener() {
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

    @ReactMethod
    public void purchase(String token, String referanceNo, String cardName, String orderNo, float amount, final Callback callback)
    {
        float total = amount * 100;

        service.purchase(token, cardName, Math.round(total), orderNo, referanceNo, new PurchaseListener() {
            @Override
            public void onSuccess(PurchaseResult purchaseResult) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(purchaseResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#7 JSONObject parsing error.");
                }
            }

            @Override
            public void onVerifyUser(ServiceResult serviceResult) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(serviceResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#8 JSONObject parsing error.");
                }
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(serviceError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#9 JSONObject parsing error.");
                }
            }

            @Override
            public void onInternalError(InternalError internalError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(internalError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("#10 JSONObject parsing error.");
                }
            }
        });
    }

    @ReactMethod
    public void deleteCard(String cardName, String token, String referanceNo, final Callback callback)
    {
        service.deleteCard(token, cardName, referanceNo, new DeleteCardListener() {
            @Override
            public void onSuccess(DeleteCardResult deleteCardResult) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(deleteCardResult));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("deleteCard.onSuccess JSONObject parsing error.");
                }
            }

            @Override
            public void onServiceError(ServiceError serviceError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(serviceError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("deleteCard.onServiceError JSONObject parsing error.");
                }
            }

            @Override
            public void onInternalError(InternalError internalError) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(internalError));
                    callback.invoke(JsonConvert.jsonToReact(obj));
                } catch (JSONException e) {
                    callback.invoke("deleteCard.onInternalError JSONObject parsing error.");
                }
            }
        });
    }
}
