package cn.hestyle.road_examination_manager.entity;

public class ExamTemplateItem {
    String examTemplateId;
    Integer examItemId;

    public ExamTemplateItem(String examTemplateId, Integer examItemId) {
        this.examTemplateId = examTemplateId;
        this.examItemId = examItemId;
    }

    public Integer getExamItemId() {
        return examItemId;
    }

    public void setExamItemId(Integer examItemId) {
        this.examItemId = examItemId;
    }

    @Override
    public String toString() {
        return "ExamTemplateItem{" +
                "examTemplateId='" + examTemplateId + '\'' +
                ", examItemId=" + examItemId +
                '}';
    }
}
