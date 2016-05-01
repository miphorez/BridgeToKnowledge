package com.cezia.bridgetoknowledge;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

enum EBookPart {
    PART0_001(0, 0, 1),

    PART1_001(1, 1, 1),
    PART1_013(2, 1, 13),
    PART1_024(3, 1, 24),
    PART1_029(4, 1, 29),
    PART1_036(5, 1, 36),
    PART1_037(6, 1, 37),
    PART1_047(7, 1, 47),
    PART1_057(8, 1, 57),
    PART1_067(9, 1, 67),
    PART1_077(10, 1, 77),
    PART1_087(11, 1, 87),
    PART1_097(12, 1, 97),
    PART1_107(13, 1, 107),
    PART1_117(14, 1, 117),
    PART1_127(15, 1, 127),
    PART1_137(16, 1, 137),
    PART1_147(17, 1, 147),
    PART1_157(18, 1, 157),
    PART1_167(19, 1, 167),
    PART1_177(20, 1, 177),
    PART1_187(21, 1, 187),
    PART1_197(22, 1, 197),;

    int bookPart;
    BookMark bookMark;

    EBookPart(int bookPart, int numPart, int numFragment) {
        this.bookPart = bookPart;
        bookMark = new BookMark();
        bookMark.setNumPart(numPart);
        bookMark.setNumFragment(numFragment);
    }

    static String getTextBookPart(Context context, BookMark bookMark) {
        return getStringFromAssetFile(context, getFileName(bookMark));
    }

    private static String getFileName(BookMark bookMark) {
        return "part" + Integer.toString(bookMark.getNumPart()) + "/" +
                "part" + Integer.toString(bookMark.getNumPart()) + "_" +
                Integer.toString(bookMark.getNumFragment()) + ".txt";
    }

    private static String getStringFromAssetFile(Context context, String fileName) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = convertStreamToString(is);
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
        } catch (OutOfMemoryError | Exception om) {
            om.printStackTrace();
        }
        return sb.toString();
    }

    public static BookMark getNextBookMark(BookMark bookMark) {
        BookMark newBookMark = null;
        int lastPart = getNumBookPart(bookMark);
        if (lastPart == (EBookPart.values().length - 1)) return bookMark;
        lastPart++;
        for (EBookPart eBookPart : values()) {
            if (eBookPart.bookPart == lastPart) {
                newBookMark = new BookMark(eBookPart.bookMark);
                break;
            }
        }
        return newBookMark;
    }

    public static BookMark getPrevBookMark(BookMark bookMark) {
        BookMark newBookMark = null;
        int lastPart = getNumBookPart(bookMark);
        if (lastPart == 0) return bookMark;
        lastPart--;
        for (EBookPart eBookPart : values()) {
            if (eBookPart.bookPart == lastPart) {
                newBookMark = new BookMark(eBookPart.bookMark);
                break;
            }
        }
        return newBookMark;
    }

    private static int getNumBookPart(BookMark bookMark) {
        int lastPart = 0;
        for (EBookPart eBookPart : values()) {
            if (eBookPart.bookMark.getNumPart() == bookMark.getNumPart() &&
                    eBookPart.bookMark.getNumFragment() == bookMark.getNumFragment()) {
                lastPart = eBookPart.bookPart;
                break;
            }
        }
        return lastPart;
    }

    public static boolean isLastBookPart(BookMark bookMark) {
        int lastPart = getNumBookPart(bookMark);
        return lastPart == (EBookPart.values().length - 1);
    }

    public static BookMark getFirstBookPart() {
        return new BookMark(0, 1);
    }
}
