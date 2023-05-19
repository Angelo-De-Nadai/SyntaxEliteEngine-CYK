package es.ceu.gisi.modcomp.cyk_algorithm.algorithm;

import es.ceu.gisi.modcomp.cyk_algorithm.algorithm.exceptions.CYKAlgorithmException;
import es.ceu.gisi.modcomp.cyk_algorithm.algorithm.interfaces.CYKAlgorithmInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Esta clase contiene la implementación de la interfaz CYKAlgorithmInterface
 * que establece los métodos necesarios para el correcto funcionamiento del
 * proyecto de programación de la asignatura Modelos de Computación.
 *
 * @author Sergio Saugar García <sergio.saugargarcia@ceu.es>
 */
public class CYKAlgorithm implements CYKAlgorithmInterface {

    private String[][] table;

    private List<Character> nonTerminals;
    private List<Character> terminals;
    private Character startSymbol;
    private Map<Character, List<String>> productions;

    public CYKAlgorithm() {
        terminals = new ArrayList();
        nonTerminals = new ArrayList();
        startSymbol = null;
        productions = new HashMap();
    }

    @Override
    /**
     * Método que añade los elementos no terminales de la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     * @throws CYKAlgorithmException Si el elemento no es una letra mayúscula.
     */
    public void addNonTerminal(char nonterminal) throws CYKAlgorithmException {
        if (!nonTerminals.contains(nonterminal) && Character.isLetter(nonterminal) && Character.isUpperCase(nonterminal)) {
            nonTerminals.add(nonterminal);
        } else {
            throw new CYKAlgorithmException();
        }
    }

    @Override
    /**
     * Método que añade los elementos terminales de la gramática.
     *
     * @param terminal Por ejemplo, 'a'
     * @throws CYKAlgorithmException Si el elemento no es una letra minúscula.
     */
    public void addTerminal(char terminal) throws CYKAlgorithmException {
        if (!terminals.contains(terminal) && Character.isLetter(terminal) && Character.isLowerCase(terminal)) {
            terminals.add(terminal);
        } else {
            throw new CYKAlgorithmException();
        }
    }

    @Override
    /**
     * Método que indica, de los elementos no terminales, cuál es el axioma de
     * la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     * @throws CYKAlgorithmException Si el elemento insertado no forma parte del
     * conjunto de elementos no terminales.
     */
    public void setStartSymbol(char nonterminal) throws CYKAlgorithmException {
        if (nonTerminals.contains(nonterminal)) {
            startSymbol = nonterminal;
        } else {
            throw new CYKAlgorithmException();
        }
    }

    @Override
    /**
     * Método utilizado para construir la gramática. Admite producciones en FNC,
     * es decir de tipo A::=BC o A::=a
     *
     * @param nonterminal A
     * @param production "BC" o "a"
     * @throws CYKAlgorithmException Si la producción no se ajusta a FNC o está
     * compuesta por elementos (terminales o no terminales) no definidos
     * previamente.
     */
    public void addProduction(char nonterminal, String production) throws CYKAlgorithmException {
        if (!nonTerminals.contains(nonterminal) || !validProduction(production)) {
            throw new CYKAlgorithmException();
        }
        if (productions.get(nonterminal) != null) {
            if (!productions.get(nonterminal).contains(production)) {//Se già lo contiene
                productions.get(nonterminal).add(production);
            } else {
                throw new CYKAlgorithmException();
            }
        }else{
            ArrayList<String> array = new ArrayList<>();
            array.add(production);
            productions.put(nonterminal, array);
        }

    }

