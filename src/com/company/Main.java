package com.company;

import com.company.ExpressionParser.Parser;
import com.company.ProofCorrector.ProofCorrector;
import com.company.ProofCorrector.Test;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Enter a logic expression:");
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser();
        ProofCorrector proofCorrector = new ProofCorrector(parser);

        proofCorrector.correctProof(Test.PROOF[0]);
        proofCorrector.correctProof(Test.PROOF[1]);


    }
}
