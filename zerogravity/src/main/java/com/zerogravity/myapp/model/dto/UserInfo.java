package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 추가 정보")
public class UserInfo {
	@Schema(description = "사용자 ID", example = "1")
	private long userId;
	
	@Schema(description = "성별", example = "male")
	private Gender gender;
	
	@Schema(description = "연령 범위", example = "20~29")
	private String ageRange;
	
	@Schema(description = "생일", example = "0105")
	private String birthday;
	
	@Schema(description = "출생년도", example = "1985")
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
