package itauamachado.ownpos.network;

import org.json.JSONArray;

import itauamachado.ownpos.domain.WrapObjToNetwork;


public interface Transaction {
    WrapObjToNetwork doBefore();
    void doAfter(JSONArray jsonArray);
}
