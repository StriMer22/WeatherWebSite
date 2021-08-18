package com.oplao.Utils;

import java.util.Arrays;

public class LanguageUtil {

    public static String getCountryCode(String language){
        switch (language){
            case "en":
                return "EN";
            case "ru":
                return "RU";
            case "ua":
                return "UA";
            case "by":
                return "BY";
            case "fr":
                return "FR";
            case "it":
                return "IT";
            case "de":
                return "DE";
        }
        return "en";
    }

    public static boolean isSlav(String countryCode){
        return Arrays.asList("ua", "by", "ru").contains(countryCode);

    }
    public static String validateOldCountryCodes(String langCode) {

        if (langCode.equals("ua")) {
            return "uk";
        } else if (langCode.equals("by")) {
            return "be";
        } else {
            return langCode;
        }
    }

    static final String[][] RU_MONTHS = { { "Январь", "января" }, { "Февраль", "февраля" }, { "Март", "марта" }, { "Апрель", "апреля" },
            { "Май", "мая" }, { "Июнь", "июня" }, { "Июль", "июля" }, { "Август", "августа" }, { "Сентябрь", "сентября" }, { "Октябрь", "октября" },
            { "Ноябрь", "ноября" }, { "Декабрь", "декабря" } };

    static final String[][] UK_MONTHS = { { "Січень", "січня" }, { "Лютий", "лютого" }, { "Березень", "березня" }, { "Квітень", "квітня" },
            { "Травень", "травня" }, { "Червень", "червня" }, { "Липень", "липня" }, { "Серпень", "серпня" }, { "Вересень", "вересня" },
            { "Жовтень", "жовтня" }, { "Листопад", "листопада" }, { "Грудень", "грудня" } };

    static final String[][] BE_MONTHS = { { "Студзень", "студзеня" }, { "Люты", "лютага" }, { "Сакавік", "сакавіка" }, { "Красавік", "красавіка" },
            { "Май", "мая" }, { "Чэрвень", "чэрвеня" }, { "Ліпень", "ліпеня" }, { "Жнівень", "жніўня" }, { "Верасень", "верасня" },
            { "Кастрычнік", "кастрычніка" }, { "Лістапад", "лістапада" }, { "Студзень", "студзеня" } };

    public static String replaceRuMonth(String formattedDate, int monthIndex) {

        return formattedDate.replace(RU_MONTHS[monthIndex][0], RU_MONTHS[monthIndex][1]);
    }

    public static String replaceUkMonth(String formattedDate, int monthIndex) {

        return formattedDate.replace(UK_MONTHS[monthIndex][0], UK_MONTHS[monthIndex][1]);
    }

    public static String replaceBeMonth(String formattedDate, int monthIndex) {

        return formattedDate.replace(BE_MONTHS[monthIndex][0], BE_MONTHS[monthIndex][1]);
    }

    public static String validateSlavCurrentCode(String cityName, String countryCode){
        switch (countryCode){
            case "ua": return ukNameInLocativeCase(cityName, true);
            case "ru": return ruNameInLocativeCase(cityName, true);
            case "by": return beNameInLocativeCase(cityName, true);
            default:return cityName;
        }
    }
    public static String ruNameInLocativeCase(String name, boolean rus) {

        if (name.endsWith("а")) {
            name = name.replaceAll("а$", "е");
        } else if (name.endsWith("ия")) {
            name = name.replaceAll("ия$", "ии");
        } else if (name.endsWith("я")) {
            name = name.replaceAll("я$", "е");
        } else if (name.matches("^.*[бвгджзклмнпрстфхцчшщ]$")) {
            name += "е";
        } else if (name.endsWith("ь")) {
            if (name.matches("^.*[л]ь$")) {
                name = name.replaceAll("ь$", "е");
            } else {
                name = name.replaceAll("ь$", "и");
            }
        } else if (rus && name.endsWith("и")) {
            if (name.matches("^.*[кч]и$")) {
                if (!name.equalsIgnoreCase("сочи")) {
                    name = name.replaceAll("и$", "ах");
                }
            } else {
                if (!name.equalsIgnoreCase("тольятти")) {
                    name = name.replaceAll("и$", "ях");
                }
            }
        }
        return name;
    }

