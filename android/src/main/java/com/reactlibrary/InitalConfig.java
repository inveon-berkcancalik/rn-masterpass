package com.reactlibrary;

public class InitalConfig {
    private String environment;
    private String merchantName;
    private String checkoutId;
    private Boolean expressCheckoutEnabled;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public Boolean getExpressCheckoutEnabled() {
        return expressCheckoutEnabled;
    }

    public void setExpressCheckoutEnabled(Boolean expressCheckoutEnabled) {
        this.expressCheckoutEnabled = expressCheckoutEnabled;
    }
}
