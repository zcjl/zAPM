package im.zcjl.zapm;

import java.util.HashMap;
import java.util.Map;

public class PasswdGenerator {

    private static final PasswdGenerator           instance  = new PasswdGenerator();
    private static final Map<Character, Character> charMap   = new HashMap<Character, Character>();
    static {
        charMap.put('1', '!');
        charMap.put('2', '@');
        charMap.put('3', '#');
        charMap.put('4', '$');
        charMap.put('5', '%');
        charMap.put('6', '^');
        charMap.put('7', '&');
        charMap.put('8', '*');
        charMap.put('9', '(');
        charMap.put('0', ')');
        charMap.put('a', 'A');
        charMap.put('b', 'B');
        charMap.put('c', 'C');
        charMap.put('d', 'D');
        charMap.put('e', 'E');
        charMap.put('f', 'F');
    }

    public static final String                     encrypted = "******";

    private PasswdGenerator() {
    }

    public synchronized static PasswdGenerator getInstance() {
        return instance;
    }

    public String getPasswd(CharSequence host, CharSequence account) {
        int hash = host.hashCode() + account.hashCode() + encrypted.hashCode();
        return getConfoundedString(Math.abs(hash));
    }

    private int getOffset(int hash) {
        return hash % 8;
    }

    private String getConfoundedString(int hash) {
        int offset = getOffset(hash);
        String passwd = Long.toHexString(hash);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwd.length(); i++) {
            if (i == offset) sb.append(charMap.get(passwd.charAt(i)));
            else sb.append(passwd.charAt(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.err.println("=================");
        System.err.println(PasswdGenerator.getInstance().getPasswd(args[0], args[1]));
    }
}
