package com.example.arouter_annotation;

import javax.lang.model.element.Element;

public class RouterBean {

    public enum TypeEnum {
        ACTIVITY
    }

    private TypeEnum typeEnum;
    private Element element;

    private String path;
    private String group;
}
