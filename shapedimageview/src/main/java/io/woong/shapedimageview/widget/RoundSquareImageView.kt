@file:Suppress("MemberVisibilityCanBePrivate")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.R
import io.woong.shapedimageview.SquareShapedImageView
import io.woong.shapedimageview.util.drawRoundRect

class RoundSquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SquareShapedImageView(context, attrs, defStyle) {

    companion object {
        /** The default value of [RoundSquareImageView]'s radius. */
        const val DEFAULT_RADIUS: Float = -1f
    }

    /** The value for using when radius is [DEFAULT_RADIUS]. */
    private val defaultRadius: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        16f,
        this.resources.displayMetrics
    )

    /**
     * The radius of the imageview's top-left.
     * Its unit is pixel.
     */
    var topLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * The radius of the imageview's top-right.
     * Its unit is pixel.
     */
    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * The radius of the imageview's bottom-right.
     * Its unit is pixel.
     */
    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * The radius of the imageview's bottom-left.
     * Its unit is pixel.
     */
    var bottomLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * The radius of the border's top-left.
     * Its unit is pixel.
     */
    private var borderTopLeftRadius: Float = defaultRadius

    /**
     * The radius of the border's top-right.
     * Its unit is pixel.
     */
    private var borderTopRightRadius: Float = defaultRadius

    /**
     * The radius of the border's bottom-right.
     * Its unit is pixel.
     */
    private var borderBottomRightRadius: Float = defaultRadius

    /**
     * The radius of the border's bottom-left.
     * Its unit is pixel.
     */
    private var borderBottomLeftRadius: Float = defaultRadius

    /**
     * The radius of the shadow's top-left.
     * Its unit is pixel.
     */
    private var shadowTopLeftRadius: Float = defaultRadius

    /**
     * The radius of the shadow's top-right.
     * Its unit is pixel.
     */
    private var shadowTopRightRadius: Float = defaultRadius

    /**
     * The radius of the shadow's bottom-right.
     * Its unit is pixel.
     */
    private var shadowBottomRightRadius: Float = defaultRadius

    /**
     * The radius of the shadow's bottom-left.
     * Its unit is pixel.
     */
    private var shadowBottomLeftRadius: Float = defaultRadius

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundSquareImageView, defStyle, 0)

        try {
            val r = a.getDimension(R.styleable.RoundSquareImageView_radius, DEFAULT_RADIUS)
            if (r != DEFAULT_RADIUS) {
                this.topLeftRadius = r
                this.topRightRadius = r
                this.bottomRightRadius = r
                this.bottomLeftRadius = r
            }

            val bs = if (borderEnabled) {
                this.borderSize
            } else {
                0f
            }
            this.borderTopLeftRadius = this.topLeftRadius + bs
            this.borderTopRightRadius = this.topRightRadius + bs
            this.borderBottomRightRadius = this.bottomRightRadius + bs
            this.borderBottomLeftRadius = this.bottomLeftRadius + bs

            if (shadowEnabled) {
                this.shadowTopLeftRadius = this.borderTopLeftRadius
                this.shadowTopRightRadius = this.borderTopRightRadius
                this.shadowBottomRightRadius = this.borderBottomRightRadius
                this.shadowBottomLeftRadius = this.borderBottomLeftRadius
            } else {
                this.shadowTopLeftRadius = this.topLeftRadius
                this.shadowTopRightRadius = this.topRightRadius
                this.shadowBottomRightRadius = this.bottomRightRadius
                this.shadowBottomLeftRadius = this.bottomLeftRadius
            }

            val rtl = a.getDimension(R.styleable.RoundSquareImageView_top_left_radius, DEFAULT_RADIUS)
            if (rtl != DEFAULT_RADIUS) {
                this.topLeftRadius = rtl
                this.borderTopLeftRadius = if (borderEnabled) {
                    rtl + this.borderSize
                } else {
                    rtl
                }
                this.shadowTopLeftRadius = if (shadowEnabled) {
                    this.borderTopLeftRadius
                } else {
                    rtl
                }
            }

            val rtr = a.getDimension(R.styleable.RoundSquareImageView_top_right_radius, DEFAULT_RADIUS)
            if (rtr != DEFAULT_RADIUS) {
                this.topRightRadius = rtr
                this.borderTopRightRadius = if (borderEnabled) {
                    rtr + this.borderSize
                } else {
                    rtr
                }
                this.shadowTopRightRadius = if (shadowEnabled) {
                    this.borderTopRightRadius
                } else {
                    rtr
                }
            }

            val rbr = a.getDimension(R.styleable.RoundSquareImageView_bottom_right_radius, DEFAULT_RADIUS)
            if (rbr != DEFAULT_RADIUS) {
                this.bottomRightRadius = rbr
                this.borderBottomRightRadius = if (borderEnabled) {
                    rbr + this.borderSize
                } else {
                    rbr
                }
                this.shadowBottomRightRadius = if (shadowEnabled) {
                    this.borderBottomRightRadius
                } else {
                    rbr
                }
            }

            val rbl = a.getDimension(R.styleable.RoundSquareImageView_bottom_left_radius, DEFAULT_RADIUS)
            if (rbl != DEFAULT_RADIUS) {
                this.bottomLeftRadius = rbl
                this.borderBottomLeftRadius = if (borderEnabled) {
                    rbl + this.borderSize
                } else {
                    rbl
                }
                this.shadowBottomLeftRadius = if (shadowEnabled) {
                    this.borderBottomLeftRadius
                } else {
                    rbl
                }
            }
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled) {
            canvas.drawRoundRect(
                shadowRect,
                shadowTopLeftRadius,
                shadowTopRightRadius,
                shadowBottomRightRadius,
                shadowBottomLeftRadius,
                shadowPaint
            )
        }

        if (borderEnabled) {
            canvas.drawRoundRect(
                borderRect,
                borderTopLeftRadius,
                borderTopRightRadius,
                borderBottomRightRadius,
                borderBottomLeftRadius,
                borderPaint
            )
        }

        canvas.drawRoundRect(
            imageRect,
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            imagePaint
        )
    }
}
