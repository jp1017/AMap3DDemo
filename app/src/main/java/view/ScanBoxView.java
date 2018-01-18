package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.amap.map3d.demo.R;

public class ScanBoxView extends View {
    private int mMoveStepDistance;
    private int mAnimDelayTime;

    private Rect mFramingRect;
    private float mScanLineTop;
    private Paint mPaint;

    private int mCornerColor;
    private int mCornerLength;
    private int mCornerSize;
    private int mRectWidth;
    private int mRectHeight;
    private int mScanLineSize;
    private int mScanLineColor;
    private int mScanLineMargin;
    private Bitmap mScanLineBitmap;
    private int mBorderSize;
    private int mBorderColor;
    private int mAnimTime;
    private Bitmap mOriginQRCodeGridScanLineBitmap;


    private float mHalfCornerSize;

    public ScanBoxView(Context context) {
        super(context);
    }

    public ScanBoxView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScanBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCornerColor = Color.GREEN;
        mCornerLength = BGAQRCodeUtil.dp2px(context, 30);
        mCornerSize = BGAQRCodeUtil.dp2px(context, 10);
        mScanLineSize = BGAQRCodeUtil.dp2px(context, 2);
        mScanLineColor = Color.GREEN;
        mRectWidth = BGAQRCodeUtil.dp2px(context, 200);
        mScanLineMargin = 8;
        mScanLineBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_default_scan_line);
        mBorderSize = BGAQRCodeUtil.dp2px(context, 2);
        mBorderColor = Color.RED;
        mAnimTime = 1500;
        mMoveStepDistance = BGAQRCodeUtil.dp2px(context, 2);
        initCustomAttrs(context, attrs);
    }

    public void initCustomAttrs(Context context, AttributeSet attrs) {
/*        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRCodeView);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();*/

        afterInitCustomAttrs();
    }

