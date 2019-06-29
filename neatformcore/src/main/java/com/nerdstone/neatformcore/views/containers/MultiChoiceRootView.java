package com.nerdstone.neatformcore.views.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nerdstone.neatformcore.R;
import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.controls.CheckboxNFormView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;
import java.util.List;

public class MultiChoiceRootView extends LinearLayout implements RootView {

  private String label;

  private TextView labelTextView;

  public MultiChoiceRootView(Context context) {
    super(context);
    setupView();
  }

  public MultiChoiceRootView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.multi_choice_nformview_layout,
        this, true);
    setupAttributes(attrs);
    setupView();
  }

  @Override
  public RootView initRootView() {
    setupView();
    return this;
  }

  @Override
  public void addChild(NFormView nFormView) {
    addView(nFormView.getViewOption().getView());
  }

  @Override
  public void addChildren(List<NFormViewProperty> viewProperties, ViewDataHandler viewDataHandler) {
    for (int count = 0; count < 5; count++) {
      addChild(new CheckboxNFormView(getContext(), null));
    }
  }

  @Override
  public List<NFormViewData> getViewsData() {

    return null;
  }

  private void setupAttributes(AttributeSet attrs) {
    TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs,
        R.styleable.CheckboxNFormView, 0, 0);
    try {
      label = typedArray.getString(R.styleable.CheckboxNFormView_multiChoiceLabel);
    } finally {
      typedArray.recycle();
    }
  }

  private void createLabel() {
    labelTextView = findViewById(R.id.multichoiceLabel);
    labelTextView.setText(label != null ? label : "");
    labelTextView.setTextSize(18f);
    labelTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    labelTextView.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
  }

  @Override
  public void setupView() {
    setOrientation(VERTICAL);
    if (getLayoutParams() != null) {
      getLayoutParams().height = LayoutParams.WRAP_CONTENT;
      getLayoutParams().width = LayoutParams.MATCH_PARENT;
    }
    createLabel();
  }
}