    public static String beNameInLocativeCase(String name, boolean rus) {

        if (name.endsWith("а")) {
            if (name.matches("^.*[ч]а$")) {
                name = name.replaceAll("а$", "ы");
            } else if (name.matches("^.*[д]а$")) {
                name = name.replaceAll("да$", "дзе");
            } else if (name.matches("^.*[г]а$")) {
                name = name.replaceAll("га$", "зе");
            } else {
                name = name.replaceAll("а$", "е");
            }
        } else if (name.endsWith("ія")) {
            name = name.replaceAll("ія$", "іі");
        } else if (name.endsWith("я")) {
            name = name.replaceAll("я$", "і");
        } else if (name.endsWith("е")) {
            name = name.replaceAll("е$", "і");
        } else if (name.endsWith("ў")) {
            name = name.replaceAll("ў$", "ве");
        } else if (name.endsWith("т")) {
            name = name.replaceAll("т$", "це");
        } else if (name.endsWith("д")) {
            name = name.replaceAll("д$", "дзе");
        } else if (name.endsWith("н")) {
            name = name.replaceAll("н$", "не");
        } else if (name.endsWith("м")) {
            name = name.replaceAll("м$", "ме");
        } else if (name.matches("^.*[бвгжзклпрсфхцчш]$")) {
            if (name.matches("^.*[а]к$")) {
                name = name.replaceAll("ак$", "ку");
            } else {
                name += "у";
            }
        } else if (name.endsWith("ь")) {
            name = name.replaceAll("ь$", "і");
        } else if (rus && name.endsWith("ы")) {
            if (!name.equalsIgnoreCase("сочы")) {
                if (name.matches("^.*[в]ы$")) {
                    name += "м";
                } else {
                    name = name.replaceAll("ы$", "ах");
                }
            }
        } else if (rus && name.endsWith("і")) {
            if (!name.equalsIgnoreCase("тальяці")) {
                name = name.replaceAll("і$", "ях");
            }
        }
        return name;
    }

    public static String ukNameInLocativeCase(String name, boolean rus) {

        if (name.endsWith("а")) {
            if (name.matches("^.*[г]а$")) {
                name = name.replaceAll("га$", "зі");
            } else if (name.matches("^.*[к]а$")) {
                name = name.replaceAll("ка$", "ці");
            } else {
                name = name.replaceAll("а$", "і");
            }
        } else if (name.endsWith("ія")) {
            name = name.replaceAll("ія$", "ії");
        } else if (name.endsWith("ий")) {
            name = name.replaceAll("ий$", "ом");
        } else if (name.endsWith("е")) {
            name = name.replaceAll("е$", "і");
        } else if (name.endsWith("о")) {
            name = name.replaceAll("о$", "і");
        } else if (name.endsWith("я")) {
            name = name.replaceAll("я$", "і");
        } else if (name.endsWith("г")) {
            name = name.replaceAll("г$", "зі");
        } else if (name.endsWith("м")) {
            name = name.replaceAll("м$", "мі");
        } else if (name.endsWith("к")) {
            name += "у";
        } else if (name.endsWith("т")) {
            name = name.replaceAll("т$", "ці");
        } else if (name.matches("^.*[бвджзлнпрсфхцчш]$")) {
            if (name.matches("^.*[а]к$")) {
                name = name.replaceAll("ак$", "ку");
            } else if (name.matches("^.*[ї]в$")) {
                name = name.replaceAll("їв$", "єві");
            } else {
                name += "і";
            }
        } else if (name.endsWith("іль")) {
            name = name.replaceAll("іль$", "олі");
        } else if (name.endsWith("ь")) {
            name = name.replaceAll("ь$", "і");
        } else if (rus && name.endsWith("и")) {
            if (!name.equalsIgnoreCase("сочи")) {
                if (name.matches("^.*[в]и$")) {
                    name += "м";
                } else {
                    name = name.replaceAll("и$", "ах");
                }
            }
        } else if (rus && name.endsWith("і")) {
            if (!name.equalsIgnoreCase("тальяці")) {
                name = name.replaceAll("і$", "ях");
            }
        }
        return name;
    }
}
