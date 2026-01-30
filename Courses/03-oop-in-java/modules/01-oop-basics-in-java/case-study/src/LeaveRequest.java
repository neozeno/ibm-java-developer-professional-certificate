import java.util.UUID;

public class LeaveRequest {
    private UUID id;
    private Employee employee;
    private String startDate, endDate, status, reason; // Status can be of { "Pending", "Approved", "Denied" }

    public LeaveRequest(Employee employee, String startDate, String endDate, String reason) {
        setId(null);
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending";
        this.reason = reason;
    }

    public String getId() {
        return id.toString();
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    private void setId(UUID id) {
        if (id == null) {
            this.id = UUID.randomUUID();
        } else {
            this.id = id;
        }
    }
}
