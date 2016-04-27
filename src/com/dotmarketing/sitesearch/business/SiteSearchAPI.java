package com.dotmarketing.sitesearch.business;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.search.facet.Facet;
import org.quartz.SchedulerException;

import com.dotcms.enterprise.publishing.sitesearch.SiteSearchConfig;
import com.dotcms.enterprise.publishing.sitesearch.SiteSearchPublishStatus;
import com.dotcms.enterprise.publishing.sitesearch.SiteSearchResult;
import com.dotcms.enterprise.publishing.sitesearch.SiteSearchResults;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.quartz.ScheduledTask;


public interface SiteSearchAPI {
    public static final String ES_SITE_SEARCH_NAME = "sitesearch";
    public static final String ES_SITE_SEARCH_MAPPING = "dot_site_search";
    public static final String ES_SITE_SEARCH_EXECUTE_JOB_NAME = "runningOnce";

	List<String> listIndices();

	void activateIndex(String indexName) throws DotDataException;

	void deactivateIndex(String indexName) throws DotDataException, IOException;

	boolean createSiteSearchIndex(String indexName, String alias, int shards) throws ElasticsearchException, IOException;

	List<ScheduledTask> getTasks() throws SchedulerException;

	void deleteTask(String taskName) throws SchedulerException;

	void scheduleTask(SiteSearchConfig config) throws SchedulerException, ParseException, ClassNotFoundException;

	void putToIndex(String idx, SiteSearchResult res, String resultType);

	void putToIndex(String idx, List<SiteSearchResult> res, String resultType);

	void deleteFromIndex(String idx, String docId);

	SiteSearchResults search(String query, int start, int rows);
	
	SiteSearchResults search(String indexName, String query, int start, int rows);

	ScheduledTask getTask(String taskName) throws SchedulerException;
	
	void pauseTask(String taskName)  throws SchedulerException;

	SiteSearchPublishStatus getTaskProgress(String jobName) throws SchedulerException;

	boolean isTaskRunning(String jobName) throws SchedulerException;

	void executeTaskNow(SiteSearchConfig config) throws SchedulerException, ParseException, ClassNotFoundException;

	SiteSearchResult getFromIndex(String index, String id);

	Map<String, Facet> getFacets(String indexName, String query) throws DotDataException;

    List<String> listClosedIndices();
}
