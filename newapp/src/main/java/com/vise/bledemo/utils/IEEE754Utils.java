package com.vise.bledemo.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/***
 * IEE754 浮点数转换工具
 * 单精度校验：47218100 = 41345.0、 47849A0D = 67892.1016
 * 双精度校验：4721810047849A0D = 45442749783852262460186723560194048
 *
 * @author: 若非
 * @date: 2021/9/10 8:58
 */

public class IEEE754Utils {

    /**
     * 字节数组转IEEE 754
     *
     * @param bytes 长度4或者8
     * @author: 若非
     * @date: 2021/9/10 16:57
     */
    public static BigDecimal bytesToSingle(byte[] bytes) {
        if (bytes.length > 8) {
            throw new ArrayIndexOutOfBoundsException("转化失败，字节超出整型大小！");
        }
        String hex = new BigInteger(bytes).toString(16);
        return hexToSingle(hex, bytes.length * 8);
    }

    /**
     * 字节数组转IEEE 754
     *
     * @param hex
     * @param bitLen 32或者64
     * @author: 若非
     * @date: 2021/9/10 16:57
     */
    private static BigDecimal hexToSingle(String hex, int bitLen) {
        if (StringUtils.isEmpty(hex)) {
            return BigDecimal.valueOf(0);
        }
        if (bitLen == 32) {
            int i = Integer.parseInt(hex, 16);
            float v = Float.intBitsToFloat(i);
            return new BigDecimal(v);
        }
        if (bitLen == 64) {
            long l = Long.parseLong(hex, 16);
            double d = Double.longBitsToDouble(l);
            return new BigDecimal(d);
        }
        return BigDecimal.valueOf(0);
    }

    /**
     * IEEE 754字符串转十六进制字符串
     *
     * @param f
     * @author: 若非
     * @date: 2021/9/10 16:57
     */
    public static String singleToHex(float f) {
        int i = Float.floatToIntBits(f);
        String hex = Integer.toHexString(i);
        return hex;
    }

    /**
     * IEEE 754字符串转十六进制字符串
     *
     * @param d
     * @author: 若非
     * @date: 2021/9/10 16:57
     */
    public static String singleToHex(double d) {
        long l = Double.doubleToRawLongBits(d);
        String hex = Long.toHexString(l);
        return hex;
    }


}
