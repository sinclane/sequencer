import java.util.ArrayList;
import java.util.List;

/**
 * Stores FIX messages in memory.
 */
class MessageStore {
    private final List<FIXMessage> messages;

    public MessageStore() {
        // Pre-allocate with an expected capacity to reduce GC churn
        messages = new ArrayList<>(1024);
    }

    /**
     * Adds a FIX message to the store.
     *
     * @param msg the FIX message to store
     */
    public void add(FIXMessage msg) {
        messages.add(msg);
    }

    /**
     * Returns all stored FIX messages.
     *
     * @return list of FIXMessage objects
     */
    public List<FIXMessage> getMessages() {
        return messages;
    }

    /**
     * Filters messages by FIX MsgType (tag 35).
     *
     * @param msgType the message type to filter by (e.g., "A" for Logon)
     * @return list of matching FIX messages
     */
    public List<FIXMessage> getMessagesByType(String msgType) {
        List<FIXMessage> result = new ArrayList<>();
        for (FIXMessage msg : messages) {
            if (msgType.equals(msg.getValue(FIXTags.MsgType))) {
                result.add(msg);
            }
        }
        return result;
    }

    /**
     * Clears all stored messages (use with care).
     */
    public void clear() {
        messages.clear();
    }
}
