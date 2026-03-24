package service.interfaces;

import model.User;

/**
 * 认证服务接口 - 定义用户认证相关的操作
 * 包括登录和修改密码功能
 */
public interface IAuthService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功返回用户对象，否则返回null
     */
    User login(String username, String password);

    /**
     * 修改用户密码
     * @param user 要修改密码的用户
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 如果修改成功返回true，否则返回false
     */
    boolean changePassword(User user, String oldPassword, String newPassword);
}