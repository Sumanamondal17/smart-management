package com.scm.scmproject.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.scmproject.entities.Activity;
import com.scm.scmproject.entities.User;
import com.scm.scmproject.repositories.ActivityRepo;
import com.scm.scmproject.services.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private ActivityRepo activityRepository;

    @Override
    public void saveActivity(User user, String action, String contactName) {
        Activity activity = Activity.builder()
                .user(user)
                .action(action)
                .contactName(contactName)
                .timestamp(LocalDateTime.now())
                .build();

        activityRepository.save(activity);
    }

    @Override
    public List<Activity> getRecentActivities(User user) {
        return activityRepository.findTop5ByUserOrderByTimestampDesc(user);
    }
    
}
