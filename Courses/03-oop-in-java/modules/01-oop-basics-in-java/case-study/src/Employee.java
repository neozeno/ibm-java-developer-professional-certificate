import Utils.*;

import static Utils.Utils.isValidString;

public class Employee {
    private final int id;
    private String name, department, email;
    private int leaveBalance = 20;

    public Employee(int id, String name, String department, String email) {
        this.id = id;
        setName(name);
        setDepartment(department);
        setEmail(email);
    }

    public int getId() {
        return id;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        if (leaveBalance >= 0) {
            this.leaveBalance = leaveBalance;
        } else {
            System.out.println("Leave balance cannot be negative.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isValidString(name)) {
            System.out.println("Name cannot be empty.");
        } else {
            this.name = name;
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (!isValidString(department)) {
            System.out.println("Department name cannot be empty.");
        } else {
            this.department = department;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!isValidString(email)) {
            System.out.println("Email cannot be empty.");
        } else {
            this.email = email;
        }
    }
}
