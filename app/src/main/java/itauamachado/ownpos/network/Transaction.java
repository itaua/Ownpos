package itauamachado.ownpos.network;

import org.json.JSONArray;

import itauamachado.ownpos.domain.WrapObjToNetwork;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public interface Transaction {
    WrapObjToNetwork doBefore();

    void doAfter(JSONArray jsonArray);
}
