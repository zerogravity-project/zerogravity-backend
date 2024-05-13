package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.zerogravity.myapp.model.dto.PeriodicStatics;

public interface PeriodicStaticsDao {
	
	public abstract PeriodicStatics selectPeriodicStatics(long userId);
	public abstract PeriodicStatics selectPeriodicStaticsByUserAndType(long userId, String periodType);
	public abstract PeriodicStatics findByPeriodAndUserId(long userId, Timestamp periodStart, Timestamp periodEnd);
	public abstract int insertPeriodicStatics(PeriodicStatics statics);
	public abstract int updatePeriodicStatics(PeriodicStatics statics);

}
