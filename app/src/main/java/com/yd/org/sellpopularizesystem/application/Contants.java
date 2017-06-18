package com.yd.org.sellpopularizesystem.application;

/**
 * Created by bai on 2017/1/10.
 */

public class Contants {
    /**
     * 域名
     */
    //public static final String DOMAIN = "http://crm.vxda.cn";
    public static final String DOMAIN = "https://www.wingaid.com";
    //首页数据
    // public static final String HOME_DAA = DOMAIN + "/index.php/app/index/index_data";
    public static final String HOME_DAA = DOMAIN + "/index.php/appv2/index/index_data";
    //登录
    public static final String HOME_LOGIN = DOMAIN + "/index.php/appv2/login/index";
    //微信登录
    // public static final String WEIXIN_LOGIN = DOMAIN + "/index.php/app/login/third_login";
    public static final String WEIXIN_LOGIN = DOMAIN + "/index.php/appv2/login/third_login";
    //修改密码
    public static final String CHANGE_PASSWORD = DOMAIN + "/index.php/appv2/user/user_setpassword";
    //获取推广筛选数据
    public static final String SCALE_SEARCH = DOMAIN + "/index.php/appv2/product/product_search_option";
    //获取产品推广筛选数据
    public static final String PRODUCT_LIST = DOMAIN + "/index.php/appv2/product/product_list_v2";

    //public static final String PRODUCT_LIST = DOMAIN + "/index.php/appv/product/product_list_v";
    //获取产品详情
    //public static final String PRODUCT_DETAIL = DOMAIN + "/index.php/app/product/product_info";
    public static final String PRODUCT_DETAIL = DOMAIN + "/index.php/appv2/product/product_info";
    //获取产品子单元列表
    public static final String PRODUCT_SUBUNIT_LIST = DOMAIN + "/index.php/appv2/product/product_childs_list";
    //获取产品子单元详情
    public static final String PRODUCT_SUBUNIT_DETAIL = DOMAIN + "/index.php/appv2/product/product_childs_info";
    // public static final String PRODUCT_SUBUNIT_DETAIL = DOMAIN + "/index.php/app/product/product_childs_info";
    //调研列表
    public static final String QUESTION_LIST = DOMAIN + "/index.php/appv2/product/question_list";
    //获取客户列表
    public static final String CUSTOMER_LIST = DOMAIN + "/index.php/appv2/customer/customer_list";
    //删除销售关联的客户
    public static final String USER_CUSTOMER_DEL = DOMAIN + "/index.php/appv2/user/user_customer_del";
    //获取律师列表
    public static final String LAWYER_LIST = DOMAIN + "/index.php/appv2/lawyer/lawyer_list";
    //获取我的团队列表
    public static final String TEAM_LIST = DOMAIN + "/index.php/appv2/user/my_team_users";
    //获取学习列表
    //public static final String STUDY_LIST = DOMAIN + "/index.php/app/study/study_list";
    public static final String STUDY_LIST = DOMAIN + "/index.php/appv2/study/study_list";
    //获取学习完成反馈
    public static final String STUDY_COMPLETE = DOMAIN + "/index.php/appv2/study/study_complete";
    //获取试卷详情
    public static final String PAPER_DETAILS = DOMAIN + "/index.php/appv2/check/paper_info";
    //获取考核列表
    public static final String CHECK_LIST = DOMAIN + "/index.php/appv2/check/paper_list";
    //获取客户详情
    //public static final String CUSTOME_DETAILED = DOMAIN + "/index.php/app/customer/customer_info";
    public static final String CUSTOME_DETAILED = DOMAIN + "/index.php/appv2/customer/customer_info";
    //获取客户详情列表
    public static final String SCALE_LIST = DOMAIN + "/index.php/appv2/product/product_childs_list";
    //更新客户信息
    //public static final String UPDATE_CUSTOME = DOMAIN + "/index.php/app/customer/customer_update";
    public static final String UPDATE_CUSTOME = DOMAIN + "/index.php/appv2/customer/customer_update";


    //更新客户头像
    public static final String UPDATE_HEADIMAGE = DOMAIN + "/index.php/appv2/customer/customer_update_head_img";

    //新增客户
    // public static final String NEW_CUSTOME = DOMAIN + "/index.php/app/customer/customer_add";
    public static final String NEW_CUSTOME = DOMAIN + "/index.php/appv2/customer/customer_add";
    //新增律师
    //public static final String NEW_LAWYER = DOMAIN + "/index.php/app/lawyer/add_lawyer";

