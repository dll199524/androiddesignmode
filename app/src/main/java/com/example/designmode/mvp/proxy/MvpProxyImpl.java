package com.example.designmode.mvp.proxy;

import com.example.designmode.mvp.BasePresenter;
import com.example.designmode.mvp.BaseView;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
//                try {
//                    //这里不会报错，编译器在运行的时候会进行泛型擦除
//                    clazz = (Class<? extends BasePresenter>) field.getType();
//                } catch (Exception e) {throw new RuntimeException("no support inject presenter");}
                clazz = (Class) field.getType();
                if (!BasePresenter.class.isAssignableFrom(clazz))
                    throw new RuntimeException("not support inject presenter type" + clazz.getName());

                BasePresenter presenter = null;
                try {
                    presenter = clazz.newInstance();
                    presenter.attach(mView);
                    presenters.add(presenter);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                checkView(presenter);
            }
        }
    }

    private void checkView(BasePresenter presenter) {
        Type[] params = ((ParameterizedType)(presenter.getClass().getGenericSuperclass())).getActualTypeArguments();
        Class viewClazz = (Class) params[0];
        Class[] viewClasses = mView.getClass().getInterfaces();
        boolean isImplementView = false;
        for (Class viewClass : viewClasses) {
            if (viewClass.isAssignableFrom(viewClazz))
                isImplementView = true;
        }
        if (!isImplementView)
            throw new RuntimeException(mView.getClass().getSimpleName() + "must implements" + viewClazz.getName());
    }

    @Override
    public void unbinderPresenter() {
        for (BasePresenter presenter : presenters) {
            presenter.dettch();
        }
    }
}
