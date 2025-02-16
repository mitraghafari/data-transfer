package com.np.ifcp.repair.stagelog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author ghaffari.m
 */
public interface StageLogDao {
    
    public void insertStageLogInfo(String stage, String subStage, String desc);

    public static void log(int stage, int subStage, String msg, Connection conn) throws SQLException {
        String query = "INSERT INTO USER_NFA.STAGE_LOG"
                + " (STAGE, SUB_STAGE, LOG_DESCRIPTION, OPERATION_TIME, ID) values ('" + stage + "', '" + subStage
                + "', '" + msg + "', ?, STAGE_LOG_SEQ.NEXTVAL)";
        PreparedStatement pstmt = conn.prepareStatement(msg);
        pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
        pstmt.executeUpdate();
        pstmt.close();
    }
}
