package model;

import java.sql.Date;

public class Workshop extends Event{
	private String trainer_name;
	public String getTrainer_Name() {
		return this.trainer_name;
	}
	public void setTrainer_Name(String trainer_name) {
		this.trainer_name=trainer_name;
	}
	public Workshop(String name,String location,int capacity,Date event_date,Date registration_last_date,String category,double price,String trainer_name) {
		super(name,location,capacity,event_date,registration_last_date,category,price);
		this.trainer_name=trainer_name;
	}
	@Override
	public void notifyusers() {
		System.out.println("Reminder: Trainer assigned for workshop");
	}
	@Override
	public String getSpecialName() {
		return trainer_name;
	}
}
