package cellsociety.configuration;


import java.util.HashMap;
import java.util.Map;

public class SegregationPropertiesConfiguration extends PropertiesConfiguration {
    public SegregationPropertiesConfiguration(String f) {
        super(f);
    }

    @Override
    public Map<String, Number> getSpecialVals() {
        if (!allInfo.keySet().contains("SatisfiedThreshold")) {
            throw new ResourceKeyException("Key SatisfiedThreshold is not in resource file");
        }
        Map<String, Number> ret = new HashMap<>();
        ret.put("SatisfiedThreshold", Double.parseDouble(allInfo.get("SatisfiedThreshold")));
        return ret;
    }
}
