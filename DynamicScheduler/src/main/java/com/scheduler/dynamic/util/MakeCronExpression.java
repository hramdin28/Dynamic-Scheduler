package com.scheduler.dynamic.util;
import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;


public class MakeCronExpression {

  private static Logger log = LoggerFactory.getLogger(MakeCronExpression.class);

  private MakeCronExpression() {
    super();
  }


  /**
   * Function to get next date based on a cron extression and a date
   * 
   * @param cronExp
   * @param date
   * @return
   * @throws ParseException
   */
  public static Date getCronNextDate(String cronExp, Date date) throws ParseException {
    CronExpression cp = new CronExpression(cronExp);
    return cp.getTimeAfter(date);
  }

  /**
   * @param cronExp
   * @param date
   * @param numberDays
   */
  public static void getCronNextDates(String cronExp, Date date, int numberDays) {
    DateTime datetime = new DateTime(date);

    for (int i = 0; i < numberDays; i++) {
      try {
        log.info("" + getCronNextDate(cronExp, datetime.toDate()));
      } catch (ParseException e) {
        log.error("Error when getting next cron dates", e);
      }
      datetime = datetime.plusDays(1);
    }
  }

  /**
   * Generate an Field Expression
   * @param listExpression
   * @return
   */
  private static List<FieldExpression> getFieldExpressionList(int[] listExpression) {

    List<FieldExpression> fieldExp = new ArrayList<>(listExpression.length);
    for (int exp : listExpression) {
      fieldExp.add(FieldExpressionFactory.on(exp));
    }

    return fieldExp;

  }

  /**
   * Generate a cron expression
   * @param frequency
   * @param numDays
   * @param hour
   * @param minute
   * @param months
   * @param dayOfMonth
   * @param dayOfWeek
   * @return
   */
  public static Cron buildQuartzCronExpressionUsingCronBuilder(Freq frequency, int numDays,
      int hour, int minute, int[] months, int[] dayOfMonth, int[] dayOfWeek) {


    List<FieldExpression> monthsExp = getFieldExpressionList(months);

    List<FieldExpression> dayOfMonthExp = getFieldExpressionList(dayOfMonth);

    List<FieldExpression> dayOfWeekExp = getFieldExpressionList(dayOfWeek);

    FieldExpression fieldExpressionYear = FieldExpressionFactory.always();
    FieldExpression fieldExpressionMonth = FieldExpressionFactory.always();
    FieldExpression fieldExpressionDayOfMonth = FieldExpressionFactory.always();
    FieldExpression fieldExpressionDayOfWeek = FieldExpressionFactory.always();

    switch (frequency) {

      case DAILY:
        fieldExpressionDayOfMonth = FieldExpressionFactory.every(numDays);
        fieldExpressionDayOfWeek = FieldExpressionFactory.questionMark();
        break;

      case MONTHLY:
        fieldExpressionDayOfMonth =
        !dayOfMonthExp.isEmpty() ? FieldExpressionFactory.and(dayOfMonthExp)
            : FieldExpressionFactory.always();
        fieldExpressionDayOfWeek = questionMark();
        break;

      case WEEKLY:
        fieldExpressionDayOfWeek =
        !dayOfMonthExp.isEmpty() ? FieldExpressionFactory.and(dayOfWeekExp)
            : FieldExpressionFactory.always();
        fieldExpressionDayOfMonth = questionMark();
        break;

      case YEARLY:
        fieldExpressionDayOfWeek = FieldExpressionFactory.questionMark();
        fieldExpressionDayOfMonth =
            !dayOfMonthExp.isEmpty() ? FieldExpressionFactory.and(dayOfMonthExp)
                : FieldExpressionFactory.always();
            fieldExpressionMonth =
                !monthsExp.isEmpty() ? FieldExpressionFactory.and(monthsExp) : FieldExpressionFactory
                    .always();
                break;

      default:
        break;

    }

    return CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ))
        .withYear(fieldExpressionYear).withDoM(fieldExpressionDayOfMonth)
        .withMonth(fieldExpressionMonth).withDoW(fieldExpressionDayOfWeek)
        .withHour(FieldExpressionFactory.on(hour)).withMinute(FieldExpressionFactory.on(minute))
        .withSecond(FieldExpressionFactory.on(0)).instance();
  }
}
