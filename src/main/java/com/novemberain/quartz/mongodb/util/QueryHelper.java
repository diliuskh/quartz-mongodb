package com.novemberain.quartz.mongodb.util;

import static com.novemberain.quartz.mongodb.util.Keys.KEY_GROUP;

import com.mongodb.client.model.Filters;
import java.util.Collection;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.quartz.impl.matchers.GroupMatcher;

public class QueryHelper {

  public Bson matchingKeysConditionFor(GroupMatcher<?> matcher) {
    final String compareToValue = matcher.getCompareToValue();

    switch (matcher.getCompareWithOperator()) {
      case EQUALS:
        return Filters.eq(KEY_GROUP, compareToValue);
      case STARTS_WITH:
        return Filters.regex(KEY_GROUP, "^" + compareToValue + ".*");
      case ENDS_WITH:
        return Filters.regex(KEY_GROUP, ".*" + compareToValue + "$");
      case CONTAINS:
        return Filters.regex(KEY_GROUP, compareToValue);
    }

    return new BsonDocument();
  }

  public Bson inGroups(Collection<String> groups) {
    return Filters.in(KEY_GROUP, groups);
  }
}
