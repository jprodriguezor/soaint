package co.com.soaint.foundation.canonical.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alien GR.
 */
public class WrapperAnnotationInstrocpector extends JacksonAnnotationIntrospector {
    private Map<String,Boolean> fieldsProperties = new HashMap<String,Boolean>();

    public WrapperAnnotationInstrocpector(String[] extraProperties) {
        initializedProperties(extraProperties);
    }

    private void initializedProperties(String[] extraProperties){
        if(extraProperties != null){
            for(int i = 0; i < extraProperties.length ; i++)
                this.fieldsProperties.put(extraProperties[i],true);
        }
    }

    @Override
    protected boolean _isIgnorable(Annotated a) {
        JsonIgnore ann = a.getAnnotation(JsonIgnore.class);
        if(ann != null){
            return fieldsProperties.
                    containsKey(a.getName()) ? false : ann.value();
        }
        return false;
    }
}
