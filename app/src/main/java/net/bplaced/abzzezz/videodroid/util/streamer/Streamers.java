package net.bplaced.abzzezz.videodroid.util.streamer;

import net.bplaced.abzzezz.videodroid.util.streamer.streamer.apidmb.ApiMdb;
import net.bplaced.abzzezz.videodroid.util.streamer.streamer.gomo.Gomo;
import net.bplaced.abzzezz.videodroid.util.streamer.streamer.mixdrop.MixDrop;
import net.bplaced.abzzezz.videodroid.util.streamer.streamer.streamsb.StreamSb;
import net.bplaced.abzzezz.videodroid.util.streamer.streamer.vidcloud.Vidcloud;

public enum Streamers {

    VID_CLOUD("vidcloud", new Vidcloud()),
    STREAM_SB("streamsb", new StreamSb()),
    API_MDB("apimdb", new ApiMdb()),
    //DOOD_STREAM("doodstream", new DoodStream()),
    MIX_DROP("mixdrop", new MixDrop()),
    GOMO("gomo", new Gomo());


    private final String alt;
    private final Streamer streamer;


    Streamers(final String alt, final Streamer streamer) {
        this.alt = alt;
        this.streamer = streamer;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    public String getAlt() {
        return alt;
    }
}
