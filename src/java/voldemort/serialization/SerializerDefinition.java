package voldemort.serialization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

/**
 * A SerializerDefinition holds all the meta information for a serializer.
 * 
 * @author jay
 * 
 */
public class SerializerDefinition {

    private final String name;
    private final Integer currentSchemaVersion;
    private final Map<Integer, String> schemaInfoByVersion;

    public SerializerDefinition(String name) {
        super();
        this.name = Objects.nonNull(name);
        this.currentSchemaVersion = -1;
        this.schemaInfoByVersion = new HashMap<Integer, String>();
    }

    public SerializerDefinition(String name, String metaInfo) {
        super();
        this.name = Objects.nonNull(name);
        this.currentSchemaVersion = 0;
        this.schemaInfoByVersion = new HashMap<Integer, String>();
        this.schemaInfoByVersion.put(0, metaInfo);
    }

    public SerializerDefinition(String name, Map<Integer, String> metaInfos) {
        super();
        this.name = Objects.nonNull(name);
        this.schemaInfoByVersion = new HashMap<Integer, String>();
        int max = -1;
        for (Integer key : metaInfos.keySet()) {
            if (key < 0)
                throw new IllegalArgumentException("Version cannot be less than 0.");
            else if (key > Byte.MAX_VALUE)
                throw new IllegalArgumentException("Version cannot be more than " + Byte.MAX_VALUE);
            if (key > max)
                max = key;
            this.schemaInfoByVersion.put(key, metaInfos.get(key));
        }
        this.currentSchemaVersion = max;
    }

    public String getName() {
        return name;
    }

    public int getCurrentSchemaVersion() {
        if (currentSchemaVersion < 0)
            throw new IllegalStateException("There is no schema info associated with this serializer definition.");
        return currentSchemaVersion;
    }

    public Map<Integer, String> getAllSchemaInfoVersions() {
        return this.schemaInfoByVersion;
    }

    public boolean hasSchemaInfo() {
        return this.currentSchemaVersion >= 0;
    }

    public String getSchemaInfo(int version) {
        if (!schemaInfoByVersion.containsKey(version))
            throw new IllegalArgumentException("Unknown schema version " + version + ".");
        return schemaInfoByVersion.get(version);
    }

    public String getCurrentSchemaInfo() {
        if (currentSchemaVersion < 0)
            throw new IllegalStateException("There is no schema info associated with this serializer definition.");
        return schemaInfoByVersion.get(this.currentSchemaVersion);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj.getClass() == SerializerDefinition.class))
            return false;
        SerializerDefinition s = (SerializerDefinition) obj;
        return Objects.equal(getName(), s.getName())
               && Objects.equal(this.schemaInfoByVersion, s.schemaInfoByVersion);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { name, this.schemaInfoByVersion });
    }

}
