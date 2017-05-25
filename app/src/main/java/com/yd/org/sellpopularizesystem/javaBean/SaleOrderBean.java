package com.yd.org.sellpopularizesystem.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hejin on 2017/4/6.
 */

public class SaleOrderBean extends Domine {

    /**
     * code : 1
     * msg : 成功获取订单列表
     * total_number : 4
     * result : [{"product_orders_id":94,"company_id":"1","order_type":1,"purchaseReason":"未知","property_id":31,"price":"123.00","currency":"au","client":76,"is_firb":0,"client_type":1,"sales_id":100014,"lawyer_id":5,"lawyer_name":"自然好味道","lawyer_tel":"4008006465","lawyer_email":"fsf@d.dd","payment_method":0,"payment_amount":"0.00","add_time":"1491449958","order_money_url":"public/uploads/order_money/170406/201704061139188277.jpg","pay_time":"1491449958","order_moneycheck_time":"","order_money_status":1,"contract_apply_time":0,"contract_apply_check_time":0,"contract_apply_status":0,"buy_money_url":"","buy_money_add_time":"","buy_money_check_time":"","buy_money_status":0,"contract_url":"","contract_add_time":"","contract_check_time":"","upload_contract_status":0,"cancel_apply_time":0,"cancel_time":"","cancel_apply_status":0,"complete_time":"","remark":"","old_status":"","status":0,"product_name":"3303","customer_surname":"liu","customer_first_name":"jin","customer_en_name":"hj","sale_advice_url":"https://www.wingaid.com/index.php/admin/outpdf/out_sales_advice_pdf?type=down&order_id=94","product_info":{"product_childs_id":31,"product_id":33,"product_childs_lot_number":"3303330333033303","product_childs_unit_number":"33033303","company_id":1,"cate_id":4,"area":"4","bedroom":"2","bathroom":"1","car_space":"2","has_study":"2","ensuite":"3","level":"33","floor_type":"","aspect":"","internal":"3.00","external":"33.00","building_area":"33.00","land_size":"0.00","price":"123.00","vendor_price":"0.00","discount_amount":"0.00","land_vendor_price":"0.00","land_discount_amount":"0.00","house_vendor_price":"0.00","house_discount_amount":"0.00","is_gst":1,"sales_commission_type":1,"sales_commission_value":"0.00","commossion_first":"31.00","commossion_second":"37.00","commossion_third":"32.00","currency":"au","thumb":"public/uploads/product_childs_thumb/170320/201703201713433108.jpg","up_time":1490001223,"up_ip":"114.224.133.41","up_admin":4,"is_lock":1,"is_eoi":0,"status":0}},{"product_orders_id":93,"company_id":"1","order_type":1,"purchaseReason":"Unknown","property_id":1895,"price":"0.00","currency":"au","client":76,"is_firb":0,"client_type":1,"sales_id":100014,"lawyer_id":1,"lawyer_name":"齐大大","lawyer_tel":"1234","lawyer_email":"15252132437@qq.com","payment_method":2,"payment_amount":"5000.00","add_time":"1491444952","order_money_url":"public/uploads/order_money/170406/201704061015527672.jpg","pay_time":"1491444952","order_moneycheck_time":"","order_money_status":1,"contract_apply_time":0,"contract_apply_check_time":0,"contract_apply_status":0,"buy_money_url":"","buy_money_add_time":"","buy_money_check_time":"","buy_money_status":0,"contract_url":"","contract_add_time":"","contract_check_time":"","upload_contract_status":0,"cancel_apply_time":0,"cancel_time":"","cancel_apply_status":0,"complete_time":"","remark":"","old_status":"","status":0,"product_name":"","customer_surname":"liu","customer_first_name":"jin","customer_en_name":"hj","sale_advice_url":"https://www.wingaid.com/index.php/admin/outpdf/out_sales_advice_pdf?type=down&order_id=93","product_info":{"product_childs_id":1895,"product_id":116,"product_childs_lot_number":"批号","product_childs_unit_number":"单元号","company_id":2,"cate_id":41,"area":"","bedroom":"1","bathroom":"2","car_space":"3","has_study":"1","ensuite":"4","level":"5","floor_type":"2","aspect":"E","internal":"78.98","external":"23.23","building_area":"123.20","land_size":"12.00","price":"0.00","vendor_price":"65000.00","discount_amount":"300.00","land_vendor_price":"0.00","land_discount_amount":"0.00","house_vendor_price":"0.00","house_discount_amount":"0.00","is_gst":1,"sales_commission_type":1,"sales_commission_value":"2.00","commossion_first":"1.00","commossion_second":"1.00","commossion_third":"1.00","currency":"au","thumb":"public/uploads/product_thumb/170327/201703270947058855.jpg","up_time":1490579061,"up_ip":"117.84.78.214","up_admin":4,"is_lock":1,"is_eoi":0,"status":0}},{"product_orders_id":92,"company_id":"1","order_type":1,"purchaseReason":"Unknown","property_id":1894,"price":"0.00","currency":"au","client":76,"is_firb":0,"client_type":1,"sales_id":100014,"lawyer_id":1,"lawyer_name":"齐大大","lawyer_tel":"1234","lawyer_email":"15252132437@qq.com","payment_method":2,"payment_amount":"5000.00","add_time":"1491442583","order_money_url":"public/uploads/order_money/170406/201704060936235683.jpg","pay_time":"1491442583","order_moneycheck_time":"","order_money_status":1,"contract_apply_time":0,"contract_apply_check_time":0,"contract_apply_status":0,"buy_money_url":"","buy_money_add_time":"","buy_money_check_time":"","buy_money_status":0,"contract_url":"","contract_add_time":"","contract_check_time":"","upload_contract_status":0,"cancel_apply_time":0,"cancel_time":"","cancel_apply_status":0,"complete_time":"","remark":"","old_status":"","status":0,"product_name":"","customer_surname":"liu","customer_first_name":"jin","customer_en_name":"hj","sale_advice_url":"https://www.wingaid.com/index.php/admin/outpdf/out_sales_advice_pdf?type=down&order_id=92","product_info":{"product_childs_id":1894,"product_id":112,"product_childs_lot_number":"sdgtgwae","product_childs_unit_number":"ewwet","company_id":2,"cate_id":3,"area":"","bedroom":"3","bathroom":"4","car_space":"2","has_study":"1","ensuite":"3","level":"1","floor_type":"1","aspect":"E","internal":"3243.00","external":"234.00","building_area":"23.00","land_size":"234.00","price":"0.00","vendor_price":"234.00","discount_amount":"23.00","land_vendor_price":"0.00","land_discount_amount":"0.00","house_vendor_price":"0.00","house_discount_amount":"0.00","is_gst":1,"sales_commission_type":1,"sales_commission_value":"0.00","commossion_first":"0.00","commossion_second":"0.00","commossion_third":"0.00","currency":"au","thumb":"","up_time":1490513757,"up_ip":"117.84.78.214","up_admin":4,"is_lock":1,"is_eoi":0,"status":0}},{"product_orders_id":91,"company_id":"1","order_type":1,"purchaseReason":"Investment","property_id":25,"price":"0.00","currency":"au","client":80,"is_firb":0,"client_type":1,"sales_id":100014,"lawyer_id":1,"lawyer_name":"齐大大","lawyer_tel":"1234","lawyer_email":"15252132437@qq.com","payment_method":3,"payment_amount":"5000.00","add_time":"1491014537","order_money_url":"public/uploads/order_money/170401/201704011042173925.jpg","pay_time":"1491014537","order_moneycheck_time":"","order_money_status":1,"contract_apply_time":0,"contract_apply_check_time":0,"contract_apply_status":0,"buy_money_url":"","buy_money_add_time":"","buy_money_check_time":"","buy_money_status":0,"contract_url":"","contract_add_time":"","contract_check_time":"","upload_contract_status":0,"cancel_apply_time":1491016938,"cancel_time":"","cancel_apply_status":1,"complete_time":"","remark":"{\"product_childs_id\":25,\"product_id\":9,\"product_childs_lot_number\":\"\\u8428\\u8fbe\\u8428\\u8fbe\",\"product_childs_unit_number\":\"\\u8428\\u8fbe\\u8428\\u8fbe\",\"company_id\":1,\"cate_id\":0,\"area\":\"\",\"bedroom\":\"1\",\"bathroom\":\"12\",\"car_space\":\"1\",\"has_study\":\"0\",\"ensuit","old_status":"0","status":0,"product_name":null,"customer_surname":"bai","customer_first_name":"peng","customer_en_name":"baipeng","sale_advice_url":"https://www.wingaid.com/index.php/admin/outpdf/out_sales_advice_pdf?type=down&order_id=91","product_info":{"product_childs_id":25,"product_id":9,"product_childs_lot_number":"萨达萨达","product_childs_unit_number":"萨达萨达","company_id":1,"cate_id":0,"area":"","bedroom":"1","bathroom":"12","car_space":"1","has_study":"0","ensuite":"0","level":"0","floor_type":"","aspect":"","internal":"0.00","external":"0.00","building_area":"0.00","land_size":"0.00","price":"0.00","vendor_price":"0.00","discount_amount":"0.00","land_vendor_price":"0.00","land_discount_amount":"0.00","house_vendor_price":"0.00","house_discount_amount":"0.00","is_gst":1,"sales_commission_type":1,"sales_commission_value":"0.00","commossion_first":"0.00","commossion_second":"0.00","commossion_third":"0.00","currency":"","thumb":"public/uploads/product_childs_thumb/170214/201702141710003562.png","up_time":1487063400,"up_ip":"117.84.78.157","up_admin":4,"is_lock":1,"is_eoi":0,"status":0}}]
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
         * product_orders_id : 94
         * company_id : 1
         * order_type : 1
         * purchaseReason : 未知
         * property_id : 31
         * price : 123.00
         * currency : au
         * client : 76
         * is_firb : 0
         * client_type : 1
         * sales_id : 100014
         * lawyer_id : 5
         * lawyer_name : 自然好味道
         * lawyer_tel : 4008006465
         * lawyer_email : fsf@d.dd
         * payment_method : 0
         * payment_amount : 0.00
         * add_time : 1491449958
         * order_money_url : public/uploads/order_money/170406/201704061139188277.jpg
         * pay_time : 1491449958
         * order_moneycheck_time :
         * order_money_status : 1
         * contract_apply_time : 0
         * contract_apply_check_time : 0
         * contract_apply_status : 0
         * buy_money_url :
         * buy_money_add_time :
         * buy_money_check_time :
         * buy_money_status : 0
         * contract_url :
         * contract_add_time :
         * contract_check_time :
         * upload_contract_status : 0
         * cancel_apply_time : 0
         * cancel_time :
         * cancel_apply_status : 0
         * complete_time :
         * remark :
         * old_status :
         * status : 0
         * product_name : 3303
         * customer_surname : liu
         * customer_first_name : jin
         * customer_en_name : hj
         * sale_advice_url : https://www.wingaid.com/index.php/admin/outpdf/out_sales_advice_pdf?type=down&order_id=94
         * product_info : {"product_childs_id":31,"product_id":33,"product_childs_lot_number":"3303330333033303","product_childs_unit_number":"33033303","company_id":1,"cate_id":4,"area":"4","bedroom":"2","bathroom":"1","car_space":"2","has_study":"2","ensuite":"3","level":"33","floor_type":"","aspect":"","internal":"3.00","external":"33.00","building_area":"33.00","land_size":"0.00","price":"123.00","vendor_price":"0.00","discount_amount":"0.00","land_vendor_price":"0.00","land_discount_amount":"0.00","house_vendor_price":"0.00","house_discount_amount":"0.00","is_gst":1,"sales_commission_type":1,"sales_commission_value":"0.00","commossion_first":"31.00","commossion_second":"37.00","commossion_third":"32.00","currency":"au","thumb":"public/uploads/product_childs_thumb/170320/201703201713433108.jpg","up_time":1490001223,"up_ip":"114.224.133.41","up_admin":4,"is_lock":1,"is_eoi":0,"status":0}
         */

