package com.gl.WebForm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
public class WebFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFormApplication.class, args);
    }

}

@Controller
class WebController {

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

        if ("admin".equals(login.getUserName()) && "admin".equals(login.getPassword())) {
            System.out.println("loginSuccessFormPage");
            return "loginSuccessFormPage";
        } else {
            System.out.println("loginFailFormPage");
            return "loginFailFormPage";
        }
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

class Login {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
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
