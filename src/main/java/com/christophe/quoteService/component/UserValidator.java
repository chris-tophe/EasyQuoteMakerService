package com.christophe.quoteService.component;

import com.christophe.quoteService.models.User;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    Pattern usernamePattern = Pattern.compile("^\\w{4,9}$");

    public boolean isValid(User user){
       return isUsernameValid(user);
    }

    private boolean isUsernameValid(User user){
        Matcher matcher = usernamePattern.matcher(user.getUsername());
        return matcher.matches();
    }

}