        private int product_orders_id;
        private String company_id;
        private int order_type;
        private String product_id;
        private String eoi_id;
        private String purchaseReason;
        private int property_id;
        private String price;
        private String currency;
        private int client;
        private int is_firb;
        private int client_type;
        private int sales_id;
        private int lawyer_id;
        private String lawyer_name;
        private String lawyer_tel;
        private String lawyer_email;
        private int payment_method;
        private String payment_amount;
        private String add_time;
        private String order_money_url;
        private String pay_time;
        private String order_money_upload_time;
        private String order_moneycheck_time;
        private int order_money_status;
        private int sales_advice_is_true;
        private int sales_advice_status;
        private int contract_apply_time;
        private String contract_unsigned_url;
        private String unsign_upload_time;
        private int contract_apply_check_time;
        private int contract_apply_status;
        private String buy_money_url;
        private String buy_money_add_time;
        private String buy_money_check_time;
        private int buy_money_status;
        private String contract_url;
        private String contract_add_time;
        private String contract_check_time;
        private int upload_contract_status;
        private int cancel_apply_time;
        private String cancel_time;
        private int cancel_apply_status;
        private String complete_time;
        private String remark;
        private String old_status;
        private int status;
        private ProductName product_name;
        private String customer_surname;
        private String customer_first_name;
        private String customer_en_name;
        private String sale_advice_url;
        private ProductInfoBean product_info;

