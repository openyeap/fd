package cn.zhumingwu.research.service;

import com.opencsv.CSVReader;
import lombok.Data;
import lombok.var;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

@Data
public class CsvFileRead implements Closeable {
    private CSVReader reader;
    private StringBuffer errors;
    private String[] headers;
    private Map<String, String> records;
    private int lineNumber;

    public CsvFileRead(String path) {
        try {
            var file = new File(path);
            var fileReader = new FileReader(file);
            this.reader = new CSVReader(fileReader);
            this.errors = new StringBuffer();
            this.lineNumber = 0;
        } catch (FileNotFoundException e) {
            this.errors.append(e.getMessage() + "\r\n");
        }
    }

    public boolean readHeader() {
        if (lineNumber > 0) {
            return true;
        }
        try {
            String[] line = this.reader.readNext();

            lineNumber++;
            if (line == null) {
                return false;
            }
            this.headers = line;
            return true;
        } catch (IOException e) {
            this.errors.append(e.getMessage() + "\r\n");
            return false;
        }
    }

    public boolean readRecord() {
        this.records = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (lineNumber <= 0) {
            if (readHeader() == false) {
                return false;
            }
        }
        try {
            var line = reader.readNext();
            lineNumber++;
            if (line == null) {
                return false;
            }


            if (line.length != this.headers.length) {
                this.errors.append("length error: " + lineNumber + ", " + String.join(",", line) + "\r\n");
                return true;
            }

            for (var i = 0; i < line.length; i++) {
                this.records.put(this.headers[i], line[i]);
            }
            return true;

        } catch (IOException ex) {
            this.errors.append(ex.getMessage() + "\r\n");
            return false;
        }
    }


    @Override
    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }
}