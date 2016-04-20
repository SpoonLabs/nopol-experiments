package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

/** 
 * Translate XML numeric entities of the form &#[xX]?\d+;? to
 * the specific codepoint.
 * 
 * Note that the semi-colon is optional.
 * 
 * @since 3.0
 * @version $Id$
 */
public class NumericEntityUnescaper extends CharSequenceTranslator {
    /** 
     * {@inheritDoc}
     */
@Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int seqEnd = input.length();
        if ((((input.charAt(index)) == '&') && (index < (seqEnd - 2))) && ((input.charAt((index + 1))) == '#')) {
            int start = index + 2;
            boolean isHex = false;
            char firstChar = input.charAt(start);
            if ((firstChar == 'x') || (firstChar == 'X')) {
                start++;
                isHex = true;
                if ((start)==(seqEnd))
                    return 0;
                
            } 
            int end = start;
            while ((end < seqEnd) && (((((input.charAt(end)) >= '0') && ((input.charAt(end)) <= '9')) || (((input.charAt(end)) >= 'a') && ((input.charAt(end)) <= 'f'))) || (((input.charAt(end)) >= 'A') && ((input.charAt(end)) <= 'F')))) {
                end++;
            }
            int entityValue;
            try {
                if (isHex) {
                    entityValue = java.lang.Integer.parseInt(input.subSequence(start, end).toString(), 16);
                } else {
                    entityValue = java.lang.Integer.parseInt(input.subSequence(start, end).toString(), 10);
                }
            } catch (NumberFormatException nfe) {
                return 0;
            }
            if (entityValue > 65535) {
                char[] chrs = java.lang.Character.toChars(entityValue);
                out.write(chrs[0]);
                out.write(chrs[1]);
            } else {
                out.write(entityValue);
            }
            boolean semiNext = (end != seqEnd) && ((input.charAt(end)) == ';');
            return ((2 + (end - start)) + (isHex ? 1 : 0)) + (semiNext ? 1 : 0);
        } 
        return 0;
    }
}

