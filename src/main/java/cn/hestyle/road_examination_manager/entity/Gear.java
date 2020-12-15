package cn.hestyle.road_examination_manager.entity;

public class Gear {
    private String id;

    private String name;

    private Integer minSpeed;

    private Integer maxSpeed;

    private String description;

    private Integer isDel;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinSpeed(Integer minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinSpeed() {
        return minSpeed;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIsDel() {
        return isDel;
    }

    @Override
    public String toString() {
        return "Gear{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minSpeed=" + minSpeed +
                ", maxSpeed=" + maxSpeed +
                ", description='" + description + '\'' +
                ", isDel=" + isDel +
                '}';
    }
}
