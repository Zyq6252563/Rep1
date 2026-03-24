package model;

public class Movie {

    private String id;
    private String title;
    private String genre;
    private int year;
    private double rating;

//constructor
    public Movie(String id, String title, String genre, int year, double rating) {
        // 验证评分是否有效
        if (rating < 0.0 || rating > 10.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 10.0");
        }

        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

//电影类getter
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getGenre() {
        return genre;
    }
    public int getYear() {
        return year;
    }
    public double getRating() {
        return rating;
    }
    public void setId(String id) {
        this.id = id;
    }

//电影类setter
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setRating(double rating) {

        if (rating < 0.0 || rating > 10.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 10.0");
        }
        this.rating = rating;
    }

    //将电影对象转换为String
    @Override
    public String toString() {
        return "ID:" + id + " Title:" + title + " Genre:" + genre +
               " Year:" + year + " Rating:" + rating;
    }

    //比较两个电影对象是否相等（基于ID）
    @Override
    public boolean equals(Object obj) {

        // 检查是否为同一个对象
        if (this == obj) {
            return true;
        }

        // 检查对象是否为null或类型不匹配
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 基于ID比较电影是否相等
        Movie movie = (Movie) obj;
        if (id == null) {
            return movie.id == null;
        }
        return id.equals(movie.id);
    }

    //生成Movie对象的hashcode
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }
}
