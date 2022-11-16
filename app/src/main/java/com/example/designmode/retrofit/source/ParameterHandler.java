package com.example.designmode.retrofit.source;

public interface ParameterHandler<T> {
    public void apply(RequestBulider requestBulider, T value);

    //不同策略
    class Querry<T> implements ParameterHandler<T> {
        private String key;

        public Querry(String key) {
            this.key = key;
        }

        @Override
        public void apply(RequestBulider requestBulider, T value) {
            requestBulider.addQuerryName(key, value.toString());
        }
    }
}
