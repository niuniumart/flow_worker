package com.zdf.client.data;


public class AsyncFlowTask {
    
    private Long id;
    
    private String user_id; //NOT NULL DEFAULT '',
    
    private Long task_id; // NOT NULL DEFAULT '',
    
    private String task_type; //NOT NULL DEFAULT '',  存储任务的全类名
    
    private String task_stage; //NOT NULL DEFAULT '', 存储任务阶段信息
    
    private int status; //tinyint(3) unsigned NOT NULL DEFAULT '0',
    
    private int crt_retry_num; //NOT NULL DEFAULT '0' COMMENT '已经重试几次了',
    
    private int max_retry_num; //NOT NULL DEFAULT '0' COMMENT '最大能重试几次',
    
    private int max_retry_interval;// int(11) NOT NULL DEFAULT '0' COMMENT '最大重试间隔',
    
    private String schedule_log;// varchar(4096) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '调度信息记录',
    
    private String task_context;// varchar(8192) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务上下文，用户自定义',
    
    private Long create_time;// datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    private Long modify_time;// datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_stage() {
        return task_stage;
    }

    public void setTask_stage(String task_stage) {
        this.task_stage = task_stage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCrt_retry_num() {
        return crt_retry_num;
    }

    public void setCrt_retry_num(int crt_retry_num) {
        this.crt_retry_num = crt_retry_num;
    }

    public int getMax_retry_num() {
        return max_retry_num;
    }

    public void setMax_retry_num(int max_retry_num) {
        this.max_retry_num = max_retry_num;
    }

    public int getMax_retry_interval() {
        return max_retry_interval;
    }

    public void setMax_retry_interval(int max_retry_interval) {
        this.max_retry_interval = max_retry_interval;
    }

    public String getSchedule_log() {
        return schedule_log;
    }

    public void setSchedule_log(String schedule_log) {
        this.schedule_log = schedule_log;
    }

    public String getTask_context() {
        return task_context;
    }

    public void setTask_context(String task_context) {
        this.task_context = task_context;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getModify_time() {
        return modify_time;
    }

    public void setModify_time(Long modify_time) {
        this.modify_time = modify_time;
    }
}
