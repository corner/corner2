package corner.z7.SevenZip;

import corner.z7.Common.RecordVector;


public interface ICompressCoder2 {
    public int Code(RecordVector<java.io.InputStream> inStreams,
        Object useless1, // const UInt64 ** /* inSizes */,
        int numInStreams, RecordVector<java.io.OutputStream> outStreams,
        Object useless2, // const UInt64 ** /* outSizes */,
        int numOutStreams, ICompressProgressInfo progress)
        throws java.io.IOException;

    public void close() throws java.io.IOException; // destructor
}
