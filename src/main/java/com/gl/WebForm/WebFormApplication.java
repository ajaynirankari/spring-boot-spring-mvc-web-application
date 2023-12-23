package com.gl.WebForm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class WebFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFormApplication.class, args);
    }

}

@Controller
class WebController {

    private final LoginRepo repo;
    private final CustomerRepo customerRepo;

    WebController(LoginRepo repo, CustomerRepo customerRepo) {
        this.repo = repo;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/create/customer")
    public String createCustomerFrom(Model model) {
        System.out.println("createCustomerFrom called ");

        model.addAttribute("listAllCustomers", customerRepo.findAll());
        model.addAttribute("createCustomer", new Customer());
        model.addAttribute("updateCustomer", new Customer());

        return "customerPage";
    }

    @PostMapping("/create/customer")
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
        System.out.println("createCustomer called, customer = " + customer);
        customerRepo.save(customer);

        model.addAttribute("listAllCustomers", customerRepo.findAll());
        model.addAttribute("createCustomer", new Customer());
        model.addAttribute("updateCustomer", new Customer());

        return "customerPage";
    }

    @PostMapping("/delete/customer")
    public String deleteCustomer(@RequestParam("checkedCustomer") List<Long> ids, Model model) {
        System.out.println("deleteCustomer called, ids = " + ids);
        customerRepo.deleteAllById(ids);

        model.addAttribute("listAllCustomers", customerRepo.findAll());
        model.addAttribute("createCustomer", new Customer());
        model.addAttribute("updateCustomer", new Customer());

        return "customerPage";
    }

    @GetMapping("/update/customer/{id}")
    public String updateCustomerForm(@PathVariable Long id, Model model) {
        System.out.println("updateCustomerForm called, id = " + id);
        Optional<Customer> customerRepoById = customerRepo.findById(id);
        if (customerRepoById.isPresent()) {
            Customer customer = customerRepoById.get();

            model.addAttribute("updateCustomer", customer);

        }
        model.addAttribute("listAllCustomers", customerRepo.findAll());
        model.addAttribute("createCustomer", new Customer());

        return "customerPage";
    }

    @PostMapping("/update/customer")
    public String updateCustomer(@ModelAttribute Customer updatedCustomer, Model model) {
        System.out.println("updateCustomer called, updatedCustomer = " + updatedCustomer);
        Optional<Customer> customerRepoById = customerRepo.findById(updatedCustomer.getId());
        if (customerRepoById.isPresent()) {
            Customer dbCustomer = customerRepoById.get();
            dbCustomer.setName(updatedCustomer.getName());
            dbCustomer.setAge(updatedCustomer.getAge());
            dbCustomer.setEmail(updatedCustomer.getEmail());
            dbCustomer.setAddress(updatedCustomer.getAddress());
            customerRepo.save(dbCustomer);
        }

        model.addAttribute("listAllCustomers", customerRepo.findAll());
        model.addAttribute("createCustomer", new Customer());
        model.addAttribute("updateCustomer", new Customer());

        return "customerPage";
    }

    @GetMapping("/")
    public String loginForm(Model model) {
        System.out.println("loginForm called model = " + model);
        model.addAttribute("loginRequestObjectKey", new Login());
        return "loginFormPage";
    }

    @PostMapping("/validate/login")
    public String validateLoginPageForm(@ModelAttribute Login login, Model model) {
        System.out.println("validateLoginPageForm called, greeting = " + login);
        model.addAttribute("loginResponseObjectKey", login);
        String returnPage;

        if ("admin".equals(login.getUserName()) && "admin".equals(login.getPassword())) {
            System.out.println("loginSuccessFormPage");
            login.setStatus("Success");
            returnPage = "loginSuccessFormPage";
        } else {
            System.out.println("loginFailFormPage");
            login.setStatus("Fail");
            returnPage = "loginFailFormPage";
        }
        login.setTime(Instant.now());
        repo.save(login);

        return returnPage;
    }

    @GetMapping("/login/history")
    public String loginHistory(Model model) {
        System.out.println("loginHistory called model = " + model);

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());

        model.addAttribute("getAllLoginsByKey", allLogins);

        return "loginHistoryPage";

    }

    @GetMapping("/update/login/history")
    public String updateLoginHistory(Model model) {
        System.out.println("updateLoginHistory called model = " + model);

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());

        model.addAttribute("getAllLoginsByKey", allLogins);
        model.addAttribute("login", new Login());

        return "updateLoginHistoryPage";

    }

    @GetMapping("/logins/{id}")
    public String updateLoginFormHistory(@PathVariable Long id, Model model) {
        System.out.println("updateLoginFormHistory id = " + id);
        Optional<Login> optionalLogin = repo.findById(id);
        if (optionalLogin.isPresent()) {
            Login login = optionalLogin.get();
            model.addAttribute("loginRequestObjectKey", login);
        }

        return "updateLoginFormPage";

    }

    @PostMapping("/update/login/{id}")
    public String updateLoginHistory(@PathVariable Long id, @ModelAttribute Login updatedLogin, Model model) {
        System.out.println("updateLoginHistory id = " + id + ", updatedLogin = " + updatedLogin);
        Optional<Login> optionalLogin = repo.findById(id);
        if (optionalLogin.isPresent()) {
            Login existingLogin = optionalLogin.get();
            if (updatedLogin.getUserName() != null)
                existingLogin.setUserName(updatedLogin.getUserName());
            if (updatedLogin.getStatus() != null)
                existingLogin.setStatus(updatedLogin.getStatus());
            repo.save(existingLogin);
        }

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());

        model.addAttribute("getAllLoginsByKey", allLogins);
        model.addAttribute("login", new Login());

        return "updateLoginHistoryPage";

    }

    @GetMapping("/delete/login/history")
    public String deleteLoginHistory(Model model) {
        System.out.println("deleteLoginHistory called model = " + model);

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());

        model.addAttribute("getAllLoginsByKey", allLogins);

        return "updateLoginHistoryPage";

    }

    @PostMapping("/delete/login")
    public String deleteLogins(@RequestParam("deleteLoginId") List<Long> loginIds, Model model) {
        System.out.println("deleteLogins loginIds = " + loginIds);

        repo.deleteAllById(loginIds);

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());
        model.addAttribute("getAllLoginsByKey", allLogins);

        return "updateLoginHistoryPage";
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        System.out.println("greetingForm called model = " + model);
        model.addAttribute("greetingRequestObjectKey", new Greeting());
        return "greetingPage";
    }

    @PostMapping("/create/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        System.out.println("greetingSubmit called, greeting = " + greeting);
        model.addAttribute("greetingResponseObjectKey", greeting);
        return "resultPage";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
}

interface CustomerRepo extends JpaRepository<Customer, Long> {
}

interface LoginRepo extends JpaRepository<Login, Long> {
}

@Data
@Entity
class Customer {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int age;
    private String email;
    private String address;
}

@Data
@Entity
class Login {
    @Id
    @GeneratedValue
    private long id;
    private String userName;
    private String password;
    private String status;
    private Instant time;
}

class Greeting {

    private long id;
    private String content;
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
