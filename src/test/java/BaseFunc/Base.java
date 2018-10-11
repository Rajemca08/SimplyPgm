package BaseFunc;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Base {
	
	/** Truncating the float values into two decimal fraction ***/
	public static  float TruncateFloatValue(Float values) throws Exception{
		String strValues= null;
		try{
			DecimalFormat df = new DecimalFormat("##.##");
			df.setRoundingMode(RoundingMode.DOWN);
			strValues = df.format(values);
						
		
		}catch(Exception e){
			throw e;			
		}
		return Float.valueOf(strValues);
		
	}

}
