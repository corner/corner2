//==============================================================================
//file :       $Id$
//project:     corner
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:		the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.highlight.QueryTermExtractor;
import org.apache.lucene.search.highlight.WeightedTerm;
import org.springmodules.lucene.search.core.QueryCreator;
import org.springmodules.lucene.search.core.SearcherCallback;
import org.springmodules.lucene.search.support.LuceneSearchSupport;

import corner.orm.lucene.cd.WebLuceneHighlighter;
import corner.util.PaginationBean;

/**
 * �Ի���Lucene��Search��֧��.
 * 
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-10-19
 */
public class SearchAccessor extends LuceneSearchSupport {
	
	/**
	 * ���������������.
	 * @param queryCreator Query������.
	 * @param extractor hit��Extractor
	 * @param pb ���Ƽ�¼��bean.
	 * @return ��������б�.
	 */
	public List search(final QueryCreator queryCreator,
			final HighlighterHitExtractor extractor,final PaginationBean pb) {
		return (List) this.getTemplate().search(new SearcherCallback() {
			public Object doWithSearcher(Searcher searcher) throws IOException,
					ParseException {
				Query query = queryCreator.createQuery(getAnalyzer());
				WeightedTerm[] wts=QueryTermExtractor.getTerms(query);
				List<String> tokens= new ArrayList<String>();
				for(WeightedTerm t : wts) {
					tokens.add(t.getTerm());
				}
				WebLuceneHighlighter highlighter=new WebLuceneHighlighter((ArrayList) tokens);
				
				Hits hits = searcher.search(query);
				pb.setRowCount(hits.length());
				return extractHits(hits, extractor,pb.getFirst(),pb.getPageSize(), highlighter);
			}
		});
	}
	/**
	 * �õ������Ľ����¼��.
	 * @param qc query������.
	 * @return ƥ�����ļ�¼��.
	 */
	public int getSearchCount(final QueryCreator qc){
		return ((Integer) this.getTemplate().search(new SearcherCallback(){
			public Object doWithSearcher(Searcher searcher) throws IOException, ParseException {
				return new Integer(searcher.search(qc.createQuery(getAnalyzer())).length());
			}})).intValue();
	}

	@SuppressWarnings("unchecked")
	private List extractHits(Hits hits, HighlighterHitExtractor extractor,
			int start, int offSize, WebLuceneHighlighter h) throws IOException {
		
		List list = new ArrayList();
		int length = hits.length();
		
		if (start > length) {
			start = length - offSize;
		}
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < start + offSize && i < length; i++) {
			list.add(extractor.mapHit(i, hits.doc(i), hits.score(i), h, this
					.getAnalyzer()));
			
		}
		return list;
	}

}
