# Case Study: Leave Tracking System

## Advanced Object-Oriented Programming Concepts in Java

A comprehensive Java application demonstrating advanced OOP concepts
through a real-world employee leave management system.

---

## Table of Contents

- [Overview](#overview)
- [Class Diagram](#class-diagram)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
  - [1. Inheritance](#1-inheritance)
  - [2. Abstract Classes](#2-abstract-classes)
  - [3. Polymorphism](#3-polymorphism)
  - [4. Inner Classes](#4-inner-classes)
  - [5. Interfaces (Potential Extensions)](#5-interfaces-potential-extensions)
- [Leave Types and Business Rules](#leave-types-and-business-rules)
- [Running the Application](#running-the-application)

---

## Overview

The Leave Tracking System models an enterprise HR application that manages
various types of employee leave requests. Each leave type has specific rules,
documentation requirements, and approval workflows.

### Key Features

- **Multiple Leave Types**: Sick, Vacation, Maternity, Paternity, Bereavement,
Unpaid, and Study leave
- **Leave Balance Management**: Tracks employee balances
with tenure-based bonuses
- **Status Tracking**: Complete audit trail of all status changes
- **Approval Workflows**: Multi-level approval for certain leave types
- **Eligibility Validation**: Automatic validation
based on leave-specific rules

---

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                               EMPLOYEE                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│ - employeeId: int                                                           │
│ - firstName, lastName, email: String                                        │
│ - department: String                                                        │
│ - hireDate: LocalDate                                                       │
│ - leaveBalance: LeaveBalance  ◄──────── NON-STATIC INNER CLASS              │
├─────────────────────────────────────────────────────────────────────────────┤
│ + getFullName(): String                                                     │
│ + getTenureYears(): long                                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────┐  ┌────────────────────────────────┐ │
│ │ «inner» LeaveBalance                │  │ «static» ContactInfo           │ │
│ ├─────────────────────────────────────┤  ├────────────────────────────────┤ │
│ │ - balances: Map<String, Integer>    │  │ - phone, address: String       │ │
│ │ - used: Map<String, Integer>        │  │ - emergencyContactName: String │ │
│ │ + getBalance(type): int             │  │ + hasEmergencyContact(): bool  │ │
│ │ + deduct(type, days): boolean       │  └────────────────────────────────┘ │
│ │ + getSummary(): String ─────────────┼──► Accesses getTenureYears()        │
│ └─────────────────────────────────────┘                                     │
└─────────────────────────────────────────────────────────────────────────────┘

                                    │
                                    │ has-a
                                    ▼

┌─────────────────────────────────────────────────────────────────────────────┐
│                        «abstract» LEAVE REQUEST                             │
├─────────────────────────────────────────────────────────────────────────────┤
│ # requestId: int                                                            │
│ # employee: Employee                                                        │
│ # startDate, endDate: LocalDate                                             │
│ # status, leaveType: String                                                 │
│ # statusHistory: StatusHistory  ◄──────── NON-STATIC INNER CLASS            │
├─────────────────────────────────────────────────────────────────────────────┤
│ + calculateLeaveDays(): long                                                │
│ + «abstract» isEligible(): boolean                                          │
│ + «abstract» requiresDocumentation(): boolean                               │
│ + approve(approverName): void                                               │
│ + reject(approverName, reason): void                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────┐  ┌─────────────────────────────────┐   │
│ │ «static» StatusChange            │  │ «inner» StatusHistory           │   │
│ ├──────────────────────────────────┤  ├─────────────────────────────────┤   │
│ │ - timestamp: LocalDateTime       │  │ - changes: List<StatusChange>   │   │
│ │ - changedBy: String              │  │ + addChange(...)                │   │
│ │ - fromStatus, toStatus: String   │  │ + getFullReport(): String ──────┼─► │
│ │ - notes: String                  │  │   Accesses requestId, employee  │   │
│ └──────────────────────────────────┘  └─────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
        ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
        │SickLeaveReq.  │  │VacationLeave  │  │UnpaidLeave    │  ... (more)
        ├───────────────┤  │   Request     │  │   Request     │
        │-certificate   │  ├───────────────┤  ├───────────────┤
        │-illness       │  │-travelDetails │  │-approvalWork- │
        │-isHospitalized│  │               │  │ flow          │
        ├───────────────┤  ├───────────────┤  ├───────────────┤
        │«static»       │  │«static»       │  │«static»       │
        │Medical-       │  │TravelDetails  │  │ApprovalStep   │
        │Certificate    │  │               │  │               │
        └───────────────┘  └───────────────┘  │«inner»        │
                                              │ApprovalWork-  │
                                              │flow           │
                                              └───────────────┘
```

---

## OOP Concepts Demonstrated

### 1. Inheritance

**Concept**: Inheritance allows a class to inherit properties and methods
from a parent class, promoting code reuse and establishing an "is-a"
relationship.

#### Implementation in This Project

All leave request types extend the abstract `LeaveRequest` base class:

```java
abstract class LeaveRequest {
    protected int requestId;
    protected Employee employee;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String status;
    // Common methods...
}

class SickLeaveRequest extends LeaveRequest {
    // Inherits all fields and methods from LeaveRequest
    // Adds sick-leave specific attributes
    private MedicalCertificate medicalCertificate;
    private String illnessDescription;
    private boolean isHospitalized;
}
```

#### Inheritance Hierarchy

```
LeaveRequest (abstract)
    ├── SickLeaveRequest
    ├── VacationLeaveRequest
    ├── MaternityLeaveRequest
    ├── PaternityLeaveRequest
    ├── BereavementLeaveRequest
    ├── UnpaidLeaveRequest
    └── StudyLeaveRequest
```

#### Benefits Demonstrated

| Benefit                | Example in Code                                                        |
|------------------------|------------------------------------------------------------------------|
| **Code Reuse**         | All subclasses inherit `calculateLeaveDays()`, `approve()`, `reject()` |
| **Common Interface**   | All leaves have `startDate`, `endDate`, `employee`, `status`           |
| **Protected Access**   | Subclasses access `statusHistory` to add change notes                  |
| **Method Inheritance** | `getStatusHistory().getFullReport()` works for any leave type          |

---

### 2. Abstract Classes

**Concept**: Abstract classes define a template that cannot be instantiated
directly. They may contain abstract methods (no implementation) that subclasses
must implement, as well as concrete methods with implementations.

#### Implementation in This Project

```java
abstract class LeaveRequest {
    // Concrete method - same implementation for all subclasses
    public long calculateLeaveDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    // Abstract methods - each subclass MUST provide implementation
    public abstract boolean isEligible();
    public abstract boolean requiresDocumentation();

    // Concrete method with default behavior (can be overridden)
    public void approve(String approverName) {
        String oldStatus = this.status;
        this.status = "Approved";
        statusHistory.addChange(approverName, oldStatus, "Approved");
    }
}
```

#### How Subclasses Implement Abstract Methods

| Leave Type                  | `isEligible()` Logic            | `requiresDocumentation()` Logic |
|-----------------------------|---------------------------------|---------------------------------|
| **SickLeaveRequest**        | Requires certificate if >3 days | True if >3 days or hospitalized |
| **VacationLeaveRequest**    | Must not exceed 21 days         | Always false                    |
| **MaternityLeaveRequest**   | Always eligible                 | Always true                     |
| **PaternityLeaveRequest**   | Within 8 weeks of birth         | Always true                     |
| **BereavementLeaveRequest** | Always eligible                 | Always true                     |
| **UnpaidLeaveRequest**      | Needs all approvals, ≤30 days   | True if >5 days                 |
| **StudyLeaveRequest**       | ≤10 days unless sponsored       | Always true                     |

#### Example: Different Implementations

```java
// SickLeaveRequest - complex eligibility logic
@Override
public boolean isEligible() {
    return !requiresDocumentation() || medicalCertificate != null;  // Long sick leave needs certificate
}

// UnpaidLeaveRequest - multi-condition eligibility
@Override
public boolean isEligible() {
    if (exceedsMaxDuration()) {
        return false;  // Cannot exceed 30 days
    }
    return hasAllApprovals();  // Needs manager + HR approval
}

// BereavementLeaveRequest - always eligible
@Override
public boolean isEligible() {
    return true;  // Bereavement is always granted
}
```

---

### 3. Polymorphism

**Concept**: Polymorphism allows objects of different classes to be treated
through a common interface. The same method call behaves differently based on
the actual object type.

#### Runtime Polymorphism (Method Overriding)

```java
// Process any leave request polymorphically
private static void processLeaveRequest(LeaveRequest request) {
    // Same method call, different behavior based on actual type
    System.out.println("Days: " + request.calculateLeaveDays());
    System.out.println("Requires Documentation: " + request.requiresDocumentation());

    if (request.isEligible()) {  // Calls the ACTUAL type's implementation
        request.approve("System");
    } else {
        request.reject("System", getRejectReason(request));
    }
}

// Works with ANY leave type
processLeaveRequest(new SickLeaveRequest(...));      // Uses SickLeaveRequest.isEligible()
processLeaveRequest(new VacationLeaveRequest(...));  // Uses VacationLeaveRequest.isEligible()
processLeaveRequest(new UnpaidLeaveRequest(...));    // Uses UnpaidLeaveRequest.isEligible()
```

#### Polymorphic Collections

```java
// Store different leave types in a single collection
List<LeaveRequest> allRequests = new ArrayList<>();
allRequests.add(new SickLeaveRequest(...));
allRequests.add(new VacationLeaveRequest(...));
allRequests.add(new BereavementLeaveRequest(...));

// Process all uniformly
for (LeaveRequest req : allRequests) {
    // Each call dispatches to the correct subclass implementation
    System.out.println(req.getLeaveType() + ": " + req.calculateLeaveDays() + " days");
    System.out.println("Eligible: " + req.isEligible());
}
```

#### Method Overriding with Extension

```java
// Base class implementation
public void approve(String approverName) {
    String oldStatus = this.status;
    this.status = "Approved";
    statusHistory.addChange(approverName, oldStatus, "Approved");
}

// UnpaidLeaveRequest overrides to add precondition
@Override
public void approve(String approverName) {
    if (hasAllApprovals()) {  // Additional check before approval
        super.approve(approverName);  // Reuse parent logic
    }
    // If not all approvals, silently does nothing
}
```

---

### 4. Inner Classes

**Concept**: Inner classes are classes defined within another class.
They provide encapsulation and logical grouping of related functionality.

#### 4.1 Static Nested Classes

**When to Use**: The inner class doesn't need access to outer class instance
members. It's a logically related but independent data structure.

```java
class SickLeaveRequest extends LeaveRequest {

    // Static nested class - doesn't need SickLeaveRequest instance
    public static class MedicalCertificate {
        private final String doctorName;
        private final String hospitalName;
        private final String diagnosis;
        private final LocalDate issueDate;

        // Has its own independent logic
        public boolean isValid() {
            return ChronoUnit.DAYS.between(issueDate, LocalDate.now()) <= 30;
        }
    }
}

// Can be created independently - no outer instance needed
SickLeaveRequest.MedicalCertificate cert =
    new SickLeaveRequest.MedicalCertificate("Dr. Smith", "City Hospital", "Flu");
```

**Static Nested Classes in This Project**:

| Class                | Outer Class            | Rationale                                           |
|----------------------|------------------------|-----------------------------------------------------|
| `StatusChange`       | `LeaveRequest`         | Immutable value object, self-contained event record |
| `MedicalCertificate` | `SickLeaveRequest`     | Independent document with own validation            |
| `TravelDetails`      | `VacationLeaveRequest` | Optional, self-contained travel info                |
| `ApprovalStep`       | `UnpaidLeaveRequest`   | Immutable approval record                           |
| `ContactInfo`        | `Employee`             | Standalone contact data holder                      |

#### 4.2 Non-Static Inner Classes (Member Inner Classes)

**When to Use**: The inner class needs access to outer class instance members.
Its lifecycle is tied to the outer object.

```java
class Employee {
    private LocalDate hireDate;

    public long getTenureYears() {
        return ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }

    // Non-static inner class - needs access to Employee instance
    public class LeaveBalance {
        private Map<String, Integer> balances;

        private void initializeBalances() {
            // DIRECTLY accesses outer class method - no reference needed!
            int tenureBonus = (int) (getTenureYears() / 5) * 5;
            balances.put("Vacation", 15 + tenureBonus);
        }

        public String getSummary() {
            // Accesses outer class method getFullName()
            return "Leave Balance for " + getFullName() + "...";
        }
    }
}
```

**Non-Static Inner Classes in This Project**:

| Class              | Outer Class          | Why Non-Static                                            |
|--------------------|----------------------|-----------------------------------------------------------|
| `LeaveBalance`     | `Employee`           | Accesses `getTenureYears()` for bonus calculation         |
| `StatusHistory`    | `LeaveRequest`       | Accesses `requestId`, `employee`, `leaveType` for reports |
| `ApprovalWorkflow` | `UnpaidLeaveRequest` | Accesses `calculateLeaveDays()`, `reason`, `requestId`    |

#### 4.3 Anonymous Inner Classes

**When to Use**: One-time use implementation of an interface or abstract class.

```java
// Anonymous inner class implementing Comparator
allRequests.sort(new Comparator<LeaveRequest>() {
    @Override
    public int compare(LeaveRequest r1, LeaveRequest r2) {
        return Long.compare(r2.calculateLeaveDays(), r1.calculateLeaveDays());
    }
});
```

#### 4.4 Local Inner Classes

**When to Use**: Helper class needed only within a specific method.

```java
private static void generateLeaveReport(List<LeaveRequest> requests) {
    // Local inner class - only visible within this method
    class ReportEntry {
        String employeeName;
        String leaveType;
        long days;

        ReportEntry(LeaveRequest req) {
            this.employeeName = req.getEmployee().getFullName();
            this.leaveType = req.getLeaveType();
            this.days = req.calculateLeaveDays();
        }

        String format() {
            return String.format("| %-15s | %-18s | %3d |",
                    employeeName, leaveType, days);
        }
    }

    for (LeaveRequest req : requests) {
        ReportEntry entry = new ReportEntry(req);
        System.out.println(entry.format());
    }
}
```

#### Inner Class Decision Matrix

```
Do you need access to outer class instance members?
│
├── NO → Use STATIC NESTED CLASS
│        Examples: MedicalCertificate, StatusChange, TravelDetails
│
└── YES → Is it used in only one method?
          │
          ├── YES → Use LOCAL INNER CLASS
          │         Example: ReportEntry in generateLeaveReport()
          │
          └── NO → Is it a one-time implementation?
                   │
                   ├── YES → Use ANONYMOUS INNER CLASS
                   │         Example: Comparator for sorting
                   │
                   └── NO → Use NON-STATIC INNER CLASS
                            Examples: LeaveBalance, StatusHistory, ApprovalWorkflow
```

---

### 5. Interfaces (Potential Extensions)

While the current implementation doesn't use explicit interfaces,
here are logical extensions where interfaces would add value:

#### Potential Interface: `Approvable`

```java
public interface Approvable {
    boolean isEligible();
    void approve(String approverName);
    void reject(String approverName, String reason);
    String getStatus();
}

// Could be implemented by LeaveRequest and other approvable items
// like ExpenseReports, PurchaseOrders, etc.
```

#### Potential Interface: `Documentable`

```java
public interface Documentable {
    boolean requiresDocumentation();
    boolean hasRequiredDocuments();
    List<Document> getAttachedDocuments();
}
```

#### Potential Interface: `Notifiable`

```java
public interface Notifiable {
    void notifyEmployee(String message);
    void notifyManager(String message);
    void notifyHR(String message);
}
```

---

## Leave Types and Business Rules

### Sick Leave
| Attribute         | Description                                             |
|-------------------|---------------------------------------------------------|
| **Duration**      | Flexible                                                |
| **Documentation** | Medical certificate required if >3 days or hospitalized |
| **Approval**      | Automatic if eligible                                   |

### Vacation Leave
| Attribute          | Description         |
|--------------------|---------------------|
| **Max Duration**   | 21 consecutive days |
| **Advance Notice** | 14 days recommended |
| **Documentation**  | Not required        |

### Maternity Leave
| Attribute             | Description              |
|-----------------------|--------------------------|
| **Standard Duration** | 12 weeks                 |
| **Extension**         | Up to 4 additional weeks |
| **Documentation**     | Always required          |

### Paternity Leave
| Attribute         | Description                          |
|-------------------|--------------------------------------|
| **Duration**      | 10 days (14 for adoption)            |
| **Valid Period**  | Within 8 weeks of birth              |
| **Documentation** | Birth certificate or adoption papers |

### Bereavement Leave
| Attribute            | Description                             |
|----------------------|-----------------------------------------|
| **Immediate Family** | 5 days (spouse, parent, child, sibling) |
| **Extended Family**  | 3 days                                  |
| **Travel**           | +2 days if travel required              |

### Unpaid Leave
| Attribute         | Description                                   |
|-------------------|-----------------------------------------------|
| **Max Duration**  | 30 days                                       |
| **Approval**      | Manager required; HR also required if >5 days |
| **Documentation** | Required if >5 days                           |

### Study Leave
| Attribute         | Description                              |
|-------------------|------------------------------------------|
| **Max Duration**  | 10 days (unlimited if company-sponsored) |
| **Paid**          | If company-sponsored or job-related      |
| **Documentation** | Course enrollment proof required         |

---

## Running the Application

### Prerequisites
- Java 11 or higher (uses `LocalDate`, `String.repeat()`)

### Compile
```bash
javac Employee.java Main.java
```

### Run
```bash
java Main
```

---

## Key Takeaways

| Concept              | Purpose                               | Example in Project                                      |
|----------------------|---------------------------------------|---------------------------------------------------------|
| **Inheritance**      | Code reuse, "is-a" relationship       | All leave types extend `LeaveRequest`                   |
| **Abstract Class**   | Define template with required methods | `LeaveRequest` with abstract `isEligible()`             |
| **Polymorphism**     | Same interface, different behaviors   | `processLeaveRequest(LeaveRequest)` handles any subtype |
| **Static Nested**    | Independent, logically related class  | `MedicalCertificate`, `StatusChange`                    |
| **Non-static Inner** | Needs outer instance access           | `LeaveBalance`, `ApprovalWorkflow`                      |
| **Anonymous Class**  | One-time interface implementation     | `Comparator` for sorting                                |
| **Local Class**      | Method-scoped helper                  | `ReportEntry` in report generation                      |
