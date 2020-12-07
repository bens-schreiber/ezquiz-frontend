package etc;

import java.util.ArrayList;
import java.util.Base64;

/**
 * BitMap stored as long
 * @author benschreiber
 */
public class BitMap {
    private long bitMap;

    /**
     * Class Constructor.
     * @param arrayList List of numbers to be stored into the bitmap. No duplicates.
     */
    public BitMap(ArrayList<Integer> arrayList) {

        this.bitMap = 0;

        for (Integer id : arrayList) {
            this.bitMap |= (1L << (id - 1));
        }

    }

    /**
     * Alternative constructor.
     * @param base64 String of Base64 encoded long.
     */
    public BitMap(String base64) {

        this.bitMap = Long.parseLong(new String(Base64.getDecoder().decode(base64)));

    }

    //Get base64 encoded string of bitmap.
    public String getEncodeToBase64() {
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(String.valueOf(bitMap).getBytes());
    }

    //Decode from long
    public ArrayList<Integer> decodeToArray() {

        ArrayList<Integer> ids = new ArrayList<>();

        for (int i = 63; i >= 0; --i) {
            if ((this.bitMap < 0 && i == 63) || (this.bitMap & 1L << i) > 0) {
                ids.add(i + 1);
            }
        }

        return ids;
    }

    public long getBitMap() {
        return bitMap;
    }
}
