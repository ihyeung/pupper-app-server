package com.utahmsd.pupper.util;

import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public static LifeStage dobToLifeStage(Date date) {
        if (date.after(Date.from(Instant.now()))) {
            try {
                throw new ValidationException("Invalid future birthdate");
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        Date twelveMonths = Date.from(Instant.now().minus(365, ChronoUnit.DAYS));
        Date twoYears = Date.from(Instant.now().minus(365 * 2, ChronoUnit.DAYS));
        Date eightYears = Date.from(Instant.now().minus(365 * 8, ChronoUnit.DAYS));

        if (date.after(twelveMonths)) {
            return LifeStage.PUPPY;
        }
        else if (date.after(twoYears)) {
            return LifeStage.YOUNG;
        }
        else if (date.after(eightYears)) {
            return LifeStage.ADULT;
        }
        return LifeStage.MATURE;
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
