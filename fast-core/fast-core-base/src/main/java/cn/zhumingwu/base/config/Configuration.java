package cn.zhumingwu.base.config;


public interface Configuration {
    String get(String path);

    Configuration[] getConfigurations(String path);

    Configuration getConfiguration(String path);

    void set(String path, String value);

    Configuration clone();

    /**
     * 根据用户提供的json path，寻址String对象，如果对象不存在，返回默认字符串
     *
     * @return String对象，如果path不存在或者String不存在，返回默认字符串
     */
    default String getString(String path) {
        return this.get(path);
    }

    /**
     * 根据用户提供的json path，寻址String对象，如果对象不存在，返回默认字符串
     *
     * @return String对象，如果path不存在或者String不存在，返回默认字符串
     */
    default String getString(String path, String defaultValue) {
        var result = this.get(path);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 根据用户提供的json path，寻址Boolean对象
     *
     * @return Boolean对象，如果path值非true,false ，将报错.特别注意：当 path 不存在时，会返回：null.
     */
    default Boolean getBool(String path) {
        var result = this.get(path);
        if (result == null) {
            return false;
        }
        return Boolean.parseBoolean(result);
    }

    /**
     * 根据用户提供的json path，寻址Boolean对象，如果对象不存在，返回默认Boolean对象
     *
     * @return Boolean对象，如果path不存在或者Boolean不存在，返回默认Boolean对象
     */
    default Boolean getBool(String path, boolean defaultValue) {
        var result = this.get(path);
        if (result == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(result);
    }

    /**
     * 根据用户提供的json path，寻址Integer对象
     *
     * @return Integer对象，如果path不存在或者Integer不存在，返回null
     */
    default Integer getInt(String path) {
        var result = this.get(path);
        if (result == null) {
            return 0;
        }
        return Integer.parseInt(result);
    }

    /**
     * 根据用户提供的json path，寻址Integer对象，如果对象不存在，返回默认Integer对象
     *
     * @return Integer对象，如果path不存在或者Integer不存在，返回默认Integer对象
     */
    default Integer getInt(String path, int defaultValue) {
        var result = this.get(path);
        if (result == null) {
            return defaultValue;
        }
        return Integer.parseInt(result);
    }

    /**
     * 根据用户提供的json path，寻址Long对象
     *
     * @return Long对象，如果path不存在或者Long不存在，返回null
     */
    default Long getLong(String path) {
        var result = this.get(path);
        if (result == null) {
            return 0L;
        }
        return Long.parseLong(result);
    }

    /**
     * 根据用户提供的json path，寻址Long对象，如果对象不存在，返回默认Long对象
     *
     * @return Long对象，如果path不存在或者Integer不存在，返回默认Long对象
     */
    default Long getLong(String path, long defaultValue) {
        var result = this.get(path);
        if (result == null) {
            return defaultValue;
        }
        return Long.parseLong(result);
    }

    /**
     * 根据用户提供的json path，寻址Double对象
     *
     * @return Double对象，如果path不存在或者Double不存在，返回null
     */
    default Double getDouble(String path) {
        var result = this.get(path);
        if (result == null) {
            return 0D;
        }
        return Double.parseDouble(result);
    }

    /**
     * 根据用户提供的json path，寻址Double对象，如果对象不存在，返回默认Double对象
     *
     * @return Double对象，如果path不存在或者Double不存在，返回默认Double对象
     */
    default Double getDouble(String path, double defaultValue) {
        var result = this.get(path);
        if (result == null) {
            return defaultValue;
        }
        return Double.parseDouble(result);
    }
}
