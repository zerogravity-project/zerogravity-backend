package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.DailyStatics;

public interface DailyStaticsDao {
    public abstract DailyStatics selectDailyStatics(long userId);
    public abstract int insertDailyStatics(DailyStatics dailyStatics);
    public abstract int updateDailyStatics(DailyStatics dailyStatics);
    public abstract DailyStatics selectDailyStatics(String dailyStaticsId);
    public abstract DailyStatics findDailyStaticsByDateAndUserId(@Param("createdTime") Timestamp createdTime, @Param("userId") long userId);

}
