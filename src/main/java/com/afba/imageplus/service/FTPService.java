package com.afba.imageplus.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FTPService {

    File get(String payload) throws FileNotFoundException;

    byte[] zip(List<File> files) throws IOException;

    void remove(String filename);

    byte[] download(String path) throws Exception;
}
