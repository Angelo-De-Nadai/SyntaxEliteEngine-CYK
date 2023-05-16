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

    private Character[][][] table;

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
        if (!nonTerminals.contains(nonterminal)) {
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
        if (nonTerminals.contains(nonterminal) && validProduction(production)) {
            if (!productions.get(nonterminal).contains(production)) {
                productions.get(nonterminal).add(production);
            } else {
                throw new CYKAlgorithmException();
            }
        } else {
            throw new CYKAlgorithmException();
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
       if(!productions.containsKey(nonterminal))
            return null;
      String s = nonterminal+"::=";
      for(String n : productions.get(nonterminal))
           s+=n+"|";
      return s.substring(0, s.length()-1);
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
        for(Character c : terminals)
            grammar+=c+",";
        grammar = grammar.substring(0, grammar.length()-1);
        grammar+="},{";
        for(Character c : nonTerminals)
            grammar+=c+",";
        grammar = grammar.substring(0, grammar.length()-1);
        grammar+="},"+startSymbol+",P)\nP={\n";
        for(Map.Entry<Character, List<String>> entry : productions.entrySet())
            grammar+=getProductions(entry.getKey())+"\n";
        
        return grammar+"}";
    }

}
