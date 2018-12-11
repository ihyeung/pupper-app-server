package com.utahmsd.pupper.util;

import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class ProfileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileUtils.class);
    static final String DEFAULT_AGE = "0 years old";
    private static final String PUPPER_AGE = "%d %s old";
    public static final String NOT_ACTIVE = "No recent activity";

    public static String lastActivityFromLastLogin(Date lastLogin) {
        LocalDate now = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate lastActive = lastLogin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period lastActivity = Period.between(lastActive, now);
        if (lastActivity.getYears() > 0 || lastActivity.getMonths() > 3) {
            return NOT_ACTIVE;
        }
        if (lastActivity.getMonths() > 0) {
            return String.format("Last active %d months ago", lastActivity.getMonths());
        }
        return String.format("Last active %d days ago", lastActivity.getDays());
    }

    private static LifeStage ageToStage(String age) throws ValidationException {
        if (age == null || age.length() < 2) {
            throw new ValidationException("Invalid input format");
        }
        CharSequence ageUnits = "YMD";
        if (!StringUtils.containsAny(ageUnits, age.toUpperCase())) {
            throw new ValidationException("Missing Y/M/D units");
        }

        return null;
    }

    public static LifeStage dobToLifeStage(Date date) {
        String ageString = createAgeStringFromDate(date);

        try {
            return ageToStage(formatAgeString(ageString));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Shortens age string to abbreviated form. eg. "12 years old" to "12Y".
     * @param ageString
     * @return
     */
    private static String formatAgeString(String ageString) {
        String [] ageWords = ageString.split(" ");
        return ageWords[0] + ageWords[1].substring(0, 1).toUpperCase();
    }

    /**
     * Converts a Date object to a displayable age string. e.g. "4 years old" or "12 weeks old".
     * @param dob
     * @return
     */
    public static String createAgeStringFromDate(Date dob) {
        Date currentDate = Date.from(Instant.now());
        if (dob == null) {
            LOGGER.error("Null birthdate.");
            return DEFAULT_AGE;
        }
        LocalDate now = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate birhdate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period agePeriod = Period.between(birhdate, now);
        return createAgeStringFromPeriod(agePeriod);
    }

    private static String createAgeStringFromPeriod(Period period) {
        if (period.isZero() || period.isNegative()) {
            return DEFAULT_AGE;
        }
        if (period.getYears() > 0) {
            return String.format(PUPPER_AGE, period.getYears(), "years");
        } else {
            if (period.getMonths() >= 4) {
                return String.format(PUPPER_AGE, period.getMonths(), "months");
            }
            int weeks = period.getMonths() * 4  + period.getDays()/7;
            int days = period.getDays() % 7;

            return weeks > 0 ? String.format(PUPPER_AGE, weeks, "weeks") : days > 0 ?
                    String.format(PUPPER_AGE, period.getDays(), "days") : DEFAULT_AGE;
        }
    }
}
