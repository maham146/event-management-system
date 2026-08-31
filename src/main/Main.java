package main;
 
import dao.UserDAO;
import dao.EventDAO;
import dao.BookingDAO;
 
import model.User;
import model.Workshop;
import model.Event;
import model.JazzCash_Payment;
import model.Seminar;
import model.SimpleCash_Payment;
import model.SportsEvent;
import model.Booking;
import model.CardPayment;
import model.Payment;
 
import java.sql.Date;
import java.util.Scanner;
import java.time.LocalDate;
import javax.swing.SwingUtilities;   // ← ADDED IMPORT
 
public class Main {
	public static User current_user;
 
	// ← ONLY THIS METHOD WAS CHANGED — launches the GUI instead of console
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new EventManagementGUI().setVisible(true);
		});
	}
 
	public static void adminrole(Scanner obj) {
		System.out.println("Select which task you want to perform:");
		while(true) {
			System.out.println("1. Create event");
			System.out.println("2. Update event");
			System.out.println("3. Delete event");
			System.out.println("4. View all events");
			System.out.println("5. View all users");
			System.out.println("6. View reports");
			System.out.println("7. Exit event menu");
			System.out.print("Select which task you want to perform:");
			int choice=obj.nextInt();
			obj.nextLine();
			Event en = null;
			EventDAO dao=new EventDAO();
			UserDAO udao=new UserDAO();
			switch(choice) {
			case 1:
				System.out.println("Enter event name:");
				String event_name=obj.nextLine();
				System.out.println("Enter event location:");
				String location=obj.nextLine();
				System.out.println("Enter event capcity:");
				int capacity=obj.nextInt();
				obj.nextLine();
				System.out.println("Enter event price: ");
				double price=obj.nextDouble();
				obj.nextLine();
				Date event_date;
				Date registration_last_date;
				outerLoop:
				while(true) {
					LocalDate today=LocalDate.now();
					System.out.println("Enter event date (YYYY-MM-DD):");
					String strdate=obj.nextLine();
					event_date=Date.valueOf(strdate);
					LocalDate localevent_date=event_date.toLocalDate();
					if(today.isBefore(localevent_date)) {
						while(true) {
						System.out.println("Enter registration last date (YYYY-MM-DD):");
						String strrgs=obj.nextLine();
						registration_last_date=Date.valueOf(strrgs);
						LocalDate localregistration_last_date=registration_last_date.toLocalDate();
						if(!localevent_date.isAfter(localregistration_last_date)) {
							System.out.println("Event date must be AFTER registration last date!");
							continue;
						}
						else {
							while(true) {
							System.out.println("Enter event category:");
							String category=obj.nextLine();
							if(category.equals("Workshop")) {
								System.out.print("Enter trainer name:");
								String trainer = obj.nextLine();
								en=new Workshop(event_name,location,capacity,event_date,registration_last_date,category,price,trainer);
								break;
							}
							else if(category.equals("Seminar")) {
								System.out.print("Enter speaker name:");
								String speaker = obj.nextLine();
								en=new Seminar(event_name,location,capacity,event_date,registration_last_date,category,price,speaker);
								break;
							}
							else if(category.equals("SportsEvent")) {
								System.out.print("Enter team name:");
								String team = obj.nextLine();
								en=new SportsEvent(event_name,location,capacity,event_date,registration_last_date,category,price,team);
								break;
							}
							else {
								System.out.println("Invalid event category!");
								continue;
							}
							}
							en.notifyusers();
							dao.createEvent(en);
							break outerLoop;
						}
						}
					}
					else {
						System.out.println("Event date must be after the currrent date!");
						continue;
					}
				}
				break;
				case 2:
				System.out.print("Enter ID if event you want to update:");
				int event_id=obj.nextInt();
				obj.nextLine();
				System.out.println("1.Event Name");
				System.out.println("2.Event Location");
				System.out.println("3.Event Capacity");
				System.out.println("4.Event Date");
				System.out.println("5.Event Registration Last Date");
				System.out.println("6.Event Category");
				System.out.println("7.Event Price");
				System.out.print("Select fields you want to update");
				String input=obj.nextLine();
				String[] updchoice=input.split(" ");
				for(String c:updchoice) {
					if(c.equals("1")) {
						System.out.print("Enter new name for event:");
						String ev_name=obj.nextLine();
						dao.updateName(event_id,ev_name);
					}
					if(c.equals("2")) {
						System.out.print("Enter new location for event:");
						String loc=obj.nextLine();
						dao.updateLocation(event_id,loc);
					}
					if(c.equals("3")) {
						System.out.print("Enter new capacity for event:");
						int cap=obj.nextInt();
						obj.nextLine();
						dao.updateCapacity(event_id,cap);
					}
					if(c.equals("4")) {
						LocalDate today=LocalDate.now();
						while(true) {
						System.out.print("Enter new date for event (YYYY-MM-DD):");
						String strdt=obj.nextLine();
						Date ev_date=Date.valueOf(strdt);
						LocalDate localevent_date=ev_date.toLocalDate();
						if(today.isBefore(localevent_date)) {
						dao.updateEventDate(event_id,ev_date);
						break;
						}
						else {
							System.out.println("Event Date must be in future!");
							continue;
						}
						}
					}
					if(c.equals("5")) {
						Date evdate=dao.geteventDate(event_id);
						LocalDate localevent_date=evdate.toLocalDate();
						LocalDate today=LocalDate.now();
						while(true) {
						if(today.isBefore(localevent_date)) {
						System.out.print("Enter new last date for event registration (YYYY-MM-DD):");
						String strdt=obj.nextLine();
						Date ev_date=Date.valueOf(strdt);
						LocalDate localregistration_last_date=ev_date.toLocalDate();
						if(localregistration_last_date.isBefore(localevent_date)) {
						dao.updateRegLastDate(event_id,ev_date);
						break;
						}
						else {
							System.out.println("Event Last Date for registration must be before the event date!");
							continue;
						}
						}
						else {
							System.out.println("Event Date must be in future!Cannot set registration date for past events.");
							break;
						}
					}
					}
					if(c.equals("6")) {
						System.out.println("Enter new category for event: ");
						String cat=obj.next();
						dao.updateCategory(event_id,cat);
					}
					if(c.equals("7")) {
						System.out.println("Enter new price for the event: ");
						double ev_price=obj.nextInt();
						dao.updatePrice(event_id,ev_price);
					}
			}
			break;
			case 3:
				System.out.print("Enter ID if event you want to delete:");
				int ev_id=obj.nextInt();
				dao.deleteEvent(ev_id);
				break;
			case 4:
				dao.viewEvents();
				break;
			case 5:
				udao.viewUsers();
				break;
			case 6:
				ViewReports(obj);
				break;
			case 7:
			return;
		}
		}
	}
	public static void userrole(Scanner obj) {
		System.out.println("Select which task you want to perform:");
		EventDAO dao=new EventDAO();
		BookingDAO bdao=new BookingDAO();
		while(true) {
			System.out.println("1. View events");
			System.out.println("2. Search events");
			System.out.println("3. Book Tickets");
			System.out.println("4. Cancel Booking");
			System.out.println("5. View Booking Status");
			System.out.println("6. Update Profile");
			System.out.println("7. Exit from User menu");
			System.out.println("Select task from above to perform:");
			int choice=obj.nextInt();
			obj.nextLine();
			switch(choice) {
			case 1:
				dao.viewEvents();
			break;
			case 2:
				System.out.print("Enter event ID you want to search:");
				int id=obj.nextInt();
				dao.searchEvent(id);
			break;
			case 3:
				dao.viewEvents();
				System.out.println("Enter event ID you want to book for: ");
				int eid=obj.nextInt();
				int uid=current_user.getId();
				if(bdao.viewDuplicate(eid,uid)) {
					System.out.println("You already booked for this event!");
				}
				else if(bdao.getBookings(eid)>=dao.getCapacity(eid)) {
					System.out.println("No more seats left for this event!");
				}
				else {
					BookTickets(obj,eid);
				}
			break;
			case 4:
				CancelBooking(obj);
			break;
			case 5:
				BookingStatus(obj);
			break;
			case 6:
				UpdateProfile(obj);
			break;
			case 7:
				return;
			}	
		}
	}
	public static void ViewReports(Scanner obj) {
		BookingDAO bdao=new BookingDAO();
		UserDAO udao=new UserDAO();
		EventDAO edao=new EventDAO();
		System.out.println("===== REPORTS MENU =====");
		System.out.println("1. Event Booking Report");
		System.out.println("2. User Registration Report");
		System.out.println("3. Booking Report");
		System.out.println("4. Upcoming Events Report");
		System.out.println("5. Full Capacity Event Report");
		System.out.println("6. Back");
		System.out.print("Select which report you want to view:");
		int rep_option=obj.nextInt();
		switch(rep_option) {
		case 1:
			System.out.print("Enter event ID you want to search:");
			int id=obj.nextInt();
			edao.searchEvent(id);
			break;
		case 2:
			udao.viewUsers();
			break;
		case 3:
			bdao.viewallBookings();
			break;
		case 4:
			edao.UpcommingEvents();
			break;
		case 5:
			edao.fullCapacity();
			break;
		case 6:
			return;
		}
	}
	public static void BookTickets(Scanner obj,int ev_choice) {
		EventDAO dao=new EventDAO();
		BookingDAO bdao=new BookingDAO();
		String book_status;
		Payment p=null;
		boolean result=false;
		if(dao.isValidEvent(ev_choice)) {
			int user_id=current_user.getId();
			book_status="Confirmed";
			Date v_regdate=dao.ValidRegistration_Date(ev_choice);
			LocalDate lv_regdate=v_regdate.toLocalDate();
			System.out.println("Enter booking date (YYYY-MM-DD): ");
			String bk_date=obj.next();
			Date book_date=Date.valueOf(bk_date);
			LocalDate lb_date=book_date.toLocalDate();
			if(lb_date.isBefore(lv_regdate) || lb_date.isEqual(lv_regdate)) {
				System.out.println("1. Card Payment");
				System.out.println("2. JazzCash Payment");
				System.out.println("3. SimpleCash Payemnt");
				System.out.println("Enter payment method(1-3): ");
				int payment_method=obj.nextInt();
				String payment_name="";
				String payment_status="";
				obj.nextLine();
				double amount=dao.geteventPrice(ev_choice);
				if(payment_method==1) {
					System.out.println("Enter your card number: ");
					String cardNumber=obj.nextLine();
					System.out.println("Enter you cvv: ");
					String cvv=obj.nextLine();
					System.out.println("Enter expiry date(YYYY-MM-DD):: ");
					String exDate=obj.nextLine();
					Date expiryDate=Date.valueOf(exDate);
					p=new CardPayment(amount,cardNumber,cvv,expiryDate);
					result=p.payment();
					payment_name="Card Payment";
					payment_status="paid";
				}
				else if(payment_method==2) {
					System.out.println("Enter your phone number wihtout spaces: ");
					String mphone=obj.nextLine();
					System.out.println("Enter otp: ");
					int otp=obj.nextInt();
					p=new JazzCash_Payment(amount,mphone,otp);
					result=p.payment();
					payment_name="JazzCash Payment";
					payment_status="paid";
				}
				else if(payment_method==3) {
					p=new SimpleCash_Payment(amount);
					result=p.payment();
					payment_name="SimpleCash Payment";
					payment_status="paid";
				}
				else {
					System.out.println("Wrong Choice!Please enter the correct choice.");
					return;
				}
				boolean success=false;
				Booking b=null;
				if(result) {
					b=new Booking(ev_choice,user_id,lb_date,book_status,payment_name,payment_status);
					success=bdao.insertBooking(b);
					if(success) {
						System.out.println("Booking Confirmed");
						System.out.println("Event ID: "+b.getEvent_ID());
						System.out.println("User ID: "+b.getUser_ID());
						System.out.println("Booking Status: "+b.getBooking_Status());
					}
					else {
						System.out.println("Booking Cancelled");
					}
					}
				else {
					System.out.println("Payment method failed! Booking cannot be done.");
					return;
				}
				}
				else {
					System.out.println("Booking Date cannot be after the registration date!");
				}
		}
		else {
			System.out.println("Invalid event. Event ID for that event doesn't exists!");
		}
	}
	public static void BookingStatus(Scanner obj) {
		BookingDAO bdao=new BookingDAO();
		int user_id=current_user.getId();
		bdao.bookingStatus(user_id);
	}
	public static void CancelBooking(Scanner obj) {
		BookingDAO bdao=new BookingDAO();
		bdao.viewallBookings();
		System.out.println("Enter Booking ID of booking you want to cancel");
		int id=obj.nextInt();
		bdao.cancelBooking(id);
	}
	public static void UpdateProfile(Scanner obj) {
		UserDAO udao=new UserDAO();
		int id=current_user.getId();
		while(true) {
		System.out.println("1. Change Name");
		System.out.println("2. Change Email");
		System.out.println("3. Change Password");
		System.out.println("4. Exit from  menu");
		System.out.println("Enter your choice to update your porfile: ");
		int choice=obj.nextInt();
		obj.nextLine();
		switch(choice) {
		case 1:
			System.out.println("Enter your new name: ");
			String name=obj.nextLine();
			udao.updateName(id,name);
			System.out.println("Name was updated successfully!");
		break;
		case 2:
			String email;
			while(true) {
				System.out.print(" Enter you email:");
				email=obj.nextLine();
				if(email.endsWith("@gmail.com")|| email.endsWith("@yahoomail.com") || email.endsWith("@hotmail.com")) {
					break;
				}
				else {
					System.out.println("Wrong email format! Enter correct email.");
					continue;
				}
			}
			udao.updateEmail(id,email);
			System.out.println("Email was updated successfully!");
		break;
		case 3:
			System.out.println("Enter your new password: ");
			String password=obj.nextLine();
			udao.updatePassword(id,password);
			System.out.println("Password was updated successfully!");
		break;
		case 4:
			return;
		default:
			System.out.println("Invalid choice!Please enter again.");
		}
		}
	}
}