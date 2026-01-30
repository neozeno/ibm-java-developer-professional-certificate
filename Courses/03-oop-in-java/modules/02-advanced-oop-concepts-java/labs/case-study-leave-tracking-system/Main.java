import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract class LeaveRequest {
    protected int requestId;
    protected Employee employee;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String status;
    protected String leaveType;
    protected LocalDate requestDate;
    protected StatusHistory statusHistory;

    public LeaveRequest(int requestId, Employee employee, LocalDate startDate, LocalDate endDate) {
        this.requestId = requestId;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending";
        this.requestDate = LocalDate.now();
        this.statusHistory = new StatusHistory();
        this.statusHistory.addChange("System", "Created", "Pending");
    }

    public long calculateLeaveDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public abstract boolean isEligible();

    public abstract boolean requiresDocumentation();

    public void approve(String approverName) {
        String oldStatus = this.status;
        this.status = "Approved";
        statusHistory.addChange(approverName, oldStatus, "Approved");
    }

    public void reject(String approverName, String reason) {
        String oldStatus = this.status;
        this.status = "Rejected: " + reason;
        statusHistory.addChange(approverName, oldStatus, this.status);
    }

    public int getRequestId() { return requestId; }
    public Employee getEmployee() { return employee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public String getLeaveType() { return leaveType; }
    public StatusHistory getStatusHistory() { return statusHistory; }

    /**
     * =========================================================================
     * STATIC NESTED CLASS: `StatusChange`
     * =========================================================================
     * Rationale: `StatusChange` is an immutable data record that captures a single
     * point-in-time event. It doesn't need access to `LeaveRequest`'s instance
     * members - it just stores data about what happened. Static nested class is
     * ideal because:
     * 1. It's a simple, self-contained data holder
     * 2. Can be created independently of any `LeaveRequest` instance
     * 3. Follows the "value object" pattern - immutable after creation
     */
    public static class StatusChange {
        private final LocalDateTime timestamp;
        private final String changedBy;
        private final String fromStatus;
        private final String toStatus;
        private final String notes;

        public StatusChange(String changedBy, String fromStatus, String toStatus, String notes) {
            this.timestamp = LocalDateTime.now();
            this.changedBy = changedBy;
            this.fromStatus = fromStatus;
            this.toStatus = toStatus;
            this.notes = notes;
        }

        public StatusChange(String changedBy, String fromStatus, String toStatus) {
            this(changedBy, fromStatus, toStatus, null);
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public String getChangedBy() { return changedBy; }
        public String getFromStatus() { return fromStatus; }
        public String getToStatus() { return toStatus; }
        public String getNotes() { return notes; }

        @Override
        public String toString() {
            String base = String.format("[%s] %s: %s -> %s",
                    timestamp.toLocalDate(), changedBy, fromStatus, toStatus);
            return notes != null ? base + " (" + notes + ")" : base;
        }
    }

    // =========================================================================
    // NON-STATIC INNER CLASS: StatusHistory
    // =========================================================================
    // Rationale: StatusHistory tracks all status changes for THIS specific
    // LeaveRequest. It needs access to outer class members (requestId, employee)
    // to generate meaningful reports. Non-static inner class is appropriate because:
    // 1. Each StatusHistory belongs to exactly one LeaveRequest
    // 2. It needs access to outer class fields for context in reports
    // 3. Its lifecycle is tied to the LeaveRequest - created together, used together
    // =========================================================================
    public class StatusHistory {
        private List<StatusChange> changes;

        public StatusHistory() {
            this.changes = new ArrayList<>();
        }

        public void addChange(String changedBy, String fromStatus, String toStatus) {
            changes.add(new StatusChange(changedBy, fromStatus, toStatus));
        }

        public void addChange(String changedBy, String fromStatus, String toStatus, String notes) {
            changes.add(new StatusChange(changedBy, fromStatus, toStatus, notes));
        }

        public List<StatusChange> getChanges() {
            return new ArrayList<>(changes);
        }

        public StatusChange getLatestChange() {
            return changes.isEmpty() ? null : changes.get(changes.size() - 1);
        }

        public int getChangeCount() {
            return changes.size();
        }

        // Access outer class fields directly - benefit of non-static inner class
        public String getFullReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("Status History for Request #").append(requestId);
            sb.append(" (").append(employee.getFullName()).append(")\n");
            sb.append("Leave Type: ").append(leaveType).append("\n");
            sb.append("-".repeat(50)).append("\n");
            for (StatusChange change : changes) {
                sb.append("  ").append(change).append("\n");
            }
            return sb.toString();
        }
    }
}


class SickLeaveRequest extends LeaveRequest {
    private MedicalCertificate medicalCertificate;
    private String illnessDescription;
    private boolean isHospitalized;
    private static final int DAYS_REQUIRING_CERTIFICATE = 3;

    public SickLeaveRequest(int requestId, Employee employee,
                            LocalDate startDate, LocalDate endDate,
                            String illnessDescription) {
        super(requestId, employee, startDate, endDate);
        this.leaveType = "Sick Leave";
        this.illnessDescription = illnessDescription;
        this.medicalCertificate = null;
        this.isHospitalized = false;
    }

    public void attachMedicalCertificate(String doctorName, String hospitalName, String diagnosis) {
        this.medicalCertificate = new MedicalCertificate(doctorName, hospitalName, diagnosis);
        statusHistory.addChange("System", status, status, "Medical certificate attached");
    }

    public void markAsHospitalized() {
        this.isHospitalized = true;
    }

    @Override
    public boolean requiresDocumentation() {
        return calculateLeaveDays() > DAYS_REQUIRING_CERTIFICATE || isHospitalized;
    }

    @Override
    public boolean isEligible() {
        if (requiresDocumentation() && medicalCertificate == null) {
            return false;
        }
        return true;
    }

    public boolean hasMedicalCertificate() { return medicalCertificate != null; }
    public MedicalCertificate getMedicalCertificate() { return medicalCertificate; }
    public String getIllnessDescription() { return illnessDescription; }
    public boolean isHospitalized() { return isHospitalized; }

    // =========================================================================
    // STATIC NESTED CLASS: MedicalCertificate
    // =========================================================================
    // Rationale: MedicalCertificate is a self-contained document/record that
    // encapsulates all medical documentation details. It doesn't need access to
    // SickLeaveRequest's instance members. Static nested class is appropriate because:
    // 1. It's a standalone data structure - a certificate exists independently
    // 2. Could potentially be reused (e.g., for disability claims)
    // 3. Has its own validation logic (isValid()) independent of the leave request
    // 4. Follows real-world modeling - a certificate is a separate document
    // =========================================================================
    public static class MedicalCertificate {
        private final String doctorName;
        private final String hospitalName;
        private final String diagnosis;
        private final LocalDate issueDate;
        private final String certificateNumber;
        private static int nextCertificateNumber = 1000;

        public MedicalCertificate(String doctorName, String hospitalName, String diagnosis) {
            this.doctorName = doctorName;
            this.hospitalName = hospitalName;
            this.diagnosis = diagnosis;
            this.issueDate = LocalDate.now();
            this.certificateNumber = "MC-" + (nextCertificateNumber++);
        }

        public boolean isValid() {
            // Certificate is valid if issued within last 30 days
            return ChronoUnit.DAYS.between(issueDate, LocalDate.now()) <= 30;
        }

        public String getDoctorName() { return doctorName; }
        public String getHospitalName() { return hospitalName; }
        public String getDiagnosis() { return diagnosis; }
        public LocalDate getIssueDate() { return issueDate; }
        public String getCertificateNumber() { return certificateNumber; }

        @Override
        public String toString() {
            return String.format("Certificate %s: %s at %s - %s (Issued: %s)",
                    certificateNumber, doctorName, hospitalName, diagnosis, issueDate);
        }
    }
}


class VacationLeaveRequest extends LeaveRequest {
    private TravelDetails travelDetails;
    private static final int MIN_ADVANCE_NOTICE_DAYS = 14;
    private static final int MAX_CONSECUTIVE_DAYS = 21;

    public VacationLeaveRequest(int requestId, Employee employee,
                                LocalDate startDate, LocalDate endDate) {
        super(requestId, employee, startDate, endDate);
        this.leaveType = "Vacation Leave";
    }

    public void setTravelDetails(String destination, String accommodationType,
                                  String emergencyContact, String emergencyPhone) {
        this.travelDetails = new TravelDetails(destination, accommodationType,
                emergencyContact, emergencyPhone);
    }

    public boolean hasAdvanceNotice() {
        long daysUntilStart = ChronoUnit.DAYS.between(requestDate, startDate);
        return daysUntilStart >= MIN_ADVANCE_NOTICE_DAYS;
    }

    public boolean exceedsMaxDuration() {
        return calculateLeaveDays() > MAX_CONSECUTIVE_DAYS;
    }

    @Override
    public boolean requiresDocumentation() {
        return false;
    }

    @Override
    public boolean isEligible() {
        if (exceedsMaxDuration()) {
            return false;
        }
        return true;
    }

    public TravelDetails getTravelDetails() { return travelDetails; }
    public boolean hasTravelDetails() { return travelDetails != null; }

    // =========================================================================
    // STATIC NESTED CLASS: TravelDetails
    // =========================================================================
    // Rationale: TravelDetails encapsulates optional travel information that
    // some employees provide. It's static because:
    // 1. It's a data container that doesn't need VacationLeaveRequest's fields
    // 2. Travel details are conceptually separate - not all vacations have them
    // 3. Could be reused for other purposes (business travel tracking)
    // 4. Has its own behavior (isInternational check) independent of the leave
    // =========================================================================
    public static class TravelDetails {
        private final String destination;
        private final String accommodationType;
        private final String emergencyContactName;
        private final String emergencyContactPhone;
        private boolean isInternational;

        public TravelDetails(String destination, String accommodationType,
                            String emergencyContactName, String emergencyContactPhone) {
            this.destination = destination;
            this.accommodationType = accommodationType;
            this.emergencyContactName = emergencyContactName;
            this.emergencyContactPhone = emergencyContactPhone;
            this.isInternational = false;
        }

        public void markAsInternational() {
            this.isInternational = true;
        }

        public String getDestination() { return destination; }
        public String getAccommodationType() { return accommodationType; }
        public String getEmergencyContactName() { return emergencyContactName; }
        public String getEmergencyContactPhone() { return emergencyContactPhone; }
        public boolean isInternational() { return isInternational; }

        @Override
        public String toString() {
            return String.format("Destination: %s (%s), Emergency: %s (%s)%s",
                    destination, accommodationType, emergencyContactName,
                    emergencyContactPhone, isInternational ? " [International]" : "");
        }
    }
}


class MaternityLeaveRequest extends LeaveRequest {
    private LocalDate expectedDeliveryDate;
    private boolean isPrenatal;
    private boolean extensionRequested;
    private int extensionWeeks;
    private static final int STANDARD_MATERNITY_WEEKS = 12;
    private static final int MAX_EXTENSION_WEEKS = 4;

    public MaternityLeaveRequest(int requestId, Employee employee,
                                 LocalDate startDate, LocalDate expectedDeliveryDate) {
        super(requestId, employee, startDate, calculateEndDate(startDate, STANDARD_MATERNITY_WEEKS));
        this.leaveType = "Maternity Leave";
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.isPrenatal = startDate.isBefore(expectedDeliveryDate);
        this.extensionRequested = false;
        this.extensionWeeks = 0;
    }

    private static LocalDate calculateEndDate(LocalDate start, int weeks) {
        return start.plusWeeks(weeks);
    }

    public boolean requestExtension(int weeks) {
        if (weeks <= MAX_EXTENSION_WEEKS) {
            this.extensionRequested = true;
            this.extensionWeeks = weeks;
            this.endDate = endDate.plusWeeks(weeks);
            statusHistory.addChange("System", status, status,
                    "Extension requested: " + weeks + " weeks");
            return true;
        }
        return false;
    }

    public long getWeeksBeforeDelivery() {
        if (!isPrenatal) return 0;
        return ChronoUnit.WEEKS.between(startDate, expectedDeliveryDate);
    }

    @Override
    public boolean requiresDocumentation() {
        return true;
    }

    @Override
    public boolean isEligible() {
        return true;
    }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public boolean isPrenatal() { return isPrenatal; }
    public boolean isExtensionRequested() { return extensionRequested; }
    public int getExtensionWeeks() { return extensionWeeks; }
    public int getStandardMaternityWeeks() { return STANDARD_MATERNITY_WEEKS; }
}


class PaternityLeaveRequest extends LeaveRequest {
    private LocalDate childBirthDate;
    private boolean isAdoption;
    private static final int STANDARD_PATERNITY_DAYS = 10;
    private static final int ADOPTION_PATERNITY_DAYS = 14;

    public PaternityLeaveRequest(int requestId, Employee employee,
                                 LocalDate startDate, LocalDate childBirthDate,
                                 boolean isAdoption) {
        super(requestId, employee, startDate,
              startDate.plusDays(isAdoption ? ADOPTION_PATERNITY_DAYS : STANDARD_PATERNITY_DAYS));
        this.leaveType = "Paternity Leave";
        this.childBirthDate = childBirthDate;
        this.isAdoption = isAdoption;
    }

    public boolean isWithinValidPeriod() {
        long weeksAfterBirth = ChronoUnit.WEEKS.between(childBirthDate, startDate);
        return weeksAfterBirth >= 0 && weeksAfterBirth <= 8;
    }

    @Override
    public boolean requiresDocumentation() {
        return true;
    }

    @Override
    public boolean isEligible() {
        return isWithinValidPeriod();
    }

    public LocalDate getChildBirthDate() { return childBirthDate; }
    public boolean isAdoption() { return isAdoption; }
}


class BereavementLeaveRequest extends LeaveRequest {
    private String relationshipToDeceased;
    private String deceasedName;
    private boolean requiresTravel;
    private static final int IMMEDIATE_FAMILY_DAYS = 5;
    private static final int EXTENDED_FAMILY_DAYS = 3;

    public BereavementLeaveRequest(int requestId, Employee employee,
                                   LocalDate startDate, String relationshipToDeceased,
                                   String deceasedName) {
        super(requestId, employee, startDate,
              calculateEndDate(startDate, relationshipToDeceased));
        this.leaveType = "Bereavement Leave";
        this.relationshipToDeceased = relationshipToDeceased;
        this.deceasedName = deceasedName;
        this.requiresTravel = false;
    }

    private static LocalDate calculateEndDate(LocalDate start, String relationship) {
        int days = isImmediateFamily(relationship) ? IMMEDIATE_FAMILY_DAYS : EXTENDED_FAMILY_DAYS;
        return start.plusDays(days);
    }

    private static boolean isImmediateFamily(String relationship) {
        String rel = relationship.toLowerCase();
        return rel.contains("spouse") || rel.contains("parent") ||
               rel.contains("child") || rel.contains("sibling");
    }

    public void markRequiresTravel() {
        this.requiresTravel = true;
        this.endDate = this.endDate.plusDays(2);
    }

    @Override
    public boolean requiresDocumentation() {
        return true;
    }

    @Override
    public boolean isEligible() {
        return true;
    }

    public String getRelationshipToDeceased() { return relationshipToDeceased; }
    public String getDeceasedName() { return deceasedName; }
    public boolean requiresTravel() { return requiresTravel; }
}


class UnpaidLeaveRequest extends LeaveRequest {
    private String reason;
    private ApprovalWorkflow approvalWorkflow;
    private static final int MAX_UNPAID_LEAVE_DAYS = 30;

    public UnpaidLeaveRequest(int requestId, Employee employee,
                              LocalDate startDate, LocalDate endDate,
                              String reason) {
        super(requestId, employee, startDate, endDate);
        this.leaveType = "Unpaid Leave";
        this.reason = reason;
        this.approvalWorkflow = new ApprovalWorkflow();
    }

    public void addApproval(String approverName, String role, boolean approved, String comments) {
        approvalWorkflow.addApproval(approverName, role, approved, comments);
        String action = approved ? "approved" : "rejected";
        statusHistory.addChange(approverName, status, status,
                role + " " + action + ": " + (comments != null ? comments : "No comments"));
    }

    public boolean hasAllApprovals() {
        return approvalWorkflow.isFullyApproved();
    }

    public boolean exceedsMaxDuration() {
        return calculateLeaveDays() > MAX_UNPAID_LEAVE_DAYS;
    }

    @Override
    public boolean requiresDocumentation() {
        return calculateLeaveDays() > 5;
    }

    @Override
    public boolean isEligible() {
        if (exceedsMaxDuration()) {
            return false;
        }
        return hasAllApprovals();
    }

    @Override
    public void approve(String approverName) {
        if (hasAllApprovals()) {
            super.approve(approverName);
        }
    }

    public String getReason() { return reason; }
    public ApprovalWorkflow getApprovalWorkflow() { return approvalWorkflow; }

    // =========================================================================
    // STATIC NESTED CLASS: ApprovalStep
    // =========================================================================
    // Rationale: ApprovalStep is an immutable record of a single approval action.
    // Similar to StatusChange, it's a value object that doesn't need access to
    // outer class members. Static because:
    // 1. Self-contained data record
    // 2. Immutable after creation
    // 3. Could be used in other approval contexts (not just unpaid leave)
    // =========================================================================
    public static class ApprovalStep {
        private final String approverName;
        private final String role;
        private final boolean approved;
        private final String comments;
        private final LocalDateTime timestamp;

        public ApprovalStep(String approverName, String role, boolean approved, String comments) {
            this.approverName = approverName;
            this.role = role;
            this.approved = approved;
            this.comments = comments;
            this.timestamp = LocalDateTime.now();
        }

        public String getApproverName() { return approverName; }
        public String getRole() { return role; }
        public boolean isApproved() { return approved; }
        public String getComments() { return comments; }
        public LocalDateTime getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("[%s] %s (%s): %s%s",
                    timestamp.toLocalDate(), approverName, role,
                    approved ? "APPROVED" : "REJECTED",
                    comments != null ? " - " + comments : "");
        }
    }

    // =========================================================================
    // NON-STATIC INNER CLASS: ApprovalWorkflow
    // =========================================================================
    // Rationale: ApprovalWorkflow manages the multi-step approval process for
    // THIS specific UnpaidLeaveRequest. It needs access to outer class members
    // to update status history and check leave duration. Non-static because:
    // 1. Each workflow belongs to exactly one UnpaidLeaveRequest
    // 2. Needs to interact with outer class (update statusHistory)
    // 3. Business logic depends on outer class state (leave duration affects
    //    whether HR approval is required)
    // 4. Lifecycle is tied to the leave request
    // =========================================================================
    public class ApprovalWorkflow {
        private List<ApprovalStep> steps;
        private boolean managerApproved;
        private boolean hrApproved;

        public ApprovalWorkflow() {
            this.steps = new ArrayList<>();
            this.managerApproved = false;
            this.hrApproved = false;
        }

        public void addApproval(String approverName, String role, boolean approved, String comments) {
            steps.add(new ApprovalStep(approverName, role, approved, comments));

            if (approved) {
                if (role.equalsIgnoreCase("Manager")) {
                    managerApproved = true;
                } else if (role.equalsIgnoreCase("HR")) {
                    hrApproved = true;
                }
            }
        }

        public boolean isFullyApproved() {
            // Access outer class method - benefit of non-static inner class
            boolean needsHR = calculateLeaveDays() > 5;
            if (needsHR) {
                return managerApproved && hrApproved;
            }
            return managerApproved;
        }

        public boolean isManagerApproved() { return managerApproved; }
        public boolean isHrApproved() { return hrApproved; }
        public List<ApprovalStep> getSteps() { return new ArrayList<>(steps); }

        public String getWorkflowSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Approval Workflow for Request #").append(requestId).append("\n");
            sb.append("Reason: ").append(reason).append("\n");
            sb.append("Duration: ").append(calculateLeaveDays()).append(" days\n");
            sb.append("Required approvals: Manager");
            if (calculateLeaveDays() > 5) {
                sb.append(" + HR");
            }
            sb.append("\n");
            sb.append("-".repeat(40)).append("\n");
            for (ApprovalStep step : steps) {
                sb.append("  ").append(step).append("\n");
            }
            sb.append("Status: ").append(isFullyApproved() ? "FULLY APPROVED" : "PENDING").append("\n");
            return sb.toString();
        }
    }
}


