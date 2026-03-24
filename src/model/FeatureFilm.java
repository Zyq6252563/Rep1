package model;

//故事片类 ，以Movie为父类
public class FeatureFilm extends Movie {

    // 电影导演
    private String director;

    //constructor
    public FeatureFilm(String id, String title, String genre, int year, double rating, String director) {
        // 调用父类构造函数
        super(id, title, genre, year, rating);

        // 验证导演名不能为空
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("Director cannot be null or empty");
        }

        this.director = director;
    }

  //故事片getter
    public String getDirector() {
        return director;
    }
  //故事片setter
    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("Director cannot be null or empty");
        }
        this.director = director;
    }

    //将故事片对象转换为String

    @Override
    public String toString() {
        return super.toString() + " Director:" + director;
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

        // 比较导演是否相等
        FeatureFilm that = (FeatureFilm) obj;
        if (director == null) {
            return that.director == null;
        }
        return director.equals(that.director);
    }

    //生成纪录片对象的Hashcode
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((director == null) ? 0 : director.hashCode());
        return result;
    }
}
