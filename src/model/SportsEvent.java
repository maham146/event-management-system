package model;

import java.sql.Date;

public class SportsEvent extends Event{
	private String team_name;
	public String getTeam_Name() {
		return this.team_name;
	}
	public void setTeam_Name(String team_name) {
		this.team_name=team_name;
	}
	public SportsEvent(String name,String location,int capacity,Date event_date,Date registration_last_date,String category,double price,String team_name) {
		super(name,location,capacity,event_date,registration_last_date,category,price);
		this.team_name=team_name;
	}
	@Override
	public void notifyusers() {
		System.out.println("Reminder: Team assigned for Sports Events");
	}
	@Override
	public String getSpecialName() {
		return team_name;
	}
}
