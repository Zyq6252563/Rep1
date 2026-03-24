package service;

import model.User;
import service.interfaces.IAuthService;
import util.DataValidator;
import util.ExceptionHandler;
import util.EncryptionUtil;
import java.util.List;

public class AuthService implements IAuthService {
    // set original values
    private List<User> users;

    public AuthService(List<User> users) {
        this.users = users;
    }

    // method to log in
    public User login(String username, String password) {
        System.out.println("[DEBUG AuthService] Login attempt - username: " + username);
        System.out.println("[DEBUG AuthService] Total users in list: " + users.size());

        if (!DataValidator.isValidUsername(username) || !DataValidator.isValidPassword(password)) {
            ExceptionHandler.handleValidationException("Invalid username or password!");
            return null;
        }

        User targetUser = null;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println("[DEBUG AuthService] Checking user " + i + ": " + user.getUsername());
            if (user.getUsername().equals(username)) {
                targetUser = user;
                System.out.println("[DEBUG AuthService] Found matching user!");
                break;
            }
        }

        if (targetUser == null) {
            System.out.println("[DEBUG AuthService] User not found in list!");
            ExceptionHandler.handleValidationException("User does not exist!");
            return null;
        }

        // the format of passwords
        String storedPassword = targetUser.getPassword();
        System.out.println("[DEBUG AuthService] Stored password length: " + storedPassword.length());
        boolean passwordMatch = false;

        // check whether it is hash password
        if (storedPassword.length() == 64 && storedPassword.matches("[0-9a-fA-F]+")) {
            System.out.println("[DEBUG AuthService] Using hashed password comparison");
            // compare hash value
            String hashedInputPassword = EncryptionUtil.hashPassword(password);
            passwordMatch = storedPassword.equals(hashedInputPassword);
        } else {
            System.out.println("[DEBUG AuthService] Using plaintext password comparison");
            passwordMatch = storedPassword.equals(password);

            // after registration replace with hash password
            if (passwordMatch) {
                targetUser.setPassword(EncryptionUtil.hashPassword(password));
            }
        }

        if (passwordMatch) {
            System.out.println("[DEBUG AuthService] Login successful!");
            return targetUser;
        } else {
            System.out.println("[DEBUG AuthService] Password mismatch!");
            ExceptionHandler.handleValidationException("Password is wrong!");
            return null;
        }
    }

    public boolean changePassword(User user, String oldPassword, String newPassword) {
        // verify old password
        String storedPassword = user.getPassword();
        boolean oldPasswordCorrect = false;

        if (storedPassword.length() == 64 && storedPassword.matches("[0-9a-fA-F]+")) {
            String hashedOldPassword = EncryptionUtil.hashPassword(oldPassword);
            oldPasswordCorrect = storedPassword.equals(hashedOldPassword);
        } else {
            oldPasswordCorrect = storedPassword.equals(oldPassword);
        }

        if (!oldPasswordCorrect) {
            ExceptionHandler.handleValidationException("Password is incorrect!");
            return false;
        }

        if (!DataValidator.isValidPassword(newPassword)) {
            ExceptionHandler.handleValidationException("New password's format is incorrect!");
            return false;
        }

        // save new password
        user.setPassword(EncryptionUtil.hashPassword(newPassword));
        return true;
    }
}
