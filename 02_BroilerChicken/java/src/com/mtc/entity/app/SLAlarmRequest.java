package com.mtc.entity.app;

import java.util.Date;

public class SLAlarmRequest {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.uuid
     *
     * @mbggenerated
     */
    private String uuid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.model
     *
     * @mbggenerated
     */
    private String model;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.version
     *
     * @mbggenerated
     */
    private String version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.platform
     *
     * @mbggenerated
     */
    private String platform;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.cdate
     *
     * @mbggenerated
     */
    private Date cdate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.result
     *
     * @mbggenerated
     */
    private String result;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_l_alarm_request.userid
     *
     * @mbggenerated
     */
    private Integer userid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.id
     *
     * @return the value of s_l_alarm_request.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.id
     *
     * @param id the value for s_l_alarm_request.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.uuid
     *
     * @return the value of s_l_alarm_request.uuid
     *
     * @mbggenerated
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.uuid
     *
     * @param uuid the value for s_l_alarm_request.uuid
     *
     * @mbggenerated
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.model
     *
     * @return the value of s_l_alarm_request.model
     *
     * @mbggenerated
     */
    public String getModel() {
        return model;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.model
     *
     * @param model the value for s_l_alarm_request.model
     *
     * @mbggenerated
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.version
     *
     * @return the value of s_l_alarm_request.version
     *
     * @mbggenerated
     */
    public String getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.version
     *
     * @param version the value for s_l_alarm_request.version
     *
     * @mbggenerated
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.platform
     *
     * @return the value of s_l_alarm_request.platform
     *
     * @mbggenerated
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.platform
     *
     * @param platform the value for s_l_alarm_request.platform
     *
     * @mbggenerated
     */
    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.cdate
     *
     * @return the value of s_l_alarm_request.cdate
     *
     * @mbggenerated
     */
    public Date getCdate() {
        return cdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.cdate
     *
     * @param cdate the value for s_l_alarm_request.cdate
     *
     * @mbggenerated
     */
    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.result
     *
     * @return the value of s_l_alarm_request.result
     *
     * @mbggenerated
     */
    public String getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.result
     *
     * @param result the value for s_l_alarm_request.result
     *
     * @mbggenerated
     */
    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_l_alarm_request.userid
     *
     * @return the value of s_l_alarm_request.userid
     *
     * @mbggenerated
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_l_alarm_request.userid
     *
     * @param userid the value for s_l_alarm_request.userid
     *
     * @mbggenerated
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}