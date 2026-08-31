package model;

import java.util.Date;

public class CardPayment extends Payment {
	private String cardNumber;
	private String cvv;
	private Date expiryDate;
	public CardPayment(double amount,String cardNumber,String cvv,Date expiryDate){
		super(amount);
		this.cardNumber=cardNumber;
		this.cvv=cvv;
		this.expiryDate=expiryDate;
		
	}
	public String getcardNumber() {
		return this.cardNumber;
	}
	public void setcardNumber(String cardNumber) {
		this.cardNumber=cardNumber;
	}
	public String getcvv() {
		return this.cvv;
	}
	public void setcvv(String cvv) {
		this.cvv=cvv;
	}
	public Date getexpiryDate() {
		return this.expiryDate;
	}
	public void setexpiryDate(Date expiryDate) {
		this.expiryDate=expiryDate;
	}
	@Override
	public boolean payment() {
		System.out.println("Processing Card Payment...");
		System.out.println("Amount: "+getAmount());
		System.out.println("Card verified successfully!");
		return true;
	}

}