    public static final String NEW_LAWYER = DOMAIN + "/index.php/appv2/lawyer/add_lawyer";
    //提交试卷
    public static final String SUBMIT_PAPER = DOMAIN + "/index.php/appv2/check/sub_paper";
    //获取考核结果
    public static final String GET_TEST_RESULT = DOMAIN + "/index.php/appv2/check/check_logs_detail";
    //获取系统公告列表数据
    // public static final String SYSTEM_ANNOUNCEMENT = DOMAIN + "/index.php/app/notice/index";
    public static final String SYSTEM_ANNOUNCEMENT = DOMAIN + "/index.php/appv2/notice/index";
    //删除预定消息提示
    // public static final String DELETE_NOTICE = DOMAIN + "/index.php/app/notice/del_notice_logs";
    public static final String DELETE_NOTICE = DOMAIN + "/index.php/appv2/notice/del_notice_logs";
    //提交已读
    //public static final String SUBMIT_READED = DOMAIN + "/index.php/app/notice/have_read";
    public static final String SUBMIT_READED = DOMAIN + "/index.php/appv2/notice/have_read";


    //创建订单
    //public  static final String CREAT_ORDER =DOMAIN +"/index.php/app/order/create_order";
    public static final String CREAT_ORDER = DOMAIN + "/index.php/appv2/order/create_order";
    //支付宝支付接口
    public static final String ALIPAY_INTERFACE = DOMAIN + "/index.php/api/index/alipay";
    //微信支付接口
    public static final String WEICHAT_INTERFACE = DOMAIN + "/index.php/api/index/wxpay";
    //请求合同
    public static final String ASK_ONTRACT = DOMAIN + "/index.php/appv2/order/ask_ontract";
    //上传合同图片
    public static final String UPLOAD_CONTRACT_PHOTO = DOMAIN + "/index.php/appv2/order/upload_contract";
    //上传首付款凭证
    public static final String UPLOAD_FIRST_COMMISSION = DOMAIN + "/index.php/appv2/order/upload_buy_money";
    //查询订单列表
    public static final String INQUIRE_ORDER_LIST = DOMAIN + "/index.php/appv2/order/order_list";
    //订单详情
    public static final String ORDER_DETAIL = DOMAIN + "/index.php/appv2/order/order_info";
    //申请合同
    //public static final String APPLY_CONTRACT = DOMAIN + "/index.php/app/order/ask_ontract";
    public static final String APPLY_CONTRACT = DOMAIN + "/index.php/appv2/order/ask_ontrac";

    //修改订单
    public static final String UPDATE_ORDER = DOMAIN + "/index.php/appv2/order/edit_order";
    //取消订单
    public static final String ORDER_CANCEL = DOMAIN + "/index.php/appv2/order/cancel_order";
    //拜访记录列表
    public static final String VISIT_RECORD_LIST = DOMAIN + "/index.php/appv2/user/visit_log_list";
    //新增拜访记录
    //public static final String NEW_VISIT_RECORDER = DOMAIN + "/index.php/app/user/add_visit_log";
    public static final String NEW_VISIT_RECORDER = DOMAIN + "/index.php/appv2/user/add_visit_log";
    //修改拜访记录
    public static final String UPDATE_VISIT_RECORDER = DOMAIN + "/index.php/appv2/user/edit_visit_log";
    //删除拜访记录
    public static final String REMOVE_VISIT_RECORD = DOMAIN + "/index.php/appv2/user/delete_visit_log";
    //新增预约记录
    public static final String NEW_RESERVER_RECORDER = DOMAIN + "/index.php/appv2/user/add_order_log";
    //预约记录
    public static final String RESERVER_RECORDER_LIST = DOMAIN + "/index.php/appv2/user/order_log_list";
    //修改预约记录
    public static final String UPDATE_RESERVER_RECORDER = DOMAIN + "/index.php/appv2/user/edit_order_log";
    //删除预约记录
    public static final String REMOVE_RESERVER_RECORD = DOMAIN + "/index.php/appv2/user/delete_order_log";

    //推广列表
    public static final String SALE_LOG_LIST = DOMAIN + "/index.php/appv2/product/sale_log_list";


    //推广记录
    public static final String ADD_SALE_LOG = DOMAIN + "/index.php/appv2/product/add_sale_log";


    //购房记录
    public static final String ORDER_LIST = "/index.php/appv2/order/order_list";
    //eoi充值
    public static final String EOI_RECHARGE = DOMAIN + "/index.php/appv2/eoi/create_eoi";
    //eoi列表
    public static final String EOI_LIST = DOMAIN + "/index.php/appv2/eoi/eoi_list";
    //佣金
   // public static final String COMMOSSION_LIST = DOMAIN + "/index.php/app/commossion/commossion_list";
    public static final String COMMOSSION_LIST = DOMAIN + "/index.php/appv2/commossion/commossion_list";
    //学习详情
    public static final String STUDY_INFO = DOMAIN + "/index.php/appv2/study/study_info";
    /**
     * 用户文件夹
     */
    public static final String USER_PATH = "USER/user";
    /**
     * 缓存文件夹目录
     */
    public static final String CACHE_PATH = "cache";
    //微信相关
    public static final String WEXIN_APP_ID = "wxaaa583d7b2f02dc3";
    public static final String WEXIN_APP_SECRET = "856425a0db943831154d248d32c761c2";
    public static final String WEXIN_URL_STRING = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String BINDING_THIRD = DOMAIN + "/index.php/appv2/login/binding_third";

}
