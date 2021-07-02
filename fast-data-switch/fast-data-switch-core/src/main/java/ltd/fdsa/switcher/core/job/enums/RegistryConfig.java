package ltd.fdsa.switcher.core.job.enums;

public enum RegistryConfig {
    EXECUTOR,
    ADMIN,
    ;
    public static final int BEAT_TIMEOUT = 30;
    public static final int DEAD_TIMEOUT = BEAT_TIMEOUT * 3;


}
