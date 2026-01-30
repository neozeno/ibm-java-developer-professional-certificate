// Interface defines the contract
interface PaymentProcessor {
    void processPayment(double amount);
    void refund(double amount);
}

// Abstract class that provide common implementation
abstract class BasePaymentProcessor implements PaymentProcessor {
    protected String merchantId;
    protected double totalProcessed;

    BasePaymentProcessor(String merchantId) {
        this.merchantId = merchantId;
        this.totalProcessed = 0;
    }

    // Common implementation for all payment processors
    @Override
    public void refund(double amount) {
        if (amount > 0 && amount <= totalProcessed) {
            totalProcessed -= amount;
            System.out.printf("Refunded: $%.2f\n", amount);
            System.out.printf("Total Processed: $%.2f\n", totalProcessed);
        } else {
            System.out.println("Invalid refund amount.");
        }
    }

    // Abstract method—each processor implements it differently
    abstract void logTransaction(String type, double amount);

    protected void updateTotal(double amount) {
        totalProcessed += amount;
    }
}

class CreditCardProcessor extends BasePaymentProcessor {
    CreditCardProcessor(String merchantId) {
        super(merchantId);
    }

    @Override
    public void processPayment(double amount) {
        System.out.printf("Processing credit card payment: $%.2f\n", amount);
        logTransaction("CREDIT_CARD", amount);
        updateTotal(amount);
    }

    @Override
    void logTransaction(String type, double amount) {
        System.out.printf("[LOG] %s transaction: $%.2f (Merchant: %s)\n", type, amount, merchantId);
    }
}

class PayPalProcessor extends BasePaymentProcessor {
    PayPalProcessor(String merchantId) {
        super(merchantId);
    }

    @Override
    public void processPayment(double amount) {
        System.out.printf("Processing PayPal payment: $%.2f\n", amount);
        logTransaction("PAYPAL", amount);
        updateTotal(amount);
    }

    @Override
    void logTransaction(String type, double amount) {
        System.out.printf("[PAYPAL_LOG] %s transaction: $%.2f via %s\n", type, amount, merchantId);
    }
}

public class CombiningAbstractClassesAndInterfaces {
    public static void main(String[] args) {
        PaymentProcessor cc = new CreditCardProcessor("MERCHANT_001");
        PaymentProcessor pp = new PayPalProcessor("MERCHANT_002");

        cc.processPayment(100.0);
        cc.processPayment(50.0);
        cc.refund(30.0);

        System.out.println();

        pp.processPayment(200.0);
        pp.refund(50.0);
    }
}
