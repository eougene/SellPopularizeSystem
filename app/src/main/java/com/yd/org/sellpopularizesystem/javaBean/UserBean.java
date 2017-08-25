package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by bai on 2017/1/13.
 */

public class UserBean extends Domine {


    /**
     * code : 1
     * msg : 登录成功！
     * result : {"user_id":504,"company_id":1,"user_nick":null,"account":"test_xu","password":"e10adc3949ba59abbe56e057f20f883e","team_id":12,"leader_level":0,"tjruserid":"503","true_name":"","first_name":"test-xu","surname":"test-xu","en_name":"test-xu","sex":1,"position":"","commossion":"1.50","mcr":"0.00","line_manager":"","partner":"","mentor":"","team_leader_1":"","team_leader_2":"","director":"503","mobile":"13300003333","e_mail":"test-xu@qq.com","personal_email":"test-xu@qq.com","home_phone":"","office_phone":"","business_title":"","join_date":"","tax_number":"","residence_status":"AUS","birth_date":"","unit_number":"","street_number":"","street_address_line_1":"","street_address_line_2":"","suburb":"","state":"","postcode":"","country":"","wechat":"","facebook":"","twitter":"","sales_type":1,"business_name":"","is_gst":1,"abn":"","acn":"","sales_logo":"","request_notes":"","admin_notes":"","default_language":"en","client_id":"f7b45de540686a58de29d7c173e32f6f","last_login_time":1499937047,"last_login_ip":null,"add_time":1499670131,"add_ip":"117.85.8.67","add_admin":"1","update_time":0,"is_del":0,"is_firb":0,"is_licence":1,"is_grant":0,"grant_time":"","is_login":1,"status":0}
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

    public static class ResultBean extends Domine{
        /**
         * user_id : 504
         * company_id : 1
         * user_nick : null
         * account : test_xu
         * password : e10adc3949ba59abbe56e057f20f883e
         * team_id : 12
         * leader_level : 0
         * tjruserid : 503
         * true_name :
         * first_name : test-xu
         * surname : test-xu
         * en_name : test-xu
         * sex : 1
         * position :
         * commossion : 1.50
         * mcr : 0.00
         * line_manager :
         * partner :
         * mentor :
         * team_leader_1 :
         * team_leader_2 :
         * director : 503
         * mobile : 13300003333
         * e_mail : test-xu@qq.com
         * personal_email : test-xu@qq.com
         * home_phone :
         * office_phone :
         * business_title :
         * join_date :
         * tax_number :
         * residence_status : AUS
         * birth_date :
         * unit_number :
         * street_number :
         * street_address_line_1 :
         * street_address_line_2 :
         * suburb :
         * state :
         * postcode :
         * country :
         * wechat :
         * facebook :
         * twitter :
         * sales_type : 1
         * business_name :
         * is_gst : 1
         * abn :
         * acn :
         * sales_logo :
         * request_notes :
         * admin_notes :
         * default_language : en
         * client_id : f7b45de540686a58de29d7c173e32f6f
         * last_login_time : 1499937047
         * last_login_ip : null
         * add_time : 1499670131
         * add_ip : 117.85.8.67
         * add_admin : 1
         * update_time : 0
         * is_del : 0
         * is_firb : 0
         * is_licence : 1
         * is_grant : 0
         * grant_time :
         * is_login : 1
         * status : 0
         */

        private int user_id;
        private int company_id;
        private Object user_nick;
        private String account;
        private String password;
        private int team_id;
        private int leader_level;
        private String tjruserid;
        private String true_name;
        private String first_name;
        private String surname;
        private String en_name;
        private int sex;
        private String position;
        private String commossion;
        private String mcr;
        private String line_manager;
        private String partner;
        private String mentor;
        private String team_leader_1;
        private String team_leader_2;
        private String director;
        private String mobile;
        private String e_mail;
        private String personal_email;
        private String home_phone;
        private String office_phone;
        private String business_title;
        private String join_date;
        private String tax_number;
        private String residence_status;
        private String birth_date;
        private String unit_number;
        private String street_number;
        private String street_address_line_1;
        private String street_address_line_2;
        private String suburb;
        private String state;
        private String postcode;
        private String country;
        private String wechat;
        private String facebook;
        private String twitter;
        private int sales_type;
        private String business_name;
        private int is_gst;
        private String abn;
        private String acn;
        private String sales_logo;
        private String request_notes;
        private String admin_notes;
        private String default_language;
        private String client_id;
        private int last_login_time;
        private Object last_login_ip;
        private int add_time;
        private String add_ip;
        private String add_admin;
        private int update_time;
        private int is_del;
        private int is_firb;
        private int is_licence;
        private int is_grant;
        private String grant_time;
        private int is_login;
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

