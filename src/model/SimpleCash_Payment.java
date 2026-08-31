package model;

public class SimpleCash_Payment extends Payment {
	public SimpleCash_Payment(double amount){
		super(amount);
	}
	@Override
	public boolean payment() {
		System.out.println("Cash Payment Successful!");
		System.out.println("Amount: " + getAmount());
		return true;
	}

}
