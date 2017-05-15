package com.yd.org.sellpopularizesystem.javaBean;

import java.util.List;

/**
 * Created by hejin on 2017/5/11.
 */

public class EoilistBean {

    /**
     * code : 1
     * msg : 成功获取EOI列表
     * total_number : 3
     * result : [{"product_eois_id":5,"company_id":"1","eoi_type":3,"property_id":"","product_child_id":"0","bedroom":"0","bathroom":"0","car_space":"0","purchaseReason":"","price":"","currency":"AU","client":3684,"is_firb":0,"sales_id":100048,"payment_method":4,"payment_amount":"300.00","add_time":"1494482355","eoi_money_url":"10","pay_time":"516175154.","eoi_money_upload_time":"1494482355","eoi_moneycheck_time":"","eoi_money_status":1,"cancel_apply_time":0,"cancel_time":"","cancel_apply_status":0,"complete_time":"","use_time":0,"is_use":0,"remark":"","old_status":"","status":0,"customer_surname":"Liu","customer_first_name":"Peng","customer_en_name":"Aaron","product_info":null},{"product_eois_id":2,"company_id":"1","eoi_type":3,"property_id":"","product_child_id":"0","bedroom":"0","bathroom":"0","car_space":"0","purchaseReason":"","price":"","currency":"RMB","client":3684,"is_firb":0,"sales_id":100048,"payment_method":6,"payment_amount":"300.00","add_time":"1494481134","eoi_money_url":"7","pay_time":"516173933.","eoi_money_upload_time":"1494481134","eoi_moneycheck_time":"","eoi_money_status":1,"cancel_apply_time":0,"cancel_time":"","cancel_apply_status":0,"complete_time":"","use_time":0,"is_use":0,"remark":"","old_status":"","status":0,"customer_surname":"Liu","customer_first_name":"Peng","customer_en_name":"Aaron","product_info":null},{"product_eois_id":1,"company_id":"1","eoi_type":3,"property_id":"","product_child_id":"0","bedroom":"0","bathroom":"0","car_space":"0","purchaseReason":"","price":"","currency":"RMB","client":3684,"is_firb":0,"sales_id":100048,"payment_method":6,"payment_amount":"300.00","add_time":"1494402564","eoi_money_url":"4","pay_time":"516095762.","eoi_money_upload_time":"1494402564","eoi_moneycheck_time":"1494405179","eoi_money_status":2,"cancel_apply_time":1494405192,"cancel_time":"","cancel_apply_status":1,"complete_time":"","use_time":0,"is_use":0,"remark":"","old_status":"0","status":0,"customer_surname":"Liu","customer_first_name":"Peng","customer_en_name":"Aaron","product_info":null}]
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

    public static class ResultBean {
        /**
         * product_eois_id : 5
         * company_id : 1
         * eoi_type : 3
         * property_id :
         * product_child_id : 0
         * bedroom : 0
         * bathroom : 0
         * car_space : 0
         * purchaseReason :
         * price :
         * currency : AU
         * client : 3684
         * is_firb : 0
         * sales_id : 100048
         * payment_method : 4
         * payment_amount : 300.00
         * add_time : 1494482355
         * eoi_money_url : 10
         * pay_time : 516175154.
         * eoi_money_upload_time : 1494482355
         * eoi_moneycheck_time :
         * eoi_money_status : 1
         * cancel_apply_time : 0
         * cancel_time :
         * cancel_apply_status : 0
         * complete_time :
         * use_time : 0
         * is_use : 0
         * remark :
         * old_status :
         * status : 0
         * customer_surname : Liu
         * customer_first_name : Peng
         * customer_en_name : Aaron
         * product_info : null
         */

        private int product_eois_id;
        private String company_id;
        private int eoi_type;
        private String property_id;
        private String product_child_id;
        private String bedroom;
        private String bathroom;
        private String car_space;
        private String purchaseReason;
        private String price;
        private String currency;
        private int client;
        private int is_firb;
        private int sales_id;
        private int payment_method;
        private String payment_amount;
        private String add_time;
        private String eoi_money_url;
        private String pay_time;
        private String eoi_money_upload_time;
        private String eoi_moneycheck_time;
        private int eoi_money_status;
        private int cancel_apply_time;
        private String cancel_time;
        private int cancel_apply_status;
        private String complete_time;
        private int use_time;
        private int is_use;
        private String remark;
        private String old_status;
        private int status;
        private String customer_surname;
        private String customer_first_name;
        private String customer_en_name;
        private Object product_info;

