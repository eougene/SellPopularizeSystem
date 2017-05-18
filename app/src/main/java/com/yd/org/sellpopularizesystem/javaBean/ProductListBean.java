package com.yd.org.sellpopularizesystem.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bai on 2017/1/18.
 */

public class ProductListBean extends Domine {

    /**
     * code : 1
     * msg : 成功获取产品列表
     * total_number : 6
     * result : [{"cate_name":"","product_id":23,"company_id":1,"cate_id":3,"thumb":"public/uploads/product_thumb/170213/201702131444588426.png","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1486968366,"up_ip":"114.224.147.223","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_name":"hello world","description":null,"childs":null},{"cate_name":"","product_id":23,"company_id":1,"cate_id":3,"thumb":"public/uploads/product_thumb/170213/201702131444588426.png","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1486968366,"up_ip":"114.224.147.223","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_name":"hello world","description":null,"childs":null},{"cate_name":"","product_id":16,"company_id":1,"cate_id":1,"thumb":"public/uploads/product_thumb/170120/201701201415092991.jpg","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1487038391,"up_ip":"114.224.147.223","up_admin":3,"sell_number":0,"sign_number":0,"status":0,"product_name":"erfererwqewqe","description":null,"childs":null},{"cate_name":"","product_id":16,"company_id":1,"cate_id":1,"thumb":"public/uploads/product_thumb/170120/201701201415092991.jpg","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1487038391,"up_ip":"114.224.147.223","up_admin":3,"sell_number":0,"sign_number":0,"status":0,"product_name":"erfererwqewqe","description":null,"childs":null},{"cate_name":"","product_id":15,"company_id":1,"cate_id":3,"thumb":"public/uploads/product_thumb/170120/201701201310233549.jpg","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1484889023,"up_ip":"117.84.183.178","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_name":"rwetawetsrg","description":null,"childs":null},{"cate_name":"","product_id":9,"company_id":1,"cate_id":2,"thumb":"public/uploads/product_thumb/170119/201701191534324505.jpg","provice":"","city":"","county":"","town":"","address":"","latitude":0,"longitude":0,"up_time":1484811272,"up_ip":"180.114.133.77","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_name":"好房不等人，快来抢房啦","description":null,"childs":[{"product_childs_id":26,"product_id":9,"product_childs_lot_number":"dsefew","product_childs_unit_number":"erew","company_id":1,"cate_id":3,"area":"3","bedroom":3,"bathroom":3,"car_space":4,"has_study":3,"ensuite":1,"level":34,"internal":"758.00","external":"45.00","building_area":"33.00","price":"234.00","thumb":"public/uploads/product_childs_thumb/170215/201702151505504262.png","up_time":1487142350,"up_ip":"117.84.78.157","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_childs_name":"人有两个宝","product_childs_description":"双手和大脑，，，双手会做工，大脑会思考"},{"product_childs_id":25,"product_id":9,"product_childs_lot_number":"萨达萨达","product_childs_unit_number":"萨达萨达","company_id":1,"cate_id":0,"area":"","bedroom":1,"bathroom":12,"car_space":1,"has_study":0,"ensuite":0,"level":0,"internal":"0.00","external":"0.00","building_area":"0.00","price":"0.00","thumb":"public/uploads/product_childs_thumb/170214/201702141710003562.png","up_time":1487063400,"up_ip":"117.84.78.157","up_admin":4,"sell_number":0,"sign_number":0,"status":0,"product_childs_name":"","product_childs_description":"第三方撒旦法"}]}]
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

    public static class ResultBean implements Serializable {

        /**
         * cate_name :
         * product_id : 9
         * company_id : 1
         * cate_id : 2
         * thumb : public/uploads/product_thumb/170119/201701191534324505.jpg
         * provice :
         * city :
         * county :
         * town :
         * address :
         * latitude : -33.9688197
         * longitude : 151.3092955
         * is_firb : 0
         * firb_number : 0
         * already_firb_number : 0
         * sales_method :
         * is_promote : 0
         * eoi_open_time : 0
         * constr_start_time : 0
         * constr_end_time : 0
         * start_sales_time : 0
         * stop_sales_time : 0
         * sunset_time : 0
         * settlement_time : 0
         * preview_memo :
         * product_type :
         * vendor :
         * land_vendor :
         * vendor_lawyer :
         * builder :
         * is_da : 0
         * da_number :
         * dp_number :
         * desposit_holder :
         * deposit_holder_others :
         * days_to_pay :
         * exchange_deposit : 0.00
         * firb_exchange_deposit : 0.00
         * min_reservation_fee : 0.00
         * is_gst_inc : 0
         * sa_vendor_email :
         * sa_vendor_names :
         * sa_sent_to :
         * sa_vs_contact_name :
         * sa_vs_contact_phone :
         * sa_vs_contact_fax :
         * sa_vs_contact_email :
         * street_number :
         * street_address_1 : 街道1
         * street_address_2 : 街道2
         * address_suburb : 郊区
         * state : 州
         * postcode : 21000
         * country : 国家
         * up_time : 1490176555
         * up_ip : 116.231.51.223
         * up_admin : 3
         * sell_number : 0
         * sign_number : 0
         * status : 0
         * product_name : 好房不等人，快来抢房啦
         * description : null
         */

