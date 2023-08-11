package com.zhmyhpyh.repository;

import com.zhmyhpyh.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.totalMessageNumber = u.totalMessageNumber + 1, " +
            "u.dailyMessageNumber = u.dailyMessageNumber + 1 where u.id is not null and u.id = :id")
    void updateMsgNumberByUserId(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("update User u set u.dailyMessageNumber = 0")
    void resetDailyMsgNumber();
}
