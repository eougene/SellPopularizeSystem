package com.yd.org.sellpopularizesystem.javaBean;

import java.util.List;

/**
 * Created by hejin on 2017/3/13.
 */

public class AnnouncementBean {

    /**
     * code : 1
     * msg : 成功获取公告列表
     * total_number : 2
     * result : [{"notice_id":6,"company_id":1,"cate_id":3,"notice_title":"efewfgrgrsg","push_type":2,"content":"rgwqgwertew","add_time":1484888824,"add_admin":4,"add_ip":"117.84.183.178","status":0,"id":18,"user_id":100014,"is_read":0,"sort":99},{"notice_id":8,"company_id":1,"cate_id":2,"notice_title":"寄意寒星荃不察","push_type":1,"content":"非法俄反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复吩咐","add_time":1487403022,"add_admin":4,"add_ip":"117.84.182.119","status":0,"id":21,"user_id":100014,"is_read":0,"sort":99}]
     */

    private String code;
    private String msg;
    private int total_number;
    private List<ResultBean> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * notice_id : 6
         * company_id : 1
         * cate_id : 3
         * notice_title : efewfgrgrsg
         * push_type : 2
         * content : rgwqgwertew
         * add_time : 1484888824
         * add_admin : 4
         * add_ip : 117.84.183.178
         * status : 0
         * id : 18
         * user_id : 100014
         * is_read : 0
         * sort : 99
         */

        private int notice_id;
        private int company_id;
        private int cate_id;
        private String notice_title;
        private int push_type;
        private String content;
        private int add_time;
        private int add_admin;
        private String add_ip;
        private int status;
        private int id;
        private int user_id;
        private int is_read;
        private int sort;

        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public String getNotice_title() {
            return notice_title;
        }

        public void setNotice_title(String notice_title) {
            this.notice_title = notice_title;
        }

        public int getPush_type() {
            return push_type;
        }

        public void setPush_type(int push_type) {
            this.push_type = push_type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getAdd_admin() {
            return add_admin;
        }

        public void setAdd_admin(int add_admin) {
            this.add_admin = add_admin;
        }

        public String getAdd_ip() {
            return add_ip;
        }

        public void setAdd_ip(String add_ip) {
            this.add_ip = add_ip;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
