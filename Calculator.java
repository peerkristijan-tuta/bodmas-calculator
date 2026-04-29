import java.util.*;

class Calculate {
    static String toString(String[] input) { // accessible only through main
        String inputted_expression = "";

        for (int x = 0; x < input.length; x++)
            inputted_expression += input[x];

        String outputted_expression = "";

        for (int x = 0; x < inputted_expression.length(); x++) {
            if (inputted_expression.charAt(x) == '(') {
                if (x == 0) 
                    outputted_expression += inputted_expression.charAt(x);
                else if (inputted_expression.charAt(x-1) == '+' || inputted_expression.charAt(x-1) == '-' || 
                         inputted_expression.charAt(x-1) == 'x' || inputted_expression.charAt(x-1) == '/' || 
                         inputted_expression.charAt(x-1) == '(') 
                    outputted_expression += inputted_expression.charAt(x);
                else if (inputted_expression.charAt(x-1) == ')') 
                    outputted_expression += inputted_expression.charAt(x);
                else 
                    outputted_expression += String.valueOf('x') + String.valueOf(inputted_expression.charAt(x));
            } else if (inputted_expression.charAt(x) == ')') {
                if (inputted_expression.length() - x == 1) 
                    outputted_expression += inputted_expression.charAt(x);
                else if (inputted_expression.charAt(x+1) == '+' || inputted_expression.charAt(x+1) == '-' || inputted_expression.charAt(x+1) == 'x' || 
                         inputted_expression.charAt(x+1) == '/' || inputted_expression.charAt(x+1) == ')') 
                    outputted_expression += inputted_expression.charAt(x);
                else 
                    outputted_expression += String.valueOf(inputted_expression.charAt(x)) + String.valueOf('x');
            } else
                outputted_expression += inputted_expression.charAt(x);
        }

        return outputted_expression;
    }

    static String[] toArray(String input) { // accessible through main, computeExpressionWithBrackets, and computeExpressionWithBODMAS
        int size = 0;

        for (int x = 0; x < input.length(); x++)
            if (input.charAt(x) == '+' || input.charAt(x) == '-' || input.charAt(x) == 'x' || input.charAt(x) == '/')
                if (x != 0)
                    if (input.charAt(x-1) != '+' && input.charAt(x-1) != '-' && input.charAt(x-1) != 'x' && input.charAt(x-1) != '/')
                        size++;

        String[] elements = new String[(size * 2) + 1];
        int increment = 0;

        for (int x = 0; x < elements.length; x++) 
            elements[x] = "";

        for (int x = 0; x < input.length(); x++) {
            if (input.charAt(x) == '+' || input.charAt(x) == '-' || input.charAt(x) == 'x' || input.charAt(x) == '/') {
                if (x == 0)
                    elements[increment] += input.charAt(x);
                else if (input.charAt(x-1) == '+' || input.charAt(x-1) == '-' || input.charAt(x-1) == 'x' || input.charAt(x-1) == '/')
                    elements[increment] += input.charAt(x);
                else {
                    elements[++increment] += input.charAt(x);
                    increment++;
                }
            } else 
                elements[increment] += input.charAt(x);
        }

        return elements;
    }

    static boolean operatorNecessitatesBODMAS(String operator) { // accessible through needsBodmas, computeExpressionWithBrackets, and computeExpressionWithBODMAS
            switch (operator) {
                case "x":
                    return true;
                case "/":
                    return true;
                default:
                    return false;
        }
    }

    static boolean needsBODMAS(String[] expression) { // accessible through main and computeExpressionWithBrackets
        for (int x = 1; x < expression.length; x += 2) 
            if (operatorNecessitatesBODMAS(expression[x]))
                return true;
                
        return false;
    }

    static boolean hasBrackets(String expression) { // accessible through main and computeExpressionWithBrackets
        for (int x = 0; x < expression.length(); x++)
            if (expression.charAt(x) == '(') 
                return true;
            
        return false;
    }

