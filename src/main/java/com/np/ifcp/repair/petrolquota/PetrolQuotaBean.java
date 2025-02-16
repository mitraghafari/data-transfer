package com.np.ifcp.repair.petrolquota;

import java.util.Date;

/**
 *
 * @author ghaffari.m
 */
public class PetrolQuotaBean {

    String CARD_ID;
    Date QUOTA_DATE;
    int QUOTA;
    String RATE;
    int SEND_COUNT;
    int SEQ_NO;
    String VEHICLE_ID;
    String MAC;
    float TOTAL_REMAIN;
    Double THRESHOLD;

    public String getCARD_ID() {
        return CARD_ID;
    }

    public void setCARD_ID(String CARD_ID) {
        this.CARD_ID = CARD_ID;
    }

    public Date getQUOTA_DATE() {
        return QUOTA_DATE;
    }

    public void setQUOTA_DATE(Date QUOTA_DATE) {
        this.QUOTA_DATE = QUOTA_DATE;
    }

    public int getQUOTA() {
        return QUOTA;
    }

    public void setQUOTA(int QUOTA) {
        this.QUOTA = QUOTA;
    }

    public String getRATE() {
        return RATE;
    }

    public void setRATE(String RATE) {
        this.RATE = RATE;
    }

    public int getSEND_COUNT() {
        return SEND_COUNT;
    }

    public void setSEND_COUNT(int SEND_COUNT) {
        this.SEND_COUNT = SEND_COUNT;
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

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public float getTOTAL_REMAIN() {
        return TOTAL_REMAIN;
    }

    public void setTOTAL_REMAIN(float TOTAL_REMAIN) {
        this.TOTAL_REMAIN = TOTAL_REMAIN;
    }

   

    public void setTOTAL_REMAIN(int TOTAL_REMAIN) {
        this.TOTAL_REMAIN = TOTAL_REMAIN;
    }

    public Double getTHRESHOLD() {
        return THRESHOLD;
    }

    public void setTHRESHOLD(Double THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }
}
