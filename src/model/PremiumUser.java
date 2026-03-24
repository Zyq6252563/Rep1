package model;

//PremiumUser类，表示会员用户,以User类为父类
public class PremiumUser extends User {

    private boolean premium;

    //constructor
    public PremiumUser(String username, String password) {
        // 调用父类构造函数
        super(username, password);
        // 设置为高级会员
        this.premium = true;
    }

    //会员类getter
    public boolean getPremium() {
        return premium;
    }

    //会员类setter
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    //检查用户是否为会员

    @Override
    public boolean isPremium() {
        return true;
    }

    //将高级用户对象用String表示
    @Override
    public String toString() {
        return "PremiumUser{" +
               "username='" + getUsername() + '\'' +
               ", isPremium=" + isPremium() +
               ", watchlistSize=" + getWatchlist().size() +
               ", historySize=" + getHistory().size() +
               '}';
    }

   //比较两个高级用户对象是否相等
    @Override
    public boolean equals(Object obj) {

        if (!super.equals(obj)) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        PremiumUser that = (PremiumUser) obj;
        return premium == that.premium;
    }

    //生成会员用户对象的hashcode

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (premium ? 1 : 0);
        return result;
    }
}