    static String sequentiallyComputeExpression (String[] input) { // accessible through main, computeExpressionWithBODMAS, and computeExpressionWithBrackets
        double num = Double.valueOf(input[0]);

        for (int x = 1; x < input.length; x += 2) {
            switch (input[x]) {
                case "+":
                    num += Double.valueOf(input[x+1]);
                    break;
                case "-":
                    num -= Double.valueOf(input[x+1]);
                    break;
                case "x":
                    num *= Double.valueOf(input[x+1]);
                    break;
                case "/":
                    num /= Double.valueOf(input[x+1]);
                    break;
                case "X":
                    num *= Double.valueOf(input[x+1]);
                    break;
            }
        }

        return String.valueOf(num);
    }

    static String computeExpressionWithBrackets (String expression) { // accessible through main
        class Helper {
            static int determineBracketDepth (String expression_to_be_analyzed) {
                int current_depth = 0;
                int maximum_depth = 0;

                for (int x = 0; x < expression_to_be_analyzed.length(); x++)
                    if (expression_to_be_analyzed.charAt(x) == '(') {
                        current_depth++;
                        if (current_depth > maximum_depth) 
                            maximum_depth++;
                    } else if (expression_to_be_analyzed.charAt(x) == ')')
                        current_depth--;

                return maximum_depth;
            }

            int findFirstTokenOfInnermostExpression(String expression_to_be_analyzed, int count) {
                for (int x = count; ; x--)
                    if (expression_to_be_analyzed.charAt(x) == '(')
                        return (x+1);
            }

            int findNumberOfInnermostSubExpressions(String expression_to_be_analyzed) {
                int layer = 0;
                int quantity = 0;

                for (int x = 0; x < expression_to_be_analyzed.length(); x++) {
                    if (expression_to_be_analyzed.charAt(x) == '(') 
                        layer++;
                    else if (expression_to_be_analyzed.charAt(x) == ')') {
                        if (layer == determineBracketDepth(expression_to_be_analyzed)) {
                            quantity++;
                            layer--;
                        } else 
                            layer--;
                    }
                }

                return quantity;
            }

            int[][] findInnermostExpressions(String expression_to_be_analyzed) {
                int[][] innermost_expression_locations = new int [findNumberOfInnermostSubExpressions(expression_to_be_analyzed)][2];

                for (int x = 0; x < innermost_expression_locations.length; x++)
                    for (int y = 0; y < innermost_expression_locations[0].length; y++)
                        innermost_expression_locations[x][y] = 0;

                int first_dimensional_increment = 0;
                int second_dimensional_increment = 0;
                int layer = 0;
                int depth = determineBracketDepth(expression_to_be_analyzed);

                for (int x = 0; x < expression_to_be_analyzed.length(); x++) {
                    if (expression_to_be_analyzed.charAt(x) == '(') {
                        layer++;
                    } else if (expression_to_be_analyzed.charAt(x) == ')') {
                        if (layer == depth) {
                            innermost_expression_locations[first_dimensional_increment][second_dimensional_increment++] = 
                                findFirstTokenOfInnermostExpression(expression_to_be_analyzed, x);
                            innermost_expression_locations[first_dimensional_increment++][second_dimensional_increment] = x-1;
                            layer--;
                            second_dimensional_increment = 0;
                        } else 
                            layer--;
                    }
                }

                return innermost_expression_locations;
            }

            boolean isInnermost(int index, int[][] innermost_sub_expression_indexes) {
                for (int x = 0; x < innermost_sub_expression_indexes.length; x++)
                    for (int y = 0; y < innermost_sub_expression_indexes[0].length; y++)
                        if (index == innermost_sub_expression_indexes[x][y]) return true;

                return false;
            }

            static String retrieveSubExpression (int count, String expression_to_be_computed) {
                String sub_expression = "";

                for (int x = count + 1; x < expression_to_be_computed.length(); x++)
                    if (expression_to_be_computed.charAt(x) == ')') 
                        break;
                    else 
                        sub_expression += expression_to_be_computed.charAt(x);
        
                return sub_expression;
            }

            int retrieveNextPoint(int current_index, String expression_to_be_computed) {
                int next_point = 0;
        
                for (int x = current_index; x < expression_to_be_computed.length(); x++)
                    if (expression_to_be_computed.charAt(x) == ')') 
                        return (x+1);

                return 0;
            }

            String solveBrackets (String expression_to_be_computed) {
                if (hasBrackets(expression_to_be_computed)) {
                    int[][] innermost_expression_locations = findInnermostExpressions(expression_to_be_computed);
                    String new_expression = "";
                    int first_dimensional_increment = 0;
                    int second_dimensional_increment = 0;
                    int count = 0;

                    for (int x = 0; x < expression_to_be_computed.length(); x++) {           
                        if (expression_to_be_computed.charAt(x) != '(') {
                            new_expression += expression_to_be_computed.charAt(x);

                            if (expression_to_be_computed.length() - x == 1) 
                                return solveBrackets(new_expression);;
                        } else {
                            if (isInnermost((x+1), innermost_expression_locations)) {
                                if (needsBODMAS(toArray(retrieveSubExpression(x, expression_to_be_computed))))
                                    new_expression += computeExpressionWithBODMAS(toArray(retrieveSubExpression(x, expression_to_be_computed)));
                                else
                                    new_expression += sequentiallyComputeExpression(toArray(retrieveSubExpression(x, expression_to_be_computed)));

                                x = retrieveNextPoint(x, expression_to_be_computed);

                                if (x == expression_to_be_computed.length())
                                    return solveBrackets(new_expression);
                                else if (expression_to_be_computed.length() - x == 1) {
                                    new_expression += expression_to_be_computed.charAt(x);
                                    return solveBrackets(new_expression);
                                } else
                                    new_expression += expression_to_be_computed.charAt(x);
                            } else {
                                new_expression += expression_to_be_computed.charAt(x);
                                if (expression_to_be_computed.length() - x == 1)
                                    return solveBrackets(new_expression);
                            }
                        }
                    }
                } else
                    if (needsBODMAS(toArray(expression_to_be_computed)))
                        return computeExpressionWithBODMAS(toArray(expression_to_be_computed));
                    else
                        return sequentiallyComputeExpression(toArray(expression_to_be_computed));
                
                return solveBrackets(expression_to_be_computed);
            }
        }

        Helper h = new Helper();

        return h.solveBrackets(expression);
    }

