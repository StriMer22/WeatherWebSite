package com.oplao.model;

public enum WeatherStateExt {

    Clear("clear"),
    PartlyCloudy("partly_cloudy"),
    Cloudy("cloudy"),
    Overcast("overcast"),
    Mist("mist"),
    Fog("fog"),
    FreezingFog("freezing_fog"),
    LightPatchyRain("rain_light_patchy"),
    ModeratePatchyRain("rain_moderate_patchy"),
    HeavyPatchyRain("rain_heavy_patchy"),
    LightRain("rain_light"),
    ModerateRain("rain_moderate"),
    HeavyRain("rain_heavy"),
    RainShower("rain_shower"),
    PatchyStorm("storm_patchy"),
    LightStorm("storm_light"),
    HeavyStorm("storm_heavy"),
    FreezingRain("rain_freezing"),
    HeavyFreezingRain("rain_freezing_heavy"),
    IcePellets("ice_pellets"),
    IcePelletsShowers("ice_pellets_showers"),
    HeavyIcePelletsShowers("ice_pellets_showers_heavy"),
    PatchySleet("sleet_patchy"),
    LightSleet("sleet_light"),
    HeavySleet("sleet_heavy"),
    LightPatchySnow("snow_light_patchy"),
    ModeratePatchySnow("snow_moderate_patchy"),
    HeavyPatchySnow("snow_heavy_patchy"),
    LightSnow("snow_light"),
    ModerateSnow("snow_moderate"),
    HeavySnow("snow_heavy"),
    Snowfall("snowfall"),
    Blizzard("blizzard"),
    LightSnowStorm("snow_light_storm"),
    HeavySnowStorm("snow_heavy_storm");

    private static final WeatherStateExt[] DRY_CLEARANCE_STATES = { Clear, PartlyCloudy };

    private static final WeatherStateExt[] PATCHY_STATES = { LightPatchyRain, ModeratePatchyRain, HeavyPatchyRain, PatchyStorm, PatchySleet,
            LightPatchySnow, ModeratePatchySnow, HeavyPatchySnow };

    private String baseName;

    private WeatherStateExt(String baseName) {

        this.baseName = baseName;
    }

    public String toStringValue() {

        return name();
    }

