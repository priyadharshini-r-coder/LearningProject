package com.example.learningproject.uberclone.activities


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Transformation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.learningproject.R
import com.pnikosis.materialishprogress.ProgressWheel

class SweetAlertDialog /*Dialog(), View.OnClickListener{
    private var mDialogView: View? = null
    private var mModalInAnim: Animation? = null
    private var mModalOutAnim: Animation? = null
    private var mOverlayOutAnim: Animation? = null
    private var mErrorInAnim: Animation? = null
    private var mErrorXInAnim: Animation? = null
    private var mSuccessLayoutAnimSet: Animation? = null
    private var mSuccessBowAnim: Animation? = null
    private var mTitleTextView: TextView? = null
    private var mContentTextView: TextView? = null
    private var mTitleText: String? = null
    private var mContentText: String? = null
    private var mShowCancel = false
    private var mShowContent = false
    private var mCancelText: String? = null
    private var mConfirmText: String? = null
    private var mAlertType = 0
    private var mErrorFrame: FrameLayout? = null
    private var mSuccessFrame: FrameLayout? = null
    private var mProgressFrame: FrameLayout? = null
    private var mSuccessTick: SuccessTickView? = null
    private var mErrorX: ImageView? = null
    private var mSuccessLeftMask: View? = null
    private var mSuccessRightMask: View? = null
    private var mCustomImgDrawable: Drawable? = null
    private var mCustomImage: ImageView? = null
    private var mConfirmButton: Button? = null
    private var mCancelButton: Button? = null
    private var mProgressHelper: ProgressHelper? = null
    private var mWarningFrame: FrameLayout? = null
    private var mCancelClickListener: OnSweetClickListener? = null
    private var mConfirmClickListener: OnSweetClickListener? = null
    private var mCloseFromCancel = false

    val NORMAL_TYPE = 0
    val ERROR_TYPE = 1
    val SUCCESS_TYPE = 2
    val WARNING_TYPE = 3
    val CUSTOM_IMAGE_TYPE = 4
    val PROGRESS_TYPE = 5

    interface OnSweetClickListener {
        fun onClick(sweetAlertDialog: SweetAlertDialog?)
    }

    fun SweetAlertDialog(context: Context?){

    }

    fun SweetAlertDialog(context: Context?, alertType: Int) {

        setCancelable(true)
        setCanceledOnTouchOutside(false)
        mProgressHelper = ProgressHelper(context)
        mAlertType = alertType
        mErrorInAnim = OptAnimationLoader().loadAnimation(getContext(), R.anim.error_frame_in)
        mErrorXInAnim = OptAnimationLoader().loadAnimation(getContext(), R.anim.error_x_in)
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            val childAnims = mErrorXInAnim!!.animations
            var idx = 0
            while (idx < childAnims.size) {
                if (childAnims[idx] is AlphaAnimation) {
                    break
                }
                idx++
            }
            if (idx < childAnims.size) {
                childAnims.removeAt(idx)
            }
        }
        mSuccessBowAnim = OptAnimationLoader().loadAnimation(getContext(), R.anim.success_bow_roate)
        mSuccessLayoutAnimSet =
            OptAnimationLoader().loadAnimation(getContext(), R.anim.success_mask_layout)
        mModalInAnim = OptAnimationLoader().loadAnimation(getContext(), R.anim.modal_in)
        mModalOutAnim = OptAnimationLoader().loadAnimation(getContext(), R.anim.modal_out)
        mModalOutAnim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mDialogView!!.visibility = View.GONE
                mDialogView!!.post {
                    if (mCloseFromCancel) {
                        super.cancel()
                    } else {
                        super.dismiss()
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        // dialog overlay fade out
        mOverlayOutAnim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val wlp: WindowManager.LayoutParams = getWindow().getAttributes()
                wlp.alpha = 1 - interpolatedTime
                getWindow()?.setAttributes(wlp)
            }
        }
        mOverlayOutAnim?.setDuration(120)
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_dialog)
        mDialogView = getWindow()?.getDecorView()?.findViewById<View>(R.id.content)
        mTitleTextView = findViewById<View>(R.id.title_text) as TextView?
        mContentTextView = findViewById<View>(R.id.content_text) as TextView?
        mErrorFrame = findViewById<View>(R.id.error_frame) as FrameLayout?
        mErrorX = mErrorFrame!!.findViewById<View>(R.id.error_x) as ImageView
        mSuccessFrame = findViewById<View>(R.id.success_frame) as FrameLayout?
        mProgressFrame = findViewById<View>(R.id.progress_dialog) as FrameLayout?
        mSuccessTick = mSuccessFrame!!.findViewById<View>(R.id.success_tick) as SuccessTickView
        mSuccessLeftMask = mSuccessFrame!!.findViewById(R.id.mask_left)
        mSuccessRightMask = mSuccessFrame!!.findViewById(R.id.mask_right)
        mCustomImage = findViewById<View>(R.id.custom_image) as ImageView?
        mWarningFrame = findViewById<View>(R.id.warning_frame) as FrameLayout?
        mConfirmButton = findViewById<View>(R.id.confirm_button) as Button?
        mCancelButton = findViewById<View>(R.id.cancel_button) as Button?
        mProgressHelper.setProgressWheel(findViewById<View>(R.id.progressWheel) as ProgressWheel?)
        mConfirmButton!!.setOnClickListener(this)
        mCancelButton!!.setOnClickListener(this)
        setTitleText(mTitleText)
        setContentText(mContentText)
        setCancelText(mCancelText)
        setConfirmText(mConfirmText)
        changeAlertType(mAlertType, true)
    }

    private fun restore() {
        mCustomImage!!.visibility = View.GONE
        mErrorFrame!!.visibility = View.GONE
        mSuccessFrame!!.visibility = View.GONE
        mWarningFrame!!.visibility = View.GONE
        mProgressFrame!!.visibility = View.GONE
        mConfirmButton!!.visibility = View.VISIBLE
        mConfirmButton!!.setBackgroundResource(R.drawable.blue_button_background)
        mErrorFrame!!.clearAnimation()
        mErrorX!!.clearAnimation()
        mSuccessTick.clearAnimation()
        mSuccessLeftMask!!.clearAnimation()
        mSuccessRightMask!!.clearAnimation()
    }

    private fun playAnimation() {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame!!.startAnimation(mErrorInAnim)
            mErrorX!!.startAnimation(mErrorXInAnim)
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim()
            mSuccessRightMask!!.startAnimation(mSuccessBowAnim)
        }
    }

    private fun changeAlertType(alertType: Int, fromCreate: Boolean) {
        mAlertType = alertType
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore()
            }
            when (mAlertType) {
                ERROR_TYPE -> mErrorFrame!!.visibility = View.VISIBLE
                SUCCESS_TYPE -> {
                    mSuccessFrame!!.visibility = View.VISIBLE
                    // initial rotate layout of success mask
                    mSuccessLeftMask!!.startAnimation(mSuccessLayoutAnimSet!!.animations[0])
                    mSuccessRightMask!!.startAnimation(mSuccessLayoutAnimSet!!.animations[1])
                }
                WARNING_TYPE -> {
                    mConfirmButton!!.setBackgroundResource(R.drawable.red_button_background)
                    mWarningFrame!!.visibility = View.VISIBLE
                }
                CUSTOM_IMAGE_TYPE -> setCustomImage(mCustomImgDrawable)
                PROGRESS_TYPE -> {
                    mProgressFrame!!.visibility = View.VISIBLE
                    mConfirmButton!!.visibility = View.GONE
                }
            }
            if (!fromCreate) {
                playAnimation()
            }
        }
    }

    fun getAlerType(): Int {
        return mAlertType
    }

    fun changeAlertType(alertType: Int) {
        changeAlertType(alertType, false)
    }


    fun getTitleText(): String? {
        return mTitleText
    }

    fun setTitleText(text: String?): SweetAlertDialog? {
        mTitleText = text
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView!!.text = mTitleText
        }
        return this
    }

    fun setCustomImage(drawable: Drawable?): SweetAlertDialog? {
        mCustomImgDrawable = drawable
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage!!.visibility = View.VISIBLE
            mCustomImage!!.setImageDrawable(mCustomImgDrawable)
        }
        return this
    }

    fun setCustomImage(resourceId: Int): SweetAlertDialog? {
        return setCustomImage(getContext().getResources().getDrawable(resourceId))
    }

    fun getContentText(): String? {
        return mContentText
    }

    fun setContentText(text: String?): SweetAlertDialog? {
        mContentText = text
        if (mContentTextView != null && mContentText != null) {
            showContentText(true)
            mContentTextView!!.text = mContentText
        }
        return this
    }

    fun isShowCancelButton(): Boolean {
        return mShowCancel
    }

    fun showCancelButton(isShow: Boolean): SweetAlertDialog? {
        mShowCancel = isShow
        if (mCancelButton != null) {
            mCancelButton!!.visibility = if (mShowCancel) View.VISIBLE else View.GONE
        }
        return this
    }

    fun isShowContentText(): Boolean {
        return mShowContent
    }

    fun showContentText(isShow: Boolean): SweetAlertDialog? {
        mShowContent = isShow
        if (mContentTextView != null) {
            mContentTextView!!.visibility = if (mShowContent) View.VISIBLE else View.GONE
        }
        return this
    }

    fun getCancelText(): String? {
        return mCancelText
    }

    fun setCancelText(text: String?): SweetAlertDialog? {
        mCancelText = text
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true)
            mCancelButton!!.text = mCancelText
        }
        return this
    }

    fun getConfirmText(): String? {
        return mConfirmText
    }

    fun setConfirmText(text: String?): SweetAlertDialog? {
        mConfirmText = text
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton!!.text = mConfirmText
        }
        return this
    }

    fun setCancelClickListener(listener: OnSweetClickListener?): SweetAlertDialog? {
        mCancelClickListener = listener
        return this
    }

    fun setConfirmClickListener(listener: OnSweetClickListener?): SweetAlertDialog? {
        mConfirmClickListener = listener
        return this
    }

    protected override fun onStart() {
        mDialogView!!.startAnimation(mModalInAnim)
        playAnimation()
    }

    *//**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     *//*
    override fun cancel() {
        dismissWithAnimation(true)
    }

    *//**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     *//*
    fun dismissWithAnimation() {
        dismissWithAnimation(false)
    }

    private fun dismissWithAnimation(fromCancel: Boolean) {
        mCloseFromCancel = fromCancel
        mConfirmButton!!.startAnimation(mOverlayOutAnim)
        mDialogView!!.startAnimation(mModalOutAnim)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener!!.onClick(this@SweetAlertDialog)
            } else {
                dismissWithAnimation()
            }
        } else if (v.id == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener!!.onClick(this@SweetAlertDialog)
            } else {
                dismissWithAnimation()
            }
        }
    }

    fun getProgressHelper(): ProgressHelper? {
        return mProgressHelper
    }*/

