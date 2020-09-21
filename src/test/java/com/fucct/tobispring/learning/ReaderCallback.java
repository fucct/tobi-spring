package com.fucct.tobispring.learning;

import java.io.BufferedReader;
import java.io.IOException;

public interface ReaderCallback {
    int doSomethingWithReader(BufferedReader br) throws IOException;
}
