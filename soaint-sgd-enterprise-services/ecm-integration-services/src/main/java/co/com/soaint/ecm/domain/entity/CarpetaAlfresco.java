package co.com.soaint.ecm.domain.entity;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.runtime.FolderImpl;

import java.util.HashMap;

/**
 * Created by Dasiel on 30/05/2017.
 */
public class CarpetaAlfresco implements Carpeta<Folder> {
    public Folder newObject(HashMap<String,String> a){

        return null;//new FolderImpl();
    }
}
