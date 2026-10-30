package nl.ithelden;

import nl.ithelden.model.ndtrc.SeoMetadata.SeoDetail.OGType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SeoMetadataOGTypeTest {

    @Test
    void keepsTheOneXValuesFirst() {
        Assertions.assertEquals(OGType.event, OGType.values()[0]);
        Assertions.assertEquals(OGType.place, OGType.values()[1]);
        Assertions.assertEquals(OGType.article, OGType.values()[2]);
        Assertions.assertEquals(OGType.organization, OGType.values()[3]);
    }

    @Test
    void addsTheRestOfTheOpenGraphObjectTypes() {
        Assertions.assertEquals(OGType.music_song, OGType.valueOf("music_song"));
        Assertions.assertEquals(OGType.video_tv_show, OGType.valueOf("video_tv_show"));
        Assertions.assertEquals(OGType.website, OGType.valueOf("website"));
    }

    @Test
    void minAndMaxValueFollowTheFirstAndLastConstant() {
        OGType[] values = OGType.values();
        Assertions.assertEquals(values[0], OGType.MIN_VALUE);
        Assertions.assertEquals(values[values.length - 1], OGType.MAX_VALUE);
    }

    @Test
    void nextAndPreviousWrapAround() {
        Assertions.assertEquals(OGType.event, OGType.MAX_VALUE.next());
        Assertions.assertEquals(OGType.MAX_VALUE, OGType.MIN_VALUE.previous());
        Assertions.assertEquals(OGType.place, OGType.event.next());
    }
}
