package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

class Calendar {
    @JsonProperty List<SingleDate> singleDates = []
    @JsonProperty List<PatternDate> patternDates = []

    @JsonProperty List<ExceptionDate> opens = []
    @JsonProperty List<ExceptionDate> closeds = []
    @JsonProperty List<ExceptionDate> soldouts = []
    @JsonProperty List<ExceptionDate> cancelleds = []

    @JsonProperty boolean excludeholidays
    @JsonProperty boolean cancelled
    @JsonProperty boolean soldout

    @JsonProperty Boolean onrequest
    @JsonProperty Boolean alwaysopen

    @JsonProperty Comment comment

    static class SingleDate {
        @JsonProperty DateTime date
        @JsonProperty List<When> when
    }

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

    static class When {
        @JsonProperty String timestart
        @JsonProperty String timeend
        @JsonProperty Status status

        @JsonProperty List<StatusTranslation> statustranslations = []
        @JsonProperty List<ExtraInformation> extrainformations = []

        enum Status { normal, cancelled, soldout, movedto, premiere, reprise }
    }
    
    static class StatusTranslation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    static class ExtraInformation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    static class ExceptionDate {
        @JsonProperty DateTime date
        @JsonProperty List<When> whens = []
    }

    static class Comment {
        @JsonProperty String label
        @JsonProperty List<CommentTranslation> commentTranslations = []
    }

    static class CommentTranslation {
        @JsonProperty String label
        @JsonProperty String lang
    }
    
}