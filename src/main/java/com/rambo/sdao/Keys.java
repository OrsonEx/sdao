package com.rambo.sdao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Keys {
    public static final String separatorChar = "/";
    public static final Long TOPLEVELMENUPID = Long.valueOf(-1L);
    public static final String CURRAPP = "currapphandler";
    public static final String CURRUSER = "curruser";
    public static final String CURRDEPT = "currdept";
    public static final String CURRORGA = "currorga";
    public static final String TODAY = "today";
    public static final String UPLOADFILEPATH = "upload.filepath";
    public static final String DEFAULTPICTURE = "default_picture";
    public static final String SUPERVISORFLAG = "supervisor";
    public static final String IPSWITCH = "sys.ip.switch";
    public static final String EmptyString = "";
    public static final String NULLVALUE = "<NULL>";
    public static final String ExtendName = ".kvf";
    public static final String defaultstr = "default";
    public static final String ANONYMOUS = "anonymous";
    public static final int GENERALROW = 50;
    public static final int MAXROW = 2147483647;
    public static final String GLOBALSCOPE = "global";
    public static final DecimalFormat nf1 = new DecimalFormat("##,###");
    public static final DecimalFormat nf2 = new DecimalFormat("00");
    public static final DecimalFormat nf3 = new DecimalFormat("0000");
    public static final DecimalFormat nf4 = new DecimalFormat("####.####");
    public static final DecimalFormat nf5 = new DecimalFormat("##,##0.00");
    public static final DecimalFormat nf6 = new DecimalFormat("#####.00");
    public static final DecimalFormat nf7 = new DecimalFormat("#####.000");
    public static final SimpleDateFormat df1 = new SimpleDateFormat("MM-dd HH:mm");
    public static final SimpleDateFormat df2 = new SimpleDateFormat("yy-MM-dd");
    public static final SimpleDateFormat df3 = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat df5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat df6 = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat df7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Keys() {
    }
}