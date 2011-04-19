// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.tools;

import java.text.MessageFormat;

/**
 * GWT
 *
 * FIXME
 *  stub
 *  gettext is not implemented and a lot of other methods are missing
 *  (this won't be done on client anyway)
 */

/**
 * Internationalisation support.
 *
 * @author Immanuel.Scholz
 */
public class I18n {
    /**
     * Translates some text for the current locale.
     * These strings are collected by a script that runs on the source code files.
     * After translation, the localizations are distributed with the main program.
     *
     * @param text the text to translate.
     * Must be a string literal. (No constants or local vars.)
     * Can be broken over multiple lines.
     * An apostrophe ' must be quoted by another apostrophe.
     * @param objects Parameters for the string.
     * Mark occurrences in <code>text</code> with {0}, {1}, ...
     * @return the translated string
     *
     * Example: tr("JOSM''s default value is ''{0}''.", val);
     */
    public static final String tr(String text, Object... objects) {
        return MessageFormat.format(gettext(text, null), objects);
    }

    public static final String tr(String text) {
        if (text == null)
            return null;
        return MessageFormat.format(gettext(text, null), (Object)null);
    }

    /**
     * Provide translation in a context.
     * There can be different translations for the same text (but
     * different context).
     *
     * @param context string that helps translators to find an appropriate
     * translation for <code>text</code>
     * @param text the text to translate
     * @return the translated string
     */
    public static final String trc(String context, String text) {
        if (context == null)
            return tr(text);
        if (text == null)
            return null;
        return MessageFormat.format(gettext(text, context), (Object)null);
    }

    /**
     * Marks a string for translation (such that a script can harvest
     * the translatable strings from the source files).
     *
     * Example:
     * String[] options = new String[] {marktr("up"), marktr("down")};
     * lbl.setText(tr(options[0]));
     */
    public static final String marktr(String text) {
        return text;
    }

    public static final String marktrc(String context, String text) {
        return text;
    }

    /**
     * Example: trn("Found {0} error!", "Found {0} errors!", i, Integer.toString(i));
     */
    public static final String trn(String text, String pluralText, long n, Object... objects) {
        return MessageFormat.format(gettextn(text, pluralText, null, n), objects);
    }

    /**
     * Example: trn("There was an error!", "There were errors!", i);
     */
    public static final String trn(String text, String pluralText, long n) {
        return MessageFormat.format(gettextn(text, pluralText, null, n), (Object)null);
    }

    public static final String trnc(String ctx, String text, String pluralText, long n, Object... objects) {
        return MessageFormat.format(gettextn(text, pluralText, ctx, n), objects);
    }

    public static final String trnc(String ctx, String text, String pluralText, long n) {
        return MessageFormat.format(gettextn(text, pluralText, ctx, n), (Object)null);
    }

    private static final String gettext(String text, String ctx)
    {
//        int i;
//        if(ctx == null && text.startsWith("_:") && (i = text.indexOf("\n")) >= 0)
//        {
//            ctx = text.substring(2,i-1);
//            text = text.substring(i+1);
//        }
//        if(strings != null)
//        {
//            String trans = strings.get(ctx == null ? text : "_:"+ctx+"\n"+text);
//            if(trans != null)
//                return trans;
//        }
//        if(pstrings != null) {
//            String[] trans = pstrings.get(ctx == null ? text : "_:"+ctx+"\n"+text);
//            if(trans != null)
//                return trans[0];
//        }
        return text;
    }

    private static final String gettextn(String text, String plural, String ctx, long num)
    {
//        int i;
//        if(ctx == null && text.startsWith("_:") && (i = text.indexOf("\n")) >= 0)
//        {
//            ctx = text.substring(2,i-1);
//            text = text.substring(i+1);
//        }
//        if(pstrings != null)
//        {
//            i = pluralEval(num);
//            String[] trans = pstrings.get(ctx == null ? text : "_:"+ctx+"\n"+text);
//            if(trans != null && trans.length > i)
//                return trans[i];
//        }

        return num == 1 ? text : plural;
    }

}
