package org.jabref.model.pdf.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jabref.logic.pdf.search.SearchFieldConstants;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocCollector;

public class ResultSet {

    public final List<SearchResult> searchResults;
    private TopDocCollector collector;

    public ResultSet(Document[] documents, TopDocCollector collector) {
        this.searchResults = Collections.unmodifiableList(mapToSearchResults(documents));
        this.collector = collector;
    }

    private void sortByHits() {
        //TODO implement sorting
    }

    private void sortByAlphabet() {
        //TODO implement sorting
    }

    /**
     * Maps a lucene documents fields to a search result list
     *
     * @param documents a list of lucene documents with some fields set
     */
    private List<SearchResult> mapToSearchResults(Document[] documents) {
        List<SearchResult> results = new ArrayList<>(documents.length);

        for (int i = 0; i < documents.length; i++) {
            SearchResult result = new SearchResult();
            for (String field : SearchFieldConstants.PDF_FIELDS) {
                result.mapField(field, documents[i].getField(field).stringValue());
            }
            result.setLuceneScore(collector.topDocs().scoreDocs[i].score);
            results.add(result);
        }
        return results;
    }
}
