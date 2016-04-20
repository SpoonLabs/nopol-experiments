package org.apache.commons.math.exception.util;

import java.text.Format;
import java.util.Locale;
import java.text.MessageFormat;

/** 
 * Class for constructing localized messages.
 * 
 * @since 2.2
 * @version $Revision$ $Date$
 */
public class MessageFactory {
    /** 
     * Class contains only static methods.
     */
private MessageFactory() {
    }

    /** 
     * Builds a message string by from a pattern and its arguments.
     * 
     * @param locale Locale in which the message should be translated.
     * @param pattern Format specifier.
     * @param arguments Format arguments.
     * @return a localized message string.
     */
public static String buildMessage(Locale locale, Localizable pattern, Object... arguments) {
        return org.apache.commons.math.exception.util.MessageFactory.buildMessage(locale, null, pattern, arguments);
    }

    /** 
     * Builds a message string by from two patterns (specific and general) and
     * an argument list.
     * 
     * @param locale Locale in which the message should be translated.
     * @param specific Format specifier (may be null).
     * @param general Format specifier (may be null).
     * @param arguments Format arguments. They will be substituted first in
     * the {@code specific} format specifier, then the remaining arguments
     * will be substituted in the {@code general} format specifier.
     * @return a localized message string.
     */
public static String buildMessage(Locale locale, Localizable specific, Localizable general, Object... arguments) {
        final StringBuilder sb = new StringBuilder();
        Object[] generalArgs = arguments;
        if (specific != null) {
            final MessageFormat specificFmt = new MessageFormat(specific.getLocalizedString(locale) , locale);
            final int nbSpecific = java.lang.Math.min(arguments.length, specificFmt.getFormatsByArgumentIndex().length);
            final int nbGeneral = (arguments.length) - nbSpecific;
            Object[] specificArgs = new Object[nbSpecific];
            java.lang.System.arraycopy(arguments, 0, specificArgs, 0, nbSpecific);
            generalArgs = new Object[nbGeneral];
            java.lang.System.arraycopy(arguments, nbSpecific, generalArgs, 0, nbGeneral);
            sb.append(specificFmt.format(specificArgs));
        } 
        if (general != null) {
            if (specific!=null)
                sb.append(": ");
            
            final MessageFormat generalFmt = new MessageFormat(general.getLocalizedString(locale) , locale);
            sb.append(generalFmt.format(generalArgs));
        } 
        return sb.toString();
    }
}

