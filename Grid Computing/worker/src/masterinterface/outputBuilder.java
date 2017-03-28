package masterinterface;

import com.google.gson.Gson;

/**
 * Created by Farima on 7/24/2016.
 */
public class outputBuilder {

    public String  buildJsonOutput(Result r)
    {
        Gson gson=new Gson();

        String jsonResult=gson.toJson(r);
        return jsonResult;
    }

}
