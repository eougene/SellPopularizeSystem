package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by ${bai} on 17/2/7.
 */

public class CustomeDetailedBean extends Domine {

    /**
     * code : 1
     * msg : 成功获取客户信息
     * result : {"customer_id":76,"company_id":1,"customer_nick":null,"head_img":"public/uploads/head_img/170207/201702071030505941.jpg","true_name":"客户一","sex":0,"birth_date":0,"age":null,"mobile":"15345678901","province":"","city":"","area":"","address":null,"open_id":null,"wechat_number":null,"wechat_nick":"","wechat_img":"","qq_number":null,"e_mail":"","job":null,"income":0,"family_name":null,"family_relationship":null,"family_mobile":null,"sign_number":0,"card_id":null,"passport_id":null,"passport_country":null,"card_validity":null,"is_experience":0,"flight_date":null,"flight_number":null,"add_time":1484815607,"update_time":1486434650,"add_ip":"180.114.133.77","add_admin":"4","add_user":0,"is_del":0,"status":0}
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
         * customer_id : 76
         * company_id : 1
         * customer_nick : null
         * head_img : public/uploads/head_img/170207/201702071030505941.jpg
         * true_name : 客户一
         * sex : 0
         * birth_date : 0
         * age : null
         * mobile : 15345678901
         * province :
         * city :
         * area :
         * address : null
         * open_id : null
         * wechat_number : null
         * wechat_nick :
         * wechat_img :
         * qq_number : null
         * e_mail :
         * job : null
         * income : 0
         * family_name : null
         * family_relationship : null
         * family_mobile : null
         * sign_number : 0
         * card_id : null
         * passport_id : null
         * passport_country : null
         * card_validity : null
         * is_experience : 0
         * flight_date : null
         * flight_number : null
         * add_time : 1484815607
         * update_time : 1486434650
         * add_ip : 180.114.133.77
         * add_admin : 4
         * add_user : 0
         * is_del : 0
         * status : 0
         */

        private int customer_id;
        private int company_id;
        private Object customer_nick;
        private String head_img;
        private String true_name;
        private String first_name;
        private String en_name;
        private int sex;
        private int birth_date;
        private Object age;
        private String mobile;
        private String province;
        private String city;
        private String area;
        private Object address;
        private String zip_code;
        private Object open_id;
        private Object wechat_number;
        private String wechat_nick;
        private String wechat_img;
        private Object qq_number;
        private String e_mail;
        private Object job;
        private int income;
        private String surname;
        private Object family_name;
        private String family_first_name;
        private Object family_relationship;
        private Object family_mobile;
        private int sign_number;
        private Object card_id;
        private Object passport_id;
        private Object passport_country;
        private Object card_validity;
        private int is_experience;
        private Object flight_date;
        private Object flight_number;
        private int add_time;
        private int update_time;
        private String add_ip;
        private String add_admin;
        private int add_user;
        private int is_del;
        private int is_firb;
        private int status;
        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public Object getCustomer_nick() {
            return customer_nick;
        }

        public void setCustomer_nick(Object customer_nick) {
            this.customer_nick = customer_nick;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getTrue_name() {
            return true_name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(int birth_date) {
            this.birth_date = birth_date;
        }

        public Object getAge() {
            return age;
        }

        public void setAge(Object age) {
            this.age = age;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public Object getOpen_id() {
            return open_id;
        }

        public void setOpen_id(Object open_id) {
            this.open_id = open_id;
        }

        public Object getWechat_number() {
            return wechat_number;
        }

        public void setWechat_number(Object wechat_number) {
            this.wechat_number = wechat_number;
        }

        public String getWechat_nick() {
            return wechat_nick;
        }

        public void setWechat_nick(String wechat_nick) {
            this.wechat_nick = wechat_nick;
        }

        public String getWechat_img() {
            return wechat_img;
        }

        public void setWechat_img(String wechat_img) {
            this.wechat_img = wechat_img;
        }

        public Object getQq_number() {
            return qq_number;
        }

        public void setQq_number(Object qq_number) {
            this.qq_number = qq_number;
        }

        public String getE_mail() {
            return e_mail;
        }

        public void setE_mail(String e_mail) {
            this.e_mail = e_mail;
        }

        public Object getJob() {
            return job;
        }

        public void setJob(Object job) {
            this.job = job;
        }

        public int getIncome() {
            return income;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public void setIncome(int income) {
            this.income = income;
        }

        public Object getFamily_name() {
            return family_name;
        }

        public String getFamily_first_name() {
            return family_first_name;
        }

        public void setFamily_first_name(String family_first_name) {
            this.family_first_name = family_first_name;
        }

        public void setFamily_name(Object family_name) {
            this.family_name = family_name;
        }

        public Object getFamily_relationship() {
            return family_relationship;
        }

        public void setFamily_relationship(Object family_relationship) {
            this.family_relationship = family_relationship;
        }

        public Object getFamily_mobile() {
            return family_mobile;
        }

        public void setFamily_mobile(Object family_mobile) {
            this.family_mobile = family_mobile;
        }

        public int getSign_number() {
            return sign_number;
        }

        public void setSign_number(int sign_number) {
            this.sign_number = sign_number;
        }

        public Object getCard_id() {
            return card_id;
        }

        public void setCard_id(Object card_id) {
            this.card_id = card_id;
        }

        public Object getPassport_id() {
            return passport_id;
        }

        public void setPassport_id(Object passport_id) {
            this.passport_id = passport_id;
        }

        public Object getPassport_country() {
            return passport_country;
        }

        public void setPassport_country(Object passport_country) {
            this.passport_country = passport_country;
        }

        public Object getCard_validity() {
            return card_validity;
        }

        public void setCard_validity(Object card_validity) {
            this.card_validity = card_validity;
        }

        public int getIs_experience() {
            return is_experience;
        }

        public void setIs_experience(int is_experience) {
            this.is_experience = is_experience;
        }

        public Object getFlight_date() {
            return flight_date;
        }

        public void setFlight_date(Object flight_date) {
            this.flight_date = flight_date;
        }

        public Object getFlight_number() {
            return flight_number;
        }

        public void setFlight_number(Object flight_number) {
            this.flight_number = flight_number;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
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

        public int getAdd_user() {
            return add_user;
        }

        public void setAdd_user(int add_user) {
            this.add_user = add_user;
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
