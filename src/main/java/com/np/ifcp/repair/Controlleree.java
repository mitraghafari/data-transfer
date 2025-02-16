package com.np.ifcp.repair;

import com.np.ifcp.repair.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author ghaffari.m
 */
@Component
public class Controlleree {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controlleree.class);
    @Autowired
    private  Dispatcher dispatcher;

    @Scheduled(cron = "*/100 * * * * *")
    public void execute() {
       try {
               LOGGER.info("Job Strated: {}", Constants.AllocatedToRecovery);
               dispatcher.dispatchAllocated();
               LOGGER.info("Job ENDed-------------------------------: {}", Constants.AllocatedToRecovery);

           System.exit(0);
       } catch (Exception ex) {
           LOGGER.error("error", ex);
       }
   }
}
