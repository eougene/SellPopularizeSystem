package com.yd.org.sellpopularizesystem.javaBean;

import java.util.List;

/**
 * Created by hejin on 2017/3/13.
 */

public class AnnouncementBean {

    /**
     * code : 1
     * msg : 成功获取公告列表
     * total_number : 1
     * result : [{"id":61,"cate_id":4,"notice_info_id":0,"user_id":"100014","client_id":"6a983a60fdc032a8167c506c485417bda88a58c6af55e35b9cc7f9a304e493a5","title":"订单创建成功，订单号为：172","content":"","status":1,"is_xunhuan":0,"xunhuan_time":0,"add_time":1493344283,"fasong_time":0,"read_time":1493361005,"about_id":172,"about_table":"product_orders","is_read":1,"is_del":0}]
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
         * id : 61
         * cate_id : 4
         * notice_info_id : 0
         * user_id : 100014
         * client_id : 6a983a60fdc032a8167c506c485417bda88a58c6af55e35b9cc7f9a304e493a5
         * title : 订单创建成功，订单号为：172
         * content :
         * status : 1
         * is_xunhuan : 0
         * xunhuan_time : 0
         * add_time : 1493344283
         * fasong_time : 0
         * read_time : 1493361005
         * about_id : 172
         * about_table : product_orders
         * is_read : 1
         * is_del : 0
         */

        private int id;
        private int cate_id;
        private int notice_info_id;
        private String user_id;
        private String client_id;
        private String title;
        private String content;
        private int status;
        private int is_xunhuan;
        private int xunhuan_time;
        private int add_time;
        private int fasong_time;
        private int read_time;
        private int about_id;
        private String about_table;
        private int is_read;
        private int is_del;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getNotice_info_id() {
            return notice_info_id;
        }

        public void setNotice_info_id(int notice_info_id) {
            this.notice_info_id = notice_info_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_xunhuan() {
            return is_xunhuan;
        }

        public void setIs_xunhuan(int is_xunhuan) {
            this.is_xunhuan = is_xunhuan;
        }

        public int getXunhuan_time() {
            return xunhuan_time;
        }

        public void setXunhuan_time(int xunhuan_time) {
            this.xunhuan_time = xunhuan_time;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getFasong_time() {
            return fasong_time;
        }

        public void setFasong_time(int fasong_time) {
            this.fasong_time = fasong_time;
        }

        public int getRead_time() {
            return read_time;
        }

        public void setRead_time(int read_time) {
            this.read_time = read_time;
        }

        public int getAbout_id() {
            return about_id;
        }

        public void setAbout_id(int about_id) {
            this.about_id = about_id;
        }

        public String getAbout_table() {
            return about_table;
        }

        public void setAbout_table(String about_table) {
            this.about_table = about_table;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }
    }
}
