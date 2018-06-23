package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum CronType implements Serializable {

    MIDNIGHT("0 0 0 * * ?"),
    EVERY_TWELVE_HOURS("0 0 */12 ? * *"),
    EVERY_SIX_HOURS("0 0 */6 ? * *"),
    HOURLY("0 0 * ? * *"),

    EVERY_THIRTY_MINUTES("0 0/30 * * * ?"),
    EVERY_TEN_MINUTES("0 0/10 * * * ?"),
    EVERY_FIVE_MINUTES("0 0/5 * * * ?"),
    EVERY_TWO_MINUTES("0 */2 * ? * *"),
    EVERY_MINUTE("0 * * ? * *"),

    EVERY_THIRTY_SECONDS("*/30 * * * * *"),
    EVERY_TEN_SECONDS("*/10 * * * * *"),
    EVERY_FIVE_SECONDS("*/5 * * * * *"),
    EVERY_SECOND("* * * ? * *");

    private static Long serialVersionUID = 133L;
    private final String cron;
}
