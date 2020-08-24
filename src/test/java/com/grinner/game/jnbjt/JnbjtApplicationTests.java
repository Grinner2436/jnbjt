package com.grinner.game.jnbjt;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
class JnbjtApplicationTests {

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Transactional
    void contextLoads() {
        long count = activityRepository.count();
        Optional<Activity> activity = activityRepository.findById(Integer.valueOf(1));
        Activity a = activity.get();
        System.out.println(activity.get().toString());
    }

}
