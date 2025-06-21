/**
 * FIX 4.2 standard tag definitions and message types.
 */
class FIXTags {
    // Standard Header
    public static final int BeginString = 8;
    public static final int BodyLength = 9;
    public static final int MsgType = 35;
    public static final int SenderCompID = 49;
    public static final int TargetCompID = 56;
    public static final int MsgSeqNum = 34;
    public static final int SendingTime = 52;
    public static final int CheckSum = 10;

    // Session-level
    public static final int EncryptMethod = 98;
    public static final int HeartBtInt = 108;
    public static final int ResetSeqNumFlag = 141;
    public static final int TestReqID = 112;

    // Order Entry
    public static final int ClOrdID = 11;
    public static final int Symbol = 55;
    public static final int Side = 54;
    public static final int TransactTime = 60;
    public static final int OrderQty = 38;
    public static final int Price = 44;
    public static final int OrdType = 40;
    public static final int TimeInForce = 59;
    public static final int ExecInst = 18;

    // Order Cancel/Replace
    public static final int OrigClOrdID = 41;
    public static final int CancelReplaceRequestType = 203;

    // Execution Report
    public static final int ExecID = 17;
    public static final int ExecType = 150;
    public static final int OrdStatus = 39;
    public static final int CumQty = 14;
    public static final int LeavesQty = 151;
    public static final int AvgPx = 6;

    // Market Data
    public static final int MDReqID = 262;
    public static final int SubscriptionRequestType = 263;
    public static final int MDEntryType = 269;
    public static final int MDEntryPx = 270;
    public static final int MDEntrySize = 271;
    public static final int MDUpdateType = 265;

    // Misc
    public static final int Account = 1;
    public static final int Text = 58;
    public static final int IDSource = 22;
    public static final int SecurityID = 48;
    public static final int LastShares = 32;
    public static final int LastPx = 31;
    public static final int OrderID = 37;
    public static final int ExecTransType = 20;

    // MsgType values
    public static final String HEARTBEAT = "0";
    public static final String TEST_REQUEST = "1";
    public static final String RESEND_REQUEST = "2";
    public static final String REJECT = "3";
    public static final String SEQUENCE_RESET = "4";
    public static final String LOGOUT = "5";
    public static final String LOGON = "A";
    public static final String NEW_ORDER_SINGLE = "D";
    public static final String ORDER_CANCEL_REQUEST = "F";
    public static final String ORDER_CANCEL_REPLACE_REQUEST = "G";
    public static final String EXECUTION_REPORT = "8";
    public static final String ORDER_STATUS_REQUEST = "H";
    public static final String MARKET_DATA_REQUEST = "V";
    public static final String MARKET_DATA_SNAPSHOT_FULL_REFRESH = "W";
}
