package model;

//纪录片类 ，以Movie为父类
public class Documentary extends Movie {

    private String topic;

//constructor
    public Documentary(String id, String title, String genre, int year, double rating, String topic) {
        // 调用父类构造函数
        super(id, title, genre, year, rating);

        // 验证主题不能为空
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }

        this.topic = topic;
    }

//纪录片getter
    public String getTopic() {
        return topic;
    }

//纪录片setter
    public void setTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        this.topic = topic;
    }

//将纪录片对象转换为String
    public String toString() {
        return super.toString() + " Topic:" + topic;
    }

//比较两个纪录片对象是否相等
    @Override
    public boolean equals(Object obj) {
        // 先调用父类的equals方法检查ID是否相等
        if (!super.equals(obj)) {
            return false;
        }

        // 检查类型是否匹配
        if (getClass() != obj.getClass()) {
            return false;
        }

        // 比较主题是否相等
        Documentary that = (Documentary) obj;
        if (topic == null) {
            return that.topic == null;
        }
        return topic.equals(that.topic);
    }

//生成纪录片对象的Hashcode

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((topic == null) ? 0 : topic.hashCode());
        return result;
    }
}
