package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.PeriodicStatics;

public interface PeriodicStaticsDao {
	
	public abstract PeriodicStatics selectPeriodicStatics(long userId);
	public abstract int insertPeridodicStatics(PeriodicStatics periodicStatics);
	public abstract int updatePeriodiccStatics(PeriodicStatics periodicStatics);
	public abstract PeriodicStatics selectPeriodicStaticsByUserAndType(long userId, String periodType);

}