    static String computeExpressionWithBODMAS(String[] expression) { // accessible through main and computeExpressionWithBrackets
        class Helper {
            int[] findAffectedOperators() {
                int size = 0;

                for (int x = 1; x < expression.length; x += 2)
                    if (operatorNecessitatesBODMAS(expression[x])) 
                        size++;

                int[] affected_operators = new int[size];
                int increment = 0;

                for (int x = 1; x < expression.length; x +=2)
                    if (operatorNecessitatesBODMAS(expression[x])) 
                        affected_operators[increment++] = x;

                return affected_operators;
            }

            String[] computeSubExpressionsThatRequireBODMAS(int[] affected_operators) { 
                int size = 0;

                if (affected_operators.length == 0) 
                    return expression;
        
                for (int x = 0; x < affected_operators.length; x++)
                    if (expression.length - affected_operators[x] == 2)
                        size++;
                    else if (!operatorNecessitatesBODMAS(expression[affected_operators[x]+2]))
                        size++;

                String[] computed_sub_expressions = new String[size];
                
                for (int x = 0; x < computed_sub_expressions.length; x++) 
                    computed_sub_expressions[x] = "";

                int increment = 0;

                for (int x = 0; x < affected_operators.length; x++) {
                    if (affected_operators.length == 1) {           
                        computed_sub_expressions[increment] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        break;
                    } else if (x == 0) {            
                        if (affected_operators[x+1] - affected_operators[x] == 2) 
                            computed_sub_expressions[increment] = expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        else 
                            computed_sub_expressions[increment++] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                    } else if (expression.length - affected_operators[x] == 2) {          
                        if (affected_operators[x] - affected_operators[x-1] == 2)
                            computed_sub_expressions[increment] += expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        else           
                            computed_sub_expressions[increment] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                    } else if (affected_operators.length - x == 1) {            
                        if (affected_operators[x] - affected_operators[x-1] == 2) 
                            computed_sub_expressions[increment] += expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        else 
                            computed_sub_expressions[increment] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                    } else if (affected_operators[x+1] - affected_operators[x] == 2) {       
                        if (affected_operators[x] - affected_operators[x-1] == 2)      
                            computed_sub_expressions[increment] += expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        else       
                            computed_sub_expressions[increment] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                    } else if (affected_operators[x+1] - affected_operators[x] != 2 && affected_operators[x] - affected_operators[x-1] == 2)
                        computed_sub_expressions[increment++] += expression[affected_operators[x]] + expression[affected_operators[x]+1];
                    else { 
                        if (affected_operators[x] - affected_operators[x-1] == 2)
                            computed_sub_expressions[increment] += expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        else {
                            if (x == 0) 
                                computed_sub_expressions[increment++] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                            else 
                                computed_sub_expressions[increment++] += expression[affected_operators[x]-1] + expression[affected_operators[x]] + expression[affected_operators[x]+1];
                        }
                    }
                }

                for (int x = 0; x < computed_sub_expressions.length; x++) 
                    computed_sub_expressions[x] = sequentiallyComputeExpression(toArray(computed_sub_expressions[x]));

                return computed_sub_expressions;
            }

            String[] integrateComputedSubExpressionsThatRequireBODMAS (String[] computed_sub_expressions) {
                String new_expression = "";
                int increment = 0;
                boolean bodmas_recently_inserted = false;

                if (expression.length == 3) {
                    for (String string : expression) 
                        new_expression += string;

                    return toArray(new_expression);
                }

                for (int x = 1; x < expression.length; x += 2) {
                    if ((!expression[x].equals("x")) && (!expression[x].equals("/"))) {
                        if (bodmas_recently_inserted == false) {
                            new_expression += expression[x-1] + expression[x];

                            if (expression.length - x == 2)
                                new_expression += expression[x+1];
                        } else {
                            new_expression += expression[x];
                            bodmas_recently_inserted = false;

                            if (expression.length - x == 2) 
                                new_expression += expression[x+1];
                        }
                    } else {
                        new_expression += computed_sub_expressions[increment++];
                        bodmas_recently_inserted = true;

                        for (int y = x; y < expression.length; y += 2) 
                            if ((!expression[y].equals("x")) && (!expression[y].equals("/"))) {
                                x = y-2;
                                break;
                            } else if (expression.length - y == 2) {
                                x = expression.length;
                                break;
                            }
                    }
                }

                return toArray(new_expression);
            }
        }



        Helper h = new Helper();
        
        return sequentiallyComputeExpression(h.integrateComputedSubExpressionsThatRequireBODMAS(h.computeSubExpressionsThatRequireBODMAS(h.findAffectedOperators())));
    }

