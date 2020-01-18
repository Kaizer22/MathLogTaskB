package com.company.ProofCorrector;

/**
 * Created by denis on 16.01.20.
 */
public class Test {
    public static final  String[] PROOF = { "A->B, !B |- !A\n" +
                                            "A->B\n" +
                                            "!B\n" +
                                            "!B -> A -> !B\n" +
                                            "A -> !B\n" +
                                            "(A -> B) -> (A -> !B) -> !A\n" +
                                            "(A -> !B) -> !A\n" +
                                            "!A",
            "|- A -> A\n" +
                    "A & A -> A\n" +
                    "A -> A -> A\n" +
                    "A -> (A -> A) -> A\n" +
                    "A & A -> A\n" +
                    "(A -> A -> A) -> (A -> (A -> A) -> A) -> A -> A\n" +
                    "(A -> (A -> A) -> A) -> A -> A\n" +
                    "A & A -> A\n" +
                    "A -> A"};
}
