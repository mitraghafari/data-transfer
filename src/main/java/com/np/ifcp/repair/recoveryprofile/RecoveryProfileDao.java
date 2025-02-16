/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.ifcp.repair.recoveryprofile;

import com.np.ifcp.repair.model.AllocatedRecordsBean;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author ghaffari.m
 */
public interface RecoveryProfileDao {

    //    public int insertSkippedSequence(AllocatedRecordsBean vb);
//    public int updatePetrolInfo(AllocatedRecordsBean vb);
    public String getCardInfoRule(String vehicle_id) throws Exception;

    public RecoveryProfileBean getRevoveryProfile(String vehicle_id, int rate) throws Exception;

    public int getLastSeqNo(String vehicle_id) throws Exception;
    public int updateRecoveryProfile(RecoveryProfileBean vb, String rule_id) throws Exception;

    public int insertRecoveryProfile(AllocatedRecordsBean allocatedRecordsBean, String rule) throws Exception;

    public List<AllocatedRecordsBean> getAllocationProfilesList();

    public int updateTempRecord(String vehicle_id, int rate) throws Exception;

    public int transferAllocateTOAllocateTempRecord();

    public int updateAllocatedRecord();

    public int updateAllocatedRecords(String vehicle_id, int rate) throws Exception ;



}
