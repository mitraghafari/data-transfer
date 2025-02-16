package com.np.ifcp.repair;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;


@Component
public class DAOManager {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DAOManager.class);
    @Autowired
    private HikariDataSource qmsDataSource;


    public synchronized Connection getConnection() throws SQLException {
        return qmsDataSource.getConnection();
    }


}
