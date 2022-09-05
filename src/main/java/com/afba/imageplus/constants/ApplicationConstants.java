package com.afba.imageplus.constants;

import java.util.List;

public class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final String PRIMARY_DATASOURCE_BEAN = "primaryDatasource";
    public static final String SECONDARY_DATASOURCE_BEAN = "secondaryDatasource";
    public static final String PRIMARY_JDBC_TEMPLATE_BEAN = "sqlServerJdbcTemplate";
    public static final String SECONDARY_JDBC_TEMPLATE_BEAN = "db2JdbcTemplate";

    public static final int FTP_MAX_FILE_DOWNLOAD_PER_REQUEST = 3;
    public static final String FTP_ALLOWED_EXTENSION_REGEX = "(^[^*&%]+(\\.(?i)(tiff|pdf))$)";
    public static final int FTP_DEFAULT_MAX_SIZE_PER_FILE = 10000000; // size in bytes

    public static final String JULIAN_DATE_FORMAT_PATTERN = "yyD";
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String SEQUENCE_NUMBER_FORMAT = "%05d";

    // B<julian-date>AA.AAA with below constants
    public static final String DOCUMENT_ID_EXTENSION_INITIAL_COMBINATION = "AAA";
    public static final String DOCUMENT_ID_NAME_INITIAL_LETTER = "B";
    public static final String DOCUMENT_ID_NAME_LETTERS_BEFORE_PERIOD = "AA";

    public static final String ALLOWED_DOCUMENT_CONTENT_TYPE = "image/tiff";
    public static final String ALLOWED_DOCUMENT_EXT_TIFF = ".tiff";
    public static final Integer MAX_DOCUMENT_PER_LIBRARY = 5000;
    public static final Integer SHAREPOINT_MIN_AVAILABLE_LIBRARY_THRESHOLD = 2;
    public static final String SHAREPOINT_CONTROL_EMAIL_TEMPLATE_NAME = "EMAIL_SHAREPOINT_CONTROL";
    public static final String EKDMOVE_EMAIL_TEMPLATE_NAME = "EMAIL_EKDMOVE_ERROR";
    public static final String EMSIINBND_VALIDATION_FAILURE_EMAIL_TEMPLATE_NAME = "EMAIL_EMSIINBND_VALIDATION_FAILURE";
    public static final String DDPROCESS_FAILURE_EMAIL_TEMPLATE_NAME = "EMAIL_DDPROCESS_FAILURE";

    public static final int COMMENTS_TIFF_MAX_HEIGHT = 640;
    public static final int COMMENTS_TIFF_MAX_WIDTH = 800;

    public static final String DEFAULT_COMMENT_DOCUMENT_TYPE = "CASECMNT";
    public static final String TEMP_COMMENT_DOC_DIR = "comment-docs";

    public static final String FONT_FAMILY = "Arial";

    public static final String EMSI_DONE_QUEUE = "EMSIDONEQ";

    public static final Integer USER_LAST_NAME_SIZE = 30;
    public static final Integer USER_FIRST_NAME_SIZE = 20;

    public static final Integer EMSI_DEFAULT_PEND_DAYS = 75;
    public static final Integer HOT_QUEUE_DEFAULT_PEND_DAYS = 5;
    public static final List<String> permanentCaseDes = List.of("01 APPLICATIONS", "02 BENEFICIARY", "03 MEDICAL",
            "04 STATUS CHANGE", "05 CLAIMS", "06 ACCOUNTING", "07 MISCELLANEOUS");

    public static final String POLICY_TYPE_BA = "BA";
    public static final String POLICY_TYPE_LT = "LT";
    public static final String POLICY_TYPE_GF = "GF";

    public static final Integer LP_GF_POLICY_AUTO_ISSUE_DAYS_THRESHOLD = 2;
    public static final String LP_IMAGE_HOLD_QUEUE = "IMAGLPHLDQ";

    public static final String MOVE_QUEUE = "MOVE";
    public static final String APPS_QC_RUN_HISTORY_QUEUE = "APPSQC";
    public static final String APPS_GF_QUEUE = "APPSGF";
    public static final String APPSAUNGQ_BA = "APPSAUNGQ";
    public static final String APPSAUBAQ_BA = "APPSAUBAQ";
    public static final String APPSAULTQ_LT = "APPSAULTQ";
    public static final String LP_AUTO_ISSUE_FAILED_COMMENT = "Auto comment: Auto issue failed by LifePro system.";

    public static final String LIFE_PRO_TOKEN = "token";
    public static final String LIFE_PRO_TOKEN_EXPERIED_IN = "expiry";
    public static final String LIFE_PRO_TOKEN_GEN_TIME = "getTime";

    public static final String DOCTYPE_APPSBA = "APPSBA";
    public static final String DOCTYPE_APPSLT = "APPSLT";

    public static final Integer LIFEPRO_SUCCESS_RETURN_CODE = 0;
    public static final String APPSEMSMAS = "APPSEMSMAS";
    public static final String APPSEMSIBA = "APPSEMSIBA";
    public static final String APPSNG0702 = "APPSNG0702";
    public static final String APPSEMSICA = "APPSEMSICA";
    public static final String APPSEMSILT = "APPSEMSILT";
    public static final String MEDIATTNQ = "MEDIATTNQ";
    public static final String GOTOEMSIQ = "GOTOEMSIQ";

    public static final String ENQFLR_TIME_PATTERN = "HHmmss";
    public static final String BATCH_JOB_USER = "AFBAJOB";

    public static final String USER_TYPE="String";
    public static final String GUID_WORD="_ImageSystem";

}
