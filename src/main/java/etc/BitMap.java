package etc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * BitMap stored as long
 */
public class BitMap {

    private long bitMap;

    /**
     * Class Constructor.
     * @param arrayList List of numbers to be stored into the bitmap.
     */
    public BitMap(ArrayList<Integer> arrayList) {

        this.bitMap = 0;

        for (Integer id : arrayList) {
            this.bitMap |= (1L << (id - 1));
        }

    }

    public BitMap(long bitMap) {
        this.bitMap = bitMap;
    }

//    /**
//     * Alternative constructor.
//     * @param base64 Base64 encoded long.
//     */
//    public BitMap(String base64) {
//
//        this.bitMap = Long.parseLong(new String(Base64.getDecoder().decode(base64)));
//
//    }
//
//    //Get base64 encoded string of bitmap.
//    public String getEncodeToBase64() {
//        return Base64.getEncoder()
//                .withoutPadding()
//                .encodeToString(String.valueOf(bitMap).getBytes());
//    }

    //Decode from long
    public List<Integer> decodeToList() {

        List<Integer> ids = new LinkedList<>();

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