        public int getProduct_orders_id() {
            return product_orders_id;
        }

        public void setProduct_orders_id(int product_orders_id) {
            this.product_orders_id = product_orders_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getEoi_id() {
            return eoi_id;
        }

        public void setEoi_id(String eoi_id) {
            this.eoi_id = eoi_id;
        }

        public String getPurchaseReason() {
            return purchaseReason;
        }

        public void setPurchaseReason(String purchaseReason) {
            this.purchaseReason = purchaseReason;
        }

        public int getProperty_id() {
            return property_id;
        }

        public void setProperty_id(int property_id) {
            this.property_id = property_id;
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

        public int getClient_type() {
            return client_type;
        }

        public void setClient_type(int client_type) {
            this.client_type = client_type;
        }

        public int getSales_id() {
            return sales_id;
        }

        public void setSales_id(int sales_id) {
            this.sales_id = sales_id;
        }

        public int getLawyer_id() {
            return lawyer_id;
        }

        public void setLawyer_id(int lawyer_id) {
            this.lawyer_id = lawyer_id;
        }

        public String getLawyer_name() {
            return lawyer_name;
        }

        public void setLawyer_name(String lawyer_name) {
            this.lawyer_name = lawyer_name;
        }

        public String getLawyer_tel() {
            return lawyer_tel;
        }

        public void setLawyer_tel(String lawyer_tel) {
            this.lawyer_tel = lawyer_tel;
        }

        public String getLawyer_email() {
            return lawyer_email;
        }

        public void setLawyer_email(String lawyer_email) {
            this.lawyer_email = lawyer_email;
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

        public String getOrder_money_url() {
            return order_money_url;
        }

        public void setOrder_money_url(String order_money_url) {
            this.order_money_url = order_money_url;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getOrder_moneycheck_time() {
            return order_moneycheck_time;
        }

        public void setOrder_moneycheck_time(String order_moneycheck_time) {
            this.order_moneycheck_time = order_moneycheck_time;
        }

        public int getOrder_money_status() {
            return order_money_status;
        }

        public void setOrder_money_status(int order_money_status) {
            this.order_money_status = order_money_status;
        }

        public int getContract_apply_time() {
            return contract_apply_time;
        }

        public void setContract_apply_time(int contract_apply_time) {
            this.contract_apply_time = contract_apply_time;
        }

        public int getContract_apply_check_time() {
            return contract_apply_check_time;
        }

        public void setContract_apply_check_time(int contract_apply_check_time) {
            this.contract_apply_check_time = contract_apply_check_time;
        }

        public int getContract_apply_status() {
            return contract_apply_status;
        }

        public void setContract_apply_status(int contract_apply_status) {
            this.contract_apply_status = contract_apply_status;
        }

        public String getBuy_money_url() {
            return buy_money_url;
        }

        public void setBuy_money_url(String buy_money_url) {
            this.buy_money_url = buy_money_url;
        }

        public String getBuy_money_add_time() {
            return buy_money_add_time;
        }

        public void setBuy_money_add_time(String buy_money_add_time) {
            this.buy_money_add_time = buy_money_add_time;
        }

        public String getBuy_money_check_time() {
            return buy_money_check_time;
        }

        public void setBuy_money_check_time(String buy_money_check_time) {
            this.buy_money_check_time = buy_money_check_time;
        }

        public int getBuy_money_status() {
            return buy_money_status;
        }

        public void setBuy_money_status(int buy_money_status) {
            this.buy_money_status = buy_money_status;
        }

        public String getContract_url() {
            return contract_url;
        }

        public void setContract_url(String contract_url) {
            this.contract_url = contract_url;
        }

        public String getContract_add_time() {
            return contract_add_time;
        }

        public void setContract_add_time(String contract_add_time) {
            this.contract_add_time = contract_add_time;
        }

        public String getContract_check_time() {
            return contract_check_time;
        }

        public void setContract_check_time(String contract_check_time) {
            this.contract_check_time = contract_check_time;
        }

        public int getUpload_contract_status() {
            return upload_contract_status;
        }

        public void setUpload_contract_status(int upload_contract_status) {
            this.upload_contract_status = upload_contract_status;
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

        public ProductName getProduct_name() {
            return product_name;
        }

        public void setProduct_name(ProductName product_name) {
            this.product_name = product_name;
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

        public String getSale_advice_url() {
            return sale_advice_url;
        }

        public void setSale_advice_url(String sale_advice_url) {
            this.sale_advice_url = sale_advice_url;
        }

        public ProductInfoBean getProduct_info() {
            return product_info;
        }

        public void setProduct_info(ProductInfoBean product_info) {
            this.product_info = product_info;
        }

        public static class ProductInfoBean {
            /**
             * product_childs_id : 31
             * product_id : 33
             * product_childs_lot_number : 3303330333033303
             * product_childs_unit_number : 33033303
             * company_id : 1
             * cate_id : 4
             * area : 4
             * bedroom : 2
             * bathroom : 1
             * car_space : 2
             * has_study : 2
             * ensuite : 3
             * level : 33
             * floor_type :
             * aspect :
             * internal : 3.00
             * external : 33.00
             * building_area : 33.00
             * land_size : 0.00
             * price : 123.00
             * vendor_price : 0.00
             * discount_amount : 0.00
             * land_vendor_price : 0.00
             * land_discount_amount : 0.00
             * house_vendor_price : 0.00
             * house_discount_amount : 0.00
             * is_gst : 1
             * sales_commission_type : 1
             * sales_commission_value : 0.00
             * commossion_first : 31.00
             * commossion_second : 37.00
             * commossion_third : 32.00
             * currency : au
             * thumb : public/uploads/product_childs_thumb/170320/201703201713433108.jpg
             * up_time : 1490001223
             * up_ip : 114.224.133.41
             * up_admin : 4
             * is_lock : 1
             * is_eoi : 0
             * status : 0
             */

            private int product_childs_id;
            private int product_id;
            private String product_childs_lot_number;
            private String product_childs_unit_number;
            private int company_id;
            private int cate_id;
            private String area;
            private String bedroom;
            private String bathroom;
            private String car_space;
            private String has_study;
            private String ensuite;
            private String level;
            private String floor_type;
            private String aspect;
            private String internal;
            private String external;
            private String building_area;
            private String land_size;
            private String price;
            private String vendor_price;
            private String discount_amount;
            private String land_vendor_price;
            private String land_discount_amount;
            private String house_vendor_price;
            private String house_discount_amount;
            private int is_gst;
            private int sales_commission_type;
            private String sales_commission_value;
            private String commossion_first;
            private String commossion_second;
            private String commossion_third;
            private String currency;
            private String thumb;
            private int up_time;
            private String up_ip;
            private int up_admin;
            private int is_lock;
            private int is_eoi;
            private int status;

            public int getProduct_childs_id() {
                return product_childs_id;
            }

            public void setProduct_childs_id(int product_childs_id) {
                this.product_childs_id = product_childs_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
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

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
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

            public String getHas_study() {
                return has_study;
            }

            public void setHas_study(String has_study) {
                this.has_study = has_study;
            }

            public String getEnsuite() {
                return ensuite;
            }

            public void setEnsuite(String ensuite) {
                this.ensuite = ensuite;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getFloor_type() {
                return floor_type;
            }

            public void setFloor_type(String floor_type) {
                this.floor_type = floor_type;
            }

            public String getAspect() {
                return aspect;
            }

            public void setAspect(String aspect) {
                this.aspect = aspect;
            }

            public String getInternal() {
                return internal;
            }

            public void setInternal(String internal) {
                this.internal = internal;
            }

            public String getExternal() {
                return external;
            }

            public void setExternal(String external) {
                this.external = external;
            }

            public String getBuilding_area() {
                return building_area;
            }

            public void setBuilding_area(String building_area) {
                this.building_area = building_area;
            }

            public String getLand_size() {
                return land_size;
            }

            public void setLand_size(String land_size) {
                this.land_size = land_size;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getVendor_price() {
                return vendor_price;
            }

            public void setVendor_price(String vendor_price) {
                this.vendor_price = vendor_price;
            }

            public String getDiscount_amount() {
                return discount_amount;
            }

            public void setDiscount_amount(String discount_amount) {
                this.discount_amount = discount_amount;
            }

            public String getLand_vendor_price() {
                return land_vendor_price;
            }

            public void setLand_vendor_price(String land_vendor_price) {
                this.land_vendor_price = land_vendor_price;
            }

            public String getLand_discount_amount() {
                return land_discount_amount;
            }

            public void setLand_discount_amount(String land_discount_amount) {
                this.land_discount_amount = land_discount_amount;
            }

            public String getHouse_vendor_price() {
                return house_vendor_price;
            }

            public void setHouse_vendor_price(String house_vendor_price) {
                this.house_vendor_price = house_vendor_price;
            }

            public String getHouse_discount_amount() {
                return house_discount_amount;
            }

            public void setHouse_discount_amount(String house_discount_amount) {
                this.house_discount_amount = house_discount_amount;
            }

            public int getIs_gst() {
                return is_gst;
            }

            public void setIs_gst(int is_gst) {
                this.is_gst = is_gst;
            }

            public int getSales_commission_type() {
                return sales_commission_type;
            }

            public void setSales_commission_type(int sales_commission_type) {
                this.sales_commission_type = sales_commission_type;
            }

            public String getSales_commission_value() {
                return sales_commission_value;
            }

            public void setSales_commission_value(String sales_commission_value) {
                this.sales_commission_value = sales_commission_value;
            }

            public String getCommossion_first() {
                return commossion_first;
            }

            public void setCommossion_first(String commossion_first) {
                this.commossion_first = commossion_first;
            }

            public String getCommossion_second() {
                return commossion_second;
            }

            public void setCommossion_second(String commossion_second) {
                this.commossion_second = commossion_second;
            }

            public String getCommossion_third() {
                return commossion_third;
            }

            public void setCommossion_third(String commossion_third) {
                this.commossion_third = commossion_third;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public int getUp_time() {
                return up_time;
            }

            public void setUp_time(int up_time) {
                this.up_time = up_time;
            }

            public String getUp_ip() {
                return up_ip;
            }

            public void setUp_ip(String up_ip) {
                this.up_ip = up_ip;
            }

            public int getUp_admin() {
                return up_admin;
            }

            public void setUp_admin(int up_admin) {
                this.up_admin = up_admin;
            }

            public int getIs_lock() {
                return is_lock;
            }

            public void setIs_lock(int is_lock) {
                this.is_lock = is_lock;
            }

            public int getIs_eoi() {
                return is_eoi;
            }

            public void setIs_eoi(int is_eoi) {
                this.is_eoi = is_eoi;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
