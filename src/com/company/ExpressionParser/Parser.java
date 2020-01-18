package com.company.ExpressionParser;

import com.company.ExpressionParser.Leaf;

import java.util.ArrayList;

/**
 * Created by denis on 16.01.20.
 */
public class Parser {

    //TODO доделать реакцию на другие некорректные символы

    private ArrayList<String> lexemes; //Выражение, разбитое на лексемы
    private Lexeme current_operand = Lexeme.Variable; //Текущий приоритетный оператор
    private int brackets_position = -1; //Положение открывающей скобки
    Leaf initial;
    private enum Lexeme{  //Список *enum* возможных лексем, для удобства идентификации
        Variable, Denial, Conjunction, Disjunction, Implication, Bracket_IN, Bracket_OUT
    }

    public Parser(){
        lexemes = new ArrayList<>();

    }

    public String parse(String expression){
        expression = expression.replaceAll(" +","");
        //System.out.println(expression);
        lexemes.clear(); //Очистка лексем с прошлого теста
        buildLex(expression); //Разбивка на лексемы
        initial = new Leaf(lexemes); //Вершина дерева
        buildTree(initial); //Построение дерева
        //System.out.println(lexemes.toString());
        return initial.toString(); //Вывод дерева на экран
        //System.out.println("Parsing in Polish notation is finished");
        //System.out.println("_______________________________________________");
    }

    public Leaf getTree(){
      return initial;
    };

    private void buildLex(String exp){
        boolean isNameBegan = false; //Маркер для лексемы переменная
        boolean isImpBegan = false;  //Маркер для лексемы импликация, состоящей из двух символов
        String buf_name = ""; //Буфер с последней лексемой или же с составным именем (или импликацией)
        int len = exp.length(); //Длина выражения в символах
        char[] expCh = exp.toCharArray();
        char a;
        for (int i = 0; i < len; i++) {
            a = expCh[i];
            if (isVarName(a) && !isNameBegan) { //Если данный символ - часть имени переменной, но начало имени еще не зафиксировано
                buf_name = "";
                buf_name = buf_name.concat(a + "");
                isNameBegan = true;
                if (i == len - 1)   lexemes.add(buf_name);//В конце строки - сразу в лексемы
            } else if (isVarName(a) && isNameBegan) { //Если данный символ - часть имени переменной, а начало имени уже зафиксировано
                buf_name = buf_name.concat(a + "");
                if (i == len - 1)   lexemes.add(buf_name);//В конце строки - сразу в лексемы
            } else if (!isVarName(a) && isNameBegan) {//Если данный символ - не часть имени переменной, но начало имени уже зафиксировано
                isNameBegan = false;    //Имя переменной завершилось
                lexemes.add(buf_name);
                buf_name = "";          //Очистка буфера после вставки
                if (a == '-'){          //Не часть имени переменной - проверка на импликацию
                    isImpBegan = true;  //Начался символ импликации
                    buf_name = a+"";
                }else{
                    lexemes.add(a+"");
                }

            }else{              //Для всего остального: *скобки, операторы*
                if (!isImpBegan && a=='-') { //Проверка на импликацию после скобок - это не обработанный в коде выше случай
                    buf_name = buf_name.concat(a + "");
                    isImpBegan = true;
                }else if(isImpBegan && a=='>'){ //Продолжение импликации
                    buf_name = buf_name.concat(a + "");
                    isImpBegan = false;
                    lexemes.add(buf_name);
                    buf_name = "";
                }else{
                    lexemes.add(a+"");
                }

            }
        }

    }

    //Проверка принадлежности к имени переменной
    private boolean isVarName(char a){
        return ((a >= 'A') && (a <= 'Z')) || ((a >= '0') && (a <= '9')) || (a == 0x0027);
    }

    private  void buildTree(Leaf initial){

        if (initial.lexeme.size() != 1) { //Если лексема не односоставная
            ArrayList<Leaf> children = getChildren(initial, initial.lexeme); //Получаем побочные листья


            initial.leafs.addAll(children); //Добавляем для текущего листа
            if (initial.leafs.size() < 2){  //Унарный ли оператор?
                buildTree(children.get(0)); //Строим деревья для побочных листьев
            }else{
                buildTree(children.get(0));
                buildTree(children.get(1));
            }

        }




    }

