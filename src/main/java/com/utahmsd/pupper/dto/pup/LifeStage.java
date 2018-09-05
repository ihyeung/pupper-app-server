package com.utahmsd.pupper.dto.pup;


import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.ValidationException;

public enum LifeStage {
    PUPPY,
    YOUNG,
    ADULT,
    MATURE;

    LifeStage ageToStage(String age) throws ValidationException {
            CharSequence ageUnits = "DWMY";
                if (!StringUtils.containsAny(ageUnits, age.toUpperCase())) {
                    throw new ValidationException("Age field is missing units");
                }
                //if units contains 'D', trim, replace to get ageInDays,
        //if units contains 'W', trim, replace to get ageInWeeks,
        //if units contains 'M', trim, replace to get ageInMonths,
        return null;
    }

}
