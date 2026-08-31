package model;

import java.sql.Date;

public class Seminar extends Event{
	private String speaker_name;
	public String getSpeaker_Name() {
		return this.speaker_name;
	}
	public void setSpeaker_Name(String speaker_name) {
		this.speaker_name=speaker_name;
	}
	public Seminar(String name,String location,int capacity,Date event_date,Date registration_last_date,String category,double price,String speaker_name){
		super(name,location,capacity,event_date,registration_last_date,category,price);
		this.speaker_name=speaker_name;
	}
	@Override
	public void notifyusers() {
		System.out.println("Reminder: Speaker assigned for seminar");
	}
	@Override
	public String getSpecialName() {
		return speaker_name;
	}
}
