package com.scheduler.dynamic.util;

/**
 * @author hramdin Enum to get Schedule frequencies
 */
public enum Freq {

  DAILY("DAILY", "DAILY - LAUNCH EVERY DAY"), MONTHLY("MONTHLY", "MONTHLY - LAUNCH EVERY MONTH"), WEEKLY(
      "WEEKLY", "WEEKLY"), YEARLY("YEARLY", "YEARLY");

  private String name;
  private String description;

  private Freq(String name, String description) {
    this.name = name;

    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}