    public static void main(String[] args) {
        try {
            String expression = "";

            for (int x = 0; x < args.length; x++)
                expression += args[x];
            
            int unopened_bracket_depth = 0;

            for (int x = 0; x < expression.length(); x++)
                if (expression.charAt(x) == '(')
                    unopened_bracket_depth++;
                else if (expression.charAt(x) == ')')
                    unopened_bracket_depth--;
        
            if (unopened_bracket_depth != 0)
                Integer.valueOf("x");

            if (hasBrackets(toString(args)))
                System.out.print(computeExpressionWithBrackets(toString(args)));
            else if (needsBODMAS(toArray(toString(args))))
                System.out.print(computeExpressionWithBODMAS(toArray(toString(args))));
            else
                System.out.print(sequentiallyComputeExpression(toArray(toString(args))));
        } catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            ArrayList<Integer> problematic_operators = new ArrayList<Integer>();
            HashMap<Integer, Integer> problematic_opening_brackets = new HashMap<Integer, Integer>();
            ArrayList<Integer> problematic_closing_brackets = new ArrayList<Integer>();
            ArrayList<Integer> alphabets = new ArrayList<Integer>();

            boolean bracket_opened = false;

            String expression = "";

            for (int x = 0; x < args.length; x++)
                expression += args[x];

            Integer bracket_depth = 0;
            final Integer last_token = expression.length() - 1;

            for (Integer x = 0; x < expression.length(); x++)
                if (((64 < (int) expression.charAt(x) && expression.charAt(x) < 91) || (96 < (int) expression.charAt(x) && (int) expression.charAt(x) < 123))
                    && ((int) expression.charAt(x) != 120 && (int) expression.charAt(x) != 88))
                    alphabets.add(x);
                else if (47 < (int) expression.charAt(x) && expression.charAt(x) < 58)
                    problematic_operators = new ArrayList<Integer>();
                else
                    switch ((int) expression.charAt(x)) {
                        case 120, 45, 47, 43:
                            problematic_operators.add(x);

                            break;
                        case 40:
                            problematic_opening_brackets.put(++bracket_depth, x);
                            bracket_opened = true;

                            break;
                        case 41:
                            if (!bracket_opened)
                                problematic_closing_brackets.add(x);
                            else
                                problematic_opening_brackets.remove(bracket_depth--);

                            if (bracket_depth == 0)
                                bracket_opened = false;    

                            break;
                    }

            interface dashPrinter {
                void method(String expression);
            }

            dashPrinter dash_printer = (n) -> {
                System.out.println();

                for (int x = 0; x < ("  issue(s) | ".length() + n.length()); x++)
                    System.out.print("-");

                System.out.println();
            };

            dash_printer.method(expression);
 
            System.out.print("expression | " + expression);

            dash_printer.method(expression);

            System.out.print("  issue(s) | ");


            HashMap<String, ArrayList<Integer>> problematic_tokens = new HashMap<String, ArrayList<Integer>>();
            problematic_tokens.put("operators", problematic_operators);
            problematic_tokens.put("opening brackets", new ArrayList<Integer>());
            problematic_tokens.put("closing brackets", problematic_closing_brackets);

            for (Integer problematic_opening_bracket_locations : problematic_opening_brackets.values())
                problematic_tokens.get("opening brackets").add(problematic_opening_bracket_locations);

            problematic_tokens.put("alphabets", alphabets);
            
            String[] token_types = {"operators", "opening brackets", "closing brackets", "alphabets"};
            Integer[][] all_problematic_tokens = new Integer[4][];
            int count = 0;

            for (String token_type : token_types)
                all_problematic_tokens[count++] = problematic_tokens.get(token_type).toArray(new Integer[0]);

            HashMap<Integer, String> problematic_token_classification = new HashMap<Integer, String>();
            count = 0;
            boolean problem_alerted = false;

            for (Integer x = 0; x < expression.length(); x++) {
                for (Integer y = 0; y < all_problematic_tokens.length; y++)
                    for (Integer z = 0; z < all_problematic_tokens[y].length; z++)
                        if (x == all_problematic_tokens[y][z]) {
                            System.out.print(++count);

                            switch (y) {
                                case 0:
                                    problematic_token_classification.put(x, "this operator does not have an operand to operate on");
                                    break;
                                case 1:
                                    problematic_token_classification.put(x, "this opening bracket is not followed by a closing bracket later in the expression");
                                    break;
                                case 2:
                                    problematic_token_classification.put(x, "this closing bracket is not closing any opening bracket");
                                    break;
                                case 3:
                                    problematic_token_classification.put(x, "alphabets are not supported by this calculator");
                                    break;
                            }

                            problem_alerted = true;
                        }
                
                if (problem_alerted)
                    problem_alerted = false;
                else
                    System.out.print(" ");
            }
            
            dash_printer.method(expression);

            System.out.print("Explaination of Issue(s)");
            count = 1;

            for (Integer problematic_token : problematic_token_classification.keySet()) {
                System.out.println();
                System.out.print((count++) + ". " + "Token no. " + (problematic_token+1) + ", '" + 
                                 expression.charAt(problematic_token) + "'," + " is invalid because ");
                System.out.print(problematic_token_classification.get(problematic_token));
            }

            System.out.println();
        }
    }
}