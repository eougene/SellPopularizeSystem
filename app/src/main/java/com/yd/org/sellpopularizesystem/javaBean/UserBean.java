package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by bai on 2017/1/13.
 */

public class UserBean extends Domine {

    /**
     * code : 1
     * msg : 登录成功！
     * result : {"user_id":12,"company_id":1,"user_nick":"peng.bai22","account":"peng01","password":"81dc9bdb52d04dc20036dbd8313ed055","team_id":3,"true_name":"peng.bai","sex":1,"mobile":"12345678901","e_mail":"","wechat":"","facebook":"","twitter":"","client_id":"","last_login_time":null,"last_login_ip":null,"add_time":1483950905,"add_ip":"116.231.51.142","add_admin":"7","update_time":1484291540,"is_del":0,"status":0}
     */

    private String code;
    private String msg;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        /**
         * user_id : 100014
         * company_id : 1
         * user_nick : user004
         * account : user004
         * password : 5aa9799b09196788bcb24cdaf115e189
         * team_id : 1
         * leader_level : 0
         * tjruserid :
         * true_name :
         * first_name : 白鹏
         * surname : 999
         * sex : 1
         * mobile : 18217608367
         * e_mail : 1191884034@qq.com
         * personal_email :
         * wechat : oOGJXwckao4Jo67kYr3TNUN4hGRg
         * facebook :
         * twitter :
         * default_language : cn
         * client_id : f40e94ddfba0de4fd74fc8f7816551f0
         * last_login_time : null
         * last_login_ip : null
         * add_time : 1484877842
         * add_ip : 116.231.51.142
         * add_admin : 7
         * update_time : 1490340214
         * is_del : 0
         * is_firb : 0
         * status : 0
         */

        private int user_id;
        private int company_id;
        private String user_nick;
        private String account;
        private String password;
        private int team_id;
        private int leader_level;
        private String tjruserid;
        private String true_name;
        private String first_name;
        private String surname;
        private int sex;
        private String mobile;
        private String e_mail;
        private String personal_email;
        private String wechat;
        private String facebook;
        private String twitter;
        private String default_language;
        private String client_id;
        private Object last_login_time;
        private Object last_login_ip;
        private int add_time;
        private String add_ip;
        private String add_admin;
        private int update_time;
        private int is_del;
        private int is_firb;
        private int status;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public String getUser_nick() {
            return user_nick;
        }

        public void setUser_nick(String user_nick) {
            this.user_nick = user_nick;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getTeam_id() {
            return team_id;
        }

        public void setTeam_id(int team_id) {
            this.team_id = team_id;
        }

        public int getLeader_level() {
            return leader_level;
        }

        public void setLeader_level(int leader_level) {
            this.leader_level = leader_level;
        }

        public String getTjruserid() {
            return tjruserid;
        }

        public void setTjruserid(String tjruserid) {
            this.tjruserid = tjruserid;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getE_mail() {
            return e_mail;
        }

        public void setE_mail(String e_mail) {
            this.e_mail = e_mail;
        }

        public String getPersonal_email() {
            return personal_email;
        }

        public void setPersonal_email(String personal_email) {
            this.personal_email = personal_email;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getDefault_language() {
            return default_language;
        }

        public void setDefault_language(String default_language) {
            this.default_language = default_language;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public Object getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(Object last_login_time) {
            this.last_login_time = last_login_time;
        }

        public Object getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(Object last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public String getAdd_ip() {
            return add_ip;
        }

        public void setAdd_ip(String add_ip) {
            this.add_ip = add_ip;
        }

        public String getAdd_admin() {
            return add_admin;
        }

        public void setAdd_admin(String add_admin) {
            this.add_admin = add_admin;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public int getIs_firb() {
            return is_firb;
        }

        public void setIs_firb(int is_firb) {
            this.is_firb = is_firb;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
