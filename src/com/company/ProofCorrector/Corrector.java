package com.company.ProofCorrector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by denis on 16.01.20.
 */
public class Corrector  {
    public void correct(ArrayList<String> proof) throws IncorrectProofException {
    }

    public void deleteRepeats(ArrayList<String> proof) {
        ArrayList<String> usedExpressions = new ArrayList<>();
        ArrayList<String> lines = (ArrayList<String>)proof.clone();

        Iterator<String> iterator = proof.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (usedExpressions.contains(lines.get(i)) ) {
                iterator.remove();
            }else {
                usedExpressions.add(lines.get(i));
            }
            i++;
        }

    }
}
