package ir.mahmoud.app.Moudle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import ir.mahmoud.app.Classes.FormatHelper;

public class CustomEditText extends EditText {
    public static Typeface FONT_NAME;


    public CustomEditText(Context context) {
        super(context);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMedium.ttf");
        this.setTypeface(FONT_NAME);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMedium.ttf");
        this.setTypeface(FONT_NAME);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMedium.ttf");
        this.setTypeface(FONT_NAME);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null)
            text = FormatHelper.toPersianNumber(text.toString());
        super.setText(text, type);
    }
}