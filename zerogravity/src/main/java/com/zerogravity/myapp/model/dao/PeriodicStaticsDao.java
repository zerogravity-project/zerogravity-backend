package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.PeriodicStatics;

public interface PeriodicStaticsDao {
	
	public abstract PeriodicStatics selectPeriodicStatics(long userId);
	public abstract PeriodicStatics selectPeriodicStaticsByUserAndType(long userId, String periodType);
	public abstract PeriodicStatics findByPeriodAndUserId(@Param("userId") long userId, @Param("periodStart") Timestamp periodStart, @Param("periodEnd") Timestamp periodEnd);
	public abstract int insertPeriodicStatics(PeriodicStatics statics);
	public abstract int updatePeriodicStatics(PeriodicStatics statics);

}
