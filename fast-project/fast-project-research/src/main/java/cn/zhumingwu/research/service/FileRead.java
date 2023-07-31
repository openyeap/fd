package cn.zhumingwu.research.service;

import lombok.Data;

import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

@Data
public class FileRead implements Closeable {
    private BufferedReader reader;
    private StringBuffer errors;
    private String[] headers;
    private Map<String, String> records;
    private int lineNumber;

    public FileRead(String path) {
        try {
            var file = new File(path);
            var fileReader = new FileReader(file);
            this.reader = new BufferedReader(fileReader);
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
            String line = this.reader.readLine();
            lineNumber++;
            if (Strings.isEmpty(line)) {
                return false;
            }
            this.headers = line.split(",");
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
            var line = reader.readLine();
            lineNumber++;
            if (Strings.isEmpty(line)) {
                return false;
            }
            var i = line.indexOf('"');
            if (i > 0) {
                try {
                    line = removeQuote(line, i);
                } catch (Exception ex) {
                    this.errors.append(line + "\r\n");
                    return true;
                }
            }
            var items = line.split(",");
            if (items.length != this.headers.length) {
                this.errors.append("length error: " + lineNumber + ", " + line + "\r\n");
                return true;
            }

            for (i = 0; i < items.length; i++) {
                this.records.put(this.headers[i], items[i]);
            }
            return true;

        } catch (IOException ex) {
            this.errors.append(ex.getMessage() + "\r\n");
            return false;
        }
    }

    private String removeQuote(String line, int i) {
        if (i > 0) {
            var left = line.substring(0, i);
            System.out.println(left);
            var right = line.substring(i + 1);
            System.out.println(right);
            var middle = right.substring(0, right.indexOf('"'));
            middle = middle.replace(",", "$|$");

            System.out.println(middle);
            right = right.substring(right.indexOf('"') + 1);

            line = left + middle + right;
        }
        i = line.indexOf('"');
        if (i > 0) {
            return removeQuote(line, i);
        }
        return line;
    }

    @Override
    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }
}