package com.nerdstone.neatformcore.junit;

import android.content.Context;

import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.domain.view.RulesHandler;
import com.nerdstone.neatformcore.rules.RulesFactory;
import com.nerdstone.neatformcore.utils.NFormViewUtils;
import com.nerdstone.neatformcore.views.containers.VerticalRootView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NFormViewUtilsTest {

    @Mock
    private RulesFactory rulesFactory;

    @Mock
    private RulesHandler rulesHandler;

    @Mock
    private ViewDispatcher viewDispatcher;

    @Mock
    private Context context;

    @Mock
    private RootView rootView;

    @Mock
    private VerticalRootView verticalRootView;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
         rootView = verticalRootView;
        Mockito.doReturn(rulesHandler).when(viewDispatcher).getRulesHandler();
        Mockito.doReturn(rulesFactory).when(rulesHandler).getRulesFactory();
        Mockito.when(rulesHandler.getRulesFactory().getSubjectsRegistry()).thenReturn(new HashMap<>());
    }

    @Test
    public void testRegisterSubjects() {
        NFormViewProperty editTextProps = new NFormViewProperty();
        editTextProps.setType("edit_text");
        editTextProps.setName("name");
        NFormViewProperty spinnerProps = new NFormViewProperty();
        spinnerProps.setType("spinner");
        spinnerProps.setName("name");
        List<NFormViewProperty> list = Collections.unmodifiableList(Arrays.asList(editTextProps, spinnerProps));
        NFormViewUtils.createViews(rootView, list, context, viewDispatcher);
    }

}