class StudyLeaveRequest extends LeaveRequest {
    private String courseName;
    private String institutionName;
    private boolean isCompanySponsored;
    private boolean isJobRelated;
    private static final int MAX_STUDY_LEAVE_DAYS = 10;

    public StudyLeaveRequest(int requestId, Employee employee,
                             LocalDate startDate, LocalDate endDate,
                             String courseName, String institutionName) {
        super(requestId, employee, startDate, endDate);
        this.leaveType = "Study Leave";
        this.courseName = courseName;
        this.institutionName = institutionName;
        this.isCompanySponsored = false;
        this.isJobRelated = false;
    }

    public void markAsCompanySponsored() {
        this.isCompanySponsored = true;
    }

    public void markAsJobRelated() {
        this.isJobRelated = true;
    }

    public boolean isPaid() {
        return isCompanySponsored || isJobRelated;
    }

    @Override
    public boolean requiresDocumentation() {
        return true;
    }

    @Override
    public boolean isEligible() {
        if (calculateLeaveDays() > MAX_STUDY_LEAVE_DAYS && !isCompanySponsored) {
            return false;
        }
        return true;
    }

    public String getCourseName() { return courseName; }
    public String getInstitutionName() { return institutionName; }
    public boolean isCompanySponsored() { return isCompanySponsored; }
    public boolean isJobRelated() { return isJobRelated; }
}


