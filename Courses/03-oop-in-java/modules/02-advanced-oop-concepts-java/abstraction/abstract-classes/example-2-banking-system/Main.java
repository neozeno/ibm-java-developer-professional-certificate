abstract class BankAccount {
    protected int accountNumber;
    protected String accountHolder;
    protected double balance;

    BankAccount(int accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    abstract void calculateInterest();
    abstract double getMinimumBalance();

    void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            displayBalance();
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    void withdraw(double amount) {
        if (amount > 0 && (balance - amount >= getMinimumBalance())) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
            displayBalance();
        } else {
            System.out.println("Insufficient balance or invalid amount");
        }
    }

    void displayBalance() {
        System.out.println("Current balance: $" + balance);
    }

    void displayAccountInfo() {
        System.out.println("Account number: " + accountNumber);
        System.out.println("Account holder: " + accountHolder);
        System.out.println("Balance: $" + balance);
    }
}

class SavingsAccount extends BankAccount {
    private final double INTEREST_RATE = 0.04;  // 4% annual interest

    SavingsAccount(int accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
    }

    @Override
    void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest added: $" + interest);
        displayBalance();
    }

    @Override
    double getMinimumBalance() {
        return 1000.0;  // Minimum $1000 balance
    }
}

class CurrentAccount extends BankAccount {
    private final double OVERDRAFT_LIMIT = 5000.0;

     CurrentAccount(int accountNumber, String accountHolder, double balance) {
         super(accountNumber, accountHolder, balance);
     }

     @Override
     void calculateInterest() {
         // No interest for current accounts
         System.out.println("Current accounts do not earn interests.");
     }

     @Override
     double getMinimumBalance() {
         return -OVERDRAFT_LIMIT;  // Can go negative up to overdraft limit
     }

     @Override
     void withdraw(double amount) {
         if (amount > 0 && (balance - amount >= getMinimumBalance())) {
             balance -= amount;
             System.out.println("Withdrawn: $" + amount);

             if (balance < 0) {
                 System.out.println("WARNING: Overdraft used!");
             }

             displayBalance();
         } else {
             System.out.println("Exceeds overdraft limit or invalid amount.");
         }
     }
 }

 public class Main {
     public static void main(String[] args) {
         SavingsAccount savingsAcct = new SavingsAccount(123, "John Doe", 5000);
         CurrentAccount currentAcct = new CurrentAccount(700, "Jane Doe", 2000);

         System.out.println("=== Savings Account ===");
         savingsAcct.displayAccountInfo();
         savingsAcct.deposit(1000);
         savingsAcct.calculateInterest();
         savingsAcct.withdraw(500);

         System.out.println("=== Current Account ===");
         currentAcct.displayAccountInfo();
         currentAcct.deposit(1000);
         currentAcct.calculateInterest();
         currentAcct.withdraw(500);
     }
 }
