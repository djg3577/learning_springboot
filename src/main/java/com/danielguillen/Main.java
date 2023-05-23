package com.danielguillen;

import com.danielguillen.customer.Customer;
import com.danielguillen.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);

        }
        @Bean
        CommandLineRunner runner(CustomerRepository customerRepository){

            return args -> {
                Customer alex = new Customer(
                        "Alex",
                        "alex@gmail.com",
                        21

                );

                Customer Jamila = new Customer(
                        "Jamila",
                        "Jamila@gmail.com",
                        19

                );
                List<Customer> customers = List.of(alex, Jamila);
                customerRepository.saveAll(customers);
            };
        }
        @Bean
        public Foo getFoo(){
            return new Foo("bar");
        }
        record Foo(String name){ }


}