// =============================================================================
// MAIN CLASS - Demonstrates all inner class types
// =============================================================================
class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║        LEAVE TRACKING SYSTEM - INNER CLASSES DEMO            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

        // Create employees
        Employee alice = new Employee(1, "Alice", "Johnson", "alice@company.com",
                "Engineering", LocalDate.of(2020, 3, 15));
        Employee bob = new Employee(2, "Bob", "Smith", "bob@company.com",
                "Marketing", LocalDate.of(2018, 8, 1));  // 6+ years tenure
        Employee carol = new Employee(3, "Carol", "Williams", "carol@company.com",
                "HR", LocalDate.of(2023, 1, 10));

        // =====================================================================
        // DEMO 1: Employee.LeaveBalance (Non-static inner class)
        // =====================================================================
        System.out.println("═══ DEMO 1: Employee.LeaveBalance (Non-static Inner Class) ═══");
        System.out.println("Rationale: Needs access to employee's tenure for bonus calculation\n");

        // LeaveBalance is created automatically with each Employee
        // It accesses Employee.getTenureYears() to calculate tenure bonus
        System.out.println(alice.getLeaveBalance().getSummary());
        System.out.println(bob.getLeaveBalance().getSummary());

        // Demonstrate deduction
        bob.getLeaveBalance().deduct("Vacation", 5);
        System.out.println("After Bob takes 5 vacation days:");
        System.out.println("  Vacation balance: " + bob.getLeaveBalance().getBalance("Vacation") +
                "/" + bob.getLeaveBalance().getTotalBalance("Vacation") + " days\n");

        // =====================================================================
        // DEMO 2: Employee.ContactInfo (Static nested class)
        // =====================================================================
        System.out.println("═══ DEMO 2: Employee.ContactInfo (Static Nested Class) ═══");
        System.out.println("Rationale: Self-contained data holder, can be created independently\n");

        // Can create ContactInfo without an Employee instance
        Employee.ContactInfo aliceContact = new Employee.ContactInfo(
                "+1-555-0101", "123 Tech Street, San Francisco");
        aliceContact.setEmergencyContact("John Johnson", "+1-555-0102");
        System.out.println("Alice's contact: " + aliceContact);
        System.out.println("Has emergency contact: " + aliceContact.hasEmergencyContact() + "\n");

        // =====================================================================
        // DEMO 3: LeaveRequest.StatusChange & StatusHistory
        // =====================================================================
        System.out.println("═══ DEMO 3: StatusChange (Static) & StatusHistory (Non-static) ═══");
        System.out.println("Rationale: StatusChange is immutable data; StatusHistory needs request context\n");

        SickLeaveRequest sick1 = new SickLeaveRequest(101, alice,
                LocalDate.now(), LocalDate.now().plusDays(5), "Severe flu");

        // Simulate workflow - each action is tracked
        sick1.attachMedicalCertificate("Dr. Smith", "City Hospital", "Influenza Type A");
        sick1.approve("Manager Jane");

        // StatusHistory accesses outer class fields (requestId, employee, leaveType)
        System.out.println(sick1.getStatusHistory().getFullReport());

        // =====================================================================
        // DEMO 4: SickLeaveRequest.MedicalCertificate (Static nested class)
        // =====================================================================
        System.out.println("═══ DEMO 4: MedicalCertificate (Static Nested Class) ═══");
        System.out.println("Rationale: Independent document with own validation logic\n");

        // MedicalCertificate is a self-contained record
        SickLeaveRequest.MedicalCertificate cert = sick1.getMedicalCertificate();
        System.out.println("Certificate: " + cert);
        System.out.println("Is valid (within 30 days): " + cert.isValid() + "\n");

        // Can also create certificate independently (static)
        SickLeaveRequest.MedicalCertificate standaloneCert =
                new SickLeaveRequest.MedicalCertificate("Dr. Brown", "General Hospital", "Back pain");
        System.out.println("Standalone certificate: " + standaloneCert + "\n");

        // =====================================================================
        // DEMO 5: VacationLeaveRequest.TravelDetails (Static nested class)
        // =====================================================================
        System.out.println("═══ DEMO 5: TravelDetails (Static Nested Class) ═══");
        System.out.println("Rationale: Optional travel info, independent of leave request state\n");

        VacationLeaveRequest vacation = new VacationLeaveRequest(201, bob,
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(40));
        vacation.setTravelDetails("Tokyo, Japan", "Hotel",
                "Sarah Smith", "+1-555-0200");
        vacation.getTravelDetails().markAsInternational();

        System.out.println("Travel details: " + vacation.getTravelDetails() + "\n");

        // =====================================================================
        // DEMO 6: UnpaidLeaveRequest.ApprovalStep & ApprovalWorkflow
        // =====================================================================
        System.out.println("═══ DEMO 6: ApprovalStep (Static) & ApprovalWorkflow (Non-static) ═══");
        System.out.println("Rationale: ApprovalStep is immutable record; Workflow needs request context\n");

        UnpaidLeaveRequest unpaid = new UnpaidLeaveRequest(601, carol,
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(20),
                "Extended family vacation abroad");

        // Add approvals through the workflow
        unpaid.addApproval("Manager Mike", "Manager", true, "Approved, good timing");
        unpaid.addApproval("HR Helen", "HR", true, "All policies met");

        // ApprovalWorkflow accesses outer class (calculateLeaveDays, reason, requestId)
        System.out.println(unpaid.getApprovalWorkflow().getWorkflowSummary());

        // Now process the request
        if (unpaid.isEligible()) {
            unpaid.approve("System");
            System.out.println("Final status: " + unpaid.getStatus() + "\n");
        }

        // =====================================================================
        // DEMO 7: Anonymous Inner Class for Sorting
        // =====================================================================
        System.out.println("═══ DEMO 7: Anonymous Inner Class (Comparator) ═══");
        System.out.println("Rationale: One-time use comparator, defined inline where needed\n");

        List<LeaveRequest> allRequests = new ArrayList<>();
        allRequests.add(sick1);
        allRequests.add(vacation);
        allRequests.add(unpaid);
        allRequests.add(new BereavementLeaveRequest(501, alice, LocalDate.now(), "Parent", "John"));

        // Anonymous inner class implementing Comparator
        allRequests.sort(new Comparator<LeaveRequest>() {
            @Override
            public int compare(LeaveRequest r1, LeaveRequest r2) {
                // Sort by leave days descending
                return Long.compare(r2.calculateLeaveDays(), r1.calculateLeaveDays());
            }
        });

        System.out.println("Requests sorted by duration (descending):");
        for (LeaveRequest req : allRequests) {
            System.out.printf("  #%d %-20s %s: %d days%n",
                    req.getRequestId(), req.getLeaveType(),
                    req.getEmployee().getFullName(), req.calculateLeaveDays());
        }

        // =====================================================================
        // DEMO 8: Local Inner Class
        // =====================================================================
        System.out.println("\n═══ DEMO 8: Local Inner Class ═══");
        System.out.println("Rationale: Helper class needed only within a specific method\n");

        generateLeaveReport(allRequests);

        // =====================================================================
        // SUMMARY
        // =====================================================================
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    INNER CLASSES SUMMARY                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Type                 │ Example              │ Key Benefit     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Static Nested        │ MedicalCertificate   │ Independent     ║");
        System.out.println("║                      │ ContactInfo          │ data holder     ║");
        System.out.println("║                      │ StatusChange         │                 ║");
        System.out.println("║                      │ TravelDetails        │                 ║");
        System.out.println("║                      │ ApprovalStep         │                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Non-static Inner     │ LeaveBalance         │ Access to outer ║");
        System.out.println("║                      │ StatusHistory        │ instance fields ║");
        System.out.println("║                      │ ApprovalWorkflow     │                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Anonymous            │ Comparator           │ One-time use    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Local                │ ReportEntry          │ Method-scoped   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    // Method demonstrating LOCAL INNER CLASS
    private static void generateLeaveReport(List<LeaveRequest> requests) {
        // =====================================================================
        // LOCAL INNER CLASS: ReportEntry
        // =====================================================================
        // Rationale: This helper class is only needed within this method to
        // format report data. It doesn't make sense to expose it outside since
        // it's a temporary data structure for report generation. Local inner
        // classes are perfect for such method-specific helpers.
        // =====================================================================
        class ReportEntry {
            String employeeName;
            String leaveType;
            long days;
            String status;

            ReportEntry(LeaveRequest req) {
                this.employeeName = req.getEmployee().getFullName();
                this.leaveType = req.getLeaveType();
                this.days = req.calculateLeaveDays();
                this.status = req.getStatus();
            }

            String format() {
                return String.format("│ %-15s │ %-18s │ %3d │ %-10s │",
                        employeeName, leaveType, days,
                        status.length() > 10 ? status.substring(0, 10) : status);
            }
        }

        System.out.println("┌─────────────────┬────────────────────┬─────┬────────────┐");
        System.out.println("│ Employee        │ Leave Type         │ Days│ Status     │");
        System.out.println("├─────────────────┼────────────────────┼─────┼────────────┤");

        for (LeaveRequest req : requests) {
            ReportEntry entry = new ReportEntry(req);
            System.out.println(entry.format());
        }

        System.out.println("└─────────────────┴────────────────────┴─────┴────────────┘");
    }
}
