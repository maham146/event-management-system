package model;

public class JazzCash_Payment extends Payment {
	private String mobile_number;
	private int otp; 
	public JazzCash_Payment(double amount,String mobile_number,int otp){
		super(amount);
		this.mobile_number=mobile_number;
		this.otp=otp;
	}
	public String getmobileNumber() {
		return this.mobile_number;
	}
	public void setmobileNumber(String mobile_number) {
		this.mobile_number=mobile_number;
	}
	public int getOtp() {
		return this.otp;
	}
	public void setOtp(int otp) {
		this.otp=otp;
	}
	@Override
	public boolean payment() {
		System.out.println("Processing JazzCash Payment...");
		System.out.println("Amount: "+getAmount());
		System.out.println("OTP verified successfully!");
		return true;
	}

}
