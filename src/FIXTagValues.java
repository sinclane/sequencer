/*
 * FIX 4.2 enumerated values for specific tags.
 */
public class FIXTagValues {

    // Tag 35 - MsgType
    public static final String MSGTYPE_HEARTBEAT = "0";
    public static final String MSGTYPE_TEST_REQUEST = "1";
    public static final String MSGTYPE_RESEND_REQUEST = "2";
    public static final String MSGTYPE_REJECT = "3";
    public static final String MSGTYPE_SEQUENCE_RESET = "4";
    public static final String MSGTYPE_LOGOUT = "5";
    public static final String MSGTYPE_LOGON = "A";
    public static final String MSGTYPE_NEW_ORDER_SINGLE = "D";
    public static final String MSGTYPE_ORDER_CANCEL_REQUEST = "F";
    public static final String MSGTYPE_ORDER_CANCEL_REPLACE_REQUEST = "G";
    public static final String MSGTYPE_EXECUTION_REPORT = "8";
    public static final String MSGTYPE_ORDER_STATUS_REQUEST = "H";
    public static final String MSGTYPE_MARKET_DATA_REQUEST = "V";
    public static final String MSGTYPE_MARKET_DATA_SNAPSHOT = "W";

    // Tag 54 - Side
    public static final String SIDE_BUY = "1";
    public static final String SIDE_SELL = "2";
    public static final String SIDE_BUY_MINUS = "3";
    public static final String SIDE_SELL_PLUS = "4";
    public static final String SIDE_SELL_SHORT = "5";

    // Tag 40 - OrdType
    public static final String ORD_TYPE_MARKET = "1";
    public static final String ORD_TYPE_LIMIT = "2";
    public static final String ORD_TYPE_STOP = "3";
    public static final String ORD_TYPE_STOP_LIMIT = "4";
    public static final String ORD_TYPE_MARKET_ON_CLOSE = "5";

    // Tag 59 - TimeInForce
    public static final String TIF_DAY = "0";
    public static final String TIF_GOOD_TILL_CANCEL = "1";
    public static final String TIF_AT_THE_OPENING = "2";
    public static final String TIF_IMMEDIATE_OR_CANCEL = "3";
    public static final String TIF_FILL_OR_KILL = "4";
    public static final String TIF_GOOD_TILL_CROSSING = "5";
    public static final String TIF_GOOD_TILL_DATE = "6";

    // Tag 150 - ExecType
    public static final String EXEC_TYPE_NEW = "0";
    public static final String EXEC_TYPE_PARTIAL_FILL = "1";
    public static final String EXEC_TYPE_FILL = "2";
    public static final String EXEC_TYPE_DONE_FOR_DAY = "3";
    public static final String EXEC_TYPE_CANCELED = "4";
    public static final String EXEC_TYPE_REPLACED = "5";
    public static final String EXEC_TYPE_PENDING_CANCEL = "6";

    // Tag 39 - OrdStatus
    public static final String ORD_STATUS_NEW = "0";
    public static final String ORD_STATUS_PARTIALLY_FILLED = "1";
    public static final String ORD_STATUS_FILLED = "2";
    public static final String ORD_STATUS_DONE_FOR_DAY = "3";
    public static final String ORD_STATUS_CANCELED = "4";
    public static final String ORD_STATUS_REPLACED = "5";
    public static final String ORD_STATUS_PENDING_CANCEL = "6";

    // Tag 263 - SubscriptionRequestType
    public static final String SUBSCRIPTION_REQUEST_SNAPSHOT = "0";
    public static final String SUBSCRIPTION_REQUEST_SNAPSHOT_PLUS_UPDATES = "1";
    public static final String SUBSCRIPTION_REQUEST_DISABLE_PREVIOUS = "2";

    // Tag 269 - MDEntryType
    public static final String MDENTRYTYPE_BID = "0";
    public static final String MDENTRYTYPE_OFFER = "1";
    public static final String MDENTRYTYPE_TRADE = "2";
    public static final String MDENTRYTYPE_INDEX_VALUE = "3";
    public static final String MDENTRYTYPE_OPENING_PRICE = "4";
    public static final String MDENTRYTYPE_CLOSING_PRICE = "5";
    public static final String MDENTRYTYPE_SETTLEMENT_PRICE = "6";
    public static final String MDENTRYTYPE_TRADING_SESSION_HIGH_PRICE = "7";
    public static final String MDENTRYTYPE_TRADING_SESSION_LOW_PRICE = "8";

    // Tag 265 - MDUpdateType
    public static final String MDUPDATE_TYPE_FULL_REFRESH = "0";
    public static final String MDUPDATE_TYPE_INCREMENTAL_REFRESH = "1";

    // Tag 98 - EncryptMethod
    public static final String ENCRYPT_METHOD_NONE = "0";
    public static final String ENCRYPT_METHOD_PKCS = "1";
    public static final String ENCRYPT_METHOD_DES = "2";
    public static final String ENCRYPT_METHOD_PGP_DES = "3";
    public static final String ENCRYPT_METHOD_PGP_DES_MD5 = "4";
    public static final String ENCRYPT_METHOD_RSA_DES_MD5 = "5";
    public static final String ENCRYPT_METHOD_AES = "6";

    // Tag 18 - ExecInst (examples)
    public static final String EXEC_INST_NOT_HELD = "1";
    public static final String EXEC_INST_WORK = "2";
    public static final String EXEC_INST_GO_ALONG = "3";
    public static final String EXEC_INST_OVER_THE_DAY = "4";
    public static final String EXEC_INST_HELD = "5";
}
