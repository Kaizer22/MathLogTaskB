package com.company.ProofCorrector;

import com.company.ExpressionParser.Parser;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by denis on 16.01.20.
 */
public class ProofCorrector {
    private Parser parser;
    private Marker marker;
    private Corrector corrector;
    private ArrayList<String> proof;
    private ArrayList<String> assumptions;
    private String expression;

            //TODO внешние скобки в парсере
            //TODO удаление лишних внешних скобок во всех выражениях
    private LinkedHashSet<String> provedExpressions;

    public ProofCorrector(Parser parser){
        this.parser = parser;
        marker = new Marker();
        corrector = new Corrector();
    }

    public void correctProof(String inputFile){
        proof = marker.getProofExpressions(inputFile, parser);
        getContext();
        System.out.println(expression);
        System.out.println(assumptions.toString());
        corrector.deleteRepeats(proof);
        System.out.println(proof.toString());
        try {
            marker.annotate(proof, assumptions,parser);
            corrector.correct(proof);
            //marker.annotate(proof, assumptions, parser);
            //printProof(proof);
        }catch (IncorrectProofException incorrectProofException){
            System.out.println("Proof is incorrect");
        }

    }

    private void printProof(ArrayList<String> proof) {
    }

    private void getContext(){
        assumptions = marker.getAssumptions(proof.get(0));
        expression = marker.getExpression(proof.get(0));
        proof.remove(0);
    }
}
