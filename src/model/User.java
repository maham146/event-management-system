package model;
public class User {
	private String name,email,password,role;
	private int id;
	public User(String name,String email,String password,String role){
		this.name=name;
		this.email=email;
		this.password=password;
		this.role=role;
	}
	public User(int id,String name,String email,String password,String role){
		this.id=id;
		this.name=name;
		this.email=email;
		this.password=password;
		this.role=role;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role=role;
	}
}
