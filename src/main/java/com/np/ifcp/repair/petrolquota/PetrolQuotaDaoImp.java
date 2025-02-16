package com.np.ifcp.repair.petrolquota;

/**
 * @author ghaffari.m
 */

import com.np.ifcp.repair.DAOManager;
import com.np.ifcp.repair.common.DES;
import com.np.ifcp.repair.model.CardInfo;
import com.np.ifcp.repair.model.RuleQoutaBean;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileBean;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PetrolQuotaDaoImp implements PetrolQuotaDao {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PetrolQuotaDaoImp.class);
    @Autowired
    private DAOManager daoManager;

    private static String keyPetrolQuota = "PETROL_QUOTA_INFO";

    @Override
    public List<RecoveryProfileBean> getRecoveryProfileList() {
        List<RecoveryProfileBean> ar = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = daoManager.getConnection();
            String sql = "select VEHICLE_ID,CURRENT_MONTH_QUOTA,SEQ_NO,RULE_ID,RATE,MAC from RECOVERY_PROFILE where  VEHICLE_ID is not null and PROCESSED_FLAG=0 ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            RecoveryProfileBean arb = new RecoveryProfileBean();
            while (rs.next()) {
                arb = new RecoveryProfileBean();
                arb.setVEHICLE_ID(rs.getString(1));
                arb.setCURRENT_MONTH_QUOTA(rs.getDouble(2));
                arb.setSEQ_NO(rs.getInt(3));
                arb.setRULE_ID(rs.getString(4));
                arb.setRATE(rs.getInt(5));
                arb.setMAC(rs.getString(6));
                ar.add(arb);
            }

        } catch (SQLException ex){
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            close(ps, rs);

        }

        return ar;
    }

    private void close(PreparedStatement ps, ResultSet rs) {
        if (ps != null) {
            try {
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

        }
    }

    @Override
    public List<PetrolQuotaBean> getPetrolQuotaList() {
        List<PetrolQuotaBean> ar = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = daoManager.getConnection();
            String sql = "select CARD_ID,QUOTA_DATE,QUOTA,RATE,SEND_COUNT,SEQ_NO,VEHICLE_ID,MAC,TOTAL_REMAIN,THRESHOLD from PETROL_QUOTA_INFO ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            PetrolQuotaBean arb = null;

            while (rs.next()) {
                arb = new PetrolQuotaBean();
                arb.setCARD_ID(rs.getString(1));
                arb.setQUOTA_DATE(rs.getDate(2));
                arb.setQUOTA(rs.getInt(3));
                arb.setRATE(rs.getString(4));
                arb.setSEND_COUNT(rs.getInt(5));
                arb.setSEQ_NO(rs.getInt(6));
                arb.setVEHICLE_ID(rs.getString(7));
                arb.setMAC(rs.getString(8));
                arb.setTOTAL_REMAIN(rs.getFloat(9));
                arb.setTHRESHOLD(rs.getDouble(10));
                ar.add(arb);
            }

        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            close(ps, rs);

        }

        return ar;
    }

    @Override
    public CardInfo getCardStatus(String card_id) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        CardInfo card = new CardInfo();

        try {
            Connection conn = daoManager.getConnection();
            String sql = " select CARD_ID,CARD_STATUS  from csccenter.card_info card where CARD_ID=?   order by  REAL_ISSUE_DATE desc  ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, card_id);
            rs = ps.executeQuery();

            if (rs.next()) {

                card.setCard_id(rs.getString(1));
                card.setStatus(rs.getString(2));
            }

        } finally {

        }

        return card;

    }

    @Override
    public CardInfo getCardStatusForVehicle(String vehicle_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CardInfo card = new CardInfo();
        try {
            Connection conn = daoManager.getConnection();
            String sql = " select CARD_ID,CARD_STATUS  from csccenter.card_info,CARD_INFO_PLUS info where card_info.card_id= info.card_id  and   CARD_ID=? and rownum=1 order by  REAL_ISSUE_DATE desc  ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, vehicle_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                card.setCard_id(rs.getString(1));
                card.setStatus(rs.getString(2));
            }

        } catch (SQLException  ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            close(ps, rs);

        }

        return card;

    }

    @Override
    public int lockQms() {
        int isUpdate = 0;
        PreparedStatement ps = null;
        try {
            Connection conn = daoManager.getConnection();
            String sql = " update PARAMETERS set PARAMETER_VALUE=1 where  PARAMETER_TYPE='LOCKED' ";
            ps = conn.prepareStatement(sql);
            isUpdate = ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            closePs(ps);

        }

        return isUpdate;

    }

    @Override
    public void addToSkipped(PetrolQuotaBean petrolQuataInfoBean) throws Exception {
        int isInsert = 0;
        String[] fields = {"VEHICLE_ID", "CARD_ID", "SEQ_NO", "QUOTA", "INSERT_DATE", "SEND_COUNT"};

        PreparedStatement ps = null;
        try {
            Connection conn = daoManager.getConnection();
            StringBuilder subQuery = new StringBuilder("INSERT INTO SKIPPED_SEQUENCE(");
            for (String field : fields) {
                subQuery.append(field);
                subQuery.append(",");
            }
            subQuery.deleteCharAt(subQuery.length() - 1);
            subQuery.append(") values (?,?,?,?,?,?)");
            ps = conn.prepareStatement(subQuery.toString());
            ps.setString(1, petrolQuataInfoBean.getVEHICLE_ID());
            ps.setString(2, petrolQuataInfoBean.getCARD_ID());
            ps.setFloat(3, petrolQuataInfoBean.getSEQ_NO());
            ps.setInt(4, petrolQuataInfoBean.getQUOTA());
            ps.setDate(5, (Date) petrolQuataInfoBean.getQUOTA_DATE());
            ps.setInt(6, petrolQuataInfoBean.getSEND_COUNT());
            ps.executeUpdate();
            ps.close();

        } finally {
            closePs(ps);

        }

    }

    @Override
    public int updatePetrolQuota(String newCard_id, String oldCarddId) {
        int isUpdate = 0;
        PreparedStatement ps = null;

        try {
            Connection conn = daoManager.getConnection();
            String sql = " update PETROL_QUOTA_INFO set  CARD_ID='" + newCard_id + "' where CARD_ID='" + oldCarddId + "' ";
            ps = conn.prepareStatement(sql);
            isUpdate = ps.executeUpdate();

        } catch (SQLException  ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            closePs(ps);

        }

        return isUpdate;
    }

    @Override
    public PetrolQuotaBean getPetrolQuota(String vehicle_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PetrolQuotaBean petrolQuataInfoBean = new PetrolQuotaBean();
        try {
            Connection conn = daoManager.getConnection();
            String sql = " select CARD_ID,QUOTA_DATE,QUOTA,SEND_COUNT,SEQ_NO,VEHICLE_ID,MAC,TOTAL_REMAIN,THRESHOLD,RATE from   PETROL_QUOTA_INFO   "
                    + "where  VEHICLE_ID=:vehicle_id  ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                petrolQuataInfoBean.setCARD_ID(rs.getString(1));
                petrolQuataInfoBean.setQUOTA_DATE(rs.getDate(2));
                petrolQuataInfoBean.setQUOTA(rs.getInt(3));
                petrolQuataInfoBean.setSEND_COUNT(rs.getInt(4));
                petrolQuataInfoBean.setSEQ_NO(rs.getInt(5));
                petrolQuataInfoBean.setVEHICLE_ID(rs.getString(6));
                petrolQuataInfoBean.setMAC(rs.getString(7));
                petrolQuataInfoBean.setTOTAL_REMAIN(rs.getFloat(8));
                petrolQuataInfoBean.setTHRESHOLD(rs.getDouble(9));
                petrolQuataInfoBean.setRATE(rs.getString(10));
            }

        } catch (SQLException  ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            close(ps, rs);

        }

        return petrolQuataInfoBean;

    }

    @Override
    public int updateRecoveryProfile(RecoveryProfileBean recoveryProfileBean, int seq_no) throws Exception {

        int isUpdate = 0;
        PreparedStatement ps = null;

        try {
            Connection conn = daoManager.getConnection();
            String sql = " update RECOVERY_PROFILE set  CURRENT_MONTH_QUOTA=0,PROCESSED_FLAG=1,SEQ_NO=:seq_no where VEHICLE_ID='" + recoveryProfileBean.getVEHICLE_ID() ;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, seq_no);
            isUpdate = ps.executeUpdate();

        } finally {
            closePs(ps);

        }

        return isUpdate;
    }

    @Override
    public RuleQoutaBean getRuleQuata(String rule_id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        RuleQoutaBean ruleQoutaBean = null;
        try {
            Connection conn = daoManager.getConnection();
            String sql = " select RULE_ID,CLAZZ,USAGE_CODE,SUBUSAGE_CODE,FUEL_CODE,CARD_LIMIT from   qms.RULE_QUOTA   "
                    + "where  RULE_ID=? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, rule_id);
            ;
            rs = ps.executeQuery();

            while (rs.next()) {
                ruleQoutaBean = new RuleQoutaBean();
                ruleQoutaBean.setRULE_ID(rs.getString(1));
                ruleQoutaBean.setCLAZZ(rs.getString(2));
                ruleQoutaBean.setUSAGE_CODE(rs.getString(3));
                ruleQoutaBean.setSUBUSAGE_CODE(rs.getString(4));
                ruleQoutaBean.setFUEL_CODE(rs.getString(5));
                ruleQoutaBean.setCARD_LIMIT(rs.getFloat(6));

            }

        } finally {
            close(ps, rs);

        }

        return ruleQoutaBean;

    }

    @Override
    public int insertPetrolQuota(RecoveryProfileBean recoveryProfileBean, float cardLimit, int seq_no) throws Exception {

        int isInsert = 0;
        String[] fields = {"CARD_ID", "QUOTA_DATE", "QUOTA", "RATE", "SEND_COUNT", "SEQ_NO", "VEHICLE_ID", "MAC", "TOTAL_REMAIN", "THRESHOLD", "PROCESSED_FLAG"};

        PreparedStatement ps = null;
        Double Treshold = cardLimit - recoveryProfileBean.getCURRENT_MONTH_QUOTA();
        try {
            Connection conn = daoManager.getConnection();
            String new_mac = DES.createMac(keyPetrolQuota, recoveryProfileBean.getVEHICLE_ID(),
                    recoveryProfileBean.getCURRENT_MONTH_QUOTA(), recoveryProfileBean.getSEQ_NO(),
                    0, recoveryProfileBean.getVEHICLE_ID(), 0, Treshold);
            StringBuilder subQuery = new StringBuilder("INSERT INTO PETROL_QUOTA_INFO(");
            for (String field : fields) {
                subQuery.append(field);
                subQuery.append(",");
            }
            subQuery.deleteCharAt(subQuery.length() - 1);
            subQuery.append(") values (?,SySDate,?,?,0,?,?,?,?,?,0)");
            ps = conn.prepareStatement(subQuery.toString());
            ps.setString(1, recoveryProfileBean.getCard_ID());
            ps.setDouble(2, recoveryProfileBean.getCURRENT_MONTH_QUOTA());
            ps.setInt(3, recoveryProfileBean.getRATE());
            ps.setFloat(4, seq_no);
            ps.setString(5, recoveryProfileBean.getVEHICLE_ID());
            ps.setString(6, new_mac);
            ps.setDouble(7, recoveryProfileBean.getCURRENT_MONTH_QUOTA());
            ps.setDouble(8, Treshold);
            // ps.setInt(10,0);

            isInsert = ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            closePs(ps);

        }

        return isInsert;

    }

    @Override
    public int unLockQms() {
        int isUpdate = 0;
        PreparedStatement ps = null;
        CardInfo card = new CardInfo();
        try {
            Connection conn = daoManager.getConnection();
            String sql = " update PARAMETERS set PARAMETER_VALUE=0 where  PARAMETER_TYPE='LOCKED'  ";
            ps = conn.prepareStatement(sql);
            isUpdate = ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            closePs(ps);

        }

        return isUpdate;
    }

    private void closePs(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();

            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

        }
    }

    @Override
    public CardInfo getCardInfo(String vehicle_id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CardInfo card = new CardInfo();
        try {
            Connection conn = daoManager.getConnection();
            String sql = " select CARD_ID,CARD_STATUS  from csccenter.card_info card,csccenter.CARD_INFO_PLUS info where card.Pan=info.Pan and vehicle_id=? and CARD_STATUS in (0,2) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, vehicle_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                card.setCard_id(rs.getString(1));
                card.setStatus(rs.getString(2));
            }

        } finally {
            close(ps, rs);

        }

        return card;
    }

    @Override
    public void deletefromPetrol(PetrolQuotaBean petrolQuataInfoBean) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CardInfo card = new CardInfo();
        try {
            Connection conn = daoManager.getConnection();
            String sql = "DELETE FROM PETROL_QUOTA_INFO petrol where  CARD_ID=? and VEHICLE_ID=? and SEND_COUNT=? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, petrolQuataInfoBean.getCARD_ID());
            ps.setString(2, petrolQuataInfoBean.getVEHICLE_ID());
            ps.setInt(3, petrolQuataInfoBean.getSEND_COUNT());
            int isDelete = ps.executeUpdate();

        } finally {
            close(ps, rs);

        }

    }

}
