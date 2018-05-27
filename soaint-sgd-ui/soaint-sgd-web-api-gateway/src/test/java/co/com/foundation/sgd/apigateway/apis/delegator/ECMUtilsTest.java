package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.mocks.PartUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ECMUtilsTest {

    final String FILE_1 = "data1.txt";
    final String FILE_2 = "data2.txt";
    final String DATA_1 = "data1";
    final String DATA_2 = "data2";
    final MultipartFormDataInput multipart = PartUtils.newMultiPart()
            .addPart(DATA_1, "This is Value 1")
            .addPart(DATA_2, "This is Value 2")
            .addBinaryPart(FILE_1, "Hello World")
            .addBinaryPart(FILE_2, "Hello Earth")
            .build();

    @Test
    public void findFiles() {

        // when
        Map<String, InputPart> files = ECMUtils.findFiles(multipart);

        // then
        assertThat(files.size()).isEqualTo(2);
        assertThat(files.keySet()).containsExactly(FILE_1, FILE_2);
    }

    @Test
    public void findNameForFileSuccess() {

        // given
        InputPart inputPart = multipart.getFormDataMap().get(FILE_1).get(0);

        // when
        String filename = ECMUtils.findName(inputPart);

        // then
        assertThat(filename).isEqualTo(FILE_1);
    }

    @Test
    public void findNameForFileFail() {

        // given
        InputPart inputPart = multipart.getFormDataMap().get(DATA_1).get(0);

        // when
        String filename = ECMUtils.findName(inputPart);

        // then
        assertThat(filename).isEqualTo("");
    }
}