        private String cate_name;
        private int product_id;
        private int company_id;
        private int cate_id;
        private String thumb;
        private String provice;
        private String city;
        private String county;
        private String town;
        private String address;
        private double latitude;
        private double longitude;
        private int is_firb;
        private int firb_number;
        private int already_firb_number;
        private String sales_method;
        private int is_promote;
        private int eoi_open_time;
        private int constr_start_time;
        private int constr_end_time;
        private int start_sales_time;
        private int stop_sales_time;
        private int sunset_time;
        private int settlement_time;
        private String preview_memo;
        private String product_type;
        private String vendor;
        private String land_vendor;
        private String vendor_lawyer;
        private String builder;
        private int is_da;
        private String da_number;
        private String dp_number;
        private String desposit_holder;
        private String deposit_holder_others;
        private String days_to_pay;
        private String exchange_deposit;
        private String firb_exchange_deposit;
        private String min_reservation_fee;
        private int is_gst_inc;
        private int is_gst;
        private String sales_commission_value;
        private String first_commission_type;
        private String commossion_first;
        private String second_commission_type;
        private String commossion_second;
        private String third_commission_type;
        private String commossion_third;
        private String sa_vendor_email;
        private String sa_vendor_names;
        private String sa_sent_to;
        private String sa_vs_contact_name;
        private String sa_vs_contact_phone;
        private String sa_vs_contact_fax;
        private String sa_vs_contact_email;
        private String street_number;
        private String street_address_1;
        private String street_address_2;
        private String address_suburb;
        private String state;
        private String postcode;
        private String country;
        private int is_study;
        private String study_id;
        private int up_time;
        private String up_ip;
        private int up_admin;
        private int sell_number;
        private int sign_number;
        private  String share_url;
        private int status;
        private String product_name;
        private Object description;
        private String is_can_sale;
        private List<ProSubUnitClassifyBean> childs;

        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
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

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getProvice() {
            return provice;
        }

