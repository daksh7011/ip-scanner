package `in`.technowolf.ipscanner.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.databinding.IpDetailsViewBinding

class IpDetailsView
    @JvmOverloads
    constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        private val defStyleAttr: Int = 0
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding: IpDetailsViewBinding =
            IpDetailsViewBinding.inflate(LayoutInflater.from(context), this, true)

        var titleText: String = "Title"
            set(value) {
                field = value
                invalidate()
            }

        var contentText: String = "Content"
            set(value) {
                field = value
                invalidate()
            }

        @DrawableRes
        var imageSrc: Int = 0
            set(value) {
                field = value
                invalidate()
            }

        init {
            obtainAttributes()
            setupViews()
        }

        private fun obtainAttributes() {
            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.IpDetailsView,
                    defStyleAttr,
                    0
                )

            titleText = typedArray.getString(R.styleable.IpDetailsView_titleText).orEmpty()
            contentText = typedArray.getString(R.styleable.IpDetailsView_contentText).orEmpty()
            imageSrc = typedArray.getResourceId(R.styleable.IpDetailsView_imageSrc, 0)

            typedArray.recycle()
        }

        private fun setupViews() {
            if (imageSrc == 0) {
                binding.ivIcon.visibility = View.GONE
            } else {
                binding.ivIcon.visibility = View.VISIBLE
                binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, imageSrc))
            }
            binding.tvTitle.text = titleText
            binding.tvContent.text = contentText
        }

        fun setValuesToView(
            titleText: String,
            contentText: String,
            @DrawableRes drawableInt: Int = 0
        ) {
            this.titleText = titleText
            this.contentText = contentText
            this.imageSrc = drawableInt
            setupViews()
        }
    }
