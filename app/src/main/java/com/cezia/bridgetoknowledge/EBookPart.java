package com.cezia.bridgetoknowledge;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
    PART1_197(22, 1, 197),

    PART2_001(23, 2, 1),
    PART2_011(24, 2, 11),
    PART2_021(25, 2, 21),
    PART2_033(26, 2, 33),
    PART2_045(27, 2, 45),
    PART2_057(28, 2, 57),
    PART2_069(29, 2, 69),
    PART2_081(30, 2, 81),
    PART2_093(31, 2, 93),
    PART2_103(32, 2, 103),
    PART2_113(33, 2, 113),
    PART2_123(34, 2, 123),
    PART2_133(35, 2, 133),
    PART2_143(36, 2, 143),
    PART2_153(37, 2, 153),
    PART2_154(38, 2, 154),
    PART2_164(39, 2, 164),
    PART2_174(40, 2, 174),
    PART2_187(41, 2, 187),
    PART2_199(42, 2, 199),
    PART2_211(43, 2, 211),
    PART2_223(44, 2, 223),
    PART2_235(45, 2, 235),
    PART2_247(46, 2, 247),
    PART2_259(47, 2, 259),
    PART2_271(48, 2, 271),
    PART2_283(49, 2, 283),
    PART2_295(50, 2, 295),
    PART2_307(51, 2, 307),
    PART2_319(52, 2, 319),
    PART2_331(53, 2, 331),
    PART2_343(54, 2, 343),
    PART2_355(55, 2, 355),
    PART2_367(56, 2, 367),

    PART3_001(57, 3, 1),
    PART3_013(58, 3, 13),
    PART3_025(59, 3, 25),
    PART3_037(60, 3, 37),
    PART3_049(61, 3, 49),
    PART3_051(62, 3, 51),
    PART3_063(63, 3, 63),
    PART3_076(64, 3, 76),
    PART3_084(65, 3, 84),
    PART3_096(66, 3, 96),
    PART3_109(67, 3, 109),
    PART3_121(68, 3, 121),
    PART3_133(69, 3, 133),
    PART3_145(70, 3, 145),
    PART3_157(71, 3, 157),
    PART3_170(72, 3, 170),
    PART3_182(73, 3, 182),
    PART3_192(74, 3, 192),
    PART3_205(75, 3, 205),
    PART3_217(76, 3, 217),
    PART3_229(77, 3, 229),

    PART4_001(78, 4, 1),
    PART4_012(79, 4, 12),
    PART4_021(80, 4, 21),
    PART4_034(81, 4, 34),
    PART4_054(82, 4, 54),
    PART4_069(83, 4, 69),
    PART4_081(84, 4, 81),
    PART4_092(85, 4, 92),
    PART4_103(86, 4, 103),
    PART4_115(87, 4, 115),
    PART4_126(88, 4, 126),
    PART4_138(89, 4, 138),
    PART4_150(90, 4, 150),
    PART4_161(91, 4, 161),
    PART4_173(92, 4, 173),
    PART4_187(93, 4, 187),
    PART4_199(94, 4, 199),
    PART4_212(95, 4, 212),
    PART4_224(96, 4, 224),
    PART4_236(97, 4, 236),
    PART4_248(98, 4, 248),
    PART4_260(99, 4, 260),

    PART5_001(100, 5, 1),
    PART5_014(101, 5, 14),

    ;

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

    public static String[] getListParts(Context context) {
        int iPart = -1;
        int cntPart = 0;
        for (EBookPart eBookPart : values()) {
            if (eBookPart.bookMark.getNumPart() != iPart) {
                iPart = eBookPart.bookMark.getNumPart();
                cntPart++;
            }
        }
        String[] listParts = new String[cntPart];
        for (EBookPart eBookPart : values()) {
            if (eBookPart.bookMark.getNumPart() != iPart) {
                iPart = eBookPart.bookMark.getNumPart();
                if (iPart == 0) {
                    listParts[iPart] = context.getResources().getString(R.string.foreword);
                } else {
                    listParts[iPart] = "Глава " + Integer.toString(iPart);
                }
            }
        }
        return listParts;
    }
}
