import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Class to simulate a bank account
class BankAccount128 {
    private int balance = 1000; // Initial balance in the account
    private final Lock lock = new ReentrantLock(); // Lock to ensure thread safety

    // Method to handle deposit transactions
    public void deposit(int amount) {
        lock.lock(); // Acquire the lock
        try {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " Deposited " + amount + ". New balance: " + balance);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method to handle withdrawal transactions
    public void withdraw(int amount) {
        lock.lock(); // Acquire the lock
        try {
            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " Withdrew " + amount + ". Remaining balance: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " Insufficient funds for withdrawal of " + amount);
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}

// Class to simulate a bank transaction as a thread
class BankTransactionThread extends Thread {
    private final BankAccount128 account;
    private final int amount;
    private final boolean isDeposit; // Flag to determine if it's a deposit or withdrawal

    // Constructor to initialize the transaction details
    public BankTransactionThread(BankAccount128 account, int amount, boolean isDeposit) {
        this.account = account;
        this.amount = amount;
        this.isDeposit = isDeposit;
    }

    // Method executed by the thread to process the transaction
    @Override
    public void run() {
        if (isDeposit) {
            account.deposit(amount); // Perform deposit
        } else {
            account.withdraw(amount); // Perform withdrawal
        }
    }
}

// Main class to simulate the banking system
public class BankAccount128 {
    public static void main(String[] args) {
        // Initialize the bank account
        BankAccount128 account = new BankAccount128();

        // Create and start multiple threads to simulate concurrent transactions
        BankTransactionThread thread1 = new BankTransactionThread(account, 200, true);  // Deposit 200
        BankTransactionThread thread2 = new BankTransactionThread(account, 150, false); // Withdraw 150
        BankTransactionThread thread3 = new BankTransactionThread(account, 500, true);  // Deposit 500
        BankTransactionThread thread4 = new BankTransactionThread(account, 300, false); // Withdraw 300

        // Start the threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            // Wait for all threads to finish processing
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted.");
            e.printStackTrace();
        }

        System.out.println("All transactions processed.");
    }
}
