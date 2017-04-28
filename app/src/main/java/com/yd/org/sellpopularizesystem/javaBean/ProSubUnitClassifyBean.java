package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by hejin on 2017/4/27.
 */

public class ProSubUnitClassifyBean extends Domine{

    /**
     * bedroom : 3
     * bathroom : 4
     * car_space : 4
     * max_price : 345392.00
     * min_price : 345392.00
     */
    private String product_id;
    private String bedroom;
    private String bathroom;
    private String car_space;
    private String max_price;
    private String min_price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }
}
