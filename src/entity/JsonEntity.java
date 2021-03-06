package entity;

import org.json.JSONObject;

/**
 * Created by hyx on 2015/5/31.
 */
public interface JsonEntity {

    JSONObject getJSONObject();
    JsonEntity initFromJSONObject(JSONObject jsonObject);
    JsonEntity initFromJSONSource(String source);
}
