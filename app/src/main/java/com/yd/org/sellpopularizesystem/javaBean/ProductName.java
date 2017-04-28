package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by hejin on 2017/4/14.
 */

public class ProductName extends Domine{

    /**
     * product_info_id : 26
     * product_id : 33
     * product_name : English
     * product_description : en
     * add_time : 1488534560
     * lan : en
     */

    private int product_info_id;
    private int product_id;
    private String product_name;
    private String product_description;
    private int add_time;
    private String lan;

    public int getProduct_info_id() {
        return product_info_id;
    }

    public void setProduct_info_id(int product_info_id) {
        this.product_info_id = product_info_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}
