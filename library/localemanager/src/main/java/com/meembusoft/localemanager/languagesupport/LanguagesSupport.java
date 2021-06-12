package com.meembusoft.localemanager.languagesupport;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Instead of using an {@code enum}, a {@code StringDef} is used for
 * providing an easy access to the user for defining the language he wants to
 * use for defining the Locale
 */
public abstract class LanguagesSupport {
    /**
     * Notice that annotations are to be discarded by the compiler.
     */
    @Retention(SOURCE)

    /*
        Available languages
     */
    @StringDef({
            Language.BENGALI,
            Language.GERMAN,
            Language.CHINESE,
            Language.CZECH,
            Language.DUTCH,
            Language.FRENCH,
            Language.ITALIAN,
            Language.JAPANESE,
            Language.KOREAN,
            Language.POLISH,
            Language.RUSSIAN,
            Language.SPANISH,
            Language.ARABIC,
            Language.BULGARIAN,
            Language.CATALAN,
            Language.CROATIAN,
            Language.DANISH,
            Language.FINNISH,
            Language.GREEK,
            Language.HEBREW,
            Language.HINDI,
            Language.HUNGARIAN,
            Language.INDONESIAN,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.NORWEGIAN,
            Language.PORTUGUESE,
            Language.ROMANIAN,
            Language.SERBIAN,
            Language.SLOVAK,
            Language.SLOVENIAN,
            Language.SWEDISH,
            Language.TAGALOG,
            Language.THAI,
            Language.TURKISH,
            Language.UKRAINIAN,
            Language.VIETNAMESE,
            Language.SYSTEM,
            Language.ENGLISH
    })

    /*
        Interface for accessing available languages
     */
    public @interface Language {
        String BENGALI = "bn";
        String SYSTEM = "sys";
        String ENGLISH = "en";
        String GERMAN = "de";
        String CHINESE = "zh";
        String CZECH = "cs";
        String DUTCH = "nl";
        String FRENCH = "fr";
        String ITALIAN = "it";
        String JAPANESE = "ja";
        String KOREAN = "ko";
        String POLISH = "pl";
        String RUSSIAN = "ru";
        String SPANISH = "es";
        String ARABIC = "ar";
        String BULGARIAN = "bg";
        String CATALAN = "ca";
        String CROATIAN = "hr";
        String DANISH = "da";
        String FINNISH = "fi";
        String GREEK = "el";
        String HEBREW = "iw";
        String HINDI = "hi";
        String HUNGARIAN = "hu";
        String INDONESIAN = "in";
        String LATVIAN = "lv";
        String LITHUANIAN = "lt";
        String NORWEGIAN = "nb";
        String PORTUGUESE = "pt";
        String ROMANIAN = "ro";
        String SERBIAN = "sr";
        String SLOVAK = "sk";
        String SLOVENIAN = "sl";
        String SWEDISH = "sv";
        String TAGALOG = "tl";
        String THAI = "th";
        String TURKISH = "tr";
        String UKRAINIAN = "uk";
        String VIETNAMESE = "vi";
    }
}