    /**
     * verificación de corrección de sintaxis de producción
     *
     * @param production "BC" o "a"
     * @return true si la producción respeta las reglas de la gramática, si no
     * false
     */
    public boolean validProduction(String production) {
        int length = production.length();
        if (length == 1 && terminals.contains(production.charAt(0))) {
            return true;
        }
        if (length == 2 && nonTerminals.contains(production.charAt(0)) && nonTerminals.contains(production.charAt(1))) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * Método que indica si una palabra pertenece al lenguaje generado por la
     * gramática que se ha introducido.
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     * @return TRUE si la palabra pertenece, FALSE en caso contrario
     * @throws CYKAlgorithmException Si la palabra proporcionada no está formada
     * sólo por terminales, si está formada por terminales que no pertenecen al
     * conjunto de terminales definido para la gramática introducida, si la
     * gramática es vacía o si el autómata carece de axioma.
     */
    public boolean isDerived(String word) throws CYKAlgorithmException {
        if (productions.isEmpty() || terminals.isEmpty() || startSymbol == null) //If there are no productions or terminals or startSymbol is equal to null
        {
            throw new CYKAlgorithmException();
        }

        for (char c : word.toCharArray()) {
            if (!terminals.contains(c)) //If the string doesn't contain only terminals( of the grammar)
            {
                throw new CYKAlgorithmException();
            }
        }
        createMatrix(word);

        //I take the string at the top of the triangle
        String result = table[table[0].length-1][0];

        if (result.contains(Character.toString(startSymbol))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * create and fill the matrix
     * @param word
     */
    private void createMatrix(String word) {
        int length = word.length();
        table = new String[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                table[i][j] = "";
            }
        }
        //forse devo inizializzare tutti gli elemnti della tabella con stringhe vuote

        //fill the first row
        for (int i = 0; i < length; i++) {
            for (Map.Entry<Character, List<String>> entry : productions.entrySet()) //scorrere produzioni in cerca della lettera
            {
                if (entry.getValue().contains(String.valueOf(word.charAt(i)))) {
                    table[0][i] += "" + entry.getKey();
                }
            }
        }

        //start the algorithm
        for (int i = 1; i < length; i++) {
            for (int j = 0; j < length - i; j++) {
                table[i][j] = getCombinationsResult(getColumn(i, j), getDiagonal(i, j));
            }
        }
    }

    /**
     * select the column of elements to compare in the algorithm
     * @param i current row
     * @param j current column
     * @return string array with the elements to compare in the algorithm
     */
    private String[] getDiagonal(int i, int j) {
        String[] array = new String[i];
        int k = 0;
        while (i != 0) {
            i--;
            j++;
            array[k] = table[i][j];
            k++;
        }
        return array;
    }

    /**
     * select the row of elements to compare in the algorithm
     * @param i current row
     * @param j current column
     * @return string array with the elements to compare in the algorithm
     */
    private String[] getColumn(int i, int j) {
        String[] array = new String[i];
        int k=0;
            while(k!=i) {
                array[k] = table[k][j];
                k++;
        }
        return array;
    }

    /**
     * calculates the combinations between the cells of the two arrays and checks that they are valid
     * @param array1
     * @param array2
     * @return Result string of the calculation with the cyk algorithm
     */
    private String getCombinationsResult(String[] array1, String[] array2) {
        String result = "";

        for (int i = 0; i < array1.length; i++) {   // iterate through the two arrays using the same index i

            for (int j = 0; j < array1[i].length(); j++) {  //first string
                for (int k = 0; k < array2[i].length(); k++) {  //second string
                    String s = "" + array1[i].charAt(j) + array2[i].charAt(k);
                    if (isStringContained(s) ) {
                        for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
                            if (entry.getValue().contains(s)&&!isCharacterPresent(result, entry.getKey())) {
                                result += ""+entry.getKey();
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Check if productions contains the entered string
     * @param searchValue string to check
     * @return true if contained, false if it isn't
     */
    private boolean isStringContained(String searchValue) {
        for (List<String> values : productions.values()) {
            if (values.contains(searchValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the key referring to the value contained in the productions map
     * @param value to check
     * @return key referring to the value
     */
    private Character getKeyFromValue(String value) {
        for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey();
            }
        }
        return null; // Ritorna null se il valore non è presente nella mappa
    }

    /**
     * check if a character is present in a string
     * @param str
     * @param ch
     * @return true if contained, false if it isn't
     */
    private boolean isCharacterPresent(String str, Character ch) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {//ci potrebbero essere degli errori per il confronto tra char e character
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Método que, para una palabra, devuelve un String que contiene todas las
     * celdas calculadas por el algoritmo (la visualización debe ser similar al
     * ejemplo proporcionado en el PDF que contiene el paso a paso del
     * algoritmo).
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     * @return Un String donde se vea la tabla calculada de manera completa,
     * todas las celdas que ha calculado el algoritmo.
     * @throws CYKAlgorithmException Si la palabra proporcionada no está formada
     * sólo por terminales, si está formada por terminales que no pertenecen al
     * conjunto de terminales definido para la gramática introducida, si la
     * gramática es vacía o si el autómata carece de axioma.
     */
    public String algorithmStateToString(String word) throws CYKAlgorithmException {
        if (productions.isEmpty() || terminals.isEmpty()) //If there are no productions or terminals
        {
            throw new CYKAlgorithmException();
        }

        for (char c : word.toCharArray()) {
            if (!terminals.contains(c)) //If the string doesn't contain only terminals( of the grammar)
            {
                throw new CYKAlgorithmException();
            }
        }
        createMatrix( word);
        // Calculate the maximum length of strings in each column to paginate after
        int[] maxLength = new int[table[0].length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length - i; j++) {
                int length = table[i][j].length();
                if (length > maxLength[j]) {
                    maxLength[j] = length;
                }
            }
        }

        // Create table string with pagination
        String result = "";
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length - i; j++) {
                String spazi = " ".repeat(maxLength[j] - table[i][j].length() + 1);
                result +=""+ table[i][j] + spazi;
            }
            result += "\n";
        }

        return result;
    }

    @Override
    /**
     * Elimina todos los elementos que se han introducido hasta el momento en la
     * gramática (elementos terminales, no terminales, axioma y producciones),
     * dejando el algoritmo listo para volver a insertar una gramática nueva.
     */
    public void removeGrammar() {
        terminals = new ArrayList();
        nonTerminals = new ArrayList();
        startSymbol = null;
        productions = new HashMap();
    }

    @Override
    /**
     * Devuelve un String que representa todas las producciones que han sido
     * agregadas a un elemento no terminal.
     *
     * @param nonterminal
     * @return Devuelve un String donde se indica, el elemento no terminal, el
     * símbolo de producción "::=" y las producciones agregadas separadas, única
     * y exclusivamente por una barra '|' (no incluya ningún espacio). Por
     * ejemplo, si se piden las producciones del elemento 'S', el String de
     * salida podría ser: "S::=AB|BC".
     */
    public String getProductions(char nonterminal) {
        if (!productions.containsKey(nonterminal)) {
            return "";
        }
        String s = nonterminal + "::=";
        for (String n : productions.get(nonterminal)) {
            s += n + "|";
        }
        return s.substring(0, s.length() - 1);
    }

    @Override
    /**
     * Devuelve un String con la gramática completa.
     *
     * @return Devuelve el agregado de hacer getProductions sobre todos los
     * elementos no terminales.
     */
    public String getGrammar() {
        String grammar = "G=({";
        for (Character c : terminals) {
            grammar += c + ",";
        }
        grammar = grammar.substring(0, grammar.length() - 1);
        grammar += "},{";
        for (Character c : nonTerminals) {
            grammar += c + ",";
        }
        grammar = grammar.substring(0, grammar.length() - 1);
        grammar += "}," + startSymbol + ",P)\nP={\n";
        for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
            grammar += getProductions(entry.getKey()) + "\n";
        }

        return grammar + "}";
    }
}
