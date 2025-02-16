package com.np.ifcp.repair.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class AllocatedRecordsBean implements Serializable {

    private String PAN;
    private String VEHICLE_ID;
    private Double CONFIRM_QUOTA;
    private String MAC;
    private int Rate;
    
    public int getRate() {
        return Rate;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

     public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getVEHICLE_ID() {
        return VEHICLE_ID;
    }

    public void setVEHICLE_ID(String VEHICLE_ID) {
        this.VEHICLE_ID = VEHICLE_ID;
    }

    public Double getCONFIRM_QUOTA() {
        return CONFIRM_QUOTA;
    }

    public void setCONFIRM_QUOTA(Double CONFIRM_QUOTA1) {
        this.CONFIRM_QUOTA = CONFIRM_QUOTA1;
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