        public int getProduct_eois_id() {
            return product_eois_id;
        }

        public void setProduct_eois_id(int product_eois_id) {
            this.product_eois_id = product_eois_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public int getEoi_type() {
            return eoi_type;
        }

        public void setEoi_type(int eoi_type) {
            this.eoi_type = eoi_type;
        }

        public String getProperty_id() {
            return property_id;
        }

        public void setProperty_id(String property_id) {
            this.property_id = property_id;
        }

        public String getProduct_child_id() {
            return product_child_id;
        }

        public void setProduct_child_id(String product_child_id) {
            this.product_child_id = product_child_id;
        }

        public String getBedroom() {
            return bedroom;
        }

        public void setBedroom(String bedroom) {
            this.bedroom = bedroom;
        }

        public String getBathroom() {
            return bathroom;
        }

        public void setBathroom(String bathroom) {
            this.bathroom = bathroom;
        }

        public String getCar_space() {
            return car_space;
        }

        public void setCar_space(String car_space) {
            this.car_space = car_space;
        }

        public String getPurchaseReason() {
            return purchaseReason;
        }

        public void setPurchaseReason(String purchaseReason) {
            this.purchaseReason = purchaseReason;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getClient() {
            return client;
        }

        public void setClient(int client) {
            this.client = client;
        }

        public int getIs_firb() {
            return is_firb;
        }

        public void setIs_firb(int is_firb) {
            this.is_firb = is_firb;
        }

        public int getSales_id() {
            return sales_id;
        }

        public void setSales_id(int sales_id) {
            this.sales_id = sales_id;
        }

        public int getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(int payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getEoi_money_url() {
            return eoi_money_url;
        }

        public void setEoi_money_url(String eoi_money_url) {
            this.eoi_money_url = eoi_money_url;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getEoi_money_upload_time() {
            return eoi_money_upload_time;
        }

        public void setEoi_money_upload_time(String eoi_money_upload_time) {
            this.eoi_money_upload_time = eoi_money_upload_time;
        }

        public String getEoi_moneycheck_time() {
            return eoi_moneycheck_time;
        }

        public void setEoi_moneycheck_time(String eoi_moneycheck_time) {
            this.eoi_moneycheck_time = eoi_moneycheck_time;
        }

        public int getEoi_money_status() {
            return eoi_money_status;
        }

        public void setEoi_money_status(int eoi_money_status) {
            this.eoi_money_status = eoi_money_status;
        }

        public int getCancel_apply_time() {
            return cancel_apply_time;
        }

        public void setCancel_apply_time(int cancel_apply_time) {
            this.cancel_apply_time = cancel_apply_time;
        }

        public String getCancel_time() {
            return cancel_time;
        }

        public void setCancel_time(String cancel_time) {
            this.cancel_time = cancel_time;
        }

        public int getCancel_apply_status() {
            return cancel_apply_status;
        }

        public void setCancel_apply_status(int cancel_apply_status) {
            this.cancel_apply_status = cancel_apply_status;
        }

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        public int getUse_time() {
            return use_time;
        }

        public void setUse_time(int use_time) {
            this.use_time = use_time;
        }

        public int getIs_use() {
            return is_use;
        }

        public void setIs_use(int is_use) {
            this.is_use = is_use;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOld_status() {
            return old_status;
        }

        public void setOld_status(String old_status) {
            this.old_status = old_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCustomer_surname() {
            return customer_surname;
        }

        public void setCustomer_surname(String customer_surname) {
            this.customer_surname = customer_surname;
        }

        public String getCustomer_first_name() {
            return customer_first_name;
        }

        public void setCustomer_first_name(String customer_first_name) {
            this.customer_first_name = customer_first_name;
        }

        public String getCustomer_en_name() {
            return customer_en_name;
        }

        public void setCustomer_en_name(String customer_en_name) {
            this.customer_en_name = customer_en_name;
        }

        public Object getProduct_info() {
            return product_info;
        }

        public void setProduct_info(Object product_info) {
            this.product_info = product_info;
        }
    }
}
