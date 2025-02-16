package com.np.ifcp.repair.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;



@Component
public class CardInfo implements Serializable {

    private String Card_id;
    private String Status;

    public String getCard_id() {
        return Card_id;
    }

    public void setCard_id(String Card_id) {
        this.Card_id = Card_id;
    }

    
    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
    
   
    private static String concat(String... strs) {
        StringBuilder sb = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            sb.append(strs[i]);
        }
        return sb.toString();
    }
}
