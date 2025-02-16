package com.np.ifcp.repair.rate;

import com.np.ifcp.repair.DAOManager;
import com.np.ifcp.repair.recoveryprofile.RecoveryProfileDaoImp;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Component
public class RateDaoImp {
    @Autowired
    private DAOManager daoManager;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RecoveryProfileDaoImp.class);

    public List<Rate> getRateList() {
        Rate rate = null;
        List<Rate> rateList = new ArrayList<Rate>();
        String sql = "select id,rate from rate  order by id asc";
        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rate = new Rate();
                    rate.setRate(rs.getInt(1));
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return rateList;
    }
    public int getIndex(int amount) {
       int index=0;
        List<Rate> rateList = new ArrayList<Rate>();
        String sql = "select id,rate from rate  where rate=?";
        try (Connection conn = daoManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,amount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    index=rs.getInt(1);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return index;
    }

}

