package org.jabref.logic.pdf.search;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

import org.apache.lucene.document.Document;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DocumentReaderTest {

    @Test(expected = IOException.class)
    public void unknownFileTestShouldThrowIOException() throws IOException {
        Path example = Paths.get("src/test/resources/pdfs/NOT_PRESENT.pdf");
        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.of(example.toString()));

        new DocumentReader(entry).readPDFContents();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unknownFileTestShouldThrowIllegalArgumentException() throws IOException {
        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.empty());

        new DocumentReader(entry);
    }

    @Test
    public void exampleTest() throws IOException {
        Path example = Paths.get("src/test/resources/pdfs/example.pdf");

        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.of(example.toString()));
        when(entry.getCiteKeyOptional()).thenReturn(Optional.of("Example2017"));

        Document doc = new DocumentReader(entry).readPDFContents();

        assertEquals("Example2017", doc.get(SearchFieldConstants.KEY));
        assertFalse(doc.get(SearchFieldConstants.CONTENT).isEmpty());
        assertTrue(doc.get(SearchFieldConstants.AUTHOR).isEmpty());
        assertEquals("LaTeX with hyperref package", doc.get(SearchFieldConstants.CREATOR));
        assertTrue(doc.get(SearchFieldConstants.SUBJECT).isEmpty());
        assertTrue(doc.get(SearchFieldConstants.KEYWORDS).isEmpty());
    }

    @Test
    public void thesisExampleTest() throws IOException {
        Path example = Paths.get("src/test/resources/pdfs/thesis-example.pdf");

        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.of(example.toString()));
        when(entry.getCiteKeyOptional()).thenReturn(Optional.of("ThesisExample2017"));

        Document doc = new DocumentReader(entry).readPDFContents();

        assertEquals("ThesisExample2017", doc.get(SearchFieldConstants.KEY));
        assertFalse(doc.get(SearchFieldConstants.CONTENT).isEmpty());
        assertTrue(doc.get(SearchFieldConstants.AUTHOR).isEmpty());
        assertEquals("LaTeX, hyperref, KOMA-Script", doc.get(SearchFieldConstants.CREATOR));
        assertTrue(doc.get(SearchFieldConstants.SUBJECT).isEmpty());
        assertTrue(doc.get(SearchFieldConstants.KEYWORDS).isEmpty());
    }

    @Test
    public void minimalTest() throws IOException {
        Path example = Paths.get("src/test/resources/pdfs/minimal.pdf");

        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.of(example.toString()));
        when(entry.getCiteKeyOptional()).thenReturn(Optional.of("Minimal2017"));

        Document doc = new DocumentReader(entry).readPDFContents();

        assertEquals("Minimal2017", doc.get(SearchFieldConstants.KEY));
        assertEquals("Hello World\n1\n", doc.get(SearchFieldConstants.CONTENT));
        assertTrue(doc.get(SearchFieldConstants.AUTHOR).isEmpty());
        assertEquals("TeX", doc.get(SearchFieldConstants.CREATOR));
        assertTrue(doc.get(SearchFieldConstants.SUBJECT).isEmpty());
        assertTrue(doc.get(SearchFieldConstants.KEYWORDS).isEmpty());
    }

    @Test
    public void metaDataTest() throws IOException {
        Path example = Paths.get("src/test/resources/pdfs/metaData.pdf");

        BibEntry entry = mock(BibEntry.class);
        when(entry.getField(FieldName.FILE)).thenReturn(Optional.of(example.toString()));
        when(entry.getCiteKeyOptional()).thenReturn(Optional.of("MetaData2017"));

        Document doc = new DocumentReader(entry).readPDFContents();

        assertEquals("MetaData2017", doc.get(SearchFieldConstants.KEY));
        assertEquals("Test\n", doc.get(SearchFieldConstants.CONTENT));
        assertEquals("Author Name", doc.get(SearchFieldConstants.AUTHOR));
        assertEquals("pdflatex", doc.get(SearchFieldConstants.CREATOR));
        assertEquals("A Subject", doc.get(SearchFieldConstants.SUBJECT));
        assertEquals("Test, Whatever, Metadata, JabRef", doc.get(SearchFieldConstants.KEYWORDS));
    }
}
