package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nl.ithelden.model.util.StringUtils;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents the opening times and scheduling information for events or locations.
 *
 * <h3>Overview</h3>
 * <p>The Calendar object describes when an event or location is open/available. It supports
 * multiple patterns ranging from simple to complex scheduling scenarios.</p>
 *
 * <h3>Calendar Patterns</h3>
 * <p>There are several ways to define opening times, each with different levels of complexity:</p>
 *
 * <h4>1. Simple Special Cases</h4>
 * <ul>
 *   <li><strong>ALWAYS OPEN</strong> - Set <code>alwaysopen = true</code>. Used for locations
 *       that are accessible 24/7 (e.g., public parks, outdoor monuments)</li>
 *   <li><strong>ON REQUEST</strong> - Set <code>onrequest = true</code>. Used when opening times
 *       are flexible and require advance booking or arrangement</li>
 * </ul>
 *
 * <h4>2. Simple Date/Time Combinations</h4>
 * <p>Use <strong>singleDates</strong> for specific individual dates with times:</p>
 * <ul>
 *   <li>Example: "Friday 2 Jan 10:00-13:00"</li>
 *   <li>Example: "Saturday 15 Mar 14:00-17:00, 19:00-22:00"</li>
 *   <li>Best for: Events with specific dates, temporary exhibitions, special occasions</li>
 * </ul>
 *
 * <h4>3. Recurring Patterns</h4>
 * <p>Use <strong>patternDates</strong> for regular, repeating schedules:</p>
 * <ul>
 *   <li>Example: "Every Monday, Thursday, Friday from 11:00-13:00"</li>
 *   <li>Example: "Every day from 09:00-17:00 (weekdays) and 10:00-16:00 (weekends)"</li>
 *   <li>Example: "First Monday of each month from 14:00-16:00"</li>
 *   <li>Best for: Regular opening hours, weekly events, seasonal schedules</li>
 *   <li>Supports various recurrency types:
 *     <ul>
 *       <li><strong>daily</strong> - Every day or every N days</li>
 *       <li><strong>weekly</strong> - Specific days of the week (most common for opening hours)</li>
 *       <li><strong>monthlySimple</strong> - Same day each month (e.g., 15th of every month)</li>
 *       <li><strong>monthlyComplex</strong> - Relative day in month (e.g., "first Monday", "last Friday")</li>
 *       <li><strong>yearly</strong> - Annual events</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h3>Exception Dates</h3>
 * <p>Override regular schedules for specific dates using exception lists:</p>
 * <ul>
 *   <li><strong>opens</strong> - Special opening dates (e.g., usually closed on Sundays but open this Sunday)</li>
 *   <li><strong>closeds</strong> - Special closure dates (e.g., holidays, maintenance days)</li>
 *   <li><strong>soldouts</strong> - Dates when tickets/capacity is sold out</li>
 *   <li><strong>cancelleds</strong> - Dates when scheduled events are cancelled</li>
 * </ul>
 *
 * <h3>Calendar Type Determination</h3>
 * <p>The system automatically determines the <code>calendarType</code> based on the data provided,
 * in the following priority order:</p>
 * <ol>
 *   <li><strong>ALWAYSOPEN</strong> - When alwaysopen flag is true</li>
 *   <li><strong>ONREQUEST</strong> - When onrequest flag is true</li>
 *   <li><strong>SINGLEDATES</strong> - When singleDates list has entries</li>
 *   <li><strong>OPENINGTIMES</strong> - When patternDates exist without start/end dates (ongoing hours)</li>
 *   <li><strong>PATTERNDATES</strong> - When patternDates exist with start/end dates (time-limited patterns)</li>
 *   <li><strong>NONE</strong> - When no scheduling information is provided</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Calendar {
    @JsonProperty private List<SingleDate> singleDates = new ArrayList<>();      // Specific individual dates with times
    @JsonProperty private List<PatternDate> patternDates = new ArrayList<>();    // Recurring patterns (e.g., "every Monday from 10:00-17:00")

    @JsonProperty private List<ExceptionDate> opens = new ArrayList<>();         // Special opening dates that override regular schedule
    @JsonProperty private List<ExceptionDate> closeds = new ArrayList<>();       // Special closure dates (e.g., holidays)
    @JsonProperty private List<ExceptionDate> soldouts = new ArrayList<>();      // Dates when sold out
    @JsonProperty private List<ExceptionDate> cancelleds = new ArrayList<>();    // Dates when cancelled

    @JsonProperty private boolean excludeholidays;                // Exclude public holidays from the schedule
    @JsonProperty private boolean cancelled = false;              // Event/location is cancelled
    @JsonProperty private boolean soldout = false;                // Event/location is sold out

    @JsonProperty private Boolean onrequest;                      // Opening times available on request only
    @JsonProperty private Boolean alwaysopen;                     // Location is always accessible (24/7)

    @JsonProperty private Comment comment;                        // Additional comments about the schedule
    @JsonProperty private CalendarType calendarType;              // Type of calendar pattern used

    public List<SingleDate> getSingleDates() {
        return singleDates;
    }

    public void setSingleDates(List<SingleDate> singleDates) {
        this.singleDates = singleDates;
    }

    public List<PatternDate> getPatternDates() {
        return patternDates;
    }

    public void setPatternDates(List<PatternDate> patternDates) {
        this.patternDates = patternDates;
    }

    public List<ExceptionDate> getOpens() {
        return opens;
    }

    public void setOpens(List<ExceptionDate> opens) {
        this.opens = opens;
    }

    public List<ExceptionDate> getCloseds() {
        return closeds;
    }

    public void setCloseds(List<ExceptionDate> closeds) {
        this.closeds = closeds;
    }

    public List<ExceptionDate> getSoldouts() {
        return soldouts;
    }

    public void setSoldouts(List<ExceptionDate> soldouts) {
        this.soldouts = soldouts;
    }

    public List<ExceptionDate> getCancelleds() {
        return cancelleds;
    }

    public void setCancelleds(List<ExceptionDate> cancelleds) {
        this.cancelleds = cancelleds;
    }

    public boolean getExcludeholidays() {
        return excludeholidays;
    }

    public boolean isExcludeholidays() {
        return excludeholidays;
    }

    public void setExcludeholidays(boolean excludeholidays) {
        this.excludeholidays = excludeholidays;
    }

    public boolean getCancelled() {
        return cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean getSoldout() {
        return soldout;
    }

    public boolean isSoldout() {
        return soldout;
    }

    public void setSoldout(boolean soldout) {
        this.soldout = soldout;
    }

    public Boolean getOnrequest() {
        return onrequest;
    }

    public Boolean isOnrequest() {
        return onrequest;
    }

    public void setOnrequest(Boolean onrequest) {
        this.onrequest = onrequest;
    }

    public Boolean getAlwaysopen() {
        return alwaysopen;
    }

    public Boolean isAlwaysopen() {
        return alwaysopen;
    }

    public void setAlwaysopen(Boolean alwaysopen) {
        this.alwaysopen = alwaysopen;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public CalendarType getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(CalendarType calendarType) {
        this.calendarType = calendarType;
    }

    /**
     * Defines the primary calendar pattern type.
     * The type is automatically determined based on the data provided, following a priority order.
     */
    public static enum CalendarType {
        NONE,           // No scheduling information provided
        ALWAYSOPEN,     // Always accessible (24/7)
        ONREQUEST,      // Available by appointment/request
        OPENINGTIMES,   // Recurring pattern without date limits (ongoing hours)
        PATTERNDATES,   // Recurring pattern with start/end dates (time-limited)
        SINGLEDATES;    // Specific individual dates

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final CalendarType MIN_VALUE = NONE;
        public static final CalendarType MAX_VALUE = SINGLEDATES;

        public CalendarType next() {
            CalendarType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public CalendarType previous() {
            CalendarType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public void cleanupData() {
        if (singleDates != null) {
            for (SingleDate singleDate : singleDates) {
                singleDate.setWhen(validWhens(singleDate.getWhen()));
            }
        }
        if (patternDates != null) {
            for (PatternDate patternDate : patternDates) {
                if (patternDate.getOpens() != null) {
                    for (PatternDate.Open open : patternDate.getOpens()) {
                        open.setWhens(validWhens(open.getWhens()));
                    }
                }

                if (patternDate.getOpens() != null && !patternDate.getOpens().isEmpty() && patternDate.getRecurrencyType() == null) {
                    patternDate.setRecurrencyType(PatternDate.RecurrencyType.weekly);
                }
            }
        }
        cleanupExceptionDates(opens);
        cleanupExceptionDates(closeds);
        cleanupExceptionDates(cancelleds);
        cleanupExceptionDates(soldouts);
    }

    private static void cleanupExceptionDates(List<ExceptionDate> exceptionDates) {
        if (exceptionDates == null) return;
        for (ExceptionDate exceptionDate : exceptionDates) {
            exceptionDate.setWhens(validWhens(exceptionDate.getWhens()));
        }
    }

    // `whens?.findAll { it.isValid() }`: null stays null, otherwise a new list of the valid ones
    private static List<When> validWhens(List<When> whens) {
        if (whens == null) return null;
        List<When> valid = new ArrayList<>();
        for (When when : whens) {
            if (when.isValid()) {
                valid.add(when);
            }
        }
        return valid;
    }

    // Groovy `==` on two Comparables (DateTime): compareTo() == 0, so the same instant in a
    // different time zone or chronology is equal — unlike DateTime.equals().
    private static boolean sameInstant(DateTime left, DateTime right) {
        if (left == right) return true;
        if (left == null || right == null) return false;
        return left.compareTo(right) == 0;
    }

    /**
     * Represents a single, specific date entry in the calendar with associated time slots.
     *
     * <p>Use this for events or special dates that occur on specific days, such as:</p>
     * <ul>
     *   <li>One-time events: "Friday 2 Jan 2025"</li>
     *   <li>Special opening days: "Christmas Day 25 Dec - 10:00-14:00"</li>
     *   <li>Event series with irregular dates</li>
     * </ul>
     *
     * <p>Each SingleDate can have multiple time slots (When objects) for different
     * opening periods on the same day (e.g., 10:00-13:00 and 15:00-18:00).</p>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SingleDate {
        @JsonProperty private DateTime date;                  // The specific date
        @JsonProperty private List<When> when;                // Time slots for this date (e.g., 10:00-13:00, 15:00-18:00)

        public DateTime getDate() {
            return date;
        }

        public void setDate(DateTime date) {
            this.date = date;
        }

        public List<When> getWhen() {
            return when;
        }

        public void setWhen(List<When> when) {
            this.when = when;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // identity check
            if (o == null || getClass() != o.getClass()) return false;
            SingleDate singleDate = (SingleDate) o;

            if (!sameInstant(this.date, singleDate.date)) return false;
            // Instead of comparing the lists directly, compare their contents in a way that avoids recursion
            if (this.when.size() != singleDate.when.size()) return false;
            for (int i = 0; i < this.when.size(); i++) {
                if (!this.when.get(i).isValid() || !singleDate.when.get(i).isValid()) return false;
                // Implement further non-recursive element comparison logic here
                if (!Objects.equals(this.when.get(i), singleDate.when.get(i))) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(SingleDate.class, this)
                    .add("date", date)
                    .add("when", when)
                    .build();
        }
    }

    /**
     * Represents a recurring pattern in the calendar for regular, repeating schedules.
     *
     * <p>Use this for regular opening hours and repeating events:</p>
     * <ul>
     *   <li><strong>Weekly patterns</strong> - "Every Monday, Thursday, Friday from 11:00-13:00"</li>
     *   <li><strong>Daily patterns</strong> - "Every day from 09:00-17:00"</li>
     *   <li><strong>Monthly patterns</strong> - "First Monday of each month" or "15th of every month"</li>
     *   <li><strong>Ongoing hours</strong> - Leave startdate/enddate empty for permanent opening hours</li>
     *   <li><strong>Seasonal hours</strong> - Set startdate/enddate for time-limited patterns</li>
     * </ul>
     *
     * <h4>Pattern Examples:</h4>
     * <p>Day numbers follow this mapping: 1=Sunday, 2=Monday, 3=Tuesday, 4=Wednesday, 5=Thursday, 6=Friday, 7=Saturday
     * (see {@link Open} class for full documentation).</p>
     * <ul>
     *   <li>Museum open Tue-Sun 10:00-17:00: weekly pattern with opens for days 3-7 (Tue-Sat) and 1 (Sun)</li>
     *   <li>Weekly market every Friday 08:00-14:00: weekly pattern with opens for day 6 (Friday)</li>
     *   <li>Bi-weekly event: weekly pattern with recurrence=2</li>
     *   <li>First Saturday of month: monthlyComplex with weeknumber=1, day=7 (Saturday)</li>
     * </ul>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PatternDate {
        @JsonProperty private DateTime startdate;             // Start date of pattern (null for ongoing)
        @JsonProperty private DateTime enddate;               // End date of pattern (null for ongoing)
        @JsonProperty private RecurrencyType recurrencyType;  // Type of recurrence (daily, weekly, monthly, yearly)

        @JsonProperty private Integer occurrence; // How many times the pattern repeats
                                        // (e.g., 2 with weekly = valid for 2 weeks)

        @JsonProperty private Integer recurrence; // Interval between repetitions
                                        // (e.g., 2 with weekly = bi-weekly pattern)
        @JsonProperty private List<Open> opens = new ArrayList<>();          // Opening details (days, times)

        public DateTime getStartdate() {
            return startdate;
        }

        public void setStartdate(DateTime startdate) {
            this.startdate = startdate;
        }

        public DateTime getEnddate() {
            return enddate;
        }

        public void setEnddate(DateTime enddate) {
            this.enddate = enddate;
        }

        public RecurrencyType getRecurrencyType() {
            return recurrencyType;
        }

        public void setRecurrencyType(RecurrencyType recurrencyType) {
            this.recurrencyType = recurrencyType;
        }

        public Integer getOccurrence() {
            return occurrence;
        }

        public void setOccurrence(Integer occurrence) {
            this.occurrence = occurrence;
        }

        public Integer getRecurrence() {
            return recurrence;
        }

        public void setRecurrence(Integer recurrence) {
            this.recurrence = recurrence;
        }

        public List<Open> getOpens() {
            return opens;
        }

        public void setOpens(List<Open> opens) {
            this.opens = opens;
        }

        /**
         * Defines how the pattern repeats over time.
         */
        public enum RecurrencyType {
            daily,           // Every day or every N days
            weekly,          // Specific days of the week (most common for regular hours)
            monthlySimple,   // Same day each month (e.g., 15th of every month)
            monthlyComplex,  // Relative day in month (e.g., "first Monday", "last Friday")
            yearly;          // Annual events

            // Groovy gives every enum these members; kept so the 1.x API is unchanged.
            public static final RecurrencyType MIN_VALUE = daily;
            public static final RecurrencyType MAX_VALUE = yearly;

            public RecurrencyType next() {
                RecurrencyType[] values = values();
                int ordinal = ordinal() + 1;
                return values[ordinal >= values.length ? 0 : ordinal];
            }

            public RecurrencyType previous() {
                RecurrencyType[] values = values();
                int ordinal = ordinal() - 1;
                return values[ordinal < 0 ? values.length - 1 : ordinal];
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PatternDate that = (PatternDate) obj;

            return sameInstant(this.startdate, that.startdate) &&
                    sameInstant(this.enddate, that.enddate) &&
                    this.recurrencyType == that.recurrencyType &&
                    Objects.equals(this.occurrence, that.occurrence) &&
                    Objects.equals(this.recurrence, that.recurrence) &&
                    // Compare the 'opens' list carefully to avoid recursion
                    opensEquals(this.opens, that.opens);
        }

        private boolean opensEquals(List<Open> opens1, List<Open> opens2) {
            if (opens1.size() != opens2.size()) return false;
            for (int i = 0; i < opens1.size(); i++) {
                if (!Objects.equals(opens1.get(i), opens2.get(i))) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(PatternDate.class, this)
                    .add("startdate", startdate)
                    .add("enddate", enddate)
                    .add("recurrencyType", recurrencyType)
                    .add("occurrence", occurrence)
                    .add("recurrence", recurrence)
                    .add("opens", opens)
                    .build();
        }

        /**
         * Represents the specific opening details within a recurring pattern.
         *
         * <p>This class defines which days and times are included in a PatternDate. It supports
         * various levels of specificity from simple day-of-week to complex month/week combinations.</p>
         *
         * <h4>Day of Week Mapping:</h4>
         * <p>The <code>day</code> field uses the following numbering (based on Java Calendar constants):</p>
         * <pre>
         * 1 = Sunday
         * 2 = Monday
         * 3 = Tuesday
         * 4 = Wednesday
         * 5 = Thursday
         * 6 = Friday
         * 7 = Saturday
         * </pre>
         *
         * <h4>Common Usage Patterns:</h4>
         * <ul>
         *   <li><strong>Weekly schedule</strong> - Set only <code>day</code> field:
         *     <ul>
         *       <li>Every Monday: day=2, whens=[{10:00-17:00}]</li>
         *       <li>Every Friday: day=6, whens=[{11:00-13:00}]</li>
         *     </ul>
         *   </li>
         *   <li><strong>Monthly by date</strong> - Set <code>daynumber</code>:
         *     <ul>
         *       <li>15th of each month: daynumber=15</li>
         *     </ul>
         *   </li>
         *   <li><strong>Monthly by week</strong> - Set <code>weeknumber</code> and <code>day</code>:
         *     <ul>
         *       <li>First Monday: weeknumber=1, day=2</li>
         *       <li>Last Friday: weeknumber=5, day=6</li>
         *     </ul>
         *   </li>
         *   <li><strong>Yearly patterns</strong> - Set <code>month</code>, <code>daynumber</code> or <code>day</code>:
         *     <ul>
         *       <li>Every Christmas: month=12, daynumber=25</li>
         *       <li>First Monday in September: month=9, weeknumber=1, day=2</li>
         *     </ul>
         *   </li>
         * </ul>
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Open {
            @JsonProperty private Integer month;       // Month number (1-12) for yearly patterns
            @JsonProperty private Integer weeknumber;  // Week of the month (1-5, where 5 = last week)
            @JsonProperty private Integer daynumber;   // Day of the month (1-31) for monthly patterns
            @JsonProperty private Integer day;         // Day of the week (1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat)

            @JsonProperty private List<When> whens = new ArrayList<>();  // Time slots for this opening (e.g., 10:00-13:00, 15:00-18:00)

            public Integer getMonth() {
                return month;
            }

            public void setMonth(Integer month) {
                this.month = month;
            }

            public Integer getWeeknumber() {
                return weeknumber;
            }

            public void setWeeknumber(Integer weeknumber) {
                this.weeknumber = weeknumber;
            }

            public Integer getDaynumber() {
                return daynumber;
            }

            public void setDaynumber(Integer daynumber) {
                this.daynumber = daynumber;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

            public List<When> getWhens() {
                return whens;
            }

            public void setWhens(List<When> whens) {
                this.whens = whens;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Open open = (Open) obj;

                return Objects.equals(this.month, open.month) &&
                        Objects.equals(this.weeknumber, open.weeknumber) &&
                        Objects.equals(this.daynumber, open.daynumber) &&
                        Objects.equals(this.day, open.day) &&
                        whensEquals(this.whens, open.whens);
            }

            private boolean whensEquals(List<When> whens1, List<When> whens2) {
                if (whens1.size() != whens2.size()) return false;
                for (int i = 0; i < whens1.size(); i++) {
                    if (!Objects.equals(whens1.get(i).getTimestart(), whens2.get(i).getTimestart())) return false;
                }
                return true;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(Open.class, this)
                        .add("month", month)
                        .add("weeknumber", weeknumber)
                        .add("daynumber", daynumber)
                        .add("day", day)
                        .add("whens", whens)
                        .build();
            }
        }
    }

    /**
     * Represents a specific time slot within a date or pattern.
     *
     * <p>Defines the actual opening hours for a day, such as:</p>
     * <ul>
     *   <li>10:00-13:00 (morning hours)</li>
     *   <li>15:00-18:00 (afternoon hours)</li>
     *   <li>19:00-22:00 (evening hours)</li>
     * </ul>
     *
     * <p>A single date can have multiple When objects to represent split opening times
     * (e.g., closed for lunch break).</p>
     *
     * <p>Time format is typically "HH:mm" (24-hour format).</p>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class When {
        @JsonProperty private String timestart;                          // Start time (e.g., "10:00")
        @JsonProperty private String timeend;                            // End time (e.g., "17:00")
        @JsonProperty private Status status;                             // Status of this time slot
        @JsonProperty private Boolean valid;                             // Whether this time slot is valid
        @JsonProperty private List<StatusTranslation> statustranslations = new ArrayList<>();  // Translated status messages
        @JsonProperty private List<ExtraInformation> extrainformations = new ArrayList<>();    // Additional information
        @JsonProperty private List<Contactinfo.Url> urls = new ArrayList<>();

        public String getTimestart() {
            return timestart;
        }

        public void setTimestart(String timestart) {
            this.timestart = timestart;
        }

        public String getTimeend() {
            return timeend;
        }

        public void setTimeend(String timeend) {
            this.timeend = timeend;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Boolean getValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

        public List<StatusTranslation> getStatustranslations() {
            return statustranslations;
        }

        public void setStatustranslations(List<StatusTranslation> statustranslations) {
            this.statustranslations = statustranslations;
        }

        public List<ExtraInformation> getExtrainformations() {
            return extrainformations;
        }

        public void setExtrainformations(List<ExtraInformation> extrainformations) {
            this.extrainformations = extrainformations;
        }

        public List<Contactinfo.Url> getUrls() {
            return urls;
        }

        public void setUrls(List<Contactinfo.Url> urls) {
            this.urls = urls;
        }

        // Two When objects are equal when timestart and timeend are equal. The 1.x Groovy
        // version wrote `this == o` as its identity check, which in Groovy calls equals()
        // again, so comparing two distinct When objects overflowed the stack; this is the
        // identity check that line was meant to be.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            When when = (When) o;
            return Objects.equals(this.timestart, when.timestart) && Objects.equals(this.timeend, when.timeend);
        }

        @JsonIgnore
        public boolean isValid() {
            return isTimeStartValid() || isTimeEndValid();
        }

        @JsonIgnore
        public boolean isTimeStartValid() {
            return !StringUtils.isEmpty(timestart == null ? null : timestart.trim());
        }

        @JsonIgnore
        public boolean isTimeEndValid() {
            return !StringUtils.isEmpty(timeend == null ? null : timeend.trim());
        }

        /**
         * Status of a time slot, indicating special conditions.
         */
        public enum Status {
            normal,      // Regular opening
            cancelled,   // This time slot is cancelled
            soldout,     // Tickets/capacity sold out
            movedto,     // Event moved to different time/location
            premiere,    // First showing/performance
            reprise;     // Repeat showing/performance

            // Groovy gives every enum these members; kept so the 1.x API is unchanged.
            public static final Status MIN_VALUE = normal;
            public static final Status MAX_VALUE = reprise;

            public Status next() {
                Status[] values = values();
                int ordinal = ordinal() + 1;
                return values[ordinal >= values.length ? 0 : ordinal];
            }

            public Status previous() {
                Status[] values = values();
                int ordinal = ordinal() - 1;
                return values[ordinal < 0 ? values.length - 1 : ordinal];
            }
        }

        @Override
        public String toString() {
            return new ToStringBuilder(When.class, this)
                    .add("timestart", timestart)
                    .add("timeend", timeend)
                    .add("status", status)
                    .add("valid", valid)
                    .add("statustranslations", statustranslations)
                    .add("extrainformations", extrainformations)
                    .add("urls", urls)
                    .add("timeEndValid", isTimeEndValid())
                    .add("timeStartValid", isTimeStartValid())
                    .build();
        }
    }

    /**
     * Represents a translation for the status of a time slot (`When`) in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatusTranslation {
        @JsonProperty private String lang;
        @JsonProperty private String text;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(StatusTranslation.class, this)
                    .add("lang", lang)
                    .add("text", text)
                    .build();
        }
    }

    /**
     * Provides extra information associated with a time slot (`When`) in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ExtraInformation {
        @JsonProperty private String lang;
        @JsonProperty private String text;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(ExtraInformation.class, this)
                    .add("lang", lang)
                    .add("text", text)
                    .build();
        }
    }

    /**
     * Represents an exception to the regular schedule for a specific date.
     *
     * <p>Exception dates override the normal schedule defined in singleDates or patternDates.
     * Use them to handle special circumstances:</p>
     * <ul>
     *   <li><strong>opens</strong> - Unusually open (e.g., normally closed on Sundays but open this Sunday)</li>
     *   <li><strong>closeds</strong> - Exceptionally closed (e.g., public holiday, maintenance day)</li>
     *   <li><strong>soldouts</strong> - Dates when tickets/capacity is sold out</li>
     *   <li><strong>cancelleds</strong> - Cancelled events/dates</li>
     * </ul>
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Museum closed on Christmas Day: add to closeds with date=2025-12-25</li>
     *   <li>Special opening on Sunday: add to opens with date and whens</li>
     *   <li>Event sold out on Saturday evening: add to soldouts with date and specific whens</li>
     * </ul>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ExceptionDate {
        @JsonProperty private DateTime date;                  // The exception date
        @JsonProperty private List<When> whens = new ArrayList<>();          // Time slots for this exception (if applicable)

        public DateTime getDate() {
            return date;
        }

        public void setDate(DateTime date) {
            this.date = date;
        }

        public List<When> getWhens() {
            return whens;
        }

        public void setWhens(List<When> whens) {
            this.whens = whens;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(ExceptionDate.class, this)
                    .add("date", date)
                    .add("whens", whens)
                    .build();
        }
    }

    /**
     * Represents an additional comment or note about the calendar/opening times.
     *
     * <p>Use this to provide extra context or important information, such as:</p>
     * <ul>
     *   <li>"Closed during public holidays"</li>
     *   <li>"Last entry 30 minutes before closing"</li>
     *   <li>"Extended hours during summer season"</li>
     *   <li>"Reservation recommended"</li>
     * </ul>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Comment {
        @JsonProperty private String label;                          // Main comment text
        @JsonProperty private List<CommentTranslation> commentTranslations = new ArrayList<>();  // Translated versions

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<CommentTranslation> getCommentTranslations() {
            return commentTranslations;
        }

        public void setCommentTranslations(List<CommentTranslation> commentTranslations) {
            this.commentTranslations = commentTranslations;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Comment.class, this)
                    .add("label", label)
                    .add("commentTranslations", commentTranslations)
                    .build();
        }
    }

    /**
     * Represents a translation for a calendar comment in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CommentTranslation {
        @JsonProperty private String label;                          // Translated comment text
        @JsonProperty private String lang;                           // Language code (e.g., "en", "nl", "de")

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(CommentTranslation.class, this)
                    .add("label", label)
                    .add("lang", lang)
                    .build();
        }
    }

    /**
     * Automatically determines and sets the calendar type based on the data provided.
     *
     * <p>The determination follows a priority order:</p>
     * <ol>
     *   <li>If calendarType is already set, it is never changed</li>
     *   <li>ALWAYSOPEN - if alwaysopen flag is true</li>
     *   <li>ONREQUEST - if onrequest flag is true</li>
     *   <li>SINGLEDATES - if singleDates list has entries</li>
     *   <li>OPENINGTIMES - if patternDates exist without start/end dates (ongoing hours)</li>
     *   <li>PATTERNDATES - if patternDates exist with start/end dates (time-limited)</li>
     *   <li>NONE - if no scheduling information is provided</li>
     * </ol>
     *
     * <p>This method is typically called after populating the calendar data to ensure
     * the correct type is set for proper display and processing.</p>
     */
    public void determineCalendarType() {
        if (calendarType != null) {
            // never change it whenever it is set
            return;
        }

        if (Boolean.TRUE.equals(this.alwaysopen)) {
            this.calendarType = CalendarType.ALWAYSOPEN;
            return;
        }

        if (Boolean.TRUE.equals(this.onrequest)) {
            this.calendarType = CalendarType.ONREQUEST;
            return;
        }

        if (this.singleDates != null && this.singleDates.size() > 0) {
            this.calendarType = CalendarType.SINGLEDATES;
            return;
        }

        if (this.patternDates != null && this.patternDates.size() > 0) {
            if (this.patternDates.get(0).getEnddate() == null && this.patternDates.get(0).getStartdate() == null) {
                this.calendarType = CalendarType.OPENINGTIMES;
            } else {
                this.calendarType = CalendarType.PATTERNDATES;
            }
            return;
        }

        this.calendarType = CalendarType.NONE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Calendar.class, this)
                .add("singleDates", singleDates)
                .add("patternDates", patternDates)
                .add("opens", opens)
                .add("closeds", closeds)
                .add("soldouts", soldouts)
                .add("cancelleds", cancelleds)
                .add("excludeholidays", excludeholidays)
                .add("cancelled", cancelled)
                .add("soldout", soldout)
                .add("onrequest", onrequest)
                .add("alwaysopen", alwaysopen)
                .add("comment", comment)
                .add("calendarType", calendarType)
                .build();
    }
}
