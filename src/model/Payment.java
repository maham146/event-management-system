package model;

public abstract class Payment {
	private double amount;
	Payment(double amount){
		this.amount=amount;
	}
	public double getAmount() {
		return this.amount;
	}
	public void setAmount(double amount) {
		this.amount=amount;
	}
	public abstract boolean payment();	
}
