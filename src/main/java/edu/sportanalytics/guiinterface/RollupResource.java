package edu.sportanalytics.guiinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import edu.sportanalytics.database.BasketballController;
import edu.sportanalytics.database.DBAccess;
import edu.sportanalytics.database.SportsEnum;

@Path("RollupResource")
public class RollupResource {
    private static final Logger log = Logger.getLogger(RollupResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getData(@QueryParam("token") int token) {
        RollupToken tk = RollupToken.getToken(token);
        if (tk.getSports() == SportsEnum.BASKETBALL) {
            BasketballController bc = (BasketballController) DBAccess.getInstance()
                    .getController(SportsEnum.BASKETBALL);

            List<String> rollupvals = new ArrayList<>();

            rollupvals.add(bc.getRollupStats(tk.getFactAttribute(), tk.getAggregationFunction(), tk.getDimension()));

            JSONObject jo = new JSONObject();
            jo.put("rollupvals", rollupvals);
            String returnString = jo.toString();

            log.info("JSON String created: " + returnString);
            return returnString;
        } else {
            return null;
        }
    }
}
