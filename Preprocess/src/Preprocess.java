import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocess {
	private static final Pattern params = Pattern.compile("^(?:#)?\\s*define\\s+(.*)\\s+(.*)\\s*$");
	private static final Pattern vars = 
			Pattern.compile("(\\$\\{.*\\})");
    public static void main(String[] args) {
        String fileName = "/Users/a3f/dl/example.lms";
        Hashtable<String, String>map = new Hashtable<String, String>();
        map.put("${projectName}", "PreprocessProj");
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName),
                    Charset.defaultCharset());
            Matcher m;
            for (String line : lines) {
                m = vars.matcher(line);
                if (m.find()) {
                	//System.out.println(m.group(1) + '\n');
                	String val = map.get(m.group(1));
                	line = m.replaceAll(val != null ? val : "");
                	//System.out.println("result=" + val);
                }

                Hashtable<String, String> defs = new Hashtable<String, String>();
                
                m = params.matcher(line);
                if (m.find()) defs.put(m.group(1), m.group(2));
                for(Entry<String, String> entry : defs.entrySet()) {
                	System.out.println(entry.getKey() + " is defined as " + entry.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

