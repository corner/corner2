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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

import corner.orm.lucene.cd.WebLuceneHighlighter;

/**
 * 
 * �ṩ����֧�ֵ�Extractor.
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2005-10-21
 */
public interface HighlighterHitExtractor {
	/**
	 * ��Lucene��ÿ��Hit���д���.
	 * @param id hit��ID��.
	 * @param doc Lucene�ĵ�.
	 * @param score �÷�.
	 * @param highlighter ������.
	 * @param analyzier ������.
	 * @return ����Ľ��.
	 * @throws IOException ���緢������.
	 */

	public abstract Object mapHit(int id, Document doc, float score,
			WebLuceneHighlighter highlighter, Analyzer analyzier)
			throws IOException;

}
