package com.apap.tu08.controller;

import com.apap.tu08.model.PasswordModel;
import com.apap.tu08.model.UserRoleModel;
import com.apap.tu08.repository.UserRoleDB;
import com.apap.tu08.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserRoleController {
    @Autowired
    private UserRoleService userService;

    @Autowired
    private UserRoleDB userRoleDB;

    private static Pattern passPatern = Pattern.compile("[0-9]");
    private static Pattern passAlphaPatern = Pattern.compile("[a-zA-Z]");

    @PostMapping(value = "/addUser")
    public String addUserSubmit(@ModelAttribute UserRoleModel user, Model model){
        if(user.getPassword().length() < 8){
            model.addAttribute("msg", "Password kurang dari 8 karakter.");

        }else{

            if(checkAlphaNumeric(user.getPassword())){
                userService.addUser(user);

                model.addAttribute("msg", "User berhasil ditambahkan.");

            }else{
                model.addAttribute("msg", "Password harus terdiri dari kombinasi karakter dan nomor.");
            }

        }
        return "home";
    }

    @PostMapping(value = "/updatePassword")
    public String updatePass(@ModelAttribute PasswordModel passwordModel, Principal principal, Model model){

        UserRoleModel updateUser = userRoleDB.findByUsername(principal.getName());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(passwordModel.getOldPassword(), updateUser.getPassword()) &&
                passwordModel.getNewPassword().equals(passwordModel.getConfirmPassword()) &&
                checkAlphaNumeric(passwordModel.getNewPassword()) &&
                (passwordModel.getNewPassword().length() >= 8)
        ) {

            UserRoleModel user = new UserRoleModel();
            user.setId(updateUser.getId());
            user.setUsername(updateUser.getUsername());
            user.setRole(updateUser.getRole());
            user.setPassword(passwordModel.getNewPassword());
            userService.addUser(user);

            model.addAttribute("msgPass", "Success Change Password!");
        }
        else {
            if(passwordEncoder.matches(passwordModel.getOldPassword(), updateUser.getPassword())){
                model.addAttribute("msgPass", "Password salah");

            }else if (passwordModel.getNewPassword().equals(passwordModel.getConfirmPassword())){
                model.addAttribute("msgPass", "Password baru dan password konfirmasi tidak cocok");

            }else if(checkAlphaNumeric(passwordModel.getNewPassword())){
                model.addAttribute("msgPass", "Password harus terdiri dari kombinasi huruf dan angka");

            }else if(passwordModel.getNewPassword().length() >= 8){
                model.addAttribute("msgPass", "Password harus terdiri dari minimal 8 karakter");

            }

        }

        return "home";
    }

    public static boolean checkAlphaNumeric(String s) {
        return passPatern.matcher(s).find() && passAlphaPatern.matcher(s).find();
    }

}
