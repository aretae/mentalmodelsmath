package com.l3rnz.algebrain.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SumParser {

    public String dataToBeParsed;
    public StringBuilder builder;
    public int parenthesesCount;
    public int index;
    public boolean usesAdd;

    public SumParser(String input) {
        dataToBeParsed = input;
        builder = new StringBuilder();
    }

    public char getCurrentCharacter() {
        return dataToBeParsed.charAt(index);
    }

    public char getPriorCharacter() {
        char priorCharacter = '+';
        if (index != 0) {
            priorCharacter = dataToBeParsed.charAt(index - 1);
        }
        return priorCharacter;
    }

    public void updateParenCount() {
        if (getCurrentCharacter()== '(') {
            parenthesesCount ++;
        } else if (getCurrentCharacter()== ')') {
            parenthesesCount --;
        }
    }

    public void resetBuilder() {
        builder.delete(0, builder.length());
    }

    void endPartAndAdd(final List<Map.Entry<String, Boolean>> sumParts) {
        final String entryString = (usesAdd ? "" : "-") + builder.toString();
        final Map.Entry<String, Boolean> pair = Map.entry(entryString, usesAdd);
        sumParts.add(pair);
        resetBuilder();
    }

    boolean isCurrentCharacterAnAddOperator() {
        return getCurrentCharacter() == '+' && parenthesesCount == 0;
    }

    public boolean isPriorCharacterNotAnOperator() {
        return !("+-*/".contains(String.valueOf(getPriorCharacter())));
    }

    boolean isCurrentCharacterASubtractOperator() {
        return getCurrentCharacter() == '-' && isPriorCharacterNotAnOperator() && parenthesesCount == 0;
    }

    boolean processAddOperation(final List<Map.Entry<String, Boolean>> sumParts) {
        if (isCurrentCharacterAnAddOperator()) {
            endPartAndAdd(sumParts);
            usesAdd = true;
        }
        return usesAdd;
    }

    boolean processSubtractOperation(final List<Map.Entry<String, Boolean>> sumParts) {
        if (isCurrentCharacterASubtractOperator()) {
            endPartAndAdd(sumParts);
            usesAdd = false;
        }
        return usesAdd;
    }

    void processOtherCharacters() {
        if (!isCurrentCharacterAnAddOperator() && !isCurrentCharacterASubtractOperator()) {
            builder.append(getCurrentCharacter());
        }
    }

    void processOneCharacter(final List<Map.Entry<String, Boolean>> sumParts) {
        updateParenCount();

        usesAdd = processAddOperation(sumParts);
        usesAdd = processSubtractOperation(sumParts);
        processOtherCharacters();
    }

    List<Map.Entry<String, Boolean>> splitSum() {
        final List<Map.Entry<String, Boolean>> sumParts = new ArrayList<>();
        usesAdd = true;
        for (index = 0; index < dataToBeParsed.length(); index ++) {
            processOneCharacter(sumParts);
        }
        endPartAndAdd(sumParts);
        return sumParts;
    }
}
