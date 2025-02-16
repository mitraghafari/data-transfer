package com.np.ifcp.repair;

import com.np.ifcp.repair.model.AllocatedRecordsBean;
import com.np.ifcp.repair.model.CardInfo;
import com.np.ifcp.repair.model.RuleQoutaBean;
import com.np.ifcp.repair.petrolquota.PetrolQuotaBean;
import com.np.ifcp.repair.petrolquota.PetrolQuotaDaoImp;
import com.np.ifcp.repair.petrolquota.PetrolQuotaBean;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileBean;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileDao;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileDaoImp;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileBean;
import com.np.ifcp.repair.stagelog.StageLogDao;
import com.np.ifcp.repair.stagelog.StageLogDaoImpl;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ghaffari.m
 */
@Component
public class Dispatcher {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    @Autowired
    private DAOManager daoManager;
    @Autowired
    private StageLogDaoImpl stageLogDaoImpl;
    @Autowired
    private PetrolQuotaDaoImp petrolQuotaDaoImp;
    @Autowired
    private RecoveryProfileDao recoveryProfileDao;
    @Autowired
    private  RecoveryProfileBean recoveryProfileBean;

    public void dispatchAllocated() throws InterruptedException, SQLException {
        LOGGER.info("start transfer from AllocatedRecords to tempAllocatedRecords ");
        int transferCount=recoveryProfileDao.transferAllocateTOAllocateTempRecord();
        LOGGER.info("end transfer from AllocatedRecords to tempAllocatedRecords  count: {} ",transferCount);
        LOGGER.info("start update from AllocatedRecords  ");
        int updateCount=recoveryProfileDao.updateAllocatedRecord();
        LOGGER.info("end update from AllocatedRecords  count:{}",updateCount);
        List<AllocatedRecordsBean> list = recoveryProfileDao.getAllocationProfilesList();
        list.parallelStream().forEach(this::allocatedRecovery);
        if (list.size() > 0) {
            stageLogDaoImpl.insertStageLogInfo("3", "1", "updatePetrolQuota " + list.size());
        }

    }

    public void dispatchRecovery() throws InterruptedException, SQLException, ClassNotFoundException {

        List<RecoveryProfileBean> list = petrolQuotaDaoImp.getRecoveryProfileList();
        list.parallelStream().forEach(this::recoverytoPetrolQutaInfo);
        if (list.size() > 0) {
            stageLogDaoImpl.insertStageLogInfo("3", "1", "updatePetrolQuota " + list.size());
        }

    }

    public void dispatchPetrolQuota() throws SQLException {
        List<PetrolQuotaEntity> list = petrolQuotaDaoImp.getPetrolQuotaList();
        list.parallelStream().forEach(this::petrolQuataInfoRun);
        if (list.size() > 0) {
            stageLogDaoImpl.insertStageLogInfo("3", "1", "updatePetrolQuota " + list.size());
        }
    }
    @Transactional
    void allocatedRecovery(AllocatedRecordsBean allocatedRecordsBean) {
        try{
            String rule_id = recoveryProfileDao.getCardInfoRule(allocatedRecordsBean.getVEHICLE_ID());
            RecoveryProfileEntity rp = recoveryProfileDao.getRevoveryProfile(allocatedRecordsBean.getVEHICLE_ID(), allocatedRecordsBean.getRate());
            if (rp != null) {
                recoveryProfileDao.updateRecoveryProfile(rp, rule_id);
            } else {
                recoveryProfileDao.insertRecoveryProfile(allocatedRecordsBean, rule_id);
            }
            int tempUpdateCount = recoveryProfileDao.updateTempRecord(allocatedRecordsBean.getVEHICLE_ID(), allocatedRecordsBean.getRate());


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

        }
    }

    @Transactional
    public void petrolQuataInfoRun(PetrolQuotaBean petrolQuataInfoBean) {
        try {

            CardInfo card = petrolQuotaDaoImp.getCardStatus(petrolQuataInfoBean.getCard_id());
            if (!card.getStatus().equals("0") && !card.getStatus().equals("2")) {
                if (petrolQuataInfoBean.getSEND_COUNT() > 0) {
                    petrolQuotaDaoImp.addToSkipped(petrolQuataInfoBean);
                    stageLogDaoImpl.insertStageLogInfo("3", "1", "addToSkipped");
                    petrolQuotaDaoImp.deletefromPetrol(petrolQuataInfoBean);
                    stageLogDaoImpl.insertStageLogInfo("3", "2", "deletefromPetrol");
                } else if (petrolQuataInfoBean.getSEND_COUNT() == 0) {
                    CardInfo newCard = petrolQuotaDaoImp.getCardInfo(petrolQuataInfoBean.getVEHICLE_ID());
                    petrolQuotaDaoImp.updatePetrolQuota(newCard.getCard_id(), petrolQuataInfoBean.getCard_id());
                    stageLogDaoImpl.insertStageLogInfo("3", "1", "updatePetrolQuota");
                }

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
    @Transactional
    void recoverytoPetrolQutaInfo(RecoveryProfileBean recoveryProfileEntity) {
        try {
            PetrolQuotaDaoImp adao = new PetrolQuotaDaoImp();
            CardInfo card = adao.getCardInfo(recoveryProfileBean.getVEHICLE_ID());
            if (card.getStatus().equals("0") || card.getStatus().equals("2")) {
                RuleQoutaBean ruleQoutaBean = adao.getRuleQuata(recoveryProfileBean.getRULE_ID());
                int seq_no = recoveryProfileBean.getSEQ_NO() + 1;
                recoveryProfileBean.setCard_ID(card.getCard_id());
                adao.insertPetrolQuota(recoveryProfileBean, ruleQoutaBean.getCARD_LIMIT(), seq_no);
                adao.updateRecoveryProfile(recoveryProfileBean, seq_no);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}



