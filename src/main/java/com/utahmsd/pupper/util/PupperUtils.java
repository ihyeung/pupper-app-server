package com.utahmsd.pupper.util;

import com.utahmsd.pupper.dto.pupper.LifeStage;
import com.utahmsd.pupper.service.UserProfileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import static com.utahmsd.pupper.util.Constants.PUPPER_AGE;

public class PupperUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    public static LifeStage ageToStage(String age) throws ValidationException {
        CharSequence ageUnits = "DWMY";
        if (!StringUtils.containsAny(ageUnits, age.toUpperCase())) {
            throw new ValidationException("Age field is missing units");
        }
        //if units contains 'D', trim, replace to get ageInDays,
        //if units contains 'W', trim, replace to get ageInWeeks,
        //if units contains 'M', trim, replace to get ageInMonths,
        return null;
    }

    public static LifeStage dobToLifeStage(Date date) {
        String ageString = calculateAge(date);
        try {
            return ageToStage(ageString);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String calculateAge(Date dob) {
        if (dob == null) {
            return String.format(PUPPER_AGE, 0, 0, 0, 0);
        }
        Date currentDate = Date.from(Instant.now());
        LocalDate now = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate birhdate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int years = Period.between(birhdate, now).getYears();
        int months = Period.between(birhdate, now).getMonths();
        int weeks = Period.between(birhdate, now).getDays()/7;
        int days = Period.between(birhdate, now).getDays() % 7;

        if (years < 0 || months < 0 || weeks < 0 || days < 0) {
            LOGGER.error("Invalid future date was entered for birthdate: {}", dob);
            return String.format(PUPPER_AGE, 0, 0, 0, 0);
        }
        System.out.println("Years: " + years);
        System.out.println("Months: " + months);
        System.out.println("Weeks: " + weeks);
        System.out.println("Days: " + days);

        return String.format(PUPPER_AGE, years, months, weeks, days);

    }
}
