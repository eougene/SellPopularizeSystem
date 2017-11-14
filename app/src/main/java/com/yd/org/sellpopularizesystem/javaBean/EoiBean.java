package com.yd.org.sellpopularizesystem.javaBean;

import java.util.List;

/**
 * Created by hejin on 2017/11/13.
 */

public class EoiBean {

    /**
     * code : 1
     * msg : 获取成功
     * total : 16
     * result : [{"id":33,"company_id":1,"state":0,"apply_date":1508999486,"check_date":1508999486,"refund_date":1508999486,"sale":{"id":501},"customer":{"id":4900,"surname":"android","mid_name":null,"first_name":"testyyyyy","nick_name":null},"project":{"id":52,"name":"Domus","property":{"id":1632,"unit_number":"1.02"}},"payment":{"channel":4,"amount":300,"currency":"au"}}]
     */

    private int code;
    private String msg;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 33
         * company_id : 1
         * state : 0
         * apply_date : 1508999486
         * check_date : 1508999486
         * refund_date : 1508999486
         * sale : {"id":501}
         * customer : {"id":4900,"surname":"android","mid_name":null,"first_name":"testyyyyy","nick_name":null}
         * project : {"id":52,"name":"Domus","property":{"id":1632,"unit_number":"1.02"}}
         * payment : {"channel":4,"amount":300,"currency":"au"}
         */

        private int id;
        private int company_id;
        private int state;
        private long apply_date;
        private int check_date;
        private int refund_date;
        private SaleBean sale;
        private CustomerBean customer;
        private ProjectBean project;
        private PaymentBean payment;

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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getApply_date() {
            return apply_date;
        }

        public void setApply_date(long apply_date) {
            this.apply_date = apply_date;
        }

        public int getCheck_date() {
            return check_date;
        }

        public void setCheck_date(int check_date) {
            this.check_date = check_date;
        }

        public int getRefund_date() {
            return refund_date;
        }

        public void setRefund_date(int refund_date) {
            this.refund_date = refund_date;
        }

        public SaleBean getSale() {
            return sale;
        }

        public void setSale(SaleBean sale) {
            this.sale = sale;
        }

        public CustomerBean getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerBean customer) {
            this.customer = customer;
        }

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public PaymentBean getPayment() {
            return payment;
        }

        public void setPayment(PaymentBean payment) {
            this.payment = payment;
        }

        public static class SaleBean {
            /**
             * id : 501
             */

            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class CustomerBean {
            /**
             * id : 4900
             * surname : android
             * mid_name : null
             * first_name : testyyyyy
             * nick_name : null
             */

            private int id;
            private String surname;
            private Object mid_name;
            private String first_name;
            private Object nick_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }

            public Object getMid_name() {
                return mid_name;
            }

            public void setMid_name(Object mid_name) {
                this.mid_name = mid_name;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public Object getNick_name() {
                return nick_name;
            }

            public void setNick_name(Object nick_name) {
                this.nick_name = nick_name;
            }
        }

        public static class ProjectBean {
            /**
             * id : 52
             * name : Domus
             * property : {"id":1632,"unit_number":"1.02"}
             */

            private int id;
            private String name;
            private PropertyBean property;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public PropertyBean getProperty() {
                return property;
            }

            public void setProperty(PropertyBean property) {
                this.property = property;
            }

            public static class PropertyBean {
                /**
                 * id : 1632
                 * unit_number : 1.02
                 */

                private int id;
                private String unit_number;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getUnit_number() {
                    return unit_number;
                }

                public void setUnit_number(String unit_number) {
                    this.unit_number = unit_number;
                }
            }
        }

        public static class PaymentBean {
            /**
             * channel : 4
             * amount : 300
             * currency : au
             */

            private int channel;
            private int amount;
            private String currency;

            public int getChannel() {
                return channel;
            }

            public void setChannel(int channel) {
                this.channel = channel;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }
        }
    }
}
