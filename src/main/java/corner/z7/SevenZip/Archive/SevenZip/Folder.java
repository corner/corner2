package corner.z7.SevenZip.Archive.SevenZip;

import corner.z7.Common.IntVector;
import corner.z7.Common.LongVector;
import corner.z7.Common.RecordVector;

import corner.z7.SevenZip.Archive.Common.BindPair;

import java.io.IOException;


class Folder {
    public RecordVector<CoderInfo> Coders = new RecordVector<CoderInfo>();
    RecordVector<BindPair> BindPairs = new RecordVector<BindPair>();
    IntVector PackStreams = new IntVector();
    LongVector UnPackSizes = new LongVector();
    int UnPackCRC;
    boolean UnPackCRCDefined;

    Folder() {
        UnPackCRCDefined = false;
    }

    long GetUnPackSize() throws IOException {
        if (UnPackSizes.isEmpty()) {
            return 0;
        }

        for (int i = UnPackSizes.size() - 1; i >= 0; i--)
            if (FindBindPairForOutStream(i) < 0) {
                return UnPackSizes.get(i);
            }

        throw new IOException("1"); // throw 1  // TBD
    }

    int FindBindPairForInStream(int inStreamIndex) {
        for (int i = 0; i < BindPairs.size(); i++)
            if (BindPairs.get(i).InIndex == inStreamIndex) {
                return i;
            }

        return -1;
    }

    int FindBindPairForOutStream(int outStreamIndex) {
        for (int i = 0; i < BindPairs.size(); i++)
            if (BindPairs.get(i).OutIndex == outStreamIndex) {
                return i;
            }

        return -1;
    }

    int FindPackStreamArrayIndex(int inStreamIndex) {
        for (int i = 0; i < PackStreams.size(); i++)
            if (PackStreams.get(i) == inStreamIndex) {
                return i;
            }

        return -1;
    }

    int GetNumOutStreams() {
        int result = 0;

        for (int i = 0; i < Coders.size(); i++)
            result += Coders.get(i).NumOutStreams;

        return result;
    }
}
