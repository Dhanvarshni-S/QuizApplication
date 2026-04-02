// CONCEPT: Inheritance - Base Class
public class User {

    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }

    public void displayInfo() {
        System.out.println("Username : " + username);
        System.out.println("Role     : " + role);
    }
}