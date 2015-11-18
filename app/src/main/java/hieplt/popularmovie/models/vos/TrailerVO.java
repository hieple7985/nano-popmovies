package hieplt.popularmovie.models.vos;

/**
 * Created by HiepLT on 11/18/15.
 */
public class TrailerVO {

    String mId;
    String mIso6391;
    String mKey;
    String mName;
    String mSite;
    int mSize = 0;
    String mType;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

}
