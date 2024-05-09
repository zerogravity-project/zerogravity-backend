package com.zerogravity.myapp.model.dto;

public class UserInfo {
	private long userId;
	private Gender gender;
	private String ageRange;
	private String birthday;
	private String birthyear;
	
	public UserInfo() {
		
	}

	public UserInfo(long userId, Gender gender, String ageRange, String birthday, String birthyear) {
		this.userId = userId;
		this.gender = gender;
		this.ageRange = ageRange;
		this.birthday = birthday;
		this.birthyear = birthyear;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}

	public enum Gender {
		male, female;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", gender=" + gender + ", ageRange=" + ageRange + ", birthday=" + birthday
				+ ", birthyear=" + birthyear + "]";
	}
}
