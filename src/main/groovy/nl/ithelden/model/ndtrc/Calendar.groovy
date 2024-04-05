package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils
import org.joda.time.DateTime

@ToString(includeNames = true)
class Calendar {
    @JsonProperty List<SingleDate> singleDates = []
    @JsonProperty List<PatternDate> patternDates = []

    @JsonProperty List<ExceptionDate> opens = []
    @JsonProperty List<ExceptionDate> closeds = []
    @JsonProperty List<ExceptionDate> soldouts = []
    @JsonProperty List<ExceptionDate> cancelleds = []

    @JsonProperty boolean excludeholidays
    @JsonProperty boolean cancelled = false
    @JsonProperty boolean soldout = false

    @JsonProperty Boolean onrequest
    @JsonProperty Boolean alwaysopen

    @JsonProperty Comment comment
    @JsonProperty CalendarType calendarType

    static enum CalendarType { NONE, ALWAYSOPEN, ONREQUEST, OPENINGTIMES, PATTERNDATES, SINGLEDATES }

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

    @ToString(includeNames = true)
    static class SingleDate {
        @JsonProperty DateTime date
        @JsonProperty List<When> when

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

    @ToString(includeNames = true)
    static class PatternDate {
        @JsonProperty DateTime startdate
        @JsonProperty DateTime enddate
        @JsonProperty RecurrencyType recurrencyType

        @JsonProperty Integer occurrence // Optional parameter (int) indicating how many times the pattern is repeated.
                                        // (e.g. a value of 2 in combination with a weekly pattern indicates a pattern
                                        // that is valid for 2 weeks)

        @JsonProperty Integer recurrence // Optional parameter (int) indicating after how many times the pattern is
                                        // repeated. (e.g a value of 2 in combination with a weekly pattern indicates
                                        // a bi-weekly pattern)
        @JsonProperty List<Open> opens = []

        enum RecurrencyType { daily, weekly, monthlySimple, monthlyComplex, yearly }

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
/* Openingstime of the event */
        @ToString(includeNames = true)
        static class Open {
            @JsonProperty Integer month // month number (n-th month
            @JsonProperty Integer weeknumber // weeknumber (n-th week of the month) [1..5]
            @JsonProperty Integer daynumber   // Daynumber (the n-th day of the month)
            @JsonProperty Integer day // Day in the week. Following mapping is applicable:
//                        1:	Sunday
//                        2:	Monday
//                        3:	Tuesday
//                        4:    Wednesday
//                        5:	Thursday
//                        6:	Friday
//                        7:	Saturday

            @JsonProperty List<When> whens = []

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
                    if (!whens1.get(i).isValid() || !whens2.get(i).isValid()) return false

                    if (whens1.get(i) != whens2.get(i)) return false
                }
                return true
            }
        }
    }

    @ToString(includeNames = true)
    static class When {
        @JsonProperty String timestart
        @JsonProperty String timeend
        @JsonProperty Status status
        @JsonProperty Boolean valid
        @JsonProperty List<StatusTranslation> statustranslations = []
        @JsonProperty List<ExtraInformation> extrainformations = []

        @Override
        boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            When when = (When) o;
            return this.timestart == when.timestart && this.timeend == when.timeend;
        }

        boolean isValid() {
            return isTimeStartValid() && isTimeEndValid()
        }

        boolean isTimeStartValid() {
            return !StringUtils.isEmpty(timestart?.trim())
        }

        boolean isTimeEndValid() {
            return !StringUtils.isEmpty(timeend?.trim())
        }
        enum Status { normal, cancelled, soldout, movedto, premiere, reprise }
    }

    @ToString(includeNames = true)
    static class StatusTranslation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    @ToString(includeNames = true)
    static class ExtraInformation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    @ToString(includeNames = true)
    static class ExceptionDate {
        @JsonProperty DateTime date
        @JsonProperty List<When> whens = []
    }

    @ToString(includeNames = true)
    static class Comment {
        @JsonProperty String label
        @JsonProperty List<CommentTranslation> commentTranslations = []
    }

    @ToString(includeNames = true)
    static class CommentTranslation {
        @JsonProperty String label
        @JsonProperty String lang
    }

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