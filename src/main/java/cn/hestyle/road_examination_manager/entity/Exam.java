package cn.hestyle.road_examination_manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static cn.hestyle.road_examination_manager.util.Constants.DATETIME_FORMAT;

public class Exam {
    /** 准考证号（日期+流水号） 主键 */
    private String admissionNo;
    /** 考试车id */
    private Integer carId;
    /** 考试模板id */
    private String examTemplateId;
    /** (夜考)考试模板id */
    private String lightExamTemplateId;
    /** 考官身份证 */
    private String examinerId;
    /** 考生身份证 */
    private String candidateId;
    /** 开考时间 */
    @JsonFormat(pattern = DATETIME_FORMAT, timezone = "GMT+8")
    private Date examTime;
    /** 未考0/考试中2/已完成2 */
    private Integer state;
    /** 考试完成时间 */
    @JsonFormat(pattern = DATETIME_FORMAT, timezone = "GMT+8")
    private Date completedTime;
    /** 是否通过 */
    private Integer isPass;
    /** 考试得分 */
    private Integer scored;
    /** 丢分描述 */
    private String scoreLossDesc;
    /** 是否删除 */
    private Integer isDel = 0;

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getExamTemplateId() {
        return examTemplateId;
    }

    public void setExamTemplateId(String examTemplateId) {
        this.examTemplateId = examTemplateId;
    }

    public String getLightExamTemplateId() {
        return lightExamTemplateId;
    }

    public void setLightExamTemplateId(String lightExamTemplateId) {
        this.lightExamTemplateId = lightExamTemplateId;
    }

    public String getExaminerId() {
        return examinerId;
    }

    public void setExaminerId(String examinerId) {
        this.examinerId = examinerId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public Integer getScored() {
        return scored;
    }

    public void setScored(Integer scored) {
        this.scored = scored;
    }

    public String getScoreLossDesc() {
        return scoreLossDesc;
    }

    public void setScoreLossDesc(String scoreLossDesc) {
        this.scoreLossDesc = scoreLossDesc;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "admissionNo='" + admissionNo + '\'' +
                ", carId=" + carId +
                ", examTemplateId='" + examTemplateId + '\'' +
                ", lightExamTemplateId='" + lightExamTemplateId + '\'' +
                ", examinerId='" + examinerId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                ", examTime=" + examTime +
                ", state=" + state +
                ", completedTime=" + completedTime +
                ", isPass=" + isPass +
                ", scored=" + scored +
                ", scoreLossDesc='" + scoreLossDesc + '\'' +
                ", isDel=" + isDel +
                '}';
    }
}
