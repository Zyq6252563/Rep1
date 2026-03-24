package model;

public class User {

    private String username;
    private String password;
    private Watchlist watchlist;
    private History history;

    //constructor
  public User(String username, String password) {
        // 验证用户名不能为空
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        // 验证密码不能为空
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        this.username = username;
        this.password = password;
        this.watchlist = new Watchlist();
        this.history = new History();
    }

    //getter
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Watchlist getWatchlist() {
        return watchlist;
    }
    public History getHistory() {
        return history;
    }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

//setter
public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }
public void setHistory(History history) {
        this.history = history;
    }
public boolean isPremium() {
        return false;
    }

//将用户对象转换为String表示

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", isPremium=" + isPremium() +
               ", watchlistSize=" + watchlist.size() +
               ", historySize=" + history.size() +
               '}';
    }

//比较两个用户对象是否相等
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;
        if (username == null) {
            return user.username == null;
        }
        return username.equals(user.username);
    }

    //生成用户对象的hashcode
    @Override
    public int hashCode() {
        return (username == null) ? 0 : username.hashCode();
    }
}
