package com.utahmsd.pupper.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String MATCH_RESULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);


    public static final int PAGE_NUM = 0;
    public static final int PAGE_SIZE = 10;
    public static final int MAX_RESULTS = 20;
    public static final int MAX_MESSAGES = 10;
    public static final float DEFAULT_MAX_SCORE = 100.0F;
    public static final float DEFAULT_MIN_SCORE = 0.0F;

    public static final Date RECENT_ACTIVITY_CUTOFF = DateTime.now().minusDays(30).toDate();//Considered recent activity if within the last 30 days

    public static final String DEFAULT_DESCRIPTION = "success";

    public static final String NOT_FOUND = "Resource not found.";
    public static final String USER_PROFILE_NOT_FOUND = "User profile with id %d not found";
    public static final String INVALID_PATH_VARIABLE = "Not found: one or more invalid path variables were used in URL path.";
    public static final String EMAIL_NOT_FOUND = "%s with email = '%s' not found.";
    public static final String ID_NOT_FOUND = "%s with id = '%d' not found.";
    public static final String NO_QUERY_RESULTS = "No results matching your search criteria were found.";

    public static final String NULL_FIELD = "Null check validation of %s data failed.";
    public static final String INVALID_INPUT = "Invalid input.";
    public static final String IDS_MISMATCH = "IDs found in request body and/or URL path do not match.";

    public static final String DEFAULT_ABOUT_ME = "I love my human and I love all puppers!";

}
