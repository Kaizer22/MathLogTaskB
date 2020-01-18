package com.company.ExpressionParser;

import java.util.ArrayList;

/**
 * Created by denis on 16.01.20.
 */
public class Leaf {
    ArrayList<String> lexeme; //Маркер листа - лексема; во время парсинга - массив лексем
    ArrayList<Leaf> leafs = null; //Побочные листья

    //Конструктор листа дерева результата парсинга
    //Параматры: маркер листа, побочные листья
    Leaf(ArrayList<String> lex, ArrayList<Leaf> l){
        lexeme = lex;
        leafs = l;
    }

    //Параматры: маркер листа *побочные листья - пока пусты*
    Leaf(ArrayList<String> lex){
        lexeme = lex;
        leafs = new ArrayList<>();
    }

    public Leaf getLeftLeaf(){
        return leafs.get(0);
    }

    public Leaf getRightLeaf(){
        return leafs.get(1);
    }

    public String getLexeme(){
        String result = "";
        for (String lex:lexeme) {
            result = result.concat(lex);
        }
        return result;
    }

    //Перевод в строку
    //Для бинарных операторов, затем для унарного отрицания, затем для переменной
    @Override
    public String toString() {
        if (leafs != null && leafs.size() > 1){
            return("(" +lexeme.get(0) + "," + leafs.get(0).toString() + "," + leafs.get(1).toString() + ")");
        }else if (leafs.size() == 1){
            return("(" +lexeme.get(0)  + leafs.get(0).toString()+")");
        }
        return lexeme.get(0);

    }
}