    private ArrayList<Leaf> getChildren(Leaf ini,ArrayList<String> initial){
        String lexeme; //Буферная лексема, чтобы постояенно не обращаться к initial
        int index = -1; //Индекс приоритетного оператора
        int brackets_counter = 0; //Показатель "глубины" (скобки)
        Lexeme operand; //Переменная для идентификации лексемы
        for (int i = 0; i < initial.size(); i++){ //Проходим по маркеру текущего листа
            lexeme = initial.get(i);
            operand = getOperand(lexeme);
            if (lexeme.equals("(")){ //Обновляем "глубину"
                brackets_counter++;
                brackets_position = i;
            }else if (lexeme.equals(")")){
                brackets_counter--;
            }
            switch (operand){ //Идентифицируем лексему, сравниваем веса, дабы определить приоритетный оператор на данной глубине
                case Variable:
                    break;
                case Denial:
                    if (getWeight(current_operand)<getWeight(operand) && brackets_counter == 0){
                        current_operand = operand;
                        index = i;
                    }
                    break;
                case Conjunction:
                    if (getWeight(current_operand)<=getWeight(operand) && brackets_counter == 0) {
                        current_operand = operand;
                        index = i;
                    }
                    break;
                case Disjunction:
                    if (getWeight(current_operand)<=getWeight(operand) && brackets_counter == 0) {
                        current_operand = operand;
                        index = i;
                    }
                    break;
                case Implication:
                    if (getWeight(current_operand)<getWeight(operand) && brackets_counter == 0) {
                        current_operand = operand;
                        index = i;
                    }
                    break;
            }
        }
        if (index != -1){ //Если приоритетный оператор получен, то формируем результат, разбивая массив лексем на побочные листы
            ArrayList<String> buf = new ArrayList<>();
            buf.add(getStringLexeme(current_operand));
            ini.lexeme = buf;
            ArrayList<Leaf> result = new ArrayList<>();
            if (buf.get(0).equals("!")){ //Проверка на унарный оператор
                result.add(new Leaf(getSubExpression(initial,index+1,index+1,true)));
            }else{
                result.add(new Leaf(getSubExpression(initial,0,index,false)));
                result.add(new Leaf(getSubExpression(initial,index+1,initial.size(),false)));

            }
            current_operand = Lexeme.Variable; //Сбрасываем приоритетный оператор на нулевой вес

            return result;
        }
        current_operand = Lexeme.Variable;
        System.out.println("Error: operator not found");
        return null; //По-идее, сюда и не дойдет :), т.к. приоритетный оператор существует на любой глубине вложенности
    }


    //Получение вложенного списка лексем
    private ArrayList<String> getSubExpression(ArrayList<String>expression, int start, int finish, boolean checkDenial ){
        ArrayList<String> subExp = new ArrayList<>();
        if (!checkDenial) { //Получение правого или левого выражения бинарного оператора
            for (int i = start; i < finish; i++) {
                subExp.add(expression.get(i));
            }
            if (subExp.get(0).equals("(") && subExp.get(subExp.size()-1).equals(")") && sameDepth(subExp)) {//Удаление лишних внешних скобок
                subExp.remove(0);
                subExp.remove(subExp.size() - 1);
            }
        }else{ //Получение выражения в случае с отрицанием
            int brackets_counter;
            int i = start ;
            if (expression.get(i).equals("(")){ //Обработка скобок после отрицания
                brackets_counter = 1;
                subExp.add(expression.get(i));
                i++;
                while (brackets_counter != 0) {
                    if (expression.get(i).equals("(")) {
                        brackets_counter++;
                        subExp.add(expression.get(i));
                    } else if (expression.get(i).equals(")")) {
                        brackets_counter--;
                        subExp.add(expression.get(i));
                    }else{
                        subExp.add(expression.get(i));
                    }
                    i++;
                }
                subExp.remove(0);
                subExp.remove(subExp.size()-1);
            }else if(expression.get(i).equals("!")) {  //Обработка n-ого отрицания
                while (expression.get(i).equals("!")){
                    subExp.add(expression.get(i));
                    i++;
                }
                if (expression.get(i).equals("(")) { //Обработка скобок после n-ого отрицания
                    brackets_counter = 1;
                    subExp.add(expression.get(i));
                    i++;
                    while (brackets_counter != 0) {
                        if (expression.get(i).equals("(")) {
                            brackets_counter++;
                            subExp.add(expression.get(i));
                        } else if (expression.get(i).equals(")")) {
                            brackets_counter--;
                            subExp.add(expression.get(i));
                        } else {
                            subExp.add(expression.get(i));
                        }
                        i++;
                    }
                }else{
                    subExp.add(expression.get(i));
                }
            }else{
                subExp.add(expression.get(start));
            }

        }
        return subExp;
    }

    //Проверка того, что внешние скоби связаны: _(_()()()_)_, а не ()()()
    private boolean sameDepth(ArrayList<String> exp_lex){
        int br_counter = 0;
        String lexeme;
        for (int i = 0; i < exp_lex.size(); i++) {
            lexeme = exp_lex.get(i);
            br_counter += (lexeme.equals("("))?1:0;
            br_counter += (lexeme.equals(")"))?-1:0;
            if (br_counter == 0 && i != exp_lex.size()-1){
                //System.out.println("FALSE");
                return false;
            }
        }
        return true;

    }

    //Символьное обозначение лексемы, ERROR - вариант не предусмотрен
    private String getStringLexeme(Lexeme op){
        switch (op){

            case Variable:
                return "ERROR";
            case Denial:
                return "!";
            case Conjunction:
                return "&";
            case Disjunction:
                return "|";
            case Implication:
                return "->";
            case Bracket_IN:
                return "ERROR";
            case Bracket_OUT:
                return "ERROR";
            default:
                return "ERROR";
        }
    }

    //Получение операнда по символьному значению лексемы
    private Lexeme getOperand(String lex){
        switch (lex) {
            case "!":
                return Lexeme.Denial;
            case "|":
                return Lexeme.Disjunction;
            case "&":
                return Lexeme.Conjunction;
            case "->":
                return Lexeme.Implication;
            case ")":
                return Lexeme.Bracket_OUT;
            case "(":
                return Lexeme.Bracket_IN;
            default:
                return Lexeme.Variable;
        }
    }

    //Возвращает вес операнда
    private int getWeight(Lexeme operand){
        switch (operand){
            case Denial:
                return 1;
            case Conjunction:
                return 2;
            case Disjunction:
                return 3;
            case Implication:
                return 4;
            default:
                return 0;
        }
    }

    //Вывод дерева на печать
    private void printTree(Leaf in){
        System.out.println(in.toString());

    }

}
