package cn.hestyle.road_examination_manager.entity;

/**
 * 管理员entity
 * @author hestyle
 */
public class Manager {
    /**用户名*/
    private String username;
    /**密码*/
    private String password;
    /**姓名*/
    private String name;
    /**年龄*/
    private Integer age;
    /**性别*/
    private String gender;
    /**电话号码*/
    private String phoneNumber;
    /**是否删除，0未删除，1已删除*/
    private Integer isDel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDel=" + isDel +
                '}';
    }
}
