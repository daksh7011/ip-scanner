package `in`.technowolf.ipscanner.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.databinding.LabelValueViewBinding

class LabelValueView
    @JvmOverloads
    constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        private val defStyleAttr: Int = 0
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding: LabelValueViewBinding =
            LabelValueViewBinding.inflate(LayoutInflater.from(context), this, true)

        @Suppress("MemberVisibilityCanBePrivate")
        var labelText: String = "Label"
            set(value) {
                field = value
                invalidate()
            }

        @Suppress("MemberVisibilityCanBePrivate")
        var valueText: String = "Content"
            set(value) {
                field = value
                invalidate()
            }

        @Suppress("MemberVisibilityCanBePrivate")
        @DrawableRes
        var startImageSrc: Int = 0
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
                    R.styleable.LabelValueView,
                    defStyleAttr,
                    0
                )

            labelText = typedArray.getString(R.styleable.LabelValueView_labelText).orEmpty()
            valueText = typedArray.getString(R.styleable.LabelValueView_valueText).orEmpty()
            startImageSrc = typedArray.getResourceId(R.styleable.LabelValueView_startImageSrc, 0)

            typedArray.recycle()
        }

        private fun setupViews() {
            if (startImageSrc == 0) {
                binding.ivStartIcon.visibility = View.GONE
            } else {
                binding.ivStartIcon.visibility = View.VISIBLE
                binding.ivStartIcon.setImageDrawable(ContextCompat.getDrawable(context, startImageSrc))
            }
            binding.tvLabel.text = labelText
            if (valueText.isEmpty()) {
                binding.tvValue.visibility = View.GONE
            } else {
                binding.tvValue.visibility = View.VISIBLE
                binding.tvValue.text = valueText
            }
        }

        @Suppress("unused")
        fun setLabel(labelText: String) {
            this.labelText = labelText
            binding.tvLabel.text = labelText
        }

        @Suppress("unused")
        fun setValue(valueText: String) {
            this.valueText = valueText
            binding.tvValue.text = valueText
        }

        @Suppress("unused")
        fun setStartImage(
            @DrawableRes startImageSrc: Int
        ) {
            this.startImageSrc = startImageSrc
            binding.ivStartIcon.setImageDrawable(ContextCompat.getDrawable(context, startImageSrc))
        }
    }
