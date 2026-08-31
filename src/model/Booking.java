package model;

import java.sql.Date;
import java.time.LocalDate;

public class Booking {
	private int event_id,user_id;
	private int booking_id;
	private Date booking_date;
	private LocalDate lbooking_date;
	private String booking_status; 
	private String payment_method;
	private String payment_status;
	public Booking(int event_id,int user_id,LocalDate lbooking_date,String booking_status,String payment_method,String payment_status){
		this.event_id=event_id;
		this.user_id=user_id;
		this.lbooking_date=lbooking_date;
		this.booking_status=booking_status;
		this.payment_method=payment_method;
		this.payment_status=payment_status;
	}
	public Booking(int booking_id,int event_id,int user_id,Date booking_date,String booking_status){
		this.booking_id=booking_id;
		this.event_id=event_id;
		this.user_id=user_id;
		this.booking_date=booking_date;
		this.booking_status=booking_status;
	}
	public int getEvent_ID() {
		return this.event_id;
	}
	public void setEvent_ID(int event_id) {
		this.event_id=event_id;
	}
	public int getUser_ID() {
		return this.user_id;
	}
	public void setUser_ID(int user_id) {
		this.user_id=user_id;
	}
	public int getBooking_ID() {
		return this.booking_id;
	}
	public void setBooking_ID(int booking_id) {
		this.booking_id=booking_id;
	}
	public Date getBooking_Date() {
		return this.booking_date;
	}
	public void setBooking_Date(Date booking_date) {
		this.booking_date=booking_date;
	}
	public String getBooking_Status() {
		return this.booking_status;
	}
	public void setBooking_Status(String booking_status) {
		this.booking_status=booking_status;
	}
	public LocalDate getLocal_Bdate() {
		return this.lbooking_date;
	}
	public void setLocal_Bdate(LocalDate lbooking_date) {
		this.lbooking_date=lbooking_date;
	}
	public String getpaymentMethod() {
		return this.payment_method;
	}
	public void setpaymentMethod(String payment_method) {
		this.payment_method=payment_method;
	}
	public String getpaymentStatus() {
		return this.payment_status;
	}
	public void setpaymentStatus(String payment_status) {
		this.payment_status=payment_status;
	}
}
