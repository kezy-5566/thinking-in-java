package xyz.vimtool.chapter13.section6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 匹配组
 *
 * @author  zhangzheng
 * @version 1.0.0
 * @date    2019-01-22
 */
public class Groups {

    public static final String POEM = "Twas brillig, and the slithy toves\n" +
            "Did gyre and gimble in the wabe,\n" +
            "All mimsy were the borogoves,\n" +
            "And the mome raths outgrabe,\n\n" +
            "Beware the Jabberwock, my son,\n" +
            "The jaws that bite, the claws that catch.\n" +
            "Beware the Jubjub bird, and shun\n" +
            "The frumious Bandersnatch.";

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM);
        while (matcher.find()) {
            for (int j = 0; j < matcher.groupCount(); j++) {
                System.out.print("[" + matcher.group(j) + "]");
            }
            System.out.println();
        }
    }
}