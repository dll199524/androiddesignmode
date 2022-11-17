package com.example.designmode.mvp.proxy;

import com.example.designmode.mvp.BasePresenter;
import com.example.designmode.mvp.BaseView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MvpProxyImpl<V extends BaseView> implements MvpProxy{
    private V mView;
    private List<BasePresenter> presenters;

    public MvpProxyImpl(V mView) {
        this.mView = mView;
        presenters = new ArrayList<>();
    }

    @Override
    public void bindCreatePresenter() {
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter annotation = field.getAnnotation(InjectPresenter.class);
            if (annotation != null) {
                Class<? extends BasePresenter> clazz = null;
                try {
                    clazz = (Class<? extends BasePresenter>) field.getType();
                } catch (Exception e) {throw new RuntimeException("no support inject presenter");}
                try {
                    BasePresenter presenter = clazz.newInstance();
                    presenter.attach(mView);
                    presenters.add(presenter);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void unbinderPresenter() {
        for (BasePresenter presenter : presenters) {
            presenter.dettch();
        }
    }
}
