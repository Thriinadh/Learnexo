package com.learnexo.model.connect.group;

import java.util.Date;
import java.util.Map;

public class Group {
    private String groupName;
    private String adminId;
    private Date dateCreated;

    private Map<String,Boolean> members;//user_id, true
}
