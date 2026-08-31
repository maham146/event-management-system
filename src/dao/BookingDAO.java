package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnect;
import model.Booking;

public class BookingDAO {
	public boolean insertBooking(Booking b) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("INSERT into bookings(event_id,user_id,booking_date,booking_status,payment_method,payment_status) values(?,?,?,?,?,?)");
			psmt.setInt(1,b.getEvent_ID());
			psmt.setInt(2, b.getUser_ID());
			psmt.setDate(3, java.sql.Date.valueOf(b.getLocal_Bdate()));
			psmt.setString(4, b.getBooking_Status());
			psmt.setString(5,b.getpaymentMethod());
			psmt.setString(6, b.getpaymentStatus());
			int count=psmt.executeUpdate();
			con.commit();
			psmt.close(); con.close();
			return count > 0;
		}
		catch(Exception e) { System.out.println(e); return false; }
	}

	public void bookingStatus(int user_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM bookings where user_id=?");
			psmt.setInt(1, user_id);
			ResultSet rs=psmt.executeQuery();
			while(rs.next()) {
				System.out.println("Booking ID: "+rs.getInt("booking_id"));
				System.out.println("Event ID: "+rs.getInt("event_id"));
				System.out.println("Booking Date: "+rs.getDate("booking_date"));
				System.out.println("Booking Status: "+rs.getString("booking_status"));
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	// ── NEW: returns bookings for a user as list of Object[] rows for the GUI table ──
	public List<Object[]> getBookingsForUser(int user_id) {
		List<Object[]> list = new ArrayList<>();
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement("SELECT * FROM bookings WHERE user_id=?");
			psmt.setInt(1, user_id);
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("booking_id"),
					rs.getInt("event_id"),
					rs.getDate("booking_date"),
					rs.getString("booking_status"),
					rs.getString("payment_method"),
					rs.getString("payment_status")
				});
			}
			rs.close(); psmt.close(); con.close();
		} catch (Exception e) { System.out.println(e); }
		return list;
	}

	// ── NEW: returns all bookings as list of Object[] rows for admin GUI table ──
	public List<Object[]> getAllBookings() {
		List<Object[]> list = new ArrayList<>();
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement("SELECT * FROM bookings");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{
					rs.getInt("booking_id"),
					rs.getInt("event_id"),
					rs.getInt("user_id"),
					rs.getDate("booking_date"),
					rs.getString("booking_status"),
					rs.getString("payment_method"),
					rs.getString("payment_status")
				});
			}
			rs.close(); psmt.close(); con.close();
		} catch (Exception e) { System.out.println(e); }
		return list;
	}

	public void viewallBookings() {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT * FROM bookings");
			ResultSet rs=psmt.executeQuery();
			while(rs.next()) {
				System.out.println("Booking ID: "+rs.getInt("booking_id"));
				System.out.println("Event ID: "+rs.getInt("event_id"));
				System.out.println("Booking Status: "+rs.getString("booking_status"));
			}
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	public void cancelBooking(int booking_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("UPDATE bookings SET booking_status=? WHERE booking_id=?");
			psmt.setString(1,"Cancelled");
			psmt.setInt(2, booking_id);
			psmt.executeUpdate();
			con.commit();
			psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
	}

	public boolean viewDuplicate(int event_id,int user_id) {
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT COUNT(*) from bookings where user_id=? AND event_id=?");
			psmt.setInt(1, user_id);
			psmt.setInt(2, event_id);
			ResultSet rs=psmt.executeQuery();
			rs.next();
			int count=rs.getInt(1);
			rs.close(); psmt.close(); con.close();
			return count > 0;
		}
		catch(Exception e) { System.out.println(e); }
		return false;
	}

	public int getBookings(int event_id) {
		int count=0;
		try {
			Connection con=DBConnect.getConnection();
			PreparedStatement psmt=con.prepareStatement("SELECT COUNT(*) FROM bookings WHERE event_id=?");
			psmt.setInt(1, event_id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) count=rs.getInt(1);
			rs.close(); psmt.close(); con.close();
		}
		catch(Exception e) { System.out.println(e); }
		return count;
	}

	// ── NEW: total booking count for dashboard stat ──
	public int getTotalBookingCount() {
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement psmt = con.prepareStatement("SELECT COUNT(*) FROM bookings");
			ResultSet rs = psmt.executeQuery();
			int c = rs.next() ? rs.getInt(1) : 0;
			rs.close(); psmt.close(); con.close();
			return c;
		} catch (Exception e) { System.out.println(e); return 0; }
	}
}