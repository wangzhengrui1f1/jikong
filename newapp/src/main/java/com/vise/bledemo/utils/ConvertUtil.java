package com.vise.bledemo.utils;


import android.annotation.TargetApi;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ConvertUtil {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add(null);

        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }

        System.out.println(list2String(list, ","));

        System.out.println(obj2Double(""));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> array2List(T... src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        // return Stream.of(src).collect(Collectors.toList());
        List<T> result = new ArrayList<T>();
        for (T t : src) {
            if (t == null) {
                continue;
            }
            result.add(t);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> array2Set(T... src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        // return Stream.of(src).collect(Collectors.toSet());
        Set<T> result = new HashSet<T>();
        for (T t : src) {
            if (t == null) {
                continue;
            }
            result.add(t);
        }
        return result;
    }

    public static byte[] obj2bytes(Object src) throws IOException {
        if (src == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(out);
        output.writeObject(src);
        byte[] result = out.toByteArray();
        output.close();
        out.close();
        return result;
    }

    public static Object bytes2obj(byte[] src) throws IOException, ClassNotFoundException {
        if (src == null) {
            return null;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(src);
        ObjectInputStream input = new ObjectInputStream(in);
        Object result = input.readObject();
        input.close();
        in.close();
        return result;
    }

    public static long date2sec(Date src) {
        if (src == null) {
            return 0;
        }
        return src.getTime() / 1000;
    }

    public static Date sec2Date(Long src) {
        if (src == null) {
            return null;
        }
        return new Date(src * 1000);
    }

    public static long yuan2cent(Double src) {
        if (src == null) {
            return 0;
        }
        BigDecimal bd1 = BigDecimal.valueOf(src);
        BigDecimal bd2 = BigDecimal.valueOf(100);
        return bd1.multiply(bd2).longValue();
    }

    public static double cent2yuan(Long src) {
        if (src == null) {
            return 0;
        }
        BigDecimal bd1 = BigDecimal.valueOf(src);
        BigDecimal bd2 = BigDecimal.valueOf(100);
        return bd1.divide(bd2).doubleValue();
    }

    public static List<String> string2List(String src, String separator) {
        if (src == null) {
            return null;
        }
        if (separator == null) {
            return null;
        }
        String[] strings = src.split(separator);
        return Arrays.asList(strings);
        // return Stream.of(strings).collect(Collectors.toList());
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static String list2String(List<String> src, String delimiter) {
        if (src == null) {
            return null;
        }
        if (delimiter == null) {
            return null;
        }
        return String.join(delimiter, src);
    }

    public static String obj2String(Object src) {
        if (src == null) {
            return null;
        }
        return src.toString();
    }

    public static Date obj2Date(Object src) throws ParseException {
        if (src == null) {
            return null;
        }
        if (src instanceof Date) {
            return (Date) src;
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(s);
        }
        return null;
    }

    public static BigInteger obj2BigInteger(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof BigInteger) {
            return (BigInteger) src;
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return new BigInteger(s);
        }
        return null;
    }

    public static byte obj2byte(Object src) {
        Byte n = obj2Byte(src);
        if (n == null) {
            return 0;
        }
        return n.byteValue();
    }

    public static Byte obj2Byte(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).byteValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Byte.parseByte(s);
        }
        return null;
    }

    public static short obj2short(Object src) {
        Short n = obj2Short(src);
        if (n == null) {
            return 0;
        }
        return n.shortValue();
    }

    public static Short obj2Short(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).shortValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Short.parseShort(s);
        }
        return null;
    }

    public static int obj2int(Object src) {
        Integer n = obj2Integer(src);
        if (n == null) {
            return 0;
        }
        return n.intValue();
    }

    public static Integer obj2Integer(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).intValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Integer.parseInt(s);
        }
        return null;
    }

    public static long obj2long(Object src) {
        Long n = obj2Long(src);
        if (n == null) {
            return 0;
        }
        return n.longValue();
    }

    public static Long obj2Long(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).longValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Long.parseLong(s);
        }
        return null;
    }

    public static float obj2float(Object src) {
        Float n = obj2Float(src);
        if (n == null) {
            return 0;
        }
        return n.floatValue();
    }

    public static Float obj2Float(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).floatValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Float.parseFloat(s);
        }
        return null;
    }

    public static double obj2double(Object src) {
        Double n = obj2Double(src);
        if (n == null) {
            return 0;
        }
        return n.doubleValue();
    }

    public static Double obj2Double(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Number) {
            return ((Number) src).doubleValue();
        }
        if (src instanceof String) {
            String s = src.toString();
            if ("".equals(s)) {
                return null;
            }
            return Double.parseDouble(s);
        }
        return null;
    }

    public static Boolean obj2Boolean(Object src) {
        if (src == null) {
            return null;
        }
        if (src instanceof Boolean) {
            return (Boolean) src;
        }
        Integer n = obj2Integer(src);
        if (n == null) {
            return null;
        }
        if (n > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
