package com.fucct.tobispring.learning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public <T> T lineReadTemplate(final String path, final LineCallback<T> callback, final T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } finally {
            try {
                assert br != null;
                br.close();
            } catch (IOException ignored) {
            }
        }
    }


    public int calcSum(final String filepath) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> value += Integer.parseInt(line);

        return lineReadTemplate(filepath, lineCallback, 0);
    }

    public int calcMultiply(final String path) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> value *= Integer.parseInt(line);

        return lineReadTemplate(path, lineCallback, 1);
    }

    public String conCat(final String path) throws IOException {
        LineCallback<String> lineCallback = (line, value) -> value + line;

        return lineReadTemplate(path, lineCallback, "");
    }
}
