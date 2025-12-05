package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils
import org.joda.time.DateTime

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
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Calendar {
    @JsonProperty List<SingleDate> singleDates = []      // Specific individual dates with times
    @JsonProperty List<PatternDate> patternDates = []    // Recurring patterns (e.g., "every Monday from 10:00-17:00")

    @JsonProperty List<ExceptionDate> opens = []         // Special opening dates that override regular schedule
    @JsonProperty List<ExceptionDate> closeds = []       // Special closure dates (e.g., holidays)
    @JsonProperty List<ExceptionDate> soldouts = []      // Dates when sold out
    @JsonProperty List<ExceptionDate> cancelleds = []    // Dates when cancelled

    @JsonProperty boolean excludeholidays                // Exclude public holidays from the schedule
    @JsonProperty boolean cancelled = false              // Event/location is cancelled
    @JsonProperty boolean soldout = false                // Event/location is sold out

    @JsonProperty Boolean onrequest                      // Opening times available on request only
    @JsonProperty Boolean alwaysopen                     // Location is always accessible (24/7)

    @JsonProperty Comment comment                        // Additional comments about the schedule
    @JsonProperty CalendarType calendarType              // Type of calendar pattern used

    /**
     * Defines the primary calendar pattern type.
     * The type is automatically determined based on the data provided, following a priority order.
     */
    static enum CalendarType {
        NONE,           // No scheduling information provided
        ALWAYSOPEN,     // Always accessible (24/7)
        ONREQUEST,      // Available by appointment/request
        OPENINGTIMES,   // Recurring pattern without date limits (ongoing hours)
        PATTERNDATES,   // Recurring pattern with start/end dates (time-limited)
        SINGLEDATES     // Specific individual dates
    }

    void cleanupData() {
        singleDates?.each {
            it.when = it.when?.findAll { it.isValid() }
        }
        patternDates?.each {
            it.opens?.each {
                it.whens = it.whens?.findAll( {it.isValid() } )
            }

            if (it.opens && !it.recurrencyType) {
                it.recurrencyType = PatternDate.RecurrencyType.weekly
            }
        }
        opens?.each {
            it.whens = it.whens?.findAll( {it.isValid() } )
        }
        closeds?.each {
            it.whens = it.whens?.findAll( {it.isValid() } )
        }
        cancelleds?.each {
            it.whens = it.whens?.findAll( {it.isValid() } )
        }
        soldouts?.each {
            it.whens = it.whens?.findAll( {it.isValid() } )
        }
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
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class SingleDate {
        @JsonProperty DateTime date                  // The specific date
        @JsonProperty List<When> when                // Time slots for this date (e.g., 10:00-13:00, 15:00-18:00)

        @Override
        boolean equals(Object o) {
            if (this.is(o)) return true // Use Groovy's `is` for identity check
            if (o == null || getClass() != o.getClass()) return false
            SingleDate singleDate = (SingleDate) o

            if (this.date != singleDate.date) return false
            // Instead of comparing the lists directly, compare their contents in a way that avoids recursion
            if (this.when.size() != singleDate.when.size()) return false
            for (int i = 0; i < this.when.size(); i++) {
                if (!this.when.get(i).isValid() || !singleDate.when.get(i).isValid()) return false
                // Implement further non-recursive element comparison logic here
                if (this.when.get(i) != singleDate.when.get(i)) return false
            }
            return true
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
     * <ul>
     *   <li>Museum open Tue-Sun 10:00-17:00: weekly pattern with opens for days 3-7, 2</li>
     *   <li>Weekly market every Friday 08:00-14:00: weekly pattern with opens for day 6</li>
     *   <li>Bi-weekly event: weekly pattern with recurrence=2</li>
     *   <li>First Saturday of month: monthlyComplex with weeknumber=1, day=7</li>
     * </ul>
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class PatternDate {
        @JsonProperty DateTime startdate             // Start date of pattern (null for ongoing)
        @JsonProperty DateTime enddate               // End date of pattern (null for ongoing)
        @JsonProperty RecurrencyType recurrencyType  // Type of recurrence (daily, weekly, monthly, yearly)

        @JsonProperty Integer occurrence // How many times the pattern repeats
                                        // (e.g., 2 with weekly = valid for 2 weeks)

        @JsonProperty Integer recurrence // Interval between repetitions
                                        // (e.g., 2 with weekly = bi-weekly pattern)
        @JsonProperty List<Open> opens = []          // Opening details (days, times)

        /**
         * Defines how the pattern repeats over time.
         */
        enum RecurrencyType {
            daily,           // Every day or every N days
            weekly,          // Specific days of the week (most common for regular hours)
            monthlySimple,   // Same day each month (e.g., 15th of every month)
            monthlyComplex,  // Relative day in month (e.g., "first Monday", "last Friday")
            yearly           // Annual events
        }

        @Override
        boolean equals(Object obj) {
            if (this.is(obj)) return true
            if (obj == null || getClass() != obj.getClass()) return false
            PatternDate that = (PatternDate) obj

            return this.startdate == that.startdate &&
                    this.enddate == that.enddate &&
                    this.recurrencyType == that.recurrencyType &&
                    this.occurrence == that.occurrence &&
                    this.recurrence == that.recurrence &&
                    // Compare the 'opens' list carefully to avoid recursion
                    opensEquals(this.opens, that.opens)
        }

        private boolean opensEquals(List<Open> opens1, List<Open> opens2) {
            if (opens1.size() != opens2.size()) return false
            for (int i = 0; i < opens1.size(); i++) {
                if (opens1.get(i) != opens2.get(i)) return false
            }
            return true
        }
        /**
         * Represents the specific opening details within a recurring pattern.
         *
         * <p>This class defines which days and times are included in a PatternDate. It supports
         * various levels of specificity from simple day-of-week to complex month/week combinations.</p>
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
         *
         * <h4>Day of Week Mapping:</h4>
         * <pre>
         * 1 = Sunday
         * 2 = Monday
         * 3 = Tuesday
         * 4 = Wednesday
         * 5 = Thursday
         * 6 = Friday
         * 7 = Saturday
         * </pre>
         */
        @ToString(includeNames = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        static class Open {
            @JsonProperty Integer month       // Month number (1-12) for yearly patterns
            @JsonProperty Integer weeknumber  // Week of the month (1-5, where 5 = last week)
            @JsonProperty Integer daynumber   // Day of the month (1-31) for monthly patterns
            @JsonProperty Integer day         // Day of the week (1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat)

            @JsonProperty List<When> whens = []  // Time slots for this opening (e.g., 10:00-13:00, 15:00-18:00)

            @Override
            boolean equals(Object obj) {
                if (this.is(obj)) return true
                if (obj == null || getClass() != obj.getClass()) return false
                Open open = (Open) obj

                return this.month == open.month &&
                        this.weeknumber == open.weeknumber &&
                        this.daynumber == open.daynumber &&
                        this.day == open.day &&
                        whensEquals(this.whens, open.whens)
            }

            private boolean whensEquals(List<When> whens1, List<When> whens2) {
                if (whens1.size() != whens2.size()) return false
                for (int i = 0; i < whens1.size(); i++) {
                    if (whens1.get(i).timestart != whens2.get(i).timestart) return false
                }
                return true
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
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class When {
        @JsonProperty String timestart                          // Start time (e.g., "10:00")
        @JsonProperty String timeend                            // End time (e.g., "17:00")
        @JsonProperty Status status                             // Status of this time slot
        @JsonProperty Boolean valid                             // Whether this time slot is valid
        @JsonProperty List<StatusTranslation> statustranslations = []  // Translated status messages
        @JsonProperty List<ExtraInformation> extrainformations = []    // Additional information
        @JsonProperty List<Contactinfo.Url> urls = []

        @Override
        boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            When when = (When) o;
            return this.timestart == when.timestart && this.timeend == when.timeend;
        }

        @JsonIgnore
        boolean isValid() {
            return isTimeStartValid() || isTimeEndValid()
        }

        @JsonIgnore
        boolean isTimeStartValid() {
            return !StringUtils.isEmpty(timestart?.trim())
        }

        @JsonIgnore
        boolean isTimeEndValid() {
            return !StringUtils.isEmpty(timeend?.trim())
        }
        
        /**
         * Status of a time slot, indicating special conditions.
         */
        enum Status {
            normal,      // Regular opening
            cancelled,   // This time slot is cancelled
            soldout,     // Tickets/capacity sold out
            movedto,     // Event moved to different time/location
            premiere,    // First showing/performance
            reprise      // Repeat showing/performance
        }
    }

    /**
     * Represents a translation for the status of a time slot (`When`) in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class StatusTranslation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    /**
     * Provides extra information associated with a time slot (`When`) in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExtraInformation {
        @JsonProperty String lang
        @JsonProperty String text
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
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExceptionDate {
        @JsonProperty DateTime date                  // The exception date
        @JsonProperty List<When> whens = []          // Time slots for this exception (if applicable)
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
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Comment {
        @JsonProperty String label                          // Main comment text
        @JsonProperty List<CommentTranslation> commentTranslations = []  // Translated versions
    }

    /**
     * Represents a translation for a calendar comment in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class CommentTranslation {
        @JsonProperty String label                          // Translated comment text
        @JsonProperty String lang                           // Language code (e.g., "en", "nl", "de")
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
    void determineCalendarType() {
        if (calendarType != null) {
            // never change it whenever it is set
            return
        }

        if (this.alwaysopen) {
            this.calendarType = CalendarType.ALWAYSOPEN
            return
        }

        if (this.onrequest) {
            this.calendarType = CalendarType.ONREQUEST
            return
        }

        if (this.singleDates?.size() > 0) {
            this.calendarType = CalendarType.SINGLEDATES
            return
        }

        if (this.patternDates?.size() > 0) {
            if (!this.patternDates[0].enddate && !this.patternDates[0].startdate) {
                this.calendarType = CalendarType.OPENINGTIMES
            } else {
                this.calendarType = CalendarType.PATTERNDATES
            }
            return
        }

        this.calendarType = CalendarType.NONE
    }
}