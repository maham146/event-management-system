package model;

import java.sql.Date;

public abstract class Event {
	private String name,location,category;
	private int capacity,event_id;
	private Date event_date,registration_last_date;
	private double price;
	public Event(String name,String location,int capacity,Date event_date,Date registration_last_date,String category,double price){
		this.name=name;
		this.location=location;
		this.capacity=capacity;
		this.event_date=event_date;
		this.registration_last_date=registration_last_date;
		this.category=category;
		this.price=price;
	}
	public int getEvent_Id() {
		return this.event_id;
	}
	public void setEvent_Id(int event_id) {
		this.event_id=event_id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location=location;
	}
	public int getCapacity() {
		return this.capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity=capacity;
	}
	public Date getEvent_Date() {
		return this.event_date;
	}
	public void setEvent_Date(Date event_date) {
		this.event_date=event_date;
	}
	public Date getRegistration_Last_Date() {
		return this.registration_last_date;
	}
	public void setRegistration_Last_Date(Date registration_last_date) {
		this.registration_last_date=registration_last_date;
	}
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category=category;
	}
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price=price;
	}
	public void notifyusers() {
		System.out.println("General Event Notification!");
	}
	public abstract String getSpecialName();
}


