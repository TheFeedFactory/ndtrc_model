package nl.ithelden;

import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.ndtrc.Calendar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalendarTest {
    private static Calendar.When when(String start, String end) {
        Calendar.When when = new Calendar.When();
        when.setTimestart(start);
        when.setTimeend(end);
        return when;
    }

    private static Calendar.CalendarType determine(Calendar calendar) {
        calendar.determineCalendarType();
        return calendar.getCalendarType();
    }

    // ------------------------------------------------------------ determineCalendarType

    @Test
    void emptyCalendarIsNone() {
        Assertions.assertEquals(Calendar.CalendarType.NONE, determine(new Calendar()));
    }

    @Test
    void anExistingTypeIsNeverChanged() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(Calendar.CalendarType.NONE);
        calendar.setAlwaysopen(true);
        Assertions.assertEquals(Calendar.CalendarType.NONE, determine(calendar));
    }

    @Test
    void priorityIsAlwaysOpenThenOnRequestThenSingleDatesThenPatterns() {
        Calendar calendar = new Calendar();
        calendar.setAlwaysopen(true);
        calendar.setOnrequest(true);
        calendar.getSingleDates().add(new Calendar.SingleDate());
        calendar.getPatternDates().add(new Calendar.PatternDate());
        Assertions.assertEquals(Calendar.CalendarType.ALWAYSOPEN, determine(calendar));

        calendar.setCalendarType(null);
        calendar.setAlwaysopen(false);
        Assertions.assertEquals(Calendar.CalendarType.ONREQUEST, determine(calendar));

        calendar.setCalendarType(null);
        calendar.setOnrequest(null);
        Assertions.assertEquals(Calendar.CalendarType.SINGLEDATES, determine(calendar));

        calendar.setCalendarType(null);
        calendar.setSingleDates(null);
        Assertions.assertEquals(Calendar.CalendarType.OPENINGTIMES, determine(calendar));
    }

    @Test
    void patternWithStartOrEndDateIsPatternDates() {
        Calendar withEnd = new Calendar();
        Calendar.PatternDate end = new Calendar.PatternDate();
        end.setEnddate(new DateTime(0L, DateTimeZone.UTC));
        withEnd.getPatternDates().add(end);
        Assertions.assertEquals(Calendar.CalendarType.PATTERNDATES, determine(withEnd));

        Calendar withStart = new Calendar();
        Calendar.PatternDate start = new Calendar.PatternDate();
        start.setStartdate(new DateTime(0L, DateTimeZone.UTC));
        withStart.getPatternDates().add(start);
        Assertions.assertEquals(Calendar.CalendarType.PATTERNDATES, determine(withStart));
    }

    @Test
    void nullListsAreTolerated() {
        Calendar calendar = new Calendar();
        calendar.setSingleDates(null);
        calendar.setPatternDates(null);
        Assertions.assertEquals(Calendar.CalendarType.NONE, determine(calendar));
    }

    // ------------------------------------------------------------ cleanupData

    @Test
    void cleanupDropsWhensWithoutAnyTime() {
        Calendar calendar = new Calendar();
        Calendar.SingleDate singleDate = new Calendar.SingleDate();
        List<Calendar.When> original = new ArrayList<>(List.of(when("10:00", null), when(" ", "  "), when(null, "12:00"), when("", "")));
        singleDate.setWhen(original);
        calendar.getSingleDates().add(singleDate);

        Calendar.ExceptionDate closed = new Calendar.ExceptionDate();
        closed.setWhens(new ArrayList<>(List.of(when(null, null), when("09:00", "10:00"))));
        calendar.getCloseds().add(closed);

        calendar.cleanupData();

        Assertions.assertEquals(2, singleDate.getWhen().size());
        Assertions.assertEquals("10:00", singleDate.getWhen().get(0).getTimestart());
        Assertions.assertEquals("12:00", singleDate.getWhen().get(1).getTimeend());
        Assertions.assertNotSame(original, singleDate.getWhen(), "a new list, the old one is left untouched");
        Assertions.assertEquals(4, original.size());
        Assertions.assertEquals(1, closed.getWhens().size());
    }

    @Test
    void cleanupSetsWeeklyOnlyForPatternsWithOpensAndNoRecurrency() {
        Calendar calendar = new Calendar();
        Calendar.PatternDate withOpens = new Calendar.PatternDate();
        withOpens.getOpens().add(new Calendar.PatternDate.Open());
        Calendar.PatternDate withoutOpens = new Calendar.PatternDate();
        Calendar.PatternDate daily = new Calendar.PatternDate();
        daily.getOpens().add(new Calendar.PatternDate.Open());
        daily.setRecurrencyType(Calendar.PatternDate.RecurrencyType.daily);
        calendar.getPatternDates().addAll(List.of(withOpens, withoutOpens, daily));

        calendar.cleanupData();

        Assertions.assertEquals(Calendar.PatternDate.RecurrencyType.weekly, withOpens.getRecurrencyType());
        Assertions.assertNull(withoutOpens.getRecurrencyType());
        Assertions.assertEquals(Calendar.PatternDate.RecurrencyType.daily, daily.getRecurrencyType());
    }

    @Test
    void cleanupToleratesNullListsEverywhere() {
        Calendar calendar = new Calendar();
        Calendar.SingleDate singleDate = new Calendar.SingleDate();
        calendar.getSingleDates().add(singleDate);
        Calendar.PatternDate patternDate = new Calendar.PatternDate();
        patternDate.setOpens(null);
        calendar.getPatternDates().add(patternDate);
        Calendar.ExceptionDate exceptionDate = new Calendar.ExceptionDate();
        exceptionDate.setWhens(null);
        calendar.getSoldouts().add(exceptionDate);
        calendar.setOpens(null);
        calendar.setCloseds(null);
        calendar.setCancelleds(null);

        Assertions.assertDoesNotThrow(calendar::cleanupData);
        Assertions.assertNull(singleDate.getWhen());
        Assertions.assertNull(exceptionDate.getWhens());
    }

    // ------------------------------------------------------------ When

    @Test
    void whenValidityNeedsANonBlankTime() {
        Assertions.assertFalse(when(null, null).isValid());
        Assertions.assertFalse(when(" ", "\t").isValid());
        Assertions.assertTrue(when("10:00", null).isValid());
        Assertions.assertTrue(when(null, "10:00").isValid());
        Assertions.assertTrue(when("10:00", null).isTimeStartValid());
        Assertions.assertFalse(when("10:00", null).isTimeEndValid());
    }

    @Test
    void whenValidFieldIsIndependentOfIsValid() {
        Calendar.When when = when("10:00", null);
        Assertions.assertNull(when.getValid());
        when.setValid(false);
        Assertions.assertTrue(when.isValid());
        Assertions.assertEquals(Boolean.FALSE, when.getValid());
    }

    @Test
    void whenEqualityIsByStartAndEndTime() {
        // In 1.x this threw StackOverflowError for any two distinct objects (see When.equals).
        Assertions.assertEquals(when("10:00", "11:00"), when("10:00", "11:00"));
        Assertions.assertNotEquals(when("10:00", "11:00"), when("10:00", "12:00"));
        Assertions.assertNotEquals(when("10:00", "11:00"), when("09:00", "11:00"));
        Assertions.assertEquals(when(null, null), when(null, null));
        Assertions.assertNotEquals(when("10:00", "11:00"), null);
        Assertions.assertNotEquals(when("10:00", "11:00"), "10:00");
    }

    // ------------------------------------------------------------ SingleDate / PatternDate / Open equality

    @Test
    void singleDatesCompareInstantsNotZones() {
        DateTime utc = new DateTime(2024, 5, 1, 12, 0, DateTimeZone.UTC);
        Calendar.SingleDate a = new Calendar.SingleDate();
        a.setDate(utc);
        a.setWhen(new ArrayList<>(List.of(when("10:00", "11:00"))));
        Calendar.SingleDate b = new Calendar.SingleDate();
        b.setDate(utc.withZone(DateTimeZone.forID("Europe/Amsterdam")));
        b.setWhen(new ArrayList<>(List.of(when("10:00", "11:00"))));

        Assertions.assertEquals(a, b);

        b.setDate(utc.plusMinutes(1));
        Assertions.assertNotEquals(a, b);
    }

    @Test
    void singleDatesWithInvalidWhensAreNeverEqual() {
        Calendar.SingleDate a = new Calendar.SingleDate();
        a.setWhen(new ArrayList<>(List.of(when("", ""))));
        Calendar.SingleDate b = new Calendar.SingleDate();
        b.setWhen(new ArrayList<>(List.of(when("", ""))));

        Assertions.assertNotEquals(a, b);
        Assertions.assertEquals(a, a);
    }

    @Test
    void singleDatesWithDifferentlySizedWhensDiffer() {
        Calendar.SingleDate a = new Calendar.SingleDate();
        a.setWhen(new ArrayList<>(List.of(when("10:00", null))));
        Calendar.SingleDate b = new Calendar.SingleDate();
        b.setWhen(new ArrayList<>());
        Assertions.assertNotEquals(a, b);
    }

    @Test
    void patternDateEquality() {
        Calendar.PatternDate a = new Calendar.PatternDate();
        a.setStartdate(new DateTime(2024, 1, 1, 0, 0, DateTimeZone.UTC));
        a.setRecurrencyType(Calendar.PatternDate.RecurrencyType.weekly);
        a.setOccurrence(1);
        a.setRecurrence(2);
        Calendar.PatternDate b = new Calendar.PatternDate();
        b.setStartdate(new DateTime(2024, 1, 1, 1, 0, DateTimeZone.forOffsetHours(1)));
        b.setRecurrencyType(Calendar.PatternDate.RecurrencyType.weekly);
        b.setOccurrence(1);
        b.setRecurrence(2);
        Assertions.assertEquals(a, b);

        b.setRecurrence(3);
        Assertions.assertNotEquals(a, b);
        b.setRecurrence(2);

        Calendar.PatternDate.Open openA = new Calendar.PatternDate.Open();
        openA.setDay(2);
        openA.getWhens().add(when("10:00", "11:00"));
        Calendar.PatternDate.Open openB = new Calendar.PatternDate.Open();
        openB.setDay(2);
        openB.getWhens().add(when("10:00", "23:00"));
        a.getOpens().add(openA);
        b.getOpens().add(openB);
        Assertions.assertEquals(a, b, "Open compares only the start times of its whens");

        openB.setDay(3);
        Assertions.assertNotEquals(a, b);
    }

    @Test
    void equalsDoesNotChangeHashCodeFromIdentity() {
        // equals was overridden without hashCode in 1.x; that is kept
        Calendar.When when = when("10:00", null);
        Assertions.assertEquals(System.identityHashCode(when), when.hashCode());
    }

    @Test
    void toStringMatchesThe1xFormat() {
        Assertions.assertEquals("nl.ithelden.model.ndtrc.Calendar(singleDates:[], patternDates:[], opens:[], closeds:[], soldouts:[], "
                + "cancelleds:[], excludeholidays:false, cancelled:false, soldout:false, onrequest:null, alwaysopen:null, comment:null, "
                + "calendarType:null)", new Calendar().toString());
        Assertions.assertEquals("nl.ithelden.model.ndtrc.Calendar$When(timestart:10:00, timeend:null, status:null, valid:null, "
                + "statustranslations:[], extrainformations:[], urls:[], timeEndValid:false, timeStartValid:true)",
                when("10:00", null).toString());
    }
}
