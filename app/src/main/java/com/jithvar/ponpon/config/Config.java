package com.jithvar.ponpon.config;

/**
 * Created by Arvindo Mondal on 13/9/17.
 * Company name Jithvar
 * Email arvindomondal@gmail.com
 */
public class Config {


    public static final String REGISTRATION_USER="http://192.168.1.2/ponpon/api/register";

    public static final String URL_USER_PERSONAL_REG="http://192.168.1.2/ponpon/one.php?ponpon=personal";
    public static final String URL_REG_MOBILE="http://192.168.1.2/ponpon/one.php?ponpon=start";
    public static final String URL_CHECK_OTP="http://192.168.1.2/ponpon/one.php?ponpon=check_otp";
    public static final String URL_CHECK_UPDATE_USER="http://192.168.1.2/ponpon/one.php?ponpon=update_user";
    public static final String URL_UPDATE_WALLET="http://192.168.1.2/ponpon/one.php?ponpon=wallet";
    public static final String URL_SWIPE_ACCOUNT="http://192.168.1.2/ponpon/one.php?ponpon=swipe_account";
    public static final String URL_GET_NEARBY_SPOTS="http://192.168.1.2/ponpon/one.php?ponpon=find_locations";
    public static final String URL_ALL_SPOT_TRANSACTIONS="http://192.168.1.2/ponpon/one.php?ponpon=transaction_detail";
    public static final String URL_SET_SPOT="http://192.168.1.2/ponpon/one.php?ponpon=have_spot";
    public static final String URL_BOOK_SPOT="http://192.168.1.2/ponpon/one.php?ponpon=book_spot";
    public static final String URL_GET_SPOT_DETAILS="http://192.168.1.2/ponpon/one.php?ponpon=get_spot_details";
    public static final String URL_CURRENCY_CONVERSION="http://192.168.1.2/ponpon/one.php?ponpon=get_currency";





//    public static final String LOGIN_URL="http://192.168.1.2/ponpon2/api/register";
    public static final String LOGIN_URL = "http://www.demo.jithvar.com/ponpon/login.php";
    public static final String UPDATE_PROFILE = "http://www.demo.jithvar" +
            ".com/ponpon/update_profile.php";
    public static final String CUSTOMER_PROFILE = "http://www.demo.jithvar" +
            ".com/ponpon/user_profile.php";
    public static final String UPDATE_VEHICLE = "http://www.demo.jithvar.com/ponpon/login.php";
    public static final String ACTIVATE_ACCOUNT = "http://www.demo.jithvar" +
            ".com/ponpon/activate_account.php";
    public static final String EMAIL_OTP_SEND = "http://www.demo.jithvar.com/ponpon/email_verification.php";

    public static final String HAVE_SPOT_DATA_LOAD = "http://www.demo.jithvar" +
            ".com/ponpon/spot_data_provider.php";

    public static final String HAVE_SPOT_IN_PROGRESS_DETAILS = "http://www.demo.jithvar" +
            ".com/ponpon/spot_in_progress_details.php";

    public static final String HAVE_SPOT_IN_PROGRESS = "http://www.demo.jithvar" +
            ".com/ponpon/spot_in_progress.php";

    public static final String PROVIDER_ACCEPT_SEEKER = "http://www.demo.jithvar" +
            ".com/ponpon/provider_accept_seeker.php";

    public static final String NEED_IN_PROGRESS_DEAL_CANCEL = "http://www.demo.jithvar" +
            ".com/ponpon/need_in_progress_deal_cancel.php";

    public static final String NEED_IN_PROGRESS_DEAL_ACCEPT = "http://www.demo.jithvar" +
            ".com/ponpon/need_in_progress_deal_accept.php";

    public static final String NEED_IN_PROGRESS = "http://www.demo.jithvar" +
            ".com/ponpon/need_in_progress.php";

    public static final String NEED_ACTIVE = "http://www.demo.jithvar" +
            ".com/ponpon/need_active.php";

    public static final String NEED_ACTIVE_DETAILS = "http://www.demo.jithvar" +
            ".com/ponpon/need_active_details.php";

    public static final String NEED_ACTIVE_DEAL_CANCEL = "http://www.demo.jithvar" +
            ".com/ponpon/need_active_deal_cancel.php";

    public static final String NEED_SUCCESS = "http://www.demo.jithvar" +
            ".com/ponpon/need_success.php";

    public static final String NEED_EXPIRE = "http://www.demo.jithvar" +
            ".com/ponpon/need_expire.php";

    public static final String PROVIDER_ACCEPT_SEEKER_DATA_LOAD = "http://www.demo.jithvar" +
            ".com/ponpon/provider_accept_seeker_data_load.php";

    public static final String HAVE_SPOT_SUCCESS = "http://www.demo.jithvar" +
            ".com/ponpon/provider_success_deal.php";

    public static final String HAVE_SPOT_EXPIRE = "http://www.demo.jithvar" +
            ".com/ponpon/provider_expire_deal.php";


    public static final String HAVE_SPOT_IN_PROGRESS_UPDATE_CHANGES = "http://www.demo.jithvar" +
            ".com/ponpon/spot_in_progress_cancel_or_update.php";

    public static final String SPOT_DETAIL_PROVIDER = "http://www.demo.jithvar" +
            ".com/ponpon/spot_detail_provider.php";


    public static final String SPOT_LIST_SEEKER = "http://www.demo.jithvar" +
            ".com/ponpon/spot_list_seeker.php";

    public static final String HAVE_SPOT_DATA_SEEKER = "http://www.demo.jithvar" +
            ".com/ponpon/provider_spot_data.php";


    public static final String SPOT_REGISTRATION_PROVIDER = "http://www.demo.jithvar" +
            ".com/ponpon/spot_book_provider.php";


    public static final String BOOK_SPOT_LOAD_NEAR_BY = "http://www.demo.jithvar" +
            ".com/ponpon/book_spot_load_near_by1.php";

    public static final String BOOK_SPOT_LOAD_NEAR_BY1 = "http://www.demo.jithvar" +
            ".com/ponpon/book_spot_load_near_by.php";

    public static final String BOOK_SPOT_2_DETAILS = "http://www.demo.jithvar" +
            ".com/ponpon/book_spot2_load.php";
    public static final String BOOK_SPOT_3_DO_PAYMENT = "http://www.demo.jithvar" +
            ".com/ponpon/book_spot3_do_payment.php";









    public static String sendOpt(String message, String contact){
        return "http://msg.ptindia.org/rest/services/sendSMS/sendGroupSms?" +
                "AUTH_KEY=142d53a576f75148d1f44184c4e9a71" +
                "&message=" + message + "&senderId=JITHVR" + "&routeId=1" + "&mobileNos=" +
                contact + "&smsContentType=english";
    }

    public static String sendEmailOpt(String message, String email){
        return "http://msg.ptindia.org/rest/services/sendSMS/sendGroupSms?" +
                "AUTH_KEY=142d53a576f75148d1f44184c4e9a71" +
                "&message=" + message + "&senderId=JITHVR" + "&routeId=1" + "&mobileNos=" +
                email + "&smsContentType=english";
    }

    String d = "http://msg.ptindia.org/rest/services/sendSMS/sendGroupSms?AUTH_KEY=142d53a576f75148d1f44184c4e9a71&message=message&senderId=JITHVR&routeId=1&mobileNos=contact&smsContentType=english";






}