    public static WeatherStateExt fromStringValue(String value) {

        try {
            return Enum.valueOf(WeatherStateExt.class, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getWeatherIconName(boolean day) {

        return getWeatherIconName(day, false);
    }

    public String getBaseName() {

        return baseName;
    }

    public String getWeatherIconName(boolean day, boolean cloudy) {

        String iconName = "wicon_" + baseName;
        if (isInSet(PATCHY_STATES)) {
            if (cloudy) {
                iconName += "_cloudy";
            } else {
                iconName += getDayTimeSuffix(day);
            }
        } else if (isInSet(DRY_CLEARANCE_STATES)) {
            iconName += getDayTimeSuffix(day);
        }
        return iconName;
    }

    private String getDayTimeSuffix(boolean day) {

        return day ? "_day" : "_night";
    }

    public String getWeatherIconNameOld(boolean day, boolean cloudy) {

        switch (this) {
            case Clear:
                return day ? "wicon_clear_day" : "wicon_clear_night";
            case PartlyCloudy:
                return day ? "wicon_partly_cloudy_day" : "wicon_partly_cloudy_night";
            case Cloudy:
                return "wicon_cloudy";
            case Overcast:
                return "wicon_overcast";
            case Mist:
                return "wicon_mist";
            case Fog:
                return "wicon_fog";
            case FreezingFog:
                return "wicon_freezing_fog";
            case LightPatchyRain:
                return cloudy ? "wicon_rain_light_patchy_cloudy" : (day ? "wicon_rain_light_patchy_day" : "wicon_rain_light_patchy_night");
            case ModeratePatchyRain:
                return cloudy ? "wicon_rain_moderate_patchy_cloudy" : (day ? "wicon_rain_moderate_patchy_day" : "wicon_rain_moderate_patchy_night");
            case HeavyPatchyRain:
                return cloudy ? "wicon_rain_heavy_patchy_cloudy" : (day ? "wicon_rain_heavy_patchy_day" : "wicon_rain_heavy_patchy_night");
            case LightRain:
                return "wicon_rain_light";
            case ModerateRain:
                return "wicon_rain_moderate";
            case HeavyRain:
                return "wicon_rain_heavy";
            case RainShower:
                return "wicon_rain_shower";
            case PatchyStorm:
                return cloudy ? "wicon_storm_patchy_cloudy" : (day ? "wicon_storm_patchy_day" : "wicon_storm_patchy_night");
            case LightStorm:
                return "wicon_storm_light";
            case HeavyStorm:
                return "wicon_storm_heavy";
            case FreezingRain:
                return "wicon_rain_freezing";
            case HeavyFreezingRain:
                return "wicon_rain_freezing_heavy";
            case IcePellets:
                return "wicon_ice_pellets";
            case IcePelletsShowers:
                return "wicon_ice_pellets_showers";
            case HeavyIcePelletsShowers:
                return "wicon_ice_pellets_showers_heavy";
            case PatchySleet:
                return cloudy ? "wicon_sleet_patchy_cloudy" : (day ? "wicon_sleet_patchy_day" : "wicon_sleet_patchy_night");
            case LightSleet:
                return "wicon_sleet_light";
            case HeavySleet:
                return "wicon_sleet_heavy";
            case LightPatchySnow:
                return cloudy ? "wicon_snow_light_patchy_cloudy" : (day ? "wicon_snow_light_patchy_day" : "wicon_snow_light_patchy_night");
            case ModeratePatchySnow:
                return cloudy ? "wicon_snow_moderate_patchy_cloudy" : (day ? "wicon_snow_moderate_patchy_day" : "wicon_snow_moderate_patchy_night");
            case HeavyPatchySnow:
                return cloudy ? "wicon_snow_heavy_patchy_cloudy" : (day ? "wicon_snow_heavy_patchy_day" : "wicon_snow_heavy_patchy_night");
            case LightSnow:
                return "wicon_snow_light";
            case ModerateSnow:
                return "wicon_snow_moderate";
            case HeavySnow:
                return "wicon_snow_heavy";
            case Snowfall:
                return "wicon_snowfall";
            case Blizzard:
                return "wicon_blizzard";
            case LightSnowStorm:
                return "wicon_snow_light_storm";
            case HeavySnowStorm:
                return "wicon_snow_heavy_storm";
            default:
                return "";
        }
    }

    private boolean isInSet(WeatherStateExt[] set) {

        for (WeatherStateExt clearanceState : set) {
            if (clearanceState.equals(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDryWithClearance() {

        return isInSet(DRY_CLEARANCE_STATES);
    }

    public boolean isRain() {

        return LightPatchyRain.equals(this) || ModeratePatchyRain.equals(this) || HeavyPatchyRain.equals(this) || LightRain.equals(this)
                || ModerateRain.equals(this) || HeavyRain.equals(this) || RainShower.equals(this) || FreezingRain.equals(this)
                || HeavyFreezingRain.equals(this) || PatchyStorm.equals(this) || LightStorm.equals(this) || HeavyStorm.equals(this);
    }

    public boolean isSnow() {

        return LightPatchySnow.equals(this) || ModeratePatchySnow.equals(this) || HeavyPatchySnow.equals(this) || LightSnow.equals(this)
                || ModerateSnow.equals(this) || HeavySnow.equals(this) || Snowfall.equals(this) || Blizzard.equals(this)
                || LightSnowStorm.equals(this) || HeavySnowStorm.equals(this);
    }

    public boolean isSleet() {

        return PatchySleet.equals(this) || LightSleet.equals(this) || HeavySleet.equals(this);
    }

    public boolean isStorm() {

        return PatchyStorm.equals(this) || LightStorm.equals(this) || HeavyStorm.equals(this) || LightSnowStorm.equals(this)
                || HeavySnowStorm.equals(this);
    }

    public boolean isHail() {

        return IcePellets.equals(this) || IcePelletsShowers.equals(this) || HeavyIcePelletsShowers.equals(this);
    }

    public boolean isWet() {

        if (Clear.equals(this) || PartlyCloudy.equals(this) || Cloudy.equals(this) || Overcast.equals(this) || Mist.equals(this) || Fog.equals(this)) {
            return false;
        } else {
            return true;
        }
    }
}