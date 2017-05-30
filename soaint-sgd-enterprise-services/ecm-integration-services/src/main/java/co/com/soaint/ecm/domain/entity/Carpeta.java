package co.com.soaint.ecm.domain.entity;

import lombok.Data;

import java.util.HashMap;

/**
 * Created by Dasiel on 29/05/2017.
 */

public interface Carpeta<T> {
    public T newObject(HashMap<String,String> q);


}

