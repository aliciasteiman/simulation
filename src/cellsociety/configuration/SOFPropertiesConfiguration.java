package cellsociety.configuration;

import java.util.HashMap;
import java.util.Map;

public class SOFPropertiesConfiguration extends PropertiesConfiguration{
    public SOFPropertiesConfiguration(String f) {
        super(f);
    }

    @Override
    public Map<String, Number> getSpecialVals() {
        if (!allInfo.keySet().contains("ProbabilityCatch")) {
            throw new ResourceKeyException("Key ProbabilityCatch is not in resource file");
        }
        Map<String, Number> ret = new HashMap<>();
        ret.put("ProbabilityCatch", Double.parseDouble(allInfo.get("ProbabilityCatch")));
        return ret;
    }

}
