import java.io.IOException;
import java.nio.channels.SelectableChannel;

interface IOCallback {
    void onIOReady(SelectableChannel channel) throws IOException;
}
