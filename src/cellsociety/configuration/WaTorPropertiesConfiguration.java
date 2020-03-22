package cellsociety.configuration;

import java.util.HashMap;
import java.util.Map;

public class WaTorPropertiesConfiguration extends PropertiesConfiguration{
    public WaTorPropertiesConfiguration(String f) {
        super(f);
    }

    @Override
    public Map<String, Number> getSpecialVals() {
        if (!allInfo.keySet().contains("FishReproductionTimer") || ! allInfo.keySet().contains("SharkReproductionTimer")
                ||!allInfo.keySet().contains("SharkEatingGain") ||!allInfo.keySet().contains("SharkInitEnergy") )
        {
            throw new ResourceKeyException("Key is not in resource file");
        }
        Map<String, Number> ret = new HashMap<>();
        ret.put("FishReproductionTimer", Integer.parseInt(allInfo.get("FishReproductionTimer")));
        ret.put("SharkReproductionTimer", Integer.parseInt(allInfo.get("SharkReproductionTimer")));
        ret.put("SharkEatingGain", Integer.parseInt(allInfo.get("SharkEatingGain")));
        ret.put("SharkInitEnergy", Integer.parseInt(allInfo.get("SharkInitEnergy")));
        return ret;
    }
}
