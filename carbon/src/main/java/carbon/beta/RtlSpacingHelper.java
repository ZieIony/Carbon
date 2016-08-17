package carbon.beta;

public class RtlSpacingHelper {
    public static final int UNDEFINED = Integer.MIN_VALUE;

    private int mLeft = 0;
    private int mRight = 0;
    private int mStart = UNDEFINED;
    private int mEnd = UNDEFINED;
    private int mExplicitLeft = 0;
    private int mExplicitRight = 0;

    private boolean mIsRtl = false;
    private boolean mIsRelative = false;

    public int getLeft() {
        return mLeft;
    }

    public int getRight() {
        return mRight;
    }

    public int getStart() {
        return mIsRtl ? mRight : mLeft;
    }

    public int getEnd() {
        return mIsRtl ? mLeft : mRight;
    }

    public void setRelative(int start, int end) {
        mStart = start;
        mEnd = end;
        mIsRelative = true;
        if (mIsRtl) {
            if (end != UNDEFINED) mLeft = end;
            if (start != UNDEFINED) mRight = start;
        } else {
            if (start != UNDEFINED) mLeft = start;
            if (end != UNDEFINED) mRight = end;
        }
    }

    public void setAbsolute(int left, int right) {
        mIsRelative = false;
        if (left != UNDEFINED) mLeft = mExplicitLeft = left;
        if (right != UNDEFINED) mRight = mExplicitRight = right;
    }

    public void setDirection(boolean isRtl) {
        if (isRtl == mIsRtl) {
            return;
        }
        mIsRtl = isRtl;
        if (mIsRelative) {
            if (isRtl) {
                mLeft = mEnd != UNDEFINED ? mEnd : mExplicitLeft;
                mRight = mStart != UNDEFINED ? mStart : mExplicitRight;
            } else {
                mLeft = mStart != UNDEFINED ? mStart : mExplicitLeft;
                mRight = mEnd != UNDEFINED ? mEnd : mExplicitRight;
            }
        } else {
            mLeft = mExplicitLeft;
            mRight = mExplicitRight;
        }
    }
}