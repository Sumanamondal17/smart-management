package com.scm.scmproject.services;

import java.util.List;

import com.scm.scmproject.entities.Activity;
import com.scm.scmproject.entities.User;

public interface  ActivityService {
    void saveActivity(User user, String action, String contactName);

    List<Activity> getRecentActivities(User user);
}
