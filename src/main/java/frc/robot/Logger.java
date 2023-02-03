
package frc.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logger {
   private String fileName;
   private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private boolean headerWritten = false;
   private List<String> columnOrder = new ArrayList<>();
   private FileWriter writer;
   private boolean writerIsAvailable = false;

   public Logger(String fileName, List<String> columnOrder) {
      this.fileName = fileName;
      this.columnOrder = columnOrder;
      try {
         writer = new FileWriter(fileName, true);
         writerIsAvailable = true;
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void log(Map<String, String> columns) {
      if (!writerIsAvailable) {
        // TODO: Dirty hack, please remove.
        return;
      }
      try {
         if (!headerWritten) {
            writer.append("Timestamp");
            for (String columnName : columnOrder) {
               writer.append(", ")
                     .append(columnName);
            }
            writer.append("\n");
            headerWritten = true;
         }
         writer.append(dateFormat.format(new Date()));
         for (String columnName : columnOrder) {
            String value = columns.getOrDefault(columnName, "");
            writer.append(", ")
                  .append(value);
         }
         writer.append("\n");
         writer.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void logFloat(Map<String, Float> floatColumns) {
      Map<String, String> stringColumns = new HashMap<>();
      for (Map.Entry<String, Float> entry : floatColumns.entrySet()) {
         stringColumns.put(entry.getKey(), String.valueOf(entry.getValue()));
      }
      log(stringColumns);
   }
}
/* 
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoggerTest {
   public static void main(String[] args) {
      Logger logger = new Logger("test.csv", Arrays.asList("Column 1", "Column 2"));

      Map<String, Float> floatColumns = new HashMap<>();
      floatColumns.put("Column 1", 1.23f);
      floatColumns.put("Column 2", 4.56f);
      logger.logFloat(floatColumns);

      floatColumns.clear();
      floatColumns.put("Column 1", 7.89f);
      floatColumns.put("Column 2", 10.11f);
      logger.logFloat(floatColumns);

      // Clean up the test file
      new File("test.csv").delete();
   }
}
*/