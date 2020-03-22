package cellsociety.configuration;

import java.util.HashMap;
import java.util.Map;

public class RPSPropertiesConfiguration extends PropertiesConfiguration {
    public RPSPropertiesConfiguration(String f) {
        super(f);
    }

    /**
     * @return - a Map containing the specific values relevant to a RPS simulation.
     * @throws ResourceKeyException if the key for that specific value is NOT in the file.
     */
    @Override
    public Map<String, Number> getSpecialVals() {
        if (!allInfo.keySet().contains("Threshold")) {
            throw new ResourceKeyException("Key Threshold is not in resource file");
        }
        Map<String, Number> ret = new HashMap<>();
        ret.put("Threshold", Integer.parseInt(allInfo.get("Threshold")));
        return ret;
    }
}
