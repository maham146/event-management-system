package dao;
import model.Event;
import db.DBConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
	public void createEvent(Event en) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("insert into events(event_name,location,capacity,event_date,registration_last_date,category,price,special_name) values(?,?,?,?,?,?,?,?)");
			psmt.setString(1,en.getName());
			psmt.setString(2, en.getLocation());
			psmt.setInt(3, en.getCapacity());
			psmt.setDate(4, en.getEvent_Date());
			psmt.setDate(5, en.getRegistration_Last_Date());
			psmt.setString(6, en.getCategory());
			psmt.setDouble(7, en.getPrice());
			psmt.setString(8, en.getSpecialName());
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public List<Object[]> getAllEvents() {
		List<Object[]> list = new ArrayList<>();
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement("SELECT * FROM events");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("event_id"),
					rs.getString("event_name"),
					rs.getString("location"),
					rs.getDate("event_date"),
					rs.getDate("registration_last_date"),
					rs.getString("category"),
					rs.getInt("capacity"),
					rs.getDouble("price"),
					rs.getString("special_name")
				});
			}
			rs.close(); psmt.close(); con.close();
		} catch (Exception e) { System.out.println(e); }
		return list;
	}

	public List<Object[]> getUpcomingEvents() {
		List<Object[]> list = new ArrayList<>();
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement(
				"SELECT * FROM events WHERE event_date > SYSDATE");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("event_id"),
					rs.getString("event_name"),
					rs.getString("location"),
					rs.getDate("event_date"),
					rs.getDate("registration_last_date"),
					rs.getString("category"),
					rs.getInt("capacity"),
					rs.getDouble("price"),
					rs.getString("special_name")
				});
			}
			rs.close(); psmt.close(); con.close();
		} catch (Exception e) { System.out.println(e); }
		return list;
	}

	public void updateName(int event_id,String event_name) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET event_name=? where event_id=?");
			psmt.setString(1, event_name);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
			}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public void updateLocation(int event_id,String location) {
		try {
			Connection con=DBConnect.getConnection();			
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET location=? where event_id=?");
			psmt.setString(1, location);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
			}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public void updateCapacity(int event_id,int capacity) {
		try {
			Connection con=DBConnect.getConnection();				
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET capacity=? where event_id=?");
			psmt.setInt(1, capacity);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public void updateEventDate(int event_id,Date event_date) {
		try {
			Connection con=DBConnect.getConnection();		
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET event_date=? where event_id=?");
			psmt.setDate(1, event_date);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public void updateRegLastDate(int event_id,Date registration_last_date) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET registration_last_date=? where event_id=?");
			psmt.setDate(1, registration_last_date);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	// FIX: Added missing con.commit() — without it Oracle silently rolls back the update
	public void updateCategory(int event_id,String category) {
		try {
			Connection con=DBConnect.getConnection();			
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET category=? WHERE event_id=?");
			psmt.setString(1, category);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit(); // FIX: was missing, causing silent rollback on connection close
			psmt.close();
			con.close();
			}
		catch(Exception e) {
			System.out.print(e);
		}	
	}

	public void updatePrice(int event_id,double price) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("UPDATE events SET price=? WHERE event_id=?");
			psmt.setDouble(1, price);
			psmt.setInt(2, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public void deleteEvent(int event_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("DELETE from events WHERE event_id=?");
			psmt.setInt(1, event_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close();
			con.close();
		}
		catch(Exception e) {
			System.out.print(e);
		}
	}

	public void viewEvents() {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM events");
			ResultSet rs=psmt.executeQuery();
			while(rs.next()) {
				System.out.println("Event ID: "+rs.getInt(1));
				System.out.println("Event Name: "+rs.getString(2));
				System.out.println("Location: "+rs.getString(3));
				System.out.println("Event Capacity: "+rs.getInt(4));
				System.out.println("Event Date: "+rs.getDate(5));
				System.out.println("Event Registration Last Date: "+rs.getDate(6));
				System.out.println("Event Category: "+rs.getString(7));
				System.out.println("Event Price: "+rs.getDouble(8));
				System.out.print("\n");
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.print(e); }
	}

	public void searchEvent(int event_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM events WHERE event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) {
				System.out.println("Event ID: "+rs.getInt("event_id"));
				System.out.println("Event Name: "+rs.getString("event_name"));
				System.out.println("Location: "+rs.getString("location"));
				System.out.println("Category: "+rs.getString("category"));
				System.out.println("Price: "+rs.getDouble("price"));
			} else {
	            System.out.println("Event not found!");
	        }
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	public boolean isValidEvent(int event_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT COUNT(*) FROM events where event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			rs.next();
			int count=rs.getInt(1);
			rs.close(); psmt.close(); con.close();
			return count > 0;
		}
		catch(Exception e){ System.out.println(e); }
		return false;
	}

	public Date ValidRegistration_Date(int event_id) {
		Date regdate=null;
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT registration_last_date FROM events where event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) {
				regdate=rs.getDate(1);
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
		return regdate;
	}

	public void UpcommingEvents() {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM events WHERE event_date > SYSDATE");
			ResultSet rs=psmt.executeQuery();
			while(rs.next()) {
				System.out.println("Event ID: "+rs.getInt("event_id"));
				System.out.println("Event Name: "+rs.getString("event_name"));
				System.out.println("Event Date: "+rs.getDate("event_date"));
				System.out.print("\n");
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	public void fullCapacity() {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM events");
			ResultSet rs=psmt.executeQuery();
			while(rs.next()) {
				int id=rs.getInt("event_id");
				int capacity=rs.getInt("capacity");
				PreparedStatement psmt2=con.prepareStatement("SELECT COUNT(*) FROM bookings WHERE event_id=?");
				psmt2.setInt(1, id);
				ResultSet rs2=psmt2.executeQuery();
				if(rs2.next() && rs2.getInt(1)==capacity) {
					System.out.println("Full Event ID: "+id+" Name: "+rs.getString("event_name"));
				}
				rs2.close(); psmt2.close();
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	public Date geteventDate(int event_id) {
		Date ev_date=null;
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT event_date FROM events WHERE event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) ev_date=rs.getDate("event_date");
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.print(e); }
		return ev_date;
	}

	public int getCapacity(int event_id) {
		int cap=0;
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT capacity FROM events WHERE event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) cap=rs.getInt(1);
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
		return cap;
	}

	public double geteventPrice(int event_id) {
		double amount=0;
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT price from events WHERE event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) amount=rs.getDouble("price");
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
		return amount;
	}

	public int getTotalEventCount() {
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM events");
			ResultSet rs = psmt.executeQuery();
			int c = rs.next() ? rs.getInt(1) : 0;
			rs.close(); psmt.close(); con.close();
			return c;
		} catch (Exception e) { System.out.println(e); return 0; }
	}
}