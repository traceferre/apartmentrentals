package com.libertymutual.goforcode.spark.app.models;

import org.javalite.activejdbc.Model;

public class ApartmentsUsers extends Model{
	
	public ApartmentsUsers () {}
	
	public ApartmentsUsers(Long apartmentId, Long userId) {
		setApartmentId(apartmentId);
		setUserId(userId);
	}
	
	public void setApartmentId(Long apartmentId) {
		set("apartment_id", apartmentId);
	}
	
	public void setUserId(Long userId) {
		set("user_id", userId);
	}
	
	public Long getUserId() {
		return getLong("user_id");
	}
	
	public Long getApartmentId() {
		return getLong("apartment_id");
	}
	
	public Long getId() {
		return getLong("id");
	}

}
