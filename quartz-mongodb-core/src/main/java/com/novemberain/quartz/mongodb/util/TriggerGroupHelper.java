package com.novemberain.quartz.mongodb.util;

import static com.novemberain.quartz.mongodb.util.Keys.KEY_GROUP;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class TriggerGroupHelper extends GroupHelper {
  private static final String JOB_ID = "jobId";

  public TriggerGroupHelper(MongoCollection<Document> collection, QueryHelper queryHelper) {
    super(collection, queryHelper);
  }

  public List<String> groupsForJobId(ObjectId jobId) {
    return collection
        .distinct(KEY_GROUP, String.class)
        .filter(Filters.eq(JOB_ID, jobId))
        .into(new ArrayList<>());
  }

  public List<String> groupsForJobIds(Collection<ObjectId> ids) {
    return collection
        .distinct(KEY_GROUP, String.class)
        .filter(Filters.in(JOB_ID, ids))
        .into(new ArrayList<>());
  }
}
