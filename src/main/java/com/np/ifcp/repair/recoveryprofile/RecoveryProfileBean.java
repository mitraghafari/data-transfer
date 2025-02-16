package com.np.ifcp.repair.recoveryprofile;

import org.springframework.stereotype.Component;

/**
 *
 * @author ghaffari.m
 */
@Component
public class RecoveryProfileBean {
    private String Card_ID;
    private String VEHICLE_ID;
    private Double CURRENT_MONTH_QUOTA;
    private Double SUM_QUOTA;
    private String RULE_ID;
    private int SEQ_NO;
    private int RATE;
    private String MAC;

    public String getCard_ID() {
        return Card_ID;
    }

    public void setCard_ID(String Card_ID) {
        this.Card_ID = Card_ID;
    }

    public int getRATE() {
        return RATE;
    }

    public void setRATE(int RATE) {
        this.RATE = RATE;
    }

    public Double getCURRENT_MONTH_QUOTA() {
        return CURRENT_MONTH_QUOTA;
    }

    public void setCURRENT_MONTH_QUOTA(Double CURRENT_MONTH_QUOTA) {
        this.CURRENT_MONTH_QUOTA = CURRENT_MONTH_QUOTA;
    }

    public Double getSUM_QUOTA() {
        return SUM_QUOTA;
    }

    public void setSUM_QUOTA(Double SUM_QUOTA) {
        this.SUM_QUOTA = SUM_QUOTA;
    }

    public int getSEQ_NO() {
        return SEQ_NO;
    }

    public void setSEQ_NO(int SEQ_NO) {
        this.SEQ_NO = SEQ_NO;
    }

    public String getVEHICLE_ID() {
        return VEHICLE_ID;
    }

    public void setVEHICLE_ID(String VEHICLE_ID) {
        this.VEHICLE_ID = VEHICLE_ID;
    }

    public String getRULE_ID() {
        return RULE_ID;
    }

    public void setRULE_ID(String RULE_ID) {
        this.RULE_ID = RULE_ID;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    private static String concat(String... strs) {
        StringBuilder sb = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            sb.append(strs[i]);
        }
        return sb.toString();
    }
}
