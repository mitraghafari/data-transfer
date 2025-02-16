package com.np.ifcp.repair.stagelog;

import com.np.ifcp.repair.DAOManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;


@Repository
public class StageLogDaoImpl implements StageLogDao {
    @Autowired
    DAOManager daoManager;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StageLogDaoImpl.class);

    @Override
    public void insertStageLogInfo(String stage, String subStage, String desc) {

        try {

            String st = "INSERT INTO STAGE_LOG VALUES(STAGE_LOG_SEQ.NEXTVAL,?,SYSDATE,?,?)";
            try (Connection conn = daoManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(st)) {
                ps.setString(1, desc);
                ps.setString(2, stage);
                ps.setString(3, subStage);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }
}
