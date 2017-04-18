package com.example.lorena.challengifier.utils.mappers;

import com.example.lorena.challengifier.models.Challenge;
import com.example.lorena.challengifier.models.Objective;

/**
 * Created by Lorena on 17.01.2017.
 */

public class ChallengeToObjective {
    public static Objective toObjective(Challenge challenge) {
        Objective obj =  new Objective();

        obj.setName(challenge.getTitle());
        obj.setExpectedOutcome(challenge.getDescription());
        obj.setDeadline(challenge.getSuggestedDeadline());
        //obj.s
        return obj;
    }
}
