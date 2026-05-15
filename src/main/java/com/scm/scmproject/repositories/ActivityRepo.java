package com.scm.scmproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.scmproject.entities.Activity;
import com.scm.scmproject.entities.User;

@Repository
public interface ActivityRepo extends JpaRepository<Activity, Long>{
    List<Activity>findTop5ByUserOrderByTimestampDesc(User user);
}
