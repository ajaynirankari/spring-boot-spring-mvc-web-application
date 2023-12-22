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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.List;

@SpringBootApplication
public class WebFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFormApplication.class, args);
    }

}

@Controller
class WebController {

    private final LoginRepo repo;

    WebController(LoginRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/login/history")
    public String loginHistory(Model model) {
        System.out.println("loginHistory called model = " + model);

        List<Login> allLogins = repo.findAll();
        System.out.println("allLogins.size() = " + allLogins.size());

        model.addAttribute("getAllLoginsByKey", allLogins);

        return "loginHistoryPage";

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
}

interface LoginRepo extends JpaRepository<Login, Long> {
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
