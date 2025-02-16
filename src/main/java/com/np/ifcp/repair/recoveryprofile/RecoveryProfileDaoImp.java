package com.np.ifcp.repair.recoveryprofile;

import com.np.ifcp.repair.DAOManager;
import com.np.ifcp.repair.common.DES;
import com.np.ifcp.repair.model.AllocatedRecordsBean;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ghaffari.m
 */
@Repository
public class RecoveryProfileDaoImp implements RecoveryProfileDao {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RecoveryProfileDaoImp.class);
    private static String keyRecoveryProfile = "RECOVERY_PROFILE";
    @Autowired
    private DAOManager daoManager;


    @Override
    public List<AllocatedRecordsBean> getAllocationProfilesList() {
        AllocatedRecordsBean allocatedRecordsBean=null;
        List<AllocatedRecordsBean> ar = new ArrayList<AllocatedRecordsBean>();
        String sql = "select VEHICLE_ID, sum(CONFIRM_QUOTA) CONFIRM_QUOTA ,RATE,PROCESSED_FLAG \n"
                + " from ALLOCATED_RECORDS \n"
                + " where  VEHICLE_ID is not null and PROCESSED_FLAG=0 and CONFIRM_FLAG=1 and CONFIRM_QUOTA>0 \n"
                + " group by VEHICLE_ID,RATE,PROCESSED_FLAG";
        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    allocatedRecordsBean = new AllocatedRecordsBean();
                    allocatedRecordsBean.setVEHICLE_ID(rs.getString(1));
                    allocatedRecordsBean.setCONFIRM_QUOTA(rs.getDouble(2));
                    allocatedRecordsBean.setRate(rs.getInt(3));
                    ar.add(allocatedRecordsBean);
                }
            }


        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return ar;

    }

    @Override
    public int updateRecoveryProfile(RecoveryProfileBean vb, String rule_id, AllocatedRecordsBean allocatedRecordsBean) throws Exception {
        int isUpdate = 0;
        StringBuilder sql = new StringBuilder();
        Double sum=0.0;
        Double current_quota=0.0;
        sql.append("update RECOVERY_PROFILE set  ");
       sum = allocatedRecordsBean.getCONFIRM_QUOTA()+ vb.getSUM_QUOTA();
            current_quota=allocatedRecordsBean.getCONFIRM_QUOTA()+ vb.getCURRENT_MONTH_QUOTA();
            sql.append("  CURRENT_MONTH_QUOTA1=?,SUM_QUOTA1=? ");
            int seq_no = vb.getSEQ_NO() + 1;
        sql.append(",seq_no =?,PROCESSED_FLAG=? ,MAC=? where   VEHICLE_ID=? " );
        String new_mac = DES.createMac("RECOVERY_PROFILE", vb.getVEHICLE_ID(),
                vb.getCURRENT_MONTH_QUOTA(), vb.getSUM_QUOTA(),
               seq_no);
        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setDouble(1, current_quota);
            ps.setDouble(2, sum);

            ps.setInt(3, seq_no);
            ps.setInt(4, 0);
            ps.setString(5,new_mac);
            ps.setString(6,vb.getVEHICLE_ID());
            isUpdate = ps.executeUpdate();

        }

        return isUpdate;

    }

    @Override
    public RecoveryProfileBean getRevoveryProfile(String vehicle_id) throws Exception {
        RecoveryProfileBean recoveryProfileBean=null;
        String sql = "select VEHICLE_ID,CURRENT_MONTH_QUOTA1,SUM_QUOTA1,CURENT_MONTH_QUOTA2,SUM_QUOTA2,RULE_ID,MAC,SEQ_NO  from RECOVERY_PROFILE where  VEHICLE_ID=?  ";

        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle_id);
             try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    recoveryProfileBean = new RecoveryProfileBean();
                    recoveryProfileBean.setVEHICLE_ID(rs.getString(1));
                    recoveryProfileBean.setCURRENT_MONTH_QUOTA(rs.getDouble(2));
                    recoveryProfileBean.setSUM_QUOTA(rs.getDouble(3));
                    recoveryProfileBean.setRULE_ID(rs.getString(4));
                    recoveryProfileBean.setMAC(rs.getString(5));
                    recoveryProfileBean.setSEQ_NO(rs.getInt(6));
                }
            }
        }
        return recoveryProfileBean;
    }

    @Override
    public int getLastSeqNo(String vehicle_id) throws Exception {
        int lastSeq = -1;
        String sql = "select Max(SEQ_NO)  from RECOVERY_PROFILE where  VEHICLE_ID=? ";
        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle_id);
            // ps.setInt(2, rate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    //  recoveryProfileBean = new RecoveryProfileBean();
                    lastSeq = rs.getInt(1);
                    //   recoveryProfileBean.setSEQ_NO(initSet.getInt(1));
                }
            }
        }

        return lastSeq;
    }

    @Override
    public int insertRecoveryProfile(AllocatedRecordsBean allocatedRecordsBean, String rule) throws Exception {
        int isInsert = 0;
        String[] fields = {"VEHICLE_ID",  "SEQ_NO",  "RULE_ID", "MAC", "PROCESSED_FLAG", "ID"};
        StringBuilder subQuery = new StringBuilder("INSERT INTO RECOVERY_PROFILE(");
        for (String field : fields) {
            subQuery.append(field);
            subQuery.append(",");
        }
        subQuery.append("CURRENT_MONTH_QUOTA1, SUM_QUOTA1" );
        subQuery.append(") values (?,?,?,?,0,RECOVERY_PROFILE_SEQUENCE.NEXTVAL,?,?)");

        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(subQuery.toString());) {
            ps.setString(1, allocatedRecordsBean.getVEHICLE_ID());

            int seqNo =0;
            ps.setInt(2, seqNo);
            ps.setString(3, rule);
            String new_mac = DES.createMac("RECOVERY_PROFILE", allocatedRecordsBean.getVEHICLE_ID(),
                    allocatedRecordsBean.getCONFIRM_QUOTA(), allocatedRecordsBean.getCONFIRM_QUOTA(),
                    0, allocatedRecordsBean.getRate());
            ps.setString(4, new_mac);
            ps.setDouble(5, allocatedRecordsBean.getCONFIRM_QUOTA());
            ps.setDouble(6, allocatedRecordsBean.getCONFIRM_QUOTA());

            isInsert = ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return isInsert;

    }




    @Override
    public String getCardInfoRule(String vehicle_id) throws Exception {
        String rule = "";

        String sql = "select * from RULE_QUOTA r,(select card.USAGE_CODE,card.SUBUSAGE_CODE,card.FUEL_CODE,card.clazz from csccenter.card_info card ,csccenter.CARD_INFO_PLUS cinf\n"
                + " where card.PAN=CINF.PAN and VEHICLE_ID=? and   card.real_issue_date =(select max(real_issue_date) from csccenter.CARD_INFO_PLUS inf where VEHICLE_ID=cinf.vehicle_id )) a \n"
                + " where r.USAGE_CODE=a.USAGE_CODE \n"
                + " and  r.SUBUSAGE_CODE=a.SUBUSAGE_CODE and  r.FUEL_CODE=a.FUEL_CODE and  r.clazz=a.clazz";


        try {
            try (Connection conn = daoManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, vehicle_id);
                //  ps.setString(2, vehicle_id);
                try (ResultSet initSet = ps.executeQuery()) {
                    if (initSet.next()) {
                        rule = initSet.getString(1);
                    }

                }


            }

        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return rule;
    }

    @Override
    public int updateAllocatedRecord(String vehicle_id, int rate, int flag1, int flag2) throws Exception {
        int isUpdate = 0;
        String sql = "update  ALLOCATED_RECORDS set PROCESSED_FLAG=?,PROCESSED_DATE=sysdate where  VEHICLE_ID =? and rate=? and  PROCESSED_FLAG=?   and CONFIRM_FLAG=1  ";

        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flag1);
            ps.setString(2, vehicle_id);
            ps.setInt(3, rate);
            ps.setInt(4, flag2);

            isUpdate = ps.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return isUpdate;
    }

    private void closePs(PreparedStatement ps, Connection connection) {
        if (ps != null) {
            try {
                ps.close();
                connection.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

        }
    }


}