//    private void initCustomAttr(int attr, TypedArray typedArray) {
//        if (attr == R.styleable.QRCodeView_qrcv_topOffset) {
//            mTopOffset = typedArray.getDimensionPixelSize(attr, mTopOffset);
//        } else if (attr == R.styleable.QRCodeView_qrcv_cornerSize) {
//            mCornerSize = typedArray.getDimensionPixelSize(attr, mCornerSize);
//        } else if (attr == R.styleable.QRCodeView_qrcv_cornerLength) {
//            mCornerLength = typedArray.getDimensionPixelSize(attr, mCornerLength);
//        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineSize) {
//            mScanLineSize = typedArray.getDimensionPixelSize(attr, mScanLineSize);
//        } else if (attr == R.styleable.QRCodeView_qrcv_rectWidth) {
//            mRectWidth = typedArray.getDimensionPixelSize(attr, mRectWidth);
//        } else if (attr == R.styleable.QRCodeView_qrcv_maskColor) {
//            mMaskColor = typedArray.getColor(attr, mMaskColor);
//        } else if (attr == R.styleable.QRCodeView_qrcv_cornerColor) {
//            mCornerColor = typedArray.getColor(attr, mCornerColor);
//        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineColor) {
//            mScanLineColor = typedArray.getColor(attr, mScanLineColor);
//        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineMargin) {
//            mScanLineMargin = typedArray.getDimensionPixelSize(attr, mScanLineMargin);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isShowDefaultScanLineDrawable) {
//            mIsShowDefaultScanLineDrawable = typedArray.getBoolean(attr, mIsShowDefaultScanLineDrawable);
//        } else if (attr == R.styleable.QRCodeView_qrcv_customScanLineDrawable) {
//            mCustomScanLineDrawable = typedArray.getDrawable(attr);
//        } else if (attr == R.styleable.QRCodeView_qrcv_borderSize) {
//            mBorderSize = typedArray.getDimensionPixelSize(attr, mBorderSize);
//        } else if (attr == R.styleable.QRCodeView_qrcv_borderColor) {
//            mBorderColor = typedArray.getColor(attr, mBorderColor);
//        } else if (attr == R.styleable.QRCodeView_qrcv_animTime) {
//            mAnimTime = typedArray.getInteger(attr, mAnimTime);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isCenterVertical) {
//            mIsCenterVertical = typedArray.getBoolean(attr, mIsCenterVertical);
//        } else if (attr == R.styleable.QRCodeView_qrcv_barcodeRectHeight) {
//            mBarcodeRectHeight = typedArray.getDimensionPixelSize(attr, mBarcodeRectHeight);
//        } else if (attr == R.styleable.QRCodeView_qrcv_tipTextMargin) {
//            mTipTextMargin = typedArray.getDimensionPixelSize(attr, mTipTextMargin);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isShowTipBackground) {
//            mIsShowTipBackground = typedArray.getBoolean(attr, mIsShowTipBackground);
//        } else if (attr == R.styleable.QRCodeView_qrcv_tipBackgroundColor) {
//            mTipBackgroundColor = typedArray.getColor(attr, mTipBackgroundColor);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isScanLineReverse) {
//            mIsScanLineReverse = typedArray.getBoolean(attr, mIsScanLineReverse);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isShowDefaultGridScanLineDrawable) {
//            mIsShowDefaultGridScanLineDrawable = typedArray.getBoolean(attr, mIsShowDefaultGridScanLineDrawable);
//        } else if (attr == R.styleable.QRCodeView_qrcv_customGridScanLineDrawable) {
//            mCustomGridScanLineDrawable = typedArray.getDrawable(attr);
//        } else if (attr == R.styleable.QRCodeView_qrcv_isOnlyDecodeScanBoxArea) {
//            mIsOnlyDecodeScanBoxArea = typedArray.getBoolean(attr, mIsOnlyDecodeScanBoxArea);
//        }
//    }

    private void afterInitCustomAttrs() {
        if (mOriginQRCodeGridScanLineBitmap == null) {
            mOriginQRCodeGridScanLineBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_default_grid_scan_line);
            mOriginQRCodeGridScanLineBitmap = BGAQRCodeUtil.makeTintBitmap(mOriginQRCodeGridScanLineBitmap, mScanLineColor);
        }
        mHalfCornerSize = 1.0f * mCornerSize / 2;

        setIsBarcode();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mFramingRect == null) {
            return;
        }

        // 画边框线
        drawBorderLine(canvas);

        // 画四个直角的线
        drawCornerLine(canvas);

        // 画扫描线
        drawScanLine(canvas);

        // 移动扫描线的位置
        moveScanLine();

    }

    /**
     * 画边框线
     *
     * @param canvas
     */
    private void drawBorderLine(Canvas canvas) {
        if (mBorderSize > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mBorderColor);
            mPaint.setStrokeWidth(mBorderSize);
            canvas.drawRect(mFramingRect, mPaint);
        }
    }

    /**
     * 画四个直角的线
     *
     * @param canvas
     */
    private void drawCornerLine(Canvas canvas) {
        if (mHalfCornerSize > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mCornerColor);
            mPaint.setStrokeWidth(mCornerSize);
            canvas.drawLine(mFramingRect.left - mHalfCornerSize, mFramingRect.top, mFramingRect.left - mHalfCornerSize + mCornerLength, mFramingRect.top, mPaint);
            canvas.drawLine(mFramingRect.left, mFramingRect.top - mHalfCornerSize, mFramingRect.left, mFramingRect.top - mHalfCornerSize + mCornerLength, mPaint);
            canvas.drawLine(mFramingRect.right + mHalfCornerSize, mFramingRect.top, mFramingRect.right + mHalfCornerSize - mCornerLength, mFramingRect.top, mPaint);
            canvas.drawLine(mFramingRect.right, mFramingRect.top - mHalfCornerSize, mFramingRect.right, mFramingRect.top - mHalfCornerSize + mCornerLength, mPaint);

            canvas.drawLine(mFramingRect.left - mHalfCornerSize, mFramingRect.bottom, mFramingRect.left - mHalfCornerSize + mCornerLength, mFramingRect.bottom, mPaint);
            canvas.drawLine(mFramingRect.left, mFramingRect.bottom + mHalfCornerSize, mFramingRect.left, mFramingRect.bottom + mHalfCornerSize - mCornerLength, mPaint);
            canvas.drawLine(mFramingRect.right + mHalfCornerSize, mFramingRect.bottom, mFramingRect.right + mHalfCornerSize - mCornerLength, mFramingRect.bottom, mPaint);
            canvas.drawLine(mFramingRect.right, mFramingRect.bottom + mHalfCornerSize, mFramingRect.right, mFramingRect.bottom + mHalfCornerSize - mCornerLength, mPaint);
        }
    }

    /**
     * 画扫描线
     *
     * @param canvas
     */
    private void drawScanLine(Canvas canvas) {
        if (mScanLineBitmap != null) {
            RectF lineRect = new RectF(mFramingRect.left + mHalfCornerSize + mScanLineMargin, mScanLineTop, mFramingRect.right - mHalfCornerSize - mScanLineMargin, mScanLineTop + mScanLineBitmap.getHeight());
            canvas.drawBitmap(mScanLineBitmap, null, lineRect, mPaint);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mScanLineColor);
            canvas.drawRect(mFramingRect.left + mHalfCornerSize + mScanLineMargin, mScanLineTop, mFramingRect.right - mHalfCornerSize - mScanLineMargin, mScanLineTop + mScanLineSize, mPaint);
        }
    }

    /**
     * 移动扫描线的位置
     */
    private void moveScanLine() {
        // 处理非网格扫描图片的情况
        mScanLineTop += mMoveStepDistance;
        int scanLineSize = mScanLineSize;
        if (mScanLineBitmap != null) {
            scanLineSize = mScanLineBitmap.getHeight();
        }

        if (mScanLineTop + scanLineSize > mFramingRect.bottom - mHalfCornerSize) {
            mScanLineTop = mFramingRect.top + mHalfCornerSize + 0.5f;
        }
        postInvalidateDelayed(mAnimDelayTime, mFramingRect.left, mFramingRect.top, mFramingRect.right, mFramingRect.bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calFramingRect();
    }

    private void calFramingRect() {
        int leftOffset = (getWidth() - mRectWidth) / 2;
        mFramingRect = new Rect(leftOffset, 0, leftOffset + mRectWidth, mRectHeight);
    }

    public void setIsBarcode() {
        mRectHeight = mRectWidth;
        mAnimDelayTime = (int) ((1.0f * mAnimTime * mMoveStepDistance) / mRectHeight);

        calFramingRect();

        postInvalidate();
    }
}