        public void setProvice(String provice) {
            this.provice = provice;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getIs_firb() {
            return is_firb;
        }

        public void setIs_firb(int is_firb) {
            this.is_firb = is_firb;
        }

        public int getFirb_number() {
            return firb_number;
        }

        public void setFirb_number(int firb_number) {
            this.firb_number = firb_number;
        }

        public int getAlready_firb_number() {
            return already_firb_number;
        }

        public void setAlready_firb_number(int already_firb_number) {
            this.already_firb_number = already_firb_number;
        }

        public String getSales_method() {
            return sales_method;
        }

        public void setSales_method(String sales_method) {
            this.sales_method = sales_method;
        }

        public int getIs_promote() {
            return is_promote;
        }

        public void setIs_promote(int is_promote) {
            this.is_promote = is_promote;
        }

        public int getEoi_open_time() {
            return eoi_open_time;
        }

        public void setEoi_open_time(int eoi_open_time) {
            this.eoi_open_time = eoi_open_time;
        }

        public int getConstr_start_time() {
            return constr_start_time;
        }

        public void setConstr_start_time(int constr_start_time) {
            this.constr_start_time = constr_start_time;
        }

        public int getConstr_end_time() {
            return constr_end_time;
        }

        public void setConstr_end_time(int constr_end_time) {
            this.constr_end_time = constr_end_time;
        }

        public int getStart_sales_time() {
            return start_sales_time;
        }

        public void setStart_sales_time(int start_sales_time) {
            this.start_sales_time = start_sales_time;
        }

        public int getStop_sales_time() {
            return stop_sales_time;
        }

        public void setStop_sales_time(int stop_sales_time) {
            this.stop_sales_time = stop_sales_time;
        }

        public int getSunset_time() {
            return sunset_time;
        }

        public void setSunset_time(int sunset_time) {
            this.sunset_time = sunset_time;
        }

        public int getSettlement_time() {
            return settlement_time;
        }

        public void setSettlement_time(int settlement_time) {
            this.settlement_time = settlement_time;
        }

        public String getPreview_memo() {
            return preview_memo;
        }

        public void setPreview_memo(String preview_memo) {
            this.preview_memo = preview_memo;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getLand_vendor() {
            return land_vendor;
        }

        public void setLand_vendor(String land_vendor) {
            this.land_vendor = land_vendor;
        }

        public String getVendor_lawyer() {
            return vendor_lawyer;
        }

        public void setVendor_lawyer(String vendor_lawyer) {
            this.vendor_lawyer = vendor_lawyer;
        }

        public String getBuilder() {
            return builder;
        }

        public void setBuilder(String builder) {
            this.builder = builder;
        }

        public int getIs_da() {
            return is_da;
        }

        public void setIs_da(int is_da) {
            this.is_da = is_da;
        }

        public String getDa_number() {
            return da_number;
        }

        public void setDa_number(String da_number) {
            this.da_number = da_number;
        }

        public String getDp_number() {
            return dp_number;
        }

        public void setDp_number(String dp_number) {
            this.dp_number = dp_number;
        }

        public String getDesposit_holder() {
            return desposit_holder;
        }

        public void setDesposit_holder(String desposit_holder) {
            this.desposit_holder = desposit_holder;
        }

        public String getDeposit_holder_others() {
            return deposit_holder_others;
        }

        public void setDeposit_holder_others(String deposit_holder_others) {
            this.deposit_holder_others = deposit_holder_others;
        }

        public String getDays_to_pay() {
            return days_to_pay;
        }

        public void setDays_to_pay(String days_to_pay) {
            this.days_to_pay = days_to_pay;
        }

        public String getExchange_deposit() {
            return exchange_deposit;
        }

        public void setExchange_deposit(String exchange_deposit) {
            this.exchange_deposit = exchange_deposit;
        }

        public String getFirb_exchange_deposit() {
            return firb_exchange_deposit;
        }

        public void setFirb_exchange_deposit(String firb_exchange_deposit) {
            this.firb_exchange_deposit = firb_exchange_deposit;
        }

        public String getMin_reservation_fee() {
            return min_reservation_fee;
        }

        public void setMin_reservation_fee(String min_reservation_fee) {
            this.min_reservation_fee = min_reservation_fee;
        }

        public int getIs_gst_inc() {
            return is_gst_inc;
        }

        public void setIs_gst_inc(int is_gst_inc) {
            this.is_gst_inc = is_gst_inc;
        }

        public int getIs_gst() {
            return is_gst;
        }

        public void setIs_gst(int is_gst) {
            this.is_gst = is_gst;
        }

        public String getSales_commission_value() {
            return sales_commission_value;
        }

        public void setSales_commission_value(String sales_commission_value) {
            this.sales_commission_value = sales_commission_value;
        }

        public String getFirst_commission_type() {
            return first_commission_type;
        }

        public void setFirst_commission_type(String first_commission_type) {
            this.first_commission_type = first_commission_type;
        }

        public String getCommossion_first() {
            return commossion_first;
        }

        public void setCommossion_first(String commossion_first) {
            this.commossion_first = commossion_first;
        }

        public String getSecond_commission_type() {
            return second_commission_type;
        }

        public void setSecond_commission_type(String second_commission_type) {
            this.second_commission_type = second_commission_type;
        }

        public String getCommossion_second() {
            return commossion_second;
        }

        public void setCommossion_second(String commossion_second) {
            this.commossion_second = commossion_second;
        }

        public String getThird_commission_type() {
            return third_commission_type;
        }

        public void setThird_commission_type(String third_commission_type) {
            this.third_commission_type = third_commission_type;
        }

        public String getCommossion_third() {
            return commossion_third;
        }

        public void setCommossion_third(String commossion_third) {
            this.commossion_third = commossion_third;
        }

        public String getIs_can_sale() {
            return is_can_sale;
        }

        public void setIs_can_sale(String is_can_sale) {
            this.is_can_sale = is_can_sale;
        }

        public String getSa_vendor_email() {
            return sa_vendor_email;
        }

        public void setSa_vendor_email(String sa_vendor_email) {
            this.sa_vendor_email = sa_vendor_email;
        }

        public String getSa_vendor_names() {
            return sa_vendor_names;
        }

        public void setSa_vendor_names(String sa_vendor_names) {
            this.sa_vendor_names = sa_vendor_names;
        }

        public String getSa_sent_to() {
            return sa_sent_to;
        }

        public void setSa_sent_to(String sa_sent_to) {
            this.sa_sent_to = sa_sent_to;
        }

        public String getSa_vs_contact_name() {
            return sa_vs_contact_name;
        }

        public void setSa_vs_contact_name(String sa_vs_contact_name) {
            this.sa_vs_contact_name = sa_vs_contact_name;
        }

        public String getSa_vs_contact_phone() {
            return sa_vs_contact_phone;
        }

        public void setSa_vs_contact_phone(String sa_vs_contact_phone) {
            this.sa_vs_contact_phone = sa_vs_contact_phone;
        }

        public String getSa_vs_contact_fax() {
            return sa_vs_contact_fax;
        }

        public void setSa_vs_contact_fax(String sa_vs_contact_fax) {
            this.sa_vs_contact_fax = sa_vs_contact_fax;
        }

        public String getSa_vs_contact_email() {
            return sa_vs_contact_email;
        }

        public void setSa_vs_contact_email(String sa_vs_contact_email) {
            this.sa_vs_contact_email = sa_vs_contact_email;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getStreet_address_1() {
            return street_address_1;
        }

        public void setStreet_address_1(String street_address_1) {
            this.street_address_1 = street_address_1;
        }

        public String getStreet_address_2() {
            return street_address_2;
        }

        public void setStreet_address_2(String street_address_2) {
            this.street_address_2 = street_address_2;
        }

        public String getAddress_suburb() {
            return address_suburb;
        }

        public void setAddress_suburb(String address_suburb) {
            this.address_suburb = address_suburb;
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

        public int getIs_study() {
            return is_study;
        }

        public void setIs_study(int is_study) {
            this.is_study = is_study;
        }

        public String getStudy_id() {
            return study_id;
        }

        public void setStudy_id(String study_id) {
            this.study_id = study_id;
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

        public int getSell_number() {
            return sell_number;
        }

        public void setSell_number(int sell_number) {
            this.sell_number = sell_number;
        }

        public int getSign_number() {
            return sign_number;
        }

        public void setSign_number(int sign_number) {
            this.sign_number = sign_number;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public List<ProSubUnitClassifyBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ProSubUnitClassifyBean> childs) {
            this.childs = childs;
        }
    }
}
