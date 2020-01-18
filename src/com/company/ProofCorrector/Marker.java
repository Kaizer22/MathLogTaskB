package com.company.ProofCorrector;

import com.company.ExpressionParser.Leaf;
import com.company.ExpressionParser.Parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by denis on 16.01.20.
 */
public class Marker {
    /**
     * (1)α → β → α  --- (->,A,(->,B,A))
     * (2)(α → β) → (α → β → γ) → (α → γ) --- (->,(->,A,B),(->,(->,A,(->,B,Y)),(->,A,Y)))
     * (3)α → β → α&β  --- (->,A,(->,B,(&,A,B)))
     * (4)α&β → α --- (->,(&,A,B),A)
     * (5)α&β → β  --- (->,(&,A,B),B)
     * (6)α → α ∨ β  --- (->,A,(|,A,B))
     * (7)β → α ∨ β  --- (->,B,(|,A,B))
     * (8)(α → γ) → (β → γ) → (α ∨ β → γ)  --- (->,(->,A,Y),(->,(->,B,Y),(->,(|,A,B),Y)))
     * (9)(α → β) → (α → ¬β) → ¬α  --- (->,(->,A,B),(->,(->,A,(!B)),(!A)))
     * (10) ¬¬α → α  --- (->,(!(!A)),A)
     **/

    private  class AxiomRecognizer{
        String parsedExpression;

        AxiomRecognizer(String s){
            parsedExpression = s;
        }

        boolean isFirstAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String a = expressionTree.getLeftLeaf().getLexeme();
                    if (expressionTree.getRightLeaf().getLexeme().equals("->")){
                        if (expressionTree.getRightLeaf().getRightLeaf().getLexeme().equals(a)){
                            System.out.println("true(1)");
                            return true;
                        }
                    }
            }

            return false;
        }

        boolean isThirdAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String a = expressionTree.getLeftLeaf().getLexeme();
                if (expressionTree.getRightLeaf().getLexeme().equals("->")){
                    if (expressionTree.getRightLeaf().getRightLeaf().getLexeme().equals("&")){
                        String b = expressionTree.getRightLeaf().getLeftLeaf().getLexeme();
                        if (expressionTree.getRightLeaf().getRightLeaf().getLeftLeaf().getLexeme().equals(a) &&
                                expressionTree.getRightLeaf().getRightLeaf().getRightLeaf().getLexeme().equals(b)){
                            System.out.println("true(3)");
                            return true;
                        }

                    }
                }
            }

            return false;
        }


        boolean isFourthAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(&]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String a = expressionTree.getRightLeaf().getLexeme();
                if (expressionTree.getLeftLeaf().getLexeme().equals("&")){
                    if (expressionTree.getLeftLeaf().getLeftLeaf().getLexeme().equals(a)){
                        System.out.println("true(4)");
                        return true;
                    }
                }
            }
            return false;
        }
        boolean isFifthAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String b = expressionTree.getRightLeaf().getLexeme();
                if (expressionTree.getLeftLeaf().getLexeme().equals("&")){
                    if (expressionTree.getLeftLeaf().getRightLeaf().getLexeme().equals(b)){
                        System.out.println("true(5)");
                        return true;
                    }
                }
            }
            return false;
        }
        boolean isSixthAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String a = expressionTree.getLeftLeaf().getLexeme();
                if (expressionTree.getRightLeaf().getLexeme().equals("|")){
                    if (expressionTree.getRightLeaf().getLeftLeaf().getLexeme().equals(a)){
                        System.out.println("true(6)");
                        return true;
                    }
                }
            }
            return false;
        }
        boolean isSeventhAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String b= expressionTree.getLeftLeaf().getLexeme();
                if (expressionTree.getRightLeaf().getLexeme().equals("|")){
                    if (expressionTree.getRightLeaf().getRightLeaf().getLexeme().equals(b)){
                        System.out.println("true(7)");
                        return true;
                    }
                }
            }
            return false;
        }

        boolean isTenthAxiom(Leaf expressionTree){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
            if (expressionTree.getLexeme().equals("->")){
                String a = expressionTree.getRightLeaf().getLexeme();
                if (expressionTree.getLeftLeaf().getLexeme().equals("!")){
                    if (expressionTree.getLeftLeaf().getLeftLeaf().getLexeme().equals("!")){
                        if (expressionTree.getLeftLeaf().getLeftLeaf().getLeftLeaf().equals(a)){
                            System.out.println("true(10)");
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        /**
        boolean isSecondAxiom(){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
        }


        boolean isEighthAxiom(){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
        }

        boolean isNinthAxiom(){
            //Pattern pattern = Pattern.compile("^[(->]+,[->A-Z0-9',|&!()]+,[(->]+,[->A-Z0-9',|&!()]+,[->A-Z0-9',|&!()]+[)]+$");
        }


**/

    }
    public ArrayList<String> annotate(ArrayList<String> proof, ArrayList<String> assumptions, Parser parser){
        ArrayList<String> annotations = new ArrayList<>();
        for (int i = 0; i < proof.size(); i++) {
            System.out.println(parser.parse(proof.get(i)));
            getAnnotation(parser.parse(proof.get(i)),parser);
        }
        return annotations;
    }

    private void getAnnotation(String s,Parser parser) {
        AxiomRecognizer axiomRecognizer = new AxiomRecognizer(s);
        axiomRecognizer.isFirstAxiom(parser.getTree());
        axiomRecognizer.isThirdAxiom(parser.getTree());
        axiomRecognizer.isFourthAxiom(parser.getTree());
        axiomRecognizer.isFifthAxiom(parser.getTree());
        axiomRecognizer.isSixthAxiom(parser.getTree());
        axiomRecognizer.isSeventhAxiom(parser.getTree());
        axiomRecognizer.isTenthAxiom(parser.getTree());


    }

    public void correctAnnotations(ArrayList<String> proof) throws  IncorrectProofException{
    }

    public ArrayList<String> getProofExpressions(String inputFile, Parser parser) {
        inputFile = inputFile.replaceAll(" +","");
        String buffer = "";
        ArrayList<String> result = new ArrayList<>();
        char b;
        int l = inputFile.length();
        for (int i = 0; i < l ; i++) {
            b =inputFile.charAt(i);
            if (b == '\n'){
                //buffer = parser.parse(buffer);
                result.add(buffer);
                //System.out.println(buffer);
                buffer = "";
            }else if (i == l-1){
                buffer = buffer.concat(b + "");
                result.add(buffer);
            }else {
                buffer = buffer.concat(b + "");
            }
        }

        return result;
    }

    public ArrayList<String> getAssumptions(String context) {
        int stop = context.indexOf("|-");
        ArrayList<String> result = new ArrayList<>();
        String buffer = "";
        for (int i = 0; i < stop+1 ; i++) {
            if (context.charAt(i)==',' || i == (stop)){
                result.add(buffer);
                buffer = "";
            }else{
                buffer += context.charAt(i);
            }
        }
        return result;
    }

    public String getExpression(String context) {
        return context.substring(context.indexOf("|-")+2,context.length());
    }



}
/**
 * System.out.println(result.toString());
 String exp = "(->,DSFSDFSD,(->,SDFSDF,SDFSDF))";
 if (exp.matches("(->,[->A-Z0-9()!&|,']+,(->,[->A-Z0-9()!&|,']+,[->A-Z0-9()!&|,']+))")){
 System.out.println("test");
 }

 if (buffer.matches("->,.+,->,.+,.+")){
 System.out.println("test");
 }
 */