        public Object getUser_nick() {
            return user_nick;
        }

        public void setUser_nick(Object user_nick) {
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

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCommossion() {
            return commossion;
        }

        public void setCommossion(String commossion) {
            this.commossion = commossion;
        }

        public String getMcr() {
            return mcr;
        }

        public void setMcr(String mcr) {
            this.mcr = mcr;
        }

        public String getLine_manager() {
            return line_manager;
        }

        public void setLine_manager(String line_manager) {
            this.line_manager = line_manager;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getMentor() {
            return mentor;
        }

        public void setMentor(String mentor) {
            this.mentor = mentor;
        }

        public String getTeam_leader_1() {
            return team_leader_1;
        }

        public void setTeam_leader_1(String team_leader_1) {
            this.team_leader_1 = team_leader_1;
        }

        public String getTeam_leader_2() {
            return team_leader_2;
        }

        public void setTeam_leader_2(String team_leader_2) {
            this.team_leader_2 = team_leader_2;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
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

        public String getHome_phone() {
            return home_phone;
        }

        public void setHome_phone(String home_phone) {
            this.home_phone = home_phone;
        }

        public String getOffice_phone() {
            return office_phone;
        }

        public void setOffice_phone(String office_phone) {
            this.office_phone = office_phone;
        }

        public String getBusiness_title() {
            return business_title;
        }

        public void setBusiness_title(String business_title) {
            this.business_title = business_title;
        }

        public String getJoin_date() {
            return join_date;
        }

        public void setJoin_date(String join_date) {
            this.join_date = join_date;
        }

        public String getTax_number() {
            return tax_number;
        }

        public void setTax_number(String tax_number) {
            this.tax_number = tax_number;
        }

        public String getResidence_status() {
            return residence_status;
        }

        public void setResidence_status(String residence_status) {
            this.residence_status = residence_status;
        }

        public String getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(String birth_date) {
            this.birth_date = birth_date;
        }

        public String getUnit_number() {
            return unit_number;
        }

        public void setUnit_number(String unit_number) {
            this.unit_number = unit_number;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getStreet_address_line_1() {
            return street_address_line_1;
        }

        public void setStreet_address_line_1(String street_address_line_1) {
            this.street_address_line_1 = street_address_line_1;
        }

        public String getStreet_address_line_2() {
            return street_address_line_2;
        }

        public void setStreet_address_line_2(String street_address_line_2) {
            this.street_address_line_2 = street_address_line_2;
        }

        public String getSuburb() {
            return suburb;
        }

        public void setSuburb(String suburb) {
            this.suburb = suburb;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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

        public int getSales_type() {
            return sales_type;
        }

        public void setSales_type(int sales_type) {
            this.sales_type = sales_type;
        }

        public String getBusiness_name() {
            return business_name;
        }

        public void setBusiness_name(String business_name) {
            this.business_name = business_name;
        }

        public int getIs_gst() {
            return is_gst;
        }

        public void setIs_gst(int is_gst) {
            this.is_gst = is_gst;
        }

        public String getAbn() {
            return abn;
        }

        public void setAbn(String abn) {
            this.abn = abn;
        }

        public String getAcn() {
            return acn;
        }

        public void setAcn(String acn) {
            this.acn = acn;
        }

        public String getSales_logo() {
            return sales_logo;
        }

        public void setSales_logo(String sales_logo) {
            this.sales_logo = sales_logo;
        }

        public String getRequest_notes() {
            return request_notes;
        }

        public void setRequest_notes(String request_notes) {
            this.request_notes = request_notes;
        }

        public String getAdmin_notes() {
            return admin_notes;
        }

        public void setAdmin_notes(String admin_notes) {
            this.admin_notes = admin_notes;
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

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
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

        public int getIs_licence() {
            return is_licence;
        }

        public void setIs_licence(int is_licence) {
            this.is_licence = is_licence;
        }

        public int getIs_grant() {
            return is_grant;
        }

        public void setIs_grant(int is_grant) {
            this.is_grant = is_grant;
        }

        public String getGrant_time() {
            return grant_time;
        }

        public void setGrant_time(String grant_time) {
            this.grant_time = grant_time;
        }

        public int getIs_login() {
            return is_login;
        }

        public void setIs_login(int is_login) {
            this.is_login = is_login;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
