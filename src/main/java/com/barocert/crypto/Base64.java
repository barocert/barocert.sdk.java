package com.barocert.crypto;

public class Base64 {

    private static final char[] encodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/' };

    public static char getEncode(int i) {
        return encodeTable[i & 0x3F];
    }

    public static String encode(byte[] input) {
        char[] result = new char[((input.length + 2) / 3) * 4];

        int resultIndex = 0;
        int checkLength = 0;

        for (int i = 0; i < input.length; i = i + 3) {
            checkLength = input.length - i;
            if (checkLength == 2) {
                result[resultIndex++] = getEncode(input[i] >> 2);
                result[resultIndex++] = getEncode(((input[i] & 0x3) << 4) | ((input[i + 1] >> 4) & 0xF));
                result[resultIndex++] = getEncode((input[i + 1] & 0xF) << 2);
                result[resultIndex++] = '=';
            } else if (checkLength == 1) {
                result[resultIndex++] = getEncode(input[i] >> 2);
                result[resultIndex++] = getEncode(((input[i]) & 0x3) << 4);
                result[resultIndex++] = '=';
                result[resultIndex++] = '=';
            } else {
                result[resultIndex++] = getEncode(input[i] >> 2);
                result[resultIndex++] = getEncode(((input[i] & 0x3) << 4) | ((input[i + 1] >> 4) & 0xF));
                result[resultIndex++] = getEncode(((input[i + 1] & 0xF) << 2) | ((input[i + 2] >> 6) & 0x3));
                result[resultIndex++] = getEncode(input[i + 2] & 0x3F);
            }
        }

        return new String(result);
    }

    public static String urlEncode(byte[] input) {
        return encode(input).replace("+", "-").replace("/", "_").replace("=", "");
    }

    private static final byte[] decodeTable = new byte[128];
    private static final byte PADDING = 127;

    static {
        for (int i = 0; i < decodeTable.length; i++) {
            decodeTable[i] = -1;
        }
        for (int i = 0; i < encodeTable.length; i++) {
            decodeTable[encodeTable[i]] = (byte) i;
        }
        decodeTable['='] = PADDING;
    }

    public static byte[] decode(String input) {
        int resultLength = getResultLength(input);

        byte[] result = new byte[resultLength];
        int resultIndex = 0;

        byte[] splitBuff = new byte[4];
        int bufIndex = 0;

        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            byte decodeValue = decodeTable[inputChar];

            if (decodeValue != -1) {
                splitBuff[bufIndex++] = decodeValue;
            }

            if (bufIndex == 4) {
                result[resultIndex++] = (byte) ((splitBuff[0] << 2) | (splitBuff[1] >> 4));
                if (splitBuff[2] != PADDING)
                    result[resultIndex++] = (byte) ((splitBuff[1] << 4) | (splitBuff[2] >> 2));
                if (splitBuff[3] != PADDING)
                    result[resultIndex++] = (byte) ((splitBuff[2] << 6) | (splitBuff[3]));
                bufIndex = 0;
            }
        }
        return result;
    }

    private static int getResultLength(String input) {
        final int inputLength = input.length();

        int paddingCheck = inputLength - 1;
        int paddingSize = 0;

        for (; paddingCheck >= 0; paddingCheck--) {
            byte code = decodeTable[input.charAt(paddingCheck)];
            if (code == PADDING)
                continue;
            if (code == -1) {
                return input.length() / 4 * 3;
            }
            break;
        }

        paddingCheck++;
        paddingSize = inputLength - paddingCheck;

        return input.length() / 4 * 3 - paddingSize;
    }
}