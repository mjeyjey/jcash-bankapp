package com.jcash.bankapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

      private double balance = 0;
        private List<String> history = new ArrayList<>();


    
    @GetMapping("/")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password");
        }
        return "login"; // hahanapin ang login.html sa /templates
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        // Dummy check (walang database)
        if ("akoadmin".equals(username) && "1234".equals(password)) {
            model.addAttribute("username", username);
            return "dashboard";
        }
            //
              if ("admin".equals(username) && "4321".equals(password)) {
            model.addAttribute("username", username);
            return "dashboard";

        } else {
            return "redirect:/?error"; //babalik sa login if wrong cridentials
        } 

    }
    
  
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("balance", balance);
        return "dashboard";
    }

    @GetMapping("/deposit")
    public String showDeposit() {
        return "deposit";
    }

    @PostMapping("/deposit")
    public String doDeposit(@RequestParam double amount, Model model) {
        balance += amount;
        String log = "Deposited ₱" + amount + " on " + LocalDateTime.now();
        history.add(log);
        model.addAttribute("message", log);
        model.addAttribute("balance", balance);
        return "receipt";
    }

    @GetMapping("/withdraw")
    public String showWithdraw() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String doWithdraw(@RequestParam double amount, Model model) {
        if (amount > balance) {
            model.addAttribute("message", "❌ Insufficient balance.");
        } else {
            balance -= amount;
            String log = "Withdrew ₱" + amount + " on " + LocalDateTime.now();
            history.add(log);
            model.addAttribute("message", log);
        }
        model.addAttribute("balance", balance);
        return "receipt";
    }

    @GetMapping("/cashin")
    public String showCashIn() {
        return "cashin";
    }

    @PostMapping("/cashin")
    public String doCashIn(@RequestParam double amount, Model model) {
        balance += amount;
        String log = "Cashed In ₱" + amount + " on " + LocalDateTime.now();
        history.add(log);
        model.addAttribute("message", log);
        model.addAttribute("balance", balance);
        return "receipt";
    }

    @GetMapping("/send")
    public String showSend() {
        return "send";
    }

    @PostMapping("/send")
    public String doSend(@RequestParam double amount,
                        @RequestParam String recipient,
                        Model model) {
        if (amount > balance) {
            model.addAttribute("message", "❌ Insufficient balance.");
        } else {
            balance -= amount;
            String log = "Sent ₱" + amount + " to " + recipient + " on " + LocalDateTime.now();
            history.add(log);
            model.addAttribute("message", log);
        }
        model.addAttribute("balance", balance);
        return "receipt";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        model.addAttribute("history", history);
        model.addAttribute("balance", balance);
        model.addAttribute("username", "admin"); // or kukuhain sa session if needed
        return "history";
    }

}
