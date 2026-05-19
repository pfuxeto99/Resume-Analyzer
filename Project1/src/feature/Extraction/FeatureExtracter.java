package feature.Extraction;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class FeatureExtracter {

	   public String extractText(File pdfFile) {
		   String text = "";
		    try (PDDocument document = PDDocument.load(pdfFile)) {
		        if (!document.isEncrypted()) {
		            PDFTextStripper stripper = new PDFTextStripper();
		            text = stripper.getText(document);
		        } else {
		            System.err.println("Error: PDF is encrypted.");
		            return "ERROR_ENCRYPTED";
		        }
		    } catch (IOException e) {
		        System.err.println("Error reading PDF: " + e.getMessage());
		        return "ERROR_READING";
		    }
		    
		    // REMOVE ALL WHITESPACE CHARACTER ELEMENTS (\r, \n, tabs, spaces) to test if real text exists
		    if (text.replaceAll("\\s+", "").isEmpty()) {
		        System.out.println("Warning: Extracted text is empty. The PDF might be an image.");
		        return "ERROR_EMPTY_IMAGE";
		    }
		    
		    return text;
	    }
      
    public void IsOneColomn (String cleanText) {
    	
    	
    	
    	
    	
    	
    }
    public Map<String, Double>  hasExperuence (File pdfFile) {
    	
    	Map<String, Double> map = new HashMap<>();
    	
    	PDDocument document =null;
    	  try {
    		  document = PDDocument.load(pdfFile);
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
    	  
    	  String cleanText =extractText(pdfFile);
    	  

    	if (cleanText.contains("experience")){
    		
    		// the person has experience , their cv can be 2 pages 
    		

    		if (document.getNumberOfPages()>=3) {
    			
    			
    		  map.put("NumberOfPages",1.0);
    			
    		}else {
    			
    			  map.put("NumberOfPages",0.0);
    			
    		}
    		
    		
    	}else {
    		
    		// a cv of a person with no work experience needs to be a single page 
    		
    		if (document.getNumberOfPages()>=2) {
    			
    			  map.put("NumberOfPages",1.0);
    			
    		}else {
    			
    			  map.put("NumberOfPages",0.0);
    		}
    	}
    	
    	
    	
    	return map;
    }
    public Map<String, Double> isCorrectStucture(String text) {
        Map<String, Double> features = new HashMap<>();
        
        
       // normalising the text to lower case 
        String cleanText = text.toLowerCase().trim();
        
        features.put("skills", cleanText.contains("skills") ? 1.0 : 0.0);
        features.put("education", (cleanText.contains("education") || cleanText.contains("academic")) ? 1.0 : 0.0);
        features.put("projects", cleanText.contains("project") ? 1.0 : 0.0);
        features.put("experience", (cleanText.contains("experience") || cleanText.contains("work history")) ? 1.0 : 0.0);
        features.put("github", cleanText.contains("github") ? 1.0 : 0.0);
        features.put("linkedin", cleanText.contains("linkedin") ? 1.0 : 0.0);
    
        features.put("profile", (cleanText.contains("profile") || cleanText.contains("summary") || cleanText.contains("about me")) ? 1.0 : 0.0);

        return features;
    }

    public double[] extractFeatures(File pdfFile) {
        String text = extractText(pdfFile);
        Map<String, Double> map = isCorrectStucture(text);
          
        double[] featureVector = new double[7];

       
        featureVector[0] = map.getOrDefault("skills", 0.0);
        featureVector[1] = map.getOrDefault("education", 0.0);
        featureVector[2] = map.getOrDefault("projects", 0.0);
        featureVector[3] = map.getOrDefault("experience", 0.0);
        featureVector[4] = map.getOrDefault("github", 0.0);
        featureVector[5] = map.getOrDefault("linkedin", 0.0);
        featureVector[6] = map.getOrDefault("profile", 0.0);

        return featureVector;
    }
}