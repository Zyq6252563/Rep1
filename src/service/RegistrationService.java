package service;

import model.User;
import model.PremiumUser;
import util.DataValidator;
import util.ExceptionHandler;
import util.EncryptionUtil;
import java.util.List;
import java.util.Map;

public class RegistrationService {
    // set original values
    private List<User> users;
    private Map<String, User> usersMap;

    public RegistrationService(List<User> users) {
        this.users = users;
        this.usersMap = null;
    }

    public RegistrationService(List<User> users, Map<String, User> usersMap) {
        this.users = users;
        this.usersMap = usersMap;
    }

    public boolean registerUser(String username, String password) {
        return registerUser(username, password, false);
    }
    // check whether it is correct user
    public boolean registerUser(String username, String password, boolean isPremium) {
        if (!DataValidator.isValidUsername(username)) {
            ExceptionHandler.handleValidationException("Username is invalid");
            return false;
        }
        if (!DataValidator.isValidPassword(password)) {
            ExceptionHandler.handleValidationException("Password is invalid");
            return false;
        }

        boolean exist = false;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            ExceptionHandler.handleValidationException("Username is already in use");
            return false;
        }

        // use new password to create new users
        String hashedPassword = EncryptionUtil.hashPassword(password);
        User newUser;
        if (isPremium) {
            newUser = new PremiumUser(username, hashedPassword);
        } else {
            newUser = new User(username, hashedPassword);
        }
        users.add(newUser);

        // synchronised update new users
        if (usersMap != null) {
            usersMap.put(username, newUser);
        }

        return true;
    }
}
