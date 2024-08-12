package service;

import model.Customer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomerService {
    private static CustomerService customerServiceInstance;

    private final Map<String, Customer> customers = new HashMap<>();
    public static CustomerService getInstance() {
        if (customerServiceInstance == null) {
            customerServiceInstance = new CustomerService();
        }
        return customerServiceInstance;
    }
    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(customer.getEmail(), customer);
    }

    public Customer getCustomerByEmail(String email) {
        return customers.get(email);
    }

    public Set<Customer> getAllCustomers() {
        return new HashSet<>(customers.values());
    }

}
