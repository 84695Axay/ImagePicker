package com.example.jetpackdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class FontManager {
    // This function enumerates all fonts on Android system and returns the HashMap with the font
    // absolute file name as key, and the font literal name (embedded into the font) as value.
    static public HashMap<String, String> enumerateFonts() {
        String[] fontdirs = {"/system/fonts", "/system/font", "/data/fonts"};
        HashMap<String, String> fonts = new HashMap<String, String>();
        TTFAnalyzer analyzer = new TTFAnalyzer();

        for (String fontdir : fontdirs) {
            File dir = new File(fontdir);

            if (!dir.exists())
                continue;

            File[] files = dir.listFiles();

            if (files == null)
                continue;

            for (File file : files) {
                String fontname = analyzer.getTtfFontName(file.getAbsolutePath());

                if (fontname != null)
                    fonts.put(file.getAbsolutePath(), fontname);
            }
        }

        return fonts.isEmpty() ? null : fonts;
    }
}

// The class which loads the TTF file, parses it and returns the TTF font name
