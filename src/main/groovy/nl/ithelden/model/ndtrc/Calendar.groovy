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

        boolean isValid() {
            return !StringUtils.isEmpty(timestart?.trim()) || !StringUtils.isEmpty(timeend?.trim())
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