package com.yd.org.sellpopularizesystem.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by e-dot on 2017/5/2.
 */

public class CommissionBean extends Domine {


    /**
     * code : 1
     * msg : 成功获取佣金列表
     * total_number : 1
     * result : [{"id":128,"company_id":1,"user_id":402,"true_name":"","user_first_name":"android","user_surname":"android","user_en_name":"android","customer_id":1217,"customer_first_name":"android","customer_surname":"android","customer_en_name":"android","order_id":1756,"product_id":7,"product_childs_id":69,"product_name":"APT- Rousehill Stage 3 - Building F","product_childs_lot_number":"125","product_childs_unit_number":"F305","order_price":"700000.00","commossion":"10395.00","gst":"1155.00","total":"11550.00","first_money":"37.50","first_status":0,"first_time":0,"second_money":"37.50","second_status":0,"second_time":0,"third_money":"0.00","third_status":0,"third_time":0,"add_time":1498614297,"update_time":0,"is_value":0,"status":0,"first_commossion":"37.50","first_gst":"4.1666666666667","first_total":"41.666666666667","second_commossion":"37.50","second_gst":"4.1666666666667","second_total":"41.666666666667","third_commossion":"0.00","third_gst":"0","third_total":"0"}]
     */

    private int code;
    private String msg;
    private int total_number;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public static class ResultBean implements Serializable{
        /**
         * id : 128
         * company_id : 1
         * user_id : 402
         * true_name :
         * user_first_name : android
         * user_surname : android
         * user_en_name : android
         * customer_id : 1217
         * customer_first_name : android
         * customer_surname : android
         * customer_en_name : android
         * order_id : 1756
         * product_id : 7
         * product_childs_id : 69
         * product_name : APT- Rousehill Stage 3 - Building F
         * product_childs_lot_number : 125
         * product_childs_unit_number : F305
         * order_price : 700000.00
         * commossion : 10395.00
         * gst : 1155.00
         * total : 11550.00
         * first_money : 37.50
         * first_status : 0
         * first_time : 0
         * second_money : 37.50
         * second_status : 0
         * second_time : 0
         * third_money : 0.00
         * third_status : 0
         * third_time : 0
         * add_time : 1498614297
         * update_time : 0
         * is_value : 0
         * status : 0
         * first_commossion : 37.50
         * first_gst : 4.1666666666667
         * first_total : 41.666666666667
         * second_commossion : 37.50
         * second_gst : 4.1666666666667
         * second_total : 41.666666666667
         * third_commossion : 0.00
         * third_gst : 0
         * third_total : 0
         */

        private int id;
        private int company_id;
        private int user_id;
        private String true_name;
        private String user_first_name;
        private String user_surname;
        private String user_en_name;
        private int customer_id;
        private String customer_first_name;
        private String customer_surname;
        private String customer_en_name;
        private int order_id;
        private int product_id;
        private int product_childs_id;
        private String product_name;
        private String product_childs_lot_number;
        private String product_childs_unit_number;
        private String order_price;
        private String commossion;
        private String gst;
        private String total;
        private String first_money;
        private int first_status;
        private int first_time;
        private String second_money;
        private int second_status;
        private int second_time;
        private String third_money;
        private int third_status;
        private int third_time;
        private int add_time;
        private int update_time;
        private int is_value;
        private int status;
        private String first_commossion;
        private String first_gst;
        private String first_total;
        private String second_commossion;
        private String second_gst;
        private String second_total;
        private String third_commossion;
        private String third_gst;
        private String third_total;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getUser_first_name() {
            return user_first_name;
        }

        public void setUser_first_name(String user_first_name) {
            this.user_first_name = user_first_name;
        }

        public String getUser_surname() {
            return user_surname;
        }

        public void setUser_surname(String user_surname) {
            this.user_surname = user_surname;
        }

        public String getUser_en_name() {
            return user_en_name;
        }

        public void setUser_en_name(String user_en_name) {
            this.user_en_name = user_en_name;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public String getCustomer_first_name() {
            return customer_first_name;
        }

        public void setCustomer_first_name(String customer_first_name) {
            this.customer_first_name = customer_first_name;
        }

        public String getCustomer_surname() {
            return customer_surname;
        }

        public void setCustomer_surname(String customer_surname) {
            this.customer_surname = customer_surname;
        }

        public String getCustomer_en_name() {
            return customer_en_name;
        }

        public void setCustomer_en_name(String customer_en_name) {
            this.customer_en_name = customer_en_name;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getProduct_childs_id() {
            return product_childs_id;
        }

        public void setProduct_childs_id(int product_childs_id) {
            this.product_childs_id = product_childs_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_childs_lot_number() {
            return product_childs_lot_number;
        }

        public void setProduct_childs_lot_number(String product_childs_lot_number) {
            this.product_childs_lot_number = product_childs_lot_number;
        }

        public String getProduct_childs_unit_number() {
            return product_childs_unit_number;
        }

        public void setProduct_childs_unit_number(String product_childs_unit_number) {
            this.product_childs_unit_number = product_childs_unit_number;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getCommossion() {
            return commossion;
        }

        public void setCommossion(String commossion) {
            this.commossion = commossion;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFirst_money() {
            return first_money;
        }

        public void setFirst_money(String first_money) {
            this.first_money = first_money;
        }

        public int getFirst_status() {
            return first_status;
        }

        public void setFirst_status(int first_status) {
            this.first_status = first_status;
        }

        public int getFirst_time() {
            return first_time;
        }

        public void setFirst_time(int first_time) {
            this.first_time = first_time;
        }

        public String getSecond_money() {
            return second_money;
        }

        public void setSecond_money(String second_money) {
            this.second_money = second_money;
        }

        public int getSecond_status() {
            return second_status;
        }

        public void setSecond_status(int second_status) {
            this.second_status = second_status;
        }

        public int getSecond_time() {
            return second_time;
        }

        public void setSecond_time(int second_time) {
            this.second_time = second_time;
        }

        public String getThird_money() {
            return third_money;
        }

        public void setThird_money(String third_money) {
            this.third_money = third_money;
        }

        public int getThird_status() {
            return third_status;
        }

        public void setThird_status(int third_status) {
            this.third_status = third_status;
        }

        public int getThird_time() {
            return third_time;
        }

        public void setThird_time(int third_time) {
            this.third_time = third_time;
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

        public int getIs_value() {
            return is_value;
        }

        public void setIs_value(int is_value) {
            this.is_value = is_value;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFirst_commossion() {
            return first_commossion;
        }

        public void setFirst_commossion(String first_commossion) {
            this.first_commossion = first_commossion;
        }

        public String getFirst_gst() {
            return first_gst;
        }

        public void setFirst_gst(String first_gst) {
            this.first_gst = first_gst;
        }

        public String getFirst_total() {
            return first_total;
        }

        public void setFirst_total(String first_total) {
            this.first_total = first_total;
        }

        public String getSecond_commossion() {
            return second_commossion;
        }

        public void setSecond_commossion(String second_commossion) {
            this.second_commossion = second_commossion;
        }

        public String getSecond_gst() {
            return second_gst;
        }

        public void setSecond_gst(String second_gst) {
            this.second_gst = second_gst;
        }

        public String getSecond_total() {
            return second_total;
        }

        public void setSecond_total(String second_total) {
            this.second_total = second_total;
        }

        public String getThird_commossion() {
            return third_commossion;
        }

        public void setThird_commossion(String third_commossion) {
            this.third_commossion = third_commossion;
        }

        public String getThird_gst() {
            return third_gst;
        }

        public void setThird_gst(String third_gst) {
            this.third_gst = third_gst;
        }

        public String getThird_total() {
            return third_total;
        }

        public void setThird_total(String third_total) {
            this.third_total = third_total;
        }
    }
}
