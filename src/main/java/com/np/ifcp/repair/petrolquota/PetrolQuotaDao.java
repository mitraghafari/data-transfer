/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.ifcp.repair.petrolquota;

import com.np.ifcp.repair.model.CardInfo;
import com.np.ifcp.repair.model.RuleQoutaBean;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileBean;

import java.util.List;

/**
 * @author ghaffari.m
 */
public interface PetrolQuotaDao {

    List<RecoveryProfileBean> getRecoveryProfileList();

    List<PetrolQuotaBean> getPetrolQuotaList();

    CardInfo getCardStatus(String card_id) throws Exception;

    int updatePetrolQuota(String newCard_id, String oldCarddId);

    int lockQms();

    int unLockQms();

    void addToSkipped(PetrolQuotaBean petrolQuataInfoBean) throws Exception;

    PetrolQuotaBean getPetrolQuota(String vehicle_id);

    CardInfo getCardStatusForVehicle(String vehicle_id);

    int updateRecoveryProfile(RecoveryProfileBean recoveryProfileBean, int seq_no) throws Exception;

    void deletefromPetrol(PetrolQuotaBean petrolQuataInfoBean) throws Exception;

    RuleQoutaBean getRuleQuata(String rule_id) throws Exception;

    int insertPetrolQuota(RecoveryProfileBean recoveryProfileBean, float cardLimit, int seq_no) throws Exception;

    CardInfo getCardInfo(String vehicle_id) throws Exception;
}
