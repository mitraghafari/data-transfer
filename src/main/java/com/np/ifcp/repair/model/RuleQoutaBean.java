package com.np.ifcp.repair.model;

import org.springframework.stereotype.Component;

/**
 *
 * @author ghaffari.m
 */

@Component
public class RuleQoutaBean {

    String RULE_ID;
    String CLAZZ;
    String USAGE_CODE;
    String SUBUSAGE_CODE;
    String FUEL_CODE;

    public String getRULE_ID() {
        return RULE_ID;
    }

    public void setRULE_ID(String RULE_ID) {
        this.RULE_ID = RULE_ID;
    }

    public String getCLAZZ() {
        return CLAZZ;
    }

    public void setCLAZZ(String CLAZZ) {
        this.CLAZZ = CLAZZ;
    }

    public String getUSAGE_CODE() {
        return USAGE_CODE;
    }

    public void setUSAGE_CODE(String USAGE_CODE) {
        this.USAGE_CODE = USAGE_CODE;
    }

    public String getSUBUSAGE_CODE() {
        return SUBUSAGE_CODE;
    }

    public void setSUBUSAGE_CODE(String SUBUSAGE_CODE) {
        this.SUBUSAGE_CODE = SUBUSAGE_CODE;
    }

    public String getFUEL_CODE() {
        return FUEL_CODE;
    }

    public void setFUEL_CODE(String FUEL_CODE) {
        this.FUEL_CODE = FUEL_CODE;
    }
    

    float CARD_LIMIT;

    public float getCARD_LIMIT() {
        return CARD_LIMIT;
    }

    public void setCARD_LIMIT(float CARD_LIMIT) {
        this.CARD_LIMIT = CARD_LIMIT;
    }

}
