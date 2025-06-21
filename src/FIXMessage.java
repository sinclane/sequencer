import java.util.HashMap;
import java.util.Map;

/**
 * Represents a FIX message as a raw byte array with tag offset indexing.
 */
class FIXMessage {
    private static class ValueRef {
        final int offset;
        final int length;

        ValueRef(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
    }

    private final byte[] data;
    private final Map<Integer, ValueRef> tagOffsets = new HashMap<>();

    public FIXMessage(byte[] data) {
        this.data = data;
        parseTags();
    }

    private void parseTags() {
        int i = 0;
        while (i < data.length) {
            int tagStart = i;
            while (i < data.length && data[i] != '=') i++;
            if (i >= data.length) break;

            int tag = parseInt(data, tagStart, i);
            int valueStart = ++i;

            while (i < data.length && data[i] != '\u0001') i++;
            int valueLength = i - valueStart;

            tagOffsets.put(tag, new ValueRef(valueStart, valueLength));

            i++; // skip delimiter
        }
    }

    private int parseInt(byte[] buf, int start, int end) {
        int val = 0;
        for (int i = start; i < end; i++) {
            val = val * 10 + (buf[i] - '0');
        }
        return val;
    }

    public String getValue(int tag) {

        ValueRef ref = tagOffsets.get(tag);

        if (ref == null) return null;

        return new String(data, ref.offset, ref.length);
    }

    public byte[] getRawData() {
        return data;
    }
}
