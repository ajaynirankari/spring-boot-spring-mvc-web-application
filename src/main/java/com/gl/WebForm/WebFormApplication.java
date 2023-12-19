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
