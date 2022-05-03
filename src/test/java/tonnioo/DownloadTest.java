package tonnioo;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;


public class DownloadTest {
    ClassLoader cl = DownloadTest.class.getClassLoader();

    @Test
    void SelenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File textFile = $("#raw-url").download();
        InputStream is = new FileInputStream(textFile);
        byte[] fileContent = is.readAllBytes();
        String strContent = new String(fileContent, StandardCharsets.UTF_8);
        ///org.assertj.core.api.Assertions.assertThat(strContent).contains("Junit 5");
    }

    @Test
    void pdfParsingTest() throws Exception {

        InputStream stream = cl.getResourceAsStream("pdf/junit-user-guide-5.8.2.pdf");
        PDF pdf = new PDF(stream);
        Assertions.assertEquals(166, pdf.numberOfPages);

    }

    @Test
    void xlsParsingTest() throws Exception {

        InputStream stream = cl.getResourceAsStream("xls/file_example_XLS_50.xls");
        XLS xls = new XLS(stream);
        String stringCellValue = xls.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue();
        org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("Philip");

    }

    @Test
    void csvParsingTest() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("csv/tests.csv");

             CSVReader reader = new CSVReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            List<String[]> content = reader.readAll();
            org.assertj.core.api.Assertions.assertThat(content).contains(
                    new String[]{"Name", "Surname"},
                    new String[]{"Anton", "Vlasov"},
                    new String[]{"Alex", "Pasternak"}
            );
        }
    }

    @Test
    void xlsParsingTest2() throws Exception {
        Selenide.open("http://romashka2008.ru/price");
        File xlsDownload = Selenide.$(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS(xlsDownload);
        assertThat(xls.
                excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue()).contains("693010");
    }

    @Test
    void zipParsingTest() throws Exception {
        try(InputStream is = cl.getResourceAsStream("zip/123.zip");
        ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null);
            org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("123.txt");

        }
    }

    @Test
    void zipParsingTest2()  throws Exception{
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/123.zip/"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null){
            org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("123.txt");


        }

    }
}


