package net.bplaced.abzzezz.videodroid.util.connection;

import android.os.Parcel;
import android.os.Parcelable;
import net.bplaced.abzzezz.videodroid.util.array.ArrayUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParcelableWatchableURLConnection implements Parcelable {

    public static final Creator<ParcelableWatchableURLConnection> CREATOR = new Creator<ParcelableWatchableURLConnection>() {
        @Override
        public ParcelableWatchableURLConnection createFromParcel(Parcel in) {
            return new ParcelableWatchableURLConnection(in);
        }

        @Override
        public ParcelableWatchableURLConnection[] newArray(int size) {
            return new ParcelableWatchableURLConnection[size];
        }
    };
    private final String url;
    private final int readTimeout;
    private final int connectTimeout;
    private Map<String, String> headers;

    public ParcelableWatchableURLConnection(final String url, final int readTimeout, final int connectTimeout, final String[]... requestHeaders) {
        this.url = url;
        this.headers = ArrayUtil.stringArrayToMap(requestHeaders);
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    public ParcelableWatchableURLConnection(final String url, final Map<String, String> requestHeaders) {
        this.url = url;
        this.headers = requestHeaders;
        this.readTimeout = 0;
        this.connectTimeout = 0;
    }

    protected ParcelableWatchableURLConnection(Parcel in) {
        this.headers = new LinkedHashMap<>();
        this.url = in.readString();
        this.readTimeout = in.readInt();
        this.connectTimeout = in.readInt();

        final int mapSize = in.readInt();
        for (int i = 0; i < mapSize; i++) {
            headers.put(in.readString(), in.readString());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(readTimeout);
        dest.writeInt(connectTimeout);
        dest.writeInt(headers.size());
        headers.forEach((s, strings) -> {
            dest.writeString(s);
            dest.writeString(strings);
        });
    }

    /* Getters n. Setters */

    public String getUrl() {
        return url;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
