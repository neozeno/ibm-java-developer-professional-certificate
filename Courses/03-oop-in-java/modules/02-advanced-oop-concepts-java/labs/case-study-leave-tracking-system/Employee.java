import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private LocalDate hireDate;
    private LeaveBalance leaveBalance;

    public Employee(int employeeId, String firstName, String lastName,
                    String email, String department, LocalDate hireDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.hireDate = hireDate;
        this.leaveBalance = new LeaveBalance();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getTenureMonths() {
        return java.time.temporal.ChronoUnit.MONTHS.between(hireDate, LocalDate.now());
    }

    public long getTenureYears() {
        return java.time.temporal.ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }

    // Getters
    public int getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public LocalDate getHireDate() { return hireDate; }
    public LeaveBalance getLeaveBalance() { return leaveBalance; }

    @Override
    public String toString() {
        return String.format("Employee[%d] %s (%s)", employeeId, getFullName(), department);
    }

    // =========================================================================
    // NON-STATIC INNER CLASS: LeaveBalance
    // =========================================================================
    // Rationale: LeaveBalance is tightly coupled to a specific Employee instance.
    // It needs access to the employee's tenure to calculate bonus days and
    // eligibility. As a non-static inner class, it can directly access
    // Employee's private fields like hireDate and getTenureYears().
    // Each Employee has exactly one LeaveBalance - they share the same lifecycle.
    // =========================================================================
    public class LeaveBalance {
        private Map<String, Integer> balances;
        private Map<String, Integer> used;

        // Policy constants
        private static final int BASE_VACATION_DAYS = 15;
        private static final int BASE_SICK_DAYS = 10;
        private static final int BONUS_DAYS_PER_5_YEARS = 5;

        public LeaveBalance() {
            balances = new HashMap<>();
            used = new HashMap<>();
            initializeBalances();
        }

        private void initializeBalances() {
            // Access outer class method directly (benefit of non-static inner class)
            int tenureBonus = (int) (getTenureYears() / 5) * BONUS_DAYS_PER_5_YEARS;

            balances.put("Vacation", BASE_VACATION_DAYS + tenureBonus);
            balances.put("Sick", BASE_SICK_DAYS);
            balances.put("Study", 10);
            balances.put("Bereavement", 10); // Per occurrence, resets
            balances.put("Maternity", 84);   // 12 weeks in days
            balances.put("Paternity", 10);

            // Initialize used to 0
            for (String type : balances.keySet()) {
                used.put(type, 0);
            }
        }

        public int getBalance(String leaveType) {
            return balances.getOrDefault(leaveType, 0) - used.getOrDefault(leaveType, 0);
        }

        public int getTotalBalance(String leaveType) {
            return balances.getOrDefault(leaveType, 0);
        }

        public int getUsed(String leaveType) {
            return used.getOrDefault(leaveType, 0);
        }

        public boolean canDeduct(String leaveType, int days) {
            return getBalance(leaveType) >= days;
        }

        public boolean deduct(String leaveType, int days) {
            if (canDeduct(leaveType, days)) {
                used.put(leaveType, used.get(leaveType) + days);
                return true;
            }
            return false;
        }

        public void refund(String leaveType, int days) {
            int currentUsed = used.getOrDefault(leaveType, 0);
            used.put(leaveType, Math.max(0, currentUsed - days));
        }

        // Called annually to reset balances
        public void resetAnnualBalances() {
            initializeBalances();
        }

        public String getSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Leave Balance for ").append(getFullName()).append(":\n");
            sb.append("  (Tenure bonus: ").append((int)(getTenureYears() / 5) * BONUS_DAYS_PER_5_YEARS);
            sb.append(" days for ").append(getTenureYears()).append(" years)\n");
            for (String type : balances.keySet()) {
                sb.append(String.format("  %-12s: %2d/%2d days remaining%n",
                        type, getBalance(type), getTotalBalance(type)));
            }
            return sb.toString();
        }
    }

    // =========================================================================
    // STATIC NESTED CLASS: ContactInfo
    // =========================================================================
    // Rationale: ContactInfo is a simple data holder that doesn't need access
    // to Employee instance members. It's logically related to Employee (it's
    // contact info FOR an employee) but is self-contained. Static nested class
    // is appropriate because:
    // 1. It doesn't need to access Employee's instance fields
    // 2. It can be instantiated without an Employee instance
    // 3. It's reusable - could be used for emergency contacts too
    // =========================================================================
    public static class ContactInfo {
        private String phone;
        private String alternateEmail;
        private String address;
        private String emergencyContactName;
        private String emergencyContactPhone;

        public ContactInfo(String phone, String address) {
            this.phone = phone;
            this.address = address;
        }

        public void setEmergencyContact(String name, String phone) {
            this.emergencyContactName = name;
            this.emergencyContactPhone = phone;
        }

        public void setAlternateEmail(String email) {
            this.alternateEmail = email;
        }

        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getAlternateEmail() { return alternateEmail; }
        public String getEmergencyContactName() { return emergencyContactName; }
        public String getEmergencyContactPhone() { return emergencyContactPhone; }

        public boolean hasEmergencyContact() {
            return emergencyContactName != null && emergencyContactPhone != null;
        }

        @Override
        public String toString() {
            return String.format("Phone: %s, Address: %s", phone, address);
        }
    